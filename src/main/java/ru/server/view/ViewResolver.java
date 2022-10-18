package ru.server.view;

import java.io.IOException;

public interface ViewResolver {

    String getTemplateAsText();
    void handle(Object methodReturn) throws IOException;
    String getContentType();

}
