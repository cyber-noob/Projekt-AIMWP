package org.aj.core.propertiesHandler;

import org.aj.application.configs.PropsRepo;
import org.aj.core.Exceptions.MissingMandatoryPropException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesManager {

    private static ConcurrentHashMap<String, String> properties = new ConcurrentHashMap<>();

    public void getAllProperties() throws MissingMandatoryPropException {
        System.out.println("Fetching all properties ....");
        Class<?>[] classes = PropsRepo.class.getDeclaredClasses();

        for (Class<?> aClass : classes) {
            List<Field> fields = List.of(aClass.getDeclaredFields());

            for (Field field : fields) {
                field.setAccessible(true);
                String fieldValue = null;
                
                try {
                    fieldValue = (String) field.get(aClass.getConstructor().newInstance());
                } catch (IllegalAccessException e) {
                    System.out.println("Unable to fetch Field value. Field -> " + field);
                    e.printStackTrace();
                } catch (InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }

                String envVal = System.getenv(field.getName());
                if(envVal != null && envVal.length() > 0)
                    fieldValue = System.getenv(field.getName());
                
                System.out.println("field -> " + field.getName());

                if(isMandatoryField(field) && (fieldValue == null || fieldValue.length() == 0))
                    throw new MissingMandatoryPropException(field.getName() + " is missing");

                properties.put(field.getName(), fieldValue);
            }
        }

        System.out.println("DONE >> " + properties);
    }

    private boolean isMandatoryField(Field field) {
        return field.isAnnotationPresent(Mandatory.class);
    }

    public static String getProperty(String prop){
        if(properties.containsKey(prop))
            return properties.get(prop);

        throw new RuntimeException("The provided propKey -> " + prop + " is not set\n" + properties );
    }
}
