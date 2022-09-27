package ru.server.view;

public interface ViewResolver {

    void setPostfix(String postfix);
    void setPrefix(String prefix);
    void setTemplatePath(String path);
    String getTemplateAsText();
    void handle(Object methodReturn);
    String getContentType();

}
