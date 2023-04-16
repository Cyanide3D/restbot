package ru.server.view.html.parser.tree;

public class BodyNode extends Node {
    @Override
    public String toHtml() {
        return value;
    }
}
