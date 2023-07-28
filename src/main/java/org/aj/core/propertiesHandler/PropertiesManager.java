package org.aj.core.propertiesHandler;

import org.aj.application.configs.PropsRepo;
import org.aj.core.Exceptions.MissingMandatoryPropException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesManager {

    static ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<>();

    public void getAllProperties() throws MissingMandatoryPropException {
        System.out.println("Fetching all properties ....");
        Class<?>[] classes = PropsRepo.class.getDeclaredClasses();
        List<Field> fields = new ArrayList<>();

        for (Class<?> aClass : classes) {
            fields.addAll(List.of(aClass.getDeclaredFields()));
        }

        for (Field field : fields) {
            String fieldValue = System.getenv(field.getName());
            System.out.println("field -> " + field.getName());

            if(isMandatoryField(field) && (fieldValue == null || fieldValue.length() == 0))
                throw new MissingMandatoryPropException(field.getName() + " is missing");

            properties.put(field.getName(), fieldValue);
        }

        System.out.println("DONE >> " + properties);
    }

    private boolean isMandatoryField(Field field) {
        if(field.isAnnotationPresent(Mandatory.class))
            return true;
        return false;
    }

    public static String getProperty(String prop){
        if(properties.containsKey(prop))
            return properties.get(prop);

        throw new RuntimeException("The provided propKey -> " + prop + " is not set\n" + properties );
    }
}
