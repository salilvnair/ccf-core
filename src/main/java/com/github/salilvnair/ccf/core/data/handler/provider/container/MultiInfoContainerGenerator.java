package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerDataGenerator;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.model.SectionField;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = ContainerType.Value.MULTI_INFO)
public class MultiInfoContainerGenerator extends InfoContainerGenerator implements ContainerDataGenerator {

    @Override
    public List<SectionField> generate(DataContext dataContext) throws DataException {
        return super.generate(dataContext);
    }
}
