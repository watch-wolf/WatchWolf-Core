package dev.watchwolf.core.rpc.objects.converter;

public class RPCObjectsConverterFactory {
    public RPCObjectsConverterFactory() {}

    public RPCConverter build() {
        RPCConverter r = new RPCConverter();
        // TODO add all classes in `.objects.types` that implements `RpcConverter` and contains `@MainSubconverter`
        return r;
    }
}
