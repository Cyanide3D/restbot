package ru.server.view;

import javax.swing.text.View;

public interface TemplateViewResolver extends ViewResolver {

    void setPostfix(String postfix);
    void setPrefix(String prefix);
    void setTemplatePath(String path);

}
