package com.github.salilvnair.ccf.util.commonutil.collections;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Salil V Nair
 */
public class ListGenerator {

    private final boolean immutable;

    private ListGenerator(boolean immutable) {
        this.immutable = immutable;
    }

    public static ListGenerator immutable() {
        return new ListGenerator(true);
    }

    public static  ListGenerator mutable() {
        return new ListGenerator(false);
    }

    @SafeVarargs
    public final <E> List<E> generate(E... elements) {
        if(elements==null || elements.length == 0) {
            return Collections.emptyList();
        }
        List<E> list = Arrays.stream(elements).collect(Collectors.toList());
        return immutable? list : new ArrayList<>(list);
    }

    public <E> List<E> generateRepeated(E element, int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }
        List<E> list = new ArrayList<>(Collections.nCopies(count, element));
        ObjectMapper mapper = new ObjectMapper();
        try {
            String listString = mapper.writeValueAsString(list);
            list = mapper.readValue(listString, mapper.getTypeFactory().constructCollectionType(List.class, element.getClass()));
        }
        catch (JsonProcessingException ex) {
            list = Collections.emptyList();
        }
        return immutable ? Collections.unmodifiableList(list) : list;
    }

    public static <T> List<List<T>> split(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            batches.add(list.subList(i, end));
        }
        return batches;
    }

    public static <T> List<List<T>> distributeEvenly(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        int totalSize = list.size();
        int baseBatchSize = totalSize / batchSize;
        int remainder = totalSize % batchSize;
        int start = 0;
        for (int i = 0; i < batchSize; i++) {
            int currentBatchSize = baseBatchSize + (remainder > 0 ? 1 : 0);
            int end = Math.min(start + currentBatchSize, totalSize);
            batches.add(list.subList(start, end));
            start = end;
            remainder--;
        }
        return batches;
    }
}
