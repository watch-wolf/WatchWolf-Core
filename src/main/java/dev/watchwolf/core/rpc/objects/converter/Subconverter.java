package dev.watchwolf.core.rpc.objects.converter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Subconverter {
    public Class<? extends RPCConverter<?>> value();
}
