package com.light.core.context;

import org.springframework.core.NamedInheritableThreadLocal;

/**
 * @author lihaipeng
 * @date 2020-09-28
 * @see org.springframework.context.i18n.LocaleContextHolder
 * @see org.springframework.web.context.request.RequestContextHolder
 */
public class LightContextHolder {

    private static final ThreadLocal<LightContext> contextHolder = new NamedInheritableThreadLocal<LightContext>("LightContext") {
        @Override
        protected LightContext initialValue() {
            return new LightContext();
        }
    };

    public static LightContext getLightContext() {
        return contextHolder.get();
    }

    public static void setLightContext(LightContext context) {
        contextHolder.set(context);
    }

    public static void clearLightContext() {
        contextHolder.remove();
    }
}
