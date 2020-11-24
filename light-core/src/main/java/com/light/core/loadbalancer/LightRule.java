/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.light.core.loadbalancer;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.utils.Chooser;
import com.alibaba.nacos.client.naming.utils.Pair;
import com.light.constant.LightConstants;
import com.light.core.config.LightProperties;
import com.light.core.context.LightContextHolder;
import com.light.enums.RuleModeEnum;
import com.light.enums.UserModeEnum;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 基于Nacos Discovery实现的负载均衡策略。参考NacosRule和RoundRobinRule
 * 1. 支持RANDOM_WEIGHT（权重）和ROUND_ROBIN（轮训）策略
 * 2. 支持灰度模式
 *
 * @author lihaipeng
 * @date 2020-09-28
 */
public class LightRule extends AbstractLoadBalancerRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(LightRule.class);

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    @Autowired
    private LightProperties lightProperties;

    private int currencyInstancesHashcode = 0;
    private AtomicInteger nextServerCyclicCounter;
    private Chooser<String, Instance> vipChooser;

    public LightRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
        vipChooser = new Chooser<String, Instance>("www.light-springcloud.com");
    }

    public LightRule(ILoadBalancer lb) {
        this();
        setLoadBalancer(lb);
    }

    @Override
    public Server choose(Object key) {
        try {
            String clusterName = this.nacosDiscoveryProperties.getClusterName();
            String group = this.nacosDiscoveryProperties.getGroup();
            DynamicServerListLoadBalancer loadBalancer = (DynamicServerListLoadBalancer) getLoadBalancer();
            String name = loadBalancer.getName();
            List<Instance> instances = this.nacosDiscoveryProperties.namingServiceInstance()
                    .selectInstances(name, group, true);
            if (CollectionUtils.isEmpty(instances)) {
                LOGGER.warn("no instance in service {}", name);
                return null;
            }
            if (instances.size() == 1) {
                return new NacosServer(instances.get(0));
            }

            if (lightProperties.getLoadbalancer().getMode().equals(UserModeEnum.GRAY)) {
                List<Instance> grayscaleInstances = new ArrayList<>();
                List<Instance> ungrayscaleInstances = new ArrayList<>();
                for (Instance i : instances) {
                    if (UserModeEnum.GRAY.name().equalsIgnoreCase(i.getMetadata().get("light.mode"))) {
                        grayscaleInstances.add(i);
                    } else {
                        ungrayscaleInstances.add(i);
                    }
                }

                String userMode = LightContextHolder.getLightContext().get(LightConstants.X_LIGHT_USERMODE);
                String loginName = LightContextHolder.getLightContext().get(LightConstants.X_LIGHT_USERNAME);
                if (UserModeEnum.GRAY.name().equalsIgnoreCase(userMode)) {
                    if (!grayscaleInstances.isEmpty()) {
                        LOGGER.warn("grayscale instances is enabled. serviceName:{}, username:{}, count:{} -> {}", name, loginName, instances.size(), grayscaleInstances.size());
                        instances = grayscaleInstances;
                    }
                } else {
                    if (!ungrayscaleInstances.isEmpty()) {
                        instances = ungrayscaleInstances;
                    }
                }
                if (instances.size() == 1) {
                    return new NacosServer(instances.get(0));
                }
            }

            if (lightProperties.getLoadbalancer().isSameCluster()) {
                if (StringUtils.isNotBlank(clusterName)) {
                    List<Instance> sameClusterInstances = instances.stream()
                            .filter(instance -> Objects.equals(clusterName,
                                    instance.getClusterName()))
                            .collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sameClusterInstances)) {
                        instances = sameClusterInstances;
                        if (instances.size() == 1) {
                            return new NacosServer(instances.get(0));
                        }
                    } else {
                        LOGGER.warn("A cross-cluster call occurs，name = {}, clusterName = {}, instance = {}",
                                name, clusterName, instances);
                    }
                }
            }

            Instance instance = null;
            if (RuleModeEnum.RANDOM_WEIGHT.equals(lightProperties.getLoadbalancer().getRule())) {
                instance = randomWeightChoose(instances, key);
            } else {
                instance = croundRobinRuleChoose(instances, key);
            }
            return new NacosServer(instance);
        } catch (Exception e) {
            LOGGER.error("LightRule choose error", e);
            return null;
        }

    }

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    private Instance randomWeightChoose(List<Instance> instances, Object key) {
        if (currencyInstancesHashcode == 0 || currencyInstancesHashcode != instances.hashCode()) {
            refresh(instances);
        }
        return vipChooser.randomWithWeight();
    }

    private void refresh(List<Instance> instances) {
        synchronized (this) {
            if (currencyInstancesHashcode == 0 || currencyInstancesHashcode != instances.hashCode()) {
                currencyInstancesHashcode = instances.hashCode();
                List<Pair<Instance>> hostsWithWeight = new ArrayList<Pair<Instance>>();
                for (Instance host : instances) {
                    if (host.isHealthy()) {
                        hostsWithWeight.add(new Pair<Instance>(host, host.getWeight()));
                    }
                }
                vipChooser.refresh(hostsWithWeight);
                LOGGER.info("vipChooser.refresh hashcode:{}", currencyInstancesHashcode);
            }
        }
    }

    public Instance croundRobinRuleChoose(List<Instance> instances, Object key) {
        Instance server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            int serverCount = instances.size();
            int nextServerIndex = incrementAndGetModulo(serverCount);
            server = instances.get(nextServerIndex);
            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }
            return server;
        }
        return server;
    }

    /**
     * Inspired by the implementation of {@link AtomicInteger#incrementAndGet()}.
     *
     * @param modulo The modulo to bound the value of the counter.
     * @return The next value.
     */
    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }
}
