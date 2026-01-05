package com.github.salilvnair.ccf.service;


import com.github.salilvnair.ccf.core.model.ContainerComponentRequest;
import com.github.salilvnair.ccf.core.model.ContainerComponentResponse;

public interface CcfCoreService {
    ContainerComponentResponse execute(ContainerComponentRequest request);
}
