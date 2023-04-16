package ru.server.view.html.parser.tree;

import java.util.stream.Collectors;

public class TagNode extends Node {
    @Override
    public String toHtml() {
        StringBuilder builder = new StringBuilder();
        builder
                .append("<").append(value).append(" ").append(stringifyAttrs()).append(">")
                .append(stringifyChildren())
                .append("</").append(value).append(">");
        return builder.toString();
    }

    private String stringifyAttrs() {
        return attributes.entrySet().stream()
                .map(e -> e.getKey() + "=\"" + e.getValue() + "\"")
                .collect(Collectors.joining(" "));
    }

    private String stringifyChildren() {
        return children.stream()
                .map(Node::toHtml)
                .collect(Collectors.joining());
    }
}
