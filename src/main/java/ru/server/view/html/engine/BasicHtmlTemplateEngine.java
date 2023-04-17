package ru.server.view.html.engine;

import ru.server.exception.InternalServerHttpException;
import ru.server.view.data.ViewData;
import ru.server.view.html.parser.parser.HtmlParser;
import ru.server.view.html.parser.tree.BodyNode;
import ru.server.view.html.parser.tree.Node;
import ru.server.view.html.parser.tree.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicHtmlTemplateEngine implements HtmlTemplateEngine {

    private final HtmlParser parser;
    private final Map<String, String> patterns;

    public BasicHtmlTemplateEngine(Path patterns) throws IOException {
        this.parser = new HtmlParser();
        this.patterns = new HashMap<>();
        parsePatterns(patterns);
    }

    @Override
    public String handle(Path html, ViewData data) {
        try {
            Node node = parser.parse(Files.newInputStream(html));
            replacePatterns(node);
            return node.toHtml();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalServerHttpException();
        }
    }

    private void replacePatterns(Node node) {
        List<Node> children = node.getChildren();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            if (child instanceof TagNode && child.getValue().equals("pattern") && child.getAttr("id") != null) {
                BodyNode bodyNode = new BodyNode();
                bodyNode.setValue(patterns.get(child.getAttr("id")));
                children.set(i, bodyNode);
            }
            replacePatterns(child);
        }


    }

    private void parsePatterns(Path patterns) throws IOException {
        Files.list(patterns).forEach(f -> {
            try {
                Node node = parser.parse(Files.newInputStream(f));
                node.getChildren().forEach(this::handleChildNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void handleChildNode(Node node) {
        if (!node.getValue().equals("pattern") || node.getAttr("id") == null)
            throw new InternalServerHttpException();
        patterns.put(node.getAttr("id"), node.getChildren().stream().map(Node::toHtml).collect(Collectors.joining()));
    }
}
