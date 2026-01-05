package com.github.salilvnair.ccf.core.data.query.decorator.base;
import com.github.salilvnair.ccf.core.data.context.DataContext;
import com.github.salilvnair.ccf.core.data.query.type.PlaceHolderType;

public interface QueryString {
    String build(DataContext dataContext);
    String replaceWith(DataContext dataContext);
    PlaceHolderType placeHolder(DataContext dataContext);
}
