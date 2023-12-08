package dev.watchwolf.core.rpc.objects.converter.class_type;

public class TemplateClassType<T,O> extends ClassType<T> {
    private final ClassType<O> subtype;

    public TemplateClassType(Class<T> classType, ClassType<O> subtype) {
        super(classType);
        this.subtype = subtype;
    }

    public TemplateClassType(Class<T> classType, Class<O> subtype) {
        this(classType, new ClassType<>(subtype));
    }

    public ClassType<O> getSubtype() {
        return this.subtype;
    }
}
