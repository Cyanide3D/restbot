package ru.server.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.server.constants.ContentType;
import ru.server.exception.InternalServerHttpException;

public class JsonViewResolver implements ViewResolver {

    private String result = "{}";

    @Override
    public String getTemplateAsText() {
        return result;
    }

    public String getContentType() {
        return ContentType.JSON;
    }

    @Override
    public void handle(Object methodReturn) {
        try {
            result = methodReturn instanceof String ? result = (String) methodReturn : new ObjectMapper().writeValueAsString(methodReturn);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerHttpException();
        }
    }
}
