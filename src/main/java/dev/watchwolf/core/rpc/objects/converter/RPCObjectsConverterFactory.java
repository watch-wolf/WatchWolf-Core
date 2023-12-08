package dev.watchwolf.core.rpc.objects.converter;

import org.reflections.Reflections;

import java.util.Set;

public class RPCObjectsConverterFactory {
    public RPCObjectsConverterFactory() {}

    public RPCConverter<?> build() {
        RPCConverter<?> r = new RPCConverter<>();

        Reflections reflections = new Reflections("dev.watchwolf.core.rpc.objects.types");
        Set<Class<? extends RPCConverter>> allClasses = reflections.getSubTypesOf(RPCConverter.class);
        System.out.println(allClasses.toString());

        // TODO add all classes in `.objects.types` that implements `RpcConverter` and contains `@MainSubconverter`
        return r;
    }
}
