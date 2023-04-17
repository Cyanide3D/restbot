package ru.server.view.resolver;

public interface TemplateViewResolver extends ViewResolver {

    void setPostfix(String postfix);
    void setPrefix(String prefix);
    void setTemplatePath(String path);

}
