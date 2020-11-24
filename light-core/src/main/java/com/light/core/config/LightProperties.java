package com.light.core.config;

import com.light.enums.RuleModeEnum;
import com.light.enums.UserModeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration Properties
 *
 * @author lihaipeng
 * @date 2020-09-22
 */
@ConfigurationProperties(prefix = LightProperties.PREFIX, ignoreUnknownFields = true)
public class LightProperties {
    public static final String PREFIX = "light";
    private ServiceAdvice advice = new ServiceAdvice();
    private Logger logger = new Logger();
    private Header header = new Header();
    private Loadbalancer loadbalancer = new Loadbalancer();
    private Timezone timezone = new Timezone();


    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public ServiceAdvice getAdvice() {
        return advice;
    }

    public void setAdvice(ServiceAdvice advice) {
        this.advice = advice;
    }

    public Loadbalancer getLoadbalancer() {
        return loadbalancer;
    }

    public void setLoadbalancer(Loadbalancer loadbalancer) {
        this.loadbalancer = loadbalancer;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    public static class Logger {
        private Setting feign = new Setting();
        private Setting rest = new Setting();
        private Setting controller = new Setting();

        public Setting getFeign() {
            return feign;
        }

        public void setFeign(Setting feign) {
            this.feign = feign;
        }

        public Setting getRest() {
            return rest;
        }

        public void setRest(Setting rest) {
            this.rest = rest;
        }

        public Setting getController() {
            return controller;
        }

        public void setController(Setting controller) {
            this.controller = controller;
        }
    }

    public static class Header {
        private List<String> throughNames = new ArrayList<>();

        public List<String> getThroughNames() {
            return throughNames;
        }

        public void setThroughNames(List<String> throughNames) {
            this.throughNames = throughNames;
        }
    }

    public static class Setting {

        private boolean enabled = true;

        private List<String> ignoreResponseDataMethodSigns = new ArrayList<>();

        private List<String> skipMethodSigns = new ArrayList<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getIgnoreResponseDataMethodSigns() {
            return ignoreResponseDataMethodSigns;
        }

        public void setIgnoreResponseDataMethodSigns(List<String> ignoreResponseDataMethodSigns) {
            this.ignoreResponseDataMethodSigns = ignoreResponseDataMethodSigns;
        }

        public List<String> getSkipMethodSigns() {
            return skipMethodSigns;
        }

        public void setSkipMethodSigns(List<String> skipMethodSigns) {
            this.skipMethodSigns = skipMethodSigns;
        }
    }

    public static class ServiceAdvice {
        private boolean controllerExceptionAdviceEnabled = true;
        private boolean controllerLogAdviceEnabled = true;
        private String controllerLogPointcut = "execution(* com.*.controller.*.*(..))";

        public boolean isControllerExceptionAdviceEnabled() {
            return controllerExceptionAdviceEnabled;
        }

        public void setControllerExceptionAdviceEnabled(boolean controllerExceptionAdviceEnabled) {
            this.controllerExceptionAdviceEnabled = controllerExceptionAdviceEnabled;
        }

        public boolean isControllerLogAdviceEnabled() {
            return controllerLogAdviceEnabled;
        }

        public void setControllerLogAdviceEnabled(boolean controllerLogAdviceEnabled) {
            this.controllerLogAdviceEnabled = controllerLogAdviceEnabled;
        }

        public String getControllerLogPointcut() {
            return controllerLogPointcut;
        }

        public void setControllerLogPointcut(String controllerLogPointcut) {
            this.controllerLogPointcut = controllerLogPointcut;
        }
    }

    public static class Loadbalancer {
        private RuleModeEnum rule = RuleModeEnum.ROUND_ROBIN;
        private UserModeEnum mode = UserModeEnum.NORMAL;
        private boolean sameCluster = false;

        public RuleModeEnum getRule() {
            return rule;
        }

        public void setRule(RuleModeEnum rule) {
            this.rule = rule;
        }

        public boolean isSameCluster() {
            return sameCluster;
        }

        public void setSameCluster(boolean sameCluster) {
            this.sameCluster = sameCluster;
        }

        public UserModeEnum getMode() {
            return mode;
        }

        public void setMode(UserModeEnum mode) {
            this.mode = mode;
        }
    }

    public static class Timezone {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}
