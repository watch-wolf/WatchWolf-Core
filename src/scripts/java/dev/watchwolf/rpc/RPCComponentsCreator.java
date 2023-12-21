package dev.watchwolf.rpc;

import de.dev.eth0.jcodegen.constants.Modifier;
import de.dev.eth0.jcodegen.elements.Field;
import de.dev.eth0.jcodegen.elements.JavaClass;
import de.dev.eth0.jcodegen.elements.Method;
import de.dev.eth0.jcodegen.elements.Parameter;
import dev.watchwolf.core.rpc.RPC;
import dev.watchwolf.core.rpc.RPCImplementer;
import dev.watchwolf.core.rpc.channel.MessageChannel;
import dev.watchwolf.core.rpc.objects.converter.RPCConverter;
import dev.watchwolf.rpc.definitions.Content;
import dev.watchwolf.rpc.definitions.Event;
import dev.watchwolf.rpc.definitions.WatchWolfComponent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RPCComponentsCreator {
    private static JavaClass getCommonClass(String pckge, String localName, String []localImplements, WatchWolfComponent component) {
        JavaClass clazz = new JavaClass(pckge, localName, "", localImplements);

        // info&warning
        clazz.addCommentLine("RPC implementation of " + component.getName() + " to run the requests locally.");
        clazz.addCommentLine("/!\\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\\");
        clazz.addCommentLine("");
        clazz.addCommentLine(component.getDescription());

        Method localForwardMethod = new Method(void.class, "forwardCall");
        localForwardMethod.addParameter(new Parameter(MessageChannel.class.getName(), "channel"));
        localForwardMethod.addParameter(new Parameter(RPCConverter.class.getName() + "<?>", "converter"));
        localForwardMethod.addModifier(Modifier.PUBLIC);
        clazz.addElement(localForwardMethod);

        // RPC node
        Field rpcNode = new Field(RPC.class.getName(), "rpc");
        rpcNode.addModifier(Modifier.PRIVATE);

        Method setRPCMethod = new Method(void.class, "setHandler");
        setRPCMethod.addParameter(new Parameter(RPC.class.getName(), "handler"));
        setRPCMethod.addModifier(Modifier.PUBLIC);
        setRPCMethod.addContent("this.rpc = handler;");

        clazz.addElement(setRPCMethod);
        clazz.addField(rpcNode);
        return clazz;
    }

    private static Method getForwardMethod(JavaClass clazz) {
        return (Method)clazz.getElements().stream().filter(e -> e instanceof Method)
                                .filter(m -> ((Method)m).getName().equals("forwardCall"))
                                .findFirst().orElse(null);
    }

    private static Method getInterfaceMethod(Event event) {
        Method interfaceMethod = new Method(void.class, event.getFunctionName());

        interfaceMethod.addCommentLine(event.getDescription());
        if (event.getRelatesTo() != null) interfaceMethod.addCommentLine("Relates to `" + event.getRelatesTo() + "` method");

        for (Content content : event.getContents()) {
            if (content.getType().startsWith("_")) continue; // internal use

            Parameter param = new Parameter(TypeToRPCType.typeToName(TypeToRPCType.getType(content.getType())), content.getName());
            if (content.getDescription() != null) interfaceMethod.addCommentLine("@param " + content.getName() + ": " + content.getDescription());
            interfaceMethod.addParameter(param);
        }

        return interfaceMethod;
    }

    private static Method getInterfaceMethodForClass(Event event) {
        Method interfaceMethod = getInterfaceMethod(event);
        interfaceMethod.addModifier(Modifier.PUBLIC);
        interfaceMethod.addCommentLine("Overrides method defined by interface `" + event.getClassName() + "`");
        // TODO override modifier
        return interfaceMethod;
    }

    private static void generateRPCEvent(Event event, String componentName, String stubsOutPackage, String stubsOutPath) {
        JavaClass clazz = new JavaClass(stubsOutPackage, event.getClassName());
        clazz.addModifier(Modifier.PUBLIC);

        clazz.addCommentLine("Event produced by " + componentName);
        clazz.addCommentLine("/!\\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\\");

        // create & add method
        clazz.addElement( getInterfaceMethod(event) );

        // write to file
        String localPath = stubsOutPath + "/" + event.getClassName() + ".java";
        try (FileWriter writer = new FileWriter(localPath);
             BufferedWriter bwr = new BufferedWriter(writer);) {
            String contents = clazz.toString()
                                    .replaceAll("class " + event.getClassName(), "interface " + event.getClassName());
            bwr.write(contents);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateRPCComponents(WatchWolfComponent component, String stubsOutPackage, String stubsOutPath, String subsystemOutPackage, String subsystemOutPath) {
        //
        // 1. Generate the events
        //
        for (Event e : component.getAsyncReturns()) generateRPCEvent(e, component.getName(), stubsOutPackage, stubsOutPath);

        // ---------------------
        // 1. Get the local stub
        // ---------------------
        String localName = component.getName().replaceAll("\\s", "") + "LocalStub";
        List<String> localImplements = new ArrayList<>();
        localImplements.add(RPCImplementer.class.getName());
        for (Event e : component.getAsyncReturns()) localImplements.add(stubsOutPackage + "." + e.getClassName()); // the LocalStub implements all events
        JavaClass localClass = getCommonClass(stubsOutPackage, localName, localImplements.toArray(new String[0]), component);
        localClass.addModifier(Modifier.PUBLIC);

        Method localForwardMethod = getForwardMethod(localClass);
        if (localForwardMethod == null) throw new RuntimeException("Couldn't get the 'forward' method");
        localForwardMethod.addContent(""); // TODO

        for (Event event : component.getAsyncReturns()) {
            Method eventMethod = getInterfaceMethodForClass(event);
            eventMethod.addContent(""); // TODO
            localClass.addElement(eventMethod);
        }

        // ----------------------
        // 2. Get the remote stub
        // ----------------------
        // TODO

        // ----------------
        // 3. write to file
        // ----------------
        String localPath = stubsOutPath + "/" + localName + ".java";
        try (FileWriter writer = new FileWriter(localPath);
             BufferedWriter bwr = new BufferedWriter(writer);) {
            bwr.write(localClass.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Function<String,String> subsystemToStubsPath = (s) -> "src/main/java/dev/watchwolf/core/rpc/stubs/" + s,
                                subsystemToStubsPackage = (s) -> "dev.watchwolf.core.rpc.stubs." + s,
                                subsystemToSubsystemPath = (s) -> "src/main/java/dev/watchwolf/" + s + "/rpc",
                                subsystemToSubsystemPackage = (s) -> "dev.watchwolf." + s + ".rpc";

        for (WatchWolfComponent component : DefinitionDataFactory.get()) {
            String subsystem = component.getName().replaceAll("\\s", "").toLowerCase();
            generateRPCComponents(component,
                    subsystemToStubsPackage.apply(subsystem), subsystemToStubsPath.apply(subsystem),
                    subsystemToSubsystemPackage.apply(subsystem), subsystemToSubsystemPath.apply(subsystem));
        }
    }
}
