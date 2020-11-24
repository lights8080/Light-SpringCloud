package com.light.core.context;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lihaipeng
 * @date 2020-09-28
 * @see LightContextHolder
 */
@Setter
@Getter
public class LightContext {

    private final Map<String, String> attributes = new HashMap<>();

    public LightContext add(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    public String get(String key) {
        return attributes.get(key);
    }

    public LightContext remove(String key) {
        attributes.remove(key);
        return this;
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}