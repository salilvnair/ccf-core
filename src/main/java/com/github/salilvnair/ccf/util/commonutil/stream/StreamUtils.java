package com.github.salilvnair.ccf.util.commonutil.stream;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamUtils {
    public static <T> Stream<List<T>> split(List<T> sourceList, int length) {
        if(sourceList.isEmpty()) {
            return Stream.empty();
        }
        int listSize = sourceList.size();
        int splitChunks = (listSize -1) / length ;

        return IntStream
                .range(0, splitChunks+1)
                .mapToObj(n -> sourceList.subList(n * length, n == splitChunks ? listSize : (n+1) * length ));
    }
}
