package dev.watchwolf.core.rpc.objects.converter.class_type;

public class ClassTypeFactory {
    public static <T> ClassType<T> getType(T o) {
        if (o == null) return null;

        Class<T> objectClass = (Class<T>) o.getClass();
        return ClassTypeFactory.getType(objectClass);
    }

    public static <T> ClassType<T> getType(Class<T> classType) {
        if (classType == null) return null;
        return new ClassType<>(classType);
    }
}
