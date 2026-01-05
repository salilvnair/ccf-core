package com.github.salilvnair.ccf.util.commonutil.collections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Salil V Nair
 */
public class MapGenerator {
    private final boolean immutable;

    private boolean ordered;

    private MapGenerator(boolean immutable) {
        this.immutable = immutable;
    }

    private MapGenerator(boolean immutable, boolean ordered) {
        this.immutable = immutable;
        this.ordered = ordered;
    }
    
    public static MapGenerator immutable() {
        return new MapGenerator(true);
    }

    public static MapGenerator orderedImmutable() {
        return new MapGenerator(true, true);
    }

    public static MapGenerator ordered() {
        return new MapGenerator(false, true);
    }

    public static  MapGenerator mutable() {
        return new MapGenerator(false);
    }

    public <K, V> Map<K, V> generate() {
        Map<K,V> map = ordered ? new LinkedHashMap<>() : new HashMap<>();
        return immutable? Collections.unmodifiableMap(map) : map;
    }

    public <K, V> Map<K, V> generate(K k1, V v1) {
        Map<K,V> map = ordered ? new LinkedHashMap<>() : new HashMap<>();
        map.put(k1, v1);
        return immutable? Collections.unmodifiableMap(map) : map;
    }
    public  <K, V> Map<K, V> generate(K k1, V v1, K k2, V v2) {
        Map<K, V> map = ordered ? new LinkedHashMap<>(generate(k1, v1)) : new HashMap<>(generate(k1, v1));
        map.putAll(generate(k2,v2));
        return immutable? Collections.unmodifiableMap(map) : map;
    }
    public  <K, V> Map<K, V> generate(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = ordered ? new LinkedHashMap<>(generate(k1, v1, k2, v2)) : new HashMap<>(generate(k1, v1, k2, v2));
        map.putAll(generate(k3,v3));
        return immutable? Collections.unmodifiableMap(map) : map;
    }
    public  <K, V> Map<K, V> generate(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> map = ordered ? new LinkedHashMap<>(generate(k1, v1, k2, v2, k3, v3)) : new HashMap<>(generate(k1, v1, k2, v2, k3,v3));
        map.putAll(generate(k4,v4));
        return immutable? Collections.unmodifiableMap(map) : map;
    }
    public  <K, V> Map<K, V> generate(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> map = ordered ? new LinkedHashMap<>(generate(k1, v1, k2, v2, k3, v3)) : new HashMap<>(generate(k1, v1, k2, v2, k3,v3));
        map.putAll(generate(k4,v4, k5, v5));
        return immutable? Collections.unmodifiableMap(map) : map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> cast(Object obj) {
        if (obj instanceof Map) {
            return (Map<K, V>) obj;
        }
        throw new IllegalArgumentException("Object is not a Map");
    }

    public static <String, V> Map<String, V> clone(Map<String, V> source, List<String> keysToIgnore) {
        return source.entrySet()
                    .stream()
                    .filter(entry -> Objects.nonNull(entry.getKey()) && Objects.nonNull(entry.getValue()))
                    .filter(entry -> !keysToIgnore.contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
