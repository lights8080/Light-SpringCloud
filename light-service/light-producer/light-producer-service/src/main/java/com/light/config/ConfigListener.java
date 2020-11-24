package com.light.config;

import com.light.constant.LightConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

/**
 * 初始化配置监听，基于Redis队列实现
 *
 * @author lihaipeng
 * @date 2020-09-24
 */
@Component
@Slf4j
public class ConfigListener extends MessageListenerAdapter {

    @Bean
    @ConditionalOnBean(MessageListenerAdapter.class)
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter redisListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisListener, new PatternTopic(LightConstants.CONFIG_LISTENER_TOPIC));
        return container;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        log.info("configListener: {} {}", channel, message.toString());
//        if ("demo".equals(message.toString())) {
//            // reload demo config
//        }
    }
}
