package com.github.salilvnair.ccf.core.data.query.serializer;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

@JsonIgnoreProperties({ DataContext.DATA_CONTEXT })
public class JsonStringQueryParams<K, V> extends HashMap<K, V> {
}
