package com.github.salilvnair.ccf.core.data.handler.provider.container;

import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.exception.DataException;
import com.github.salilvnair.ccf.core.data.handler.core.ContainerTableDataGenerator;
import com.github.salilvnair.ccf.core.data.type.ContainerType;
import com.github.salilvnair.ccf.core.model.SectionField;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = ContainerType.Value.INFO_TABLE)
public class InfoSectionTableContainerGenerator extends AbstractSectionTableContainerGenerator implements ContainerTableDataGenerator {

    @Override
    public List<List<SectionField>> generate(DataContext dataContext) throws DataException {
        return results(dataContext);
    }

}
