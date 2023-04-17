package ru.server.view.resolver;

import ru.server.constants.ContentType;
import ru.server.exception.InternalServerHttpException;
import ru.server.view.data.ViewData;
import ru.server.view.html.engine.HtmlTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlViewResolver implements TemplateViewResolver {

    private String prefix = "";
    private String postfix = ".html";
    private Path templatesPath = null;
    private final HtmlTemplateEngine engine;
    private final ViewData viewData;

    public HtmlViewResolver(HtmlTemplateEngine engine, ViewData viewData) {
        this.engine = engine;
        this.viewData = viewData;
    }

    @Override
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void setTemplatePath(String path) {
        Path p = Path.of(path);
        if (!Files.isDirectory(p)) throw new IllegalArgumentException();
        templatesPath = p;
    }

    @Override
    public String handle(Object methodReturn) throws IOException {
            if (methodReturn instanceof String) {
                Path temp = templatesPath.resolve(prefix + methodReturn + postfix);
                return engine.handle(temp, viewData);
            } else {
                throw new InternalServerHttpException();
            }
    }

    @Override
    public String getContentType() {
        return ContentType.HTML;
    }
}
