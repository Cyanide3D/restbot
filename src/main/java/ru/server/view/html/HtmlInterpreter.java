package ru.server.view.html;

import ru.server.view.data.ViewData;

public interface HtmlInterpreter {

    String Interpretation(String html, ViewData data);

}
