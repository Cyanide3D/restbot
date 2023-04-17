package ru.server.view.resolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.server.constants.ContentType;
import ru.server.exception.InternalServerHttpException;

public class JsonViewResolver implements ViewResolver {

    public String getContentType() {
        return ContentType.JSON;
    }

    @Override
    public String handle(Object methodReturn) {
        try {
            return methodReturn instanceof String ? (String) methodReturn : new ObjectMapper().writeValueAsString(methodReturn);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerHttpException();
        }
    }
}
