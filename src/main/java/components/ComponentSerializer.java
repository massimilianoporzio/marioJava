package components;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class ComponentSerializer implements JsonSerializer<Component> {

    @Override
    public JsonElement serialize(Component src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getCanonicalName()));
        result.add("properties",context.serialize(src, src.getClass())); //serialize the rest of the class
        return result;
    }
}
