package ru.server.view.resolver;

import java.io.IOException;

public interface ViewResolver {

    String handle(Object methodReturn) throws IOException;
    String getContentType();

}
