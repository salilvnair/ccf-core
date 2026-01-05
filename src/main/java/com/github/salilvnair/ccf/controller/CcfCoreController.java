package com.github.salilvnair.ccf.controller;

import com.github.salilvnair.ccf.core.model.ContainerComponentRequest;
import com.github.salilvnair.ccf.core.model.ContainerComponentResponse;
import com.github.salilvnair.ccf.service.CcfCoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CcfCoreController {

    private final CcfCoreService ccfCoreService;

    @RequestMapping(path = "/v1/generate", method = RequestMethod.POST)
    public ContainerComponentResponse generate(@RequestBody ContainerComponentRequest request) {
        return ccfCoreService.execute(request);
    }

}
