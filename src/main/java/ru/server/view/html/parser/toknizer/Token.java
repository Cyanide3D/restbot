package ru.server.view.html.parser.toknizer;

public class Token {

    private String value;
    private TokenType type;
    private boolean close;

    public Token() {
    }

    public Token(String value) {
        this.value = value;
    }

    public Token(TokenType type) {
        this.type = type;
    }

    public Token(boolean close) {
        this.close = close;
    }

    public Token(String value, TokenType type, boolean close) {
        this.value = value;
        this.type = type;
        this.close = close;
    }

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                ", type=" + type +
                ", close=" + close +
                '}';
    }
}
