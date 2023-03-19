package com.bawnorton.betterbookshelves.reflection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Reflection {
    public static <T> List<T> getFields(Class<?> clazz, Class<T> type) {
        List<T> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (type.isAssignableFrom(field.getType())) {
                try {
                    fields.add(type.cast(field.get(null)));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return fields;
    }
}
