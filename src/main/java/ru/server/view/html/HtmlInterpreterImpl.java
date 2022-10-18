package ru.server.view.html;

import org.jetbrains.annotations.NotNull;
import ru.server.view.data.ViewData;

import javax.xml.parsers.DocumentBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlInterpreterImpl implements HtmlInterpreter {

    @Override
    public String Interpretation(String html, ViewData data) {
        html = tagInterpretation(html, data);
        html = htmlInterpretation(html, data);
        return html;
    }

    private String htmlInterpretation(String html, ViewData data) {
        Matcher matcher = Pattern.compile("<[^>]*>").matcher(html);
        TagBuffer tagBuffer = new TagBuffer();

        while (matcher.find()) {
            tagBuffer.add(new Tag(matcher.group(), matcher.start(), matcher.end()));
        }

        TagTree tree = new TagTree();
        tree.makeTree(tagBuffer);


        return html;
    }

    private String tagInterpretation(String html, ViewData data) {
        Matcher matcher = Pattern.compile("\\{\\{.*}}").matcher(html);
        while (matcher.find()) {
            String tag = matcher.group();
            html = html.replace(tag, (String) data.get(tag.substring(2, tag.length()-2).trim()));
        }
        return html;
    }

    private class TagTree {

        private List<Tag> tags = new ArrayList<>();
        private TagTree next;

        public void makeTree(TagBuffer buffer) {

        }

        private boolean isLastClosed() {
            String tag = tags.get(tags.size() - 1).getTag();
            return tag.contains("</") || tag.contains("/>") || tag.contains("<meta");
        }

    }

    private class TagBuffer implements Iterable<Tag> {

        private final List<Tag> buffer = new ArrayList<>();
        private int index = 0;

        public void add(Tag tag) {
            buffer.add(tag);
        }

        public void back() {
            index--;
        }


        @NotNull
        @Override
        public Iterator<Tag> iterator() {
            return new Iterator<Tag>() {
                @Override
                public boolean hasNext() {
                    return buffer.size() > index;
                }

                @Override
                public Tag next() {
                    return buffer.get(index++);
                }
            };
        }
    }
}
