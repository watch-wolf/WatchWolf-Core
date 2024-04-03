package dev.watchwolf.core.rpc.objects.converter;

import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.Set;
import java.util.stream.Stream;

public class RPCObjectsConverterFactory {
    public RPCObjectsConverterFactory() {}

    public RPCConverter<?> build() {
        final RPCConverter<?> r = new RPCConverter<>();
        r.setMasterConverter(r); // the master converter is itself

        Reflections reflections = new Reflections("dev.watchwolf.core.rpc.objects.types");
        Set<Class<? extends RPCConverter>> allClasses = reflections.getSubTypesOf(RPCConverter.class);

        // TODO contains `@MainSubconverter` ?
        Stream<Class<? extends RPCConverter>> mainConverters = allClasses.stream().filter(e -> true);
        Stream<Class<? extends RPCConverter>> subconverters = allClasses.stream().filter(e -> false);

        mainConverters.forEach(converter -> {
            RPCConverter<?> instance = null;

            //System.out.println("Got main converter: " + converter.getName());
            try {
                Constructor<?> constructor = converter.getConstructor();
                instance = (RPCConverter<?>)constructor.newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            r.addSubconverter(instance);
        });

        subconverters.forEach(converter -> {
            // TODO how do we add considering the dependencies?
        });

        return r;
    }
}
