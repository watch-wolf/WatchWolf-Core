package dev.watchwolf.core.rpc.objects.converter.class_type;

public class ClassType<T> {
    private final Class<T> classType;
    public ClassType(Class<T> classType) {
        this.classType = classType;
    }

    public Class<T> getClassType() {
        return this.classType;
    }

    public T cast(Object o) {
        return this.classType.cast(o);
    }

    public boolean isAssignableFrom(ClassType<?> classType) {
        return this.getClassType().isAssignableFrom(classType.getClassType());
    }

    public boolean isAssignableFrom(Class<?> classType) {
        return this.getClassType().isAssignableFrom(classType);
    }

    public String getName() {
        return this.classType.getName();
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ClassType)) return false;

        ClassType<?> that = (ClassType<?>) o;
        return this.getClassType().equals(that.getClassType());
    }
}
