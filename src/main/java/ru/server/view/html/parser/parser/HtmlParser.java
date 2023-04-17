package ru.server.view.html.parser.parser;

import ru.server.view.html.parser.toknizer.Token;
import ru.server.view.html.parser.toknizer.Tokenizer;
import ru.server.view.html.parser.tree.BodyNode;
import ru.server.view.html.parser.tree.HeadNode;
import ru.server.view.html.parser.tree.Node;
import ru.server.view.html.parser.tree.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

public class HtmlParser {

    public Node parse(InputStream source) {
        Node head = new HeadNode();
        Tokenizer tokenizer = new Tokenizer(source);
        parseNode(tokenizer, tokenizer.nextToken(), head);

        return head;
    }

    private void parseNode(Tokenizer tokenizer, Token token, Node node) {
        Node n = new TagNode();
        node.addChild(n);
        Stack<Token> attrs = new Stack<>();
        do {
            switch (token.getType()) {
                case TAG_NAME -> {
                    if (token.isClose()) return;
                    if (n.isAssigned()) {
                        if (n.isNonClosableTag()) {
                            parseNode(tokenizer, token, node);
                        } else {
                            parseNode(tokenizer, token, n);
                        }
                    } else {
                        n.setValue(token.getValue());
                    }
                }
                case TAG_BODY -> {
                    Node body = new BodyNode();
                    body.setValue(token.getValue());
                    n.addChild(body);
                }
                case TAG_ATTR_NAME -> attrs.push(token);
                case TAG_ATTR_VALUE -> n.addAttr(attrs.pop().getValue(), token.getValue());
            }
        } while (tokenizer.hasNext() && (token = tokenizer.nextToken()) != null);
    }

}
