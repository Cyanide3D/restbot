package ru.server.view.html.parser.parser;

import ru.server.view.html.parser.toknizer.Token;
import ru.server.view.html.parser.toknizer.Tokenizer;
import ru.server.view.html.parser.tree.BodyNode;
import ru.server.view.html.parser.tree.HeadNode;
import ru.server.view.html.parser.tree.Node;
import ru.server.view.html.parser.tree.TagNode;

import java.io.InputStream;
import java.util.Stack;

public class HtmlParser {

    public Node parse(InputStream source) {
        Node head = new HeadNode();
        Tokenizer tokenizer = new Tokenizer(source);
        parseNode(tokenizer, tokenizer.nextToken(), head);

        return head;
    }

    private void parseNode(Tokenizer tokenizer, Token token, Node node) {
        TagNode tag = new TagNode();
        node.addChild(tag);
        Stack<Token> attrs = new Stack<>();
        do {
            switch (token.getType()) {
                case TAG_NAME -> {
                    if (token.isClose()) {
                        return;
                    } else if (tag.isAssigned() && !tag.isNonClosableTag()) {
                        parseNode(tokenizer, token, tag);
                    } else if (tag.isNonClosableTag()) {
                        tag = new TagNode();
                        node.addChild(tag);
                        tag.setValue(token.getValue());
                    } else {
                        tag.setValue(token.getValue());
                    }
                }
                case TAG_BODY -> {
                    Node body = new BodyNode();
                    body.setValue(token.getValue());
                    tag.addChild(body);
                }
                case TAG_ATTR_NAME -> attrs.push(token);
                case TAG_ATTR_VALUE -> tag.addAttr(attrs.pop().getValue(), token.getValue());
            }
        } while (tokenizer.hasNext() && (token = tokenizer.nextToken()) != null);
    }

}
