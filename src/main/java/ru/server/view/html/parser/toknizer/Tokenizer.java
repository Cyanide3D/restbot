package ru.server.view.html.parser.toknizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class Tokenizer {

    private final InputStream source;
    private State state = State.NAME;

    public Tokenizer(InputStream source) {
        this.source = source;
    }

    public Token nextToken() {
        try {
            char c = chopChars(nextChar(), ' ', '\n', '\t', '\r');
            if (state == State.NAME && Character.isAlphabetic(c) || c == '<' || c == '/')
                return name(c);
            if (((state == State.ATTR_NAME || state == State.ATTR_VALUE) && c == '>') || state == State.BODY)
                return body(c);
            if (state == State.ATTR_NAME)
                return attrName(c);
            if (c == '=' || state == State.ATTR_VALUE)
                return attrValue(c);

            System.out.println("null - " + c);
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Token attrValue(char c) throws IOException {
        c = chopChars(c, '"', ' ', '=');
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(c);
            c = nextChar();
        } while (c != '"');

        return new Token(builder.toString(), TokenType.TAG_ATTR_VALUE);
    }

    private Token attrName(char c) throws IOException {
        c = chopChars(c, ' ');
        StringBuilder builder = new StringBuilder();
        do {
            builder.append(c);
            c = nextChar();
        } while (c != ' ' && c != '=' && c != '>');

        if (c == '=') state = State.ATTR_VALUE;
        if (c == '>') state = State.BODY;

        return new Token(builder.toString(), TokenType.TAG_ATTR_NAME);
    }

    private Token body(char c) throws IOException {
        c = chopChars(c, ' ', '>');
        if (c == '<') return name(c);
        if (Character.isWhitespace(c)) return nextToken();

        StringBuilder builder = new StringBuilder();
        do {
            builder.append(c);
            c = nextChar();
        } while (c != '<' && hasNext());

        if (c == '<')
            state = State.NAME;

        return new Token(builder.toString(), TokenType.TAG_BODY);
    }

    private Token name(char c) throws IOException {
        c = chopChars(c, '<', ' ');
        StringBuilder builder = new StringBuilder();
        Token token = new Token();
        token.setType(TokenType.TAG_NAME);
        if (c == '/') {
            token.setClose(true);
            c = nextChar();
        }
        do {
            builder.append(c);
            c = nextChar();
        } while (c != ' ' && c != '>');
        token.setValue(builder.toString());
        state = c == '>' ? State.BODY : State.ATTR_NAME;
        return token;
    }

    private char nextChar() throws IOException {
        int read = source.read();
        if (read != -1)
            return (char) read;

        throw new RuntimeException();
    }

    private char chopChars(char in, Character... chars) throws IOException {
        Set<Character> set = Set.of(chars);
        while (set.contains(in) && hasNext())
            in = nextChar();
        return in;
    }

    public boolean hasNext() {
        try {
            return source.available() > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private enum State {
        NAME, ATTR_NAME, ATTR_VALUE, BODY
    }

}
