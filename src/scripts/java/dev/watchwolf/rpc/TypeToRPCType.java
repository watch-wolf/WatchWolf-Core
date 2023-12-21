package dev.watchwolf.rpc;

import dev.watchwolf.core.entities.files.ConfigFile;
import dev.watchwolf.core.entities.files.plugins.Plugin;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.core.rpc.objects.converter.RPCObjectsConverterFactory;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TypeToRPCType {
    public static ClassType<? extends RPCObject> getRPCType(String type) {
        RPCConverter<?> converter = new RPCObjectsConverterFactory().build();
        ClassType<?> nativeType = getType(type);
        return (nativeType == null) ? null : converter.getRPCWrapClass(nativeType);
    }

    private static Class<?> classForSimpleName(String name) {
        Class<?> []classes = {
                String.class, Plugin.class, ConfigFile.class
        };
        Map<String,Class<?>> extraAliases = new HashMap<>();
        extraAliases.put("Map", ConfigFile.class);
        extraAliases.put("File", ConfigFile.class);

        for (Class<?> c : classes) {
            if (c.getSimpleName().equals(name)) return c;
        }

        for (Map.Entry<String,Class<?>> e : extraAliases.entrySet()) {
            if (e.getKey().equals(name)) return e.getValue();
        }

        // no match
        throw new IllegalArgumentException("Couldn't find argument '" + name + "'");
    }

    public static ClassType<?> getType(String type) {
        if (type.equals("ServerType")) return ClassTypeFactory.getType(RPCString.class);

        try {

            ClassType<?> t;
            if (!type.endsWith("[]")) t = ClassTypeFactory.getType(classForSimpleName(type));
            else {
                // array
                String nonArrayType = type.substring(0, type.length()-2);
                Class<?> nonArrayClass = ClassTypeFactory.getType(classForSimpleName(nonArrayType)).getClass();
                t = ClassTypeFactory.getTemplateType(Collection.class, nonArrayClass);
            }

            return t;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String typeToName(ClassType<?> type) {
        if (type instanceof ClassType) return type.getClassType().getName();
        else if (type instanceof TemplateClassType) return type.getClassType().getName() + "<" + ((TemplateClassType)type).getSubtype().getClass().getName() + ">";
        return "?";
    }
}
