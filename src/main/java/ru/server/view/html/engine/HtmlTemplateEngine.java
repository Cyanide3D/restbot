package ru.server.view.html.engine;

import ru.server.view.data.ViewData;

import java.io.InputStream;
import java.nio.file.Path;

public interface HtmlTemplateEngine {

    String handle(Path html, ViewData data);

}
