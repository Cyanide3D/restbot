package ru.server.view.html;

public class Tag {

    private String tag;
    private int start;
    private int end;

    public Tag(String tag, int start, int end) {
        this.tag = tag;
        this.start = start;
        this.end = end;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return tag;
    }
}
