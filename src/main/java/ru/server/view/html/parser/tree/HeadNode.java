package ru.server.view.html.parser.tree;

import java.util.stream.Collectors;

public class HeadNode extends Node {
    @Override
    public String toHtml() {
        return children.stream()
                .map(Node::toHtml)
                .collect(Collectors.joining());
    }
}
