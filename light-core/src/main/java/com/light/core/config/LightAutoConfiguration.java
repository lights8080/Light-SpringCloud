package com.light.core.config;

import com.light.constant.LightConstants;
import com.light.core.config.advice.ControllerLogAdvice;
import com.light.core.config.feign.FeignConfig;
import com.light.core.config.mybatis.MybatisPlusConfig;
import com.light.core.config.redis.RedisConfig;
import com.light.core.config.rest.RestTemplateConfig;
import com.light.core.config.ribbon.RibbonConfig;
import com.light.core.config.swagger.SwaggerConfig;
import com.light.core.context.LightContextHolder;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lihaipeng
 * @date 2020-09-28
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({LightProperties.class})
@Import({FeignConfig.class, RestTemplateConfig.class, RibbonConfig.class, SwaggerConfig.class, MybatisPlusConfig.class, RedisConfig.class})
public class LightAutoConfiguration {

    @Autowired
    private LightProperties lightProperties;

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Bean(value = "controllerLogAdvice")
    @ConditionalOnProperty(value = "light.advice.controller-log-advice-enabled", havingValue = "true", matchIfMissing = true)
    public AspectJExpressionPointcutAdvisor controllerLogAdvice() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(lightProperties.getAdvice().getControllerLogPointcut());
        advisor.setAdvice(new ControllerLogAdvice(lightProperties));
        return advisor;
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(WebMvcConfigurer.class)
    static class WebMvcConfigConfiguration implements WebMvcConfigurer {

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new HandlerInterceptor() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    LightContextHolder.getLightContext().add(LightConstants.X_LIGHT_USERNAME, request.getHeader(LightConstants.X_LIGHT_USERNAME));
                    LightContextHolder.getLightContext().add(LightConstants.X_LIGHT_USERMODE, request.getHeader(LightConstants.X_LIGHT_USERMODE));
                    return true;
                }

                @Override
                public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                }

                @Override
                public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
                    LightContextHolder.clearLightContext();
                }
            }).addPathPatterns("/**");
        }
    }
}
