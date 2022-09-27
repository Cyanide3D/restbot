package ru.server.view;

import ru.server.constants.ContentType;
import ru.server.exception.InternalServerHttpException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlViewResolver implements ViewResolver {

    private String prefix = "";
    private String postfix = "";
    private Path templatesPath = null;
    private String result = "";

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
    public String getTemplateAsText() {
        return result;
    }

    @Override
    public void handle(Object methodReturn) {
        try {
            if (methodReturn instanceof String) {
                Path path = Files.list(templatesPath).filter(p -> p.getFileName().toString().equals(prefix + methodReturn + postfix)).findFirst().orElseThrow(IllegalArgumentException::new);
                result = Files.readString(path);
            } else {
                throw new InternalServerHttpException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getContentType() {
        return ContentType.HTML;
    }
}
