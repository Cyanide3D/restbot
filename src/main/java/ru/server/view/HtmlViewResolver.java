package ru.server.view;

import ru.server.constants.ContentType;
import ru.server.exception.InternalServerHttpException;
import ru.server.view.data.ViewData;
import ru.server.view.html.HtmlInterpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HtmlViewResolver implements TemplateViewResolver {

    private String prefix = "";
    private String postfix = ".html";
    private Path templatesPath = null;
    private String result = "";

    private final HtmlInterpreter interpreter;
    private final ViewData viewData;

    public HtmlViewResolver(HtmlInterpreter interpreter, ViewData viewData) {
        this.interpreter = interpreter;
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
    public String getTemplateAsText() {
        return result;
    }

    @Override
    public void handle(Object methodReturn) throws IOException {
            if (methodReturn instanceof String) {
                Path temp = templatesPath.resolve((String) methodReturn);
                result = interpreter.Interpretation(getResult(temp.getParent(), temp.getFileName().toString()), viewData);
            } else {
                throw new InternalServerHttpException();
            }
    }

    private String getResult(Path temp, String filename) throws IOException {
        return Files.readString(Files.list(temp)
                .filter(p -> p.getFileName().toString().equals(prefix + filename + postfix))
                .findFirst().orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public String getContentType() {
        return ContentType.HTML;
    }
}
