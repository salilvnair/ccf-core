package com.github.salilvnair.ccf.util.commonutil.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class FileUtils {

    public void writeAsJson(Object data, String filePath) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if(data == null) {
                return;
            }
            mapper.writeValue(new File(filePath), data);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
