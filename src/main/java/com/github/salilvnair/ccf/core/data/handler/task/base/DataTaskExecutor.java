package com.github.salilvnair.ccf.core.data.handler.task.base;

import com.github.salilvnair.ccf.core.data.context.DataTaskContext;
import com.github.salilvnair.ccf.util.commonutil.lang.ReflectionUtil;
import com.github.salilvnair.ccf.util.log.StackTraceFrame;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataTaskExecutor {
    private final ApplicationContext applicationContext;

    private Logger logger = StackTraceFrame.initLogger(DataTaskExecutor.class);

    public DataTaskExecutor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
    public Object execute(String beanName, String methodNameString, DataTaskContext dataTaskContext) {
        try {
            List<String> methodNames = transformIntoMethodNamesIfFoundMultipleNames(methodNameString);
            DataTask dataTask = (DataTask) applicationContext.getBean(beanName);
            Object[] returnedObjects = new Object[methodNames.size()];
            int i = 0;
            for (String methodName : methodNames) {
                returnedObjects[i] = ReflectionUtil.invokeMethod(dataTask, methodName, dataTaskContext);
                i++;
            }
            return returnedObjects;
        }
        catch (Exception ex) {
            logger.error(ex.getLocalizedMessage());
        }
        return null;
    }

    public List<String> transformIntoMethodNamesIfFoundMultipleNames(String methodNameString) {
        List<String> methodNames = new ArrayList<>();
        if(methodNameString != null) {
            methodNames = Arrays.asList(methodNameString.split(","));
        }
        return methodNames;
    }
}
