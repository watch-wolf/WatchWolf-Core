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
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassType;
import dev.watchwolf.core.rpc.objects.converter.class_type.ClassTypeFactory;
import dev.watchwolf.core.rpc.objects.converter.class_type.TemplateClassType;
import dev.watchwolf.core.rpc.objects.types.RPCObject;
import dev.watchwolf.core.rpc.objects.types.natives.composited.RPCString;
import dev.watchwolf.core.rpc.objects.types.natives.primitive.RPCByte;
import dev.watchwolf.rpc.definitions.Content;
import dev.watchwolf.rpc.definitions.Event;
import dev.watchwolf.rpc.definitions.Petition;
import dev.watchwolf.rpc.definitions.WatchWolfComponent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RPCComponentsCreator {
    private static JavaClass getCommonClass(String pckge, String localName, String []localImplements, String []runners, WatchWolfComponent component) {
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
        rpcNode.addCommentLine("The RPC node will be the one that will run the events raised");

        Method setRPCMethod = new Method(void.class, "setHandler");
        setRPCMethod.addParameter(new Parameter(RPC.class.getName(), "handler"));
        setRPCMethod.addModifier(Modifier.PUBLIC);
        setRPCMethod.addContent("\tthis.rpc = handler;");

        clazz.addElement(setRPCMethod);
        clazz.addField(rpcNode);

        // Runner node
        Field runnerNode = new Field(runners[0], "runner");
        runnerNode.addModifier(Modifier.PRIVATE);
        runnerNode.addCommentLine("The runner will be the one that will run the petitions captured");

        Method setRunnerMethod = new Method(void.class, "setRunner");
        setRunnerMethod.addParameter(new Parameter(runners[0], "runner"));
        setRunnerMethod.addModifier(Modifier.PUBLIC);
        setRunnerMethod.addContent("\tthis.runner = runner;");

        clazz.addElement(setRunnerMethod);
        clazz.addField(runnerNode);

        return clazz;
    }

    private static Method getForwardMethod(JavaClass clazz) {
        return (Method)clazz.getElements().stream().filter(e -> e instanceof Method)
                                .filter(m -> ((Method)m).getName().equals("forwardCall"))
                                .findFirst().orElse(null);
    }

    private static Method getInterfaceEventMethod(Event event) {
        Method interfaceMethod = new Method(void.class, event.getFunctionName());

        interfaceMethod.addCommentLine(event.getDescription());
        if (event.getRelatesTo() != null) interfaceMethod.addCommentLine("Relates to `" + event.getRelatesTo() + "` method");

        for (Content content : event.getContents()) {
            if (content.getType().startsWith("_")) continue; // internal use

            Parameter param = new Parameter(TypeToRPCType.typeToName(TypeToRPCType.getType(content.getType())), content.getVariableName());
            if (content.getDescription() != null) interfaceMethod.addCommentLine("@param " + content.getVariableName() + ": " + content.getDescription());
            interfaceMethod.addParameter(param);
        }

        return interfaceMethod;
    }

    private static Method getClassEventMethod(Event event) {
        Method interfaceMethod = getInterfaceEventMethod(event);
        interfaceMethod.addModifier(Modifier.PUBLIC);
        interfaceMethod.addCommentLine("Overrides method defined by interface `" + event.getClassName() + "`");
        // TODO override modifier
        return interfaceMethod;
    }

    private static Method []getInterfacePetitionMethods(List<Petition> petitions) {
        Method []interfaceMethods = new Method[petitions.size()];
        for (int n = 0; n < petitions.size(); n++) {
            Method interfaceMethod = new Method(void.class, petitions.get(n).getFunctionName());

            interfaceMethod.addCommentLine(petitions.get(n).getDescription());

            for (Content content : petitions.get(n).getContents()) {
                if (content.getType().startsWith("_")) continue; // internal use

                Parameter param = new Parameter(TypeToRPCType.typeToName(TypeToRPCType.getType(content.getType())), content.getVariableName());
                if (content.getDescription() != null) interfaceMethod.addCommentLine("@param " + content.getVariableName() + ": " + content.getDescription());
                interfaceMethod.addParameter(param);
            }

            interfaceMethods[n] = interfaceMethod;
        }

        return interfaceMethods;
    }

    private static Method []getClassPetitionMethod(List<Petition> petitions) {
        Method []classMethods = new Method[petitions.size()];
        for (int n = 0; n < petitions.size(); n++) {
            Method classMethod = new Method(void.class, petitions.get(n).getFunctionName());
            classMethod.addModifier(Modifier.PRIVATE);

            // comments
            classMethod.addCommentLine(petitions.get(n).getDescription());
            StringBuilder sb = new StringBuilder();
            for (Content content : petitions.get(n).getContents()) {
                if (content.getType().startsWith("_")) continue; // internal use

                sb.append("- ").append(content.getVariableName()).append((content.getDescription() != null) ? (": " + content.getDescription()) : "").append("\n");
            }
            if (sb.length() > 0) {
                sb.insert(0, "This method will extract the following parameters for the `runner`:\n");
                classMethod.addCommentLine(sb.toString());
            }

            // arguments
            Parameter channelParam = new Parameter(MessageChannel.class.getName(), "channel"),
                    converterParam = new Parameter(RPCConverter.class.getName() + "<?>", "converter");
            classMethod.addParameter(channelParam);
            classMethod.addParameter(converterParam);

            // contents
            StringBuilder params = new StringBuilder(); // params list to call the function
            for (Content content : petitions.get(n).getContents()) {
                if (content.getType().startsWith("_")) continue; // internal use

                ClassType<?> nativeType = TypeToRPCType.getType(content.getType());
                if (nativeType == null) throw new RuntimeException("Couldn't get the type of '" + content.getType() + "'");
                ClassType<? extends RPCObject> rpcType = TypeToRPCType.getRPCType(nativeType);
                String unmarshallClassParam = TypeToRPCType.typeToName(rpcType) + ".class";
                if (rpcType instanceof TemplateClassType) unmarshallClassParam = ClassTypeFactory.class.getName() + ".getTemplateType(" + rpcType.getName() /* don't use `typeToName` here; we need the raw class */ + ".class, " + TypeToRPCType.typeToName(((TemplateClassType)rpcType).getSubtype()) + ".class)";
                classMethod.addContent("\t" + TypeToRPCType.typeToName(nativeType) + " " + content.getVariableName() + " = converter.unmarshall(channel, " + unmarshallClassParam + ").getObject();");
                params.append(content.getVariableName()).append(", ");
            }
            if (params.length() > 0) params.setLength(params.length()-2); // remove last ', '
            classMethod.addContent("\tthis.runner." + petitions.get(n).getFunctionName() + "(" + params.toString() + ");");

            classMethods[n] = classMethod;
        }

        return classMethods;
    }

    private static void generateRPCEvent(Event event, String componentName, String stubsOutPackage, String stubsOutPath) {
        JavaClass clazz = new JavaClass(stubsOutPackage, event.getClassName());
        clazz.addModifier(Modifier.PUBLIC);

        clazz.addCommentLine("Event produced by " + componentName);
        clazz.addCommentLine("/!\\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\\");

        // create & add method
        Method interfaceMethod = getInterfaceEventMethod(event);
        clazz.addElement(interfaceMethod);

        // write to file
        String localPath = stubsOutPath + "/" + event.getClassName() + ".java";
        try (FileWriter writer = new FileWriter(localPath);
             BufferedWriter bwr = new BufferedWriter(writer);) {
            String contents = clazz.toString()
                                    .replaceAll("class " + event.getClassName(), "interface " + event.getClassName());
            // throws
            Pattern p = Pattern.compile("(" + interfaceMethod.getName() + "\\([^\\)]*\\))(;)");
            Matcher m = p.matcher(contents);
            if (m.find()) {
                contents = m.replaceAll("$1 throws java.io.IOException$2");
            }

            bwr.write(contents);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String fillEventCall(WatchWolfComponent component, Event event) {
        StringBuilder sb = new StringBuilder();

        List<Content> arguments = event.getContents();
        Content operation = arguments.get(0);
        if (!operation.getType().equals("_operation")) throw new IllegalArgumentException("Event must have a first content of type '_operation'");
        arguments = arguments.subList(1, arguments.size());

        int header_lsb = component.getDestinyId() | 0b0000_1_000
                            | ((((Number)operation.getValue()).intValue() & 0b1111) << 4),
            header_msb = ((Number)operation.getValue()).intValue() >> 4;

        sb.append("\tif (this.rpc == null) throw new ").append(RuntimeException.class.getName()).append("(\"Send event call before RPC instance\");\n")
            .append("\tsynchronized (this) {\n")
            .append("\t\tthis.rpc.sendEvent(\n");
        // header
        String header_lsb_str = Integer.toBinaryString(header_lsb);
        while (header_lsb_str.length() < 8) header_lsb_str = "0" + header_lsb_str;
        sb.append("\t\t\t\tnew " + RPCByte.class.getName() + "((byte) 0b" + header_lsb_str.substring(0, 4) + "_" + header_lsb_str.charAt(4) + "_" + header_lsb_str.substring(5, 8) + "),\n");
        String header_msb_str = Integer.toBinaryString(header_msb);
        while (header_msb_str.length() < 8) header_msb_str = "0" + header_msb_str;
        sb.append("\t\t\t\tnew " + RPCByte.class.getName() + "((byte) 0b" + header_msb_str + "),\n");
        // arguments
        for (Content arg : arguments) {
            ClassType<? extends RPCObject> rpcArgType = TypeToRPCType.getRPCType(arg.getType());
            if (rpcArgType == null) throw new IllegalArgumentException("Couldn't parse " + arg.getType() + " into RPC!\n");
            sb.append("\t\t\t\tnew " + TypeToRPCType.typeToName(rpcArgType) + "(" + arg.getName() + "),\n");
        }

        // done
        sb.setLength(sb.length()-2); // remove last ',\n'
        sb.append("\n\t\t);\n\t}");

        return sb.toString();
    }

    private static void generateMethodsInterface(String componentName, List<Petition> petitions, String pckge, String name, String outPath) {
        JavaClass clazz = new JavaClass(pckge, name);
        clazz.addModifier(Modifier.PUBLIC);

        clazz.addCommentLine("Petitions managed by " + componentName);
        clazz.addCommentLine("/!\\ Class generated automatically; do not modify. Please refer to dev.watchwolf.rpc.RPCComponentsCreator /!\\");

        // create & add method
        Method []petitionMethods = getInterfacePetitionMethods(petitions);
        for (Method petition : petitionMethods) clazz.addElement(petition);

        // write to file
        String localPath = outPath + "/" + name + ".java";
        try (FileWriter writer = new FileWriter(localPath);
             BufferedWriter bwr = new BufferedWriter(writer);) {
            String contents = clazz.toString()
                    .replaceAll("class " + name, "interface " + name);
            // throws
            for (Method petition : petitionMethods) {
                Pattern p = Pattern.compile("(" + petition.getName() + "\\([^\\)]*\\))(;)");
                Matcher m = p.matcher(contents);
                if (m.find()) {
                    contents = m.replaceAll("$1 throws java.io.IOException$2");
                }
            }

            bwr.write(contents);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void generateRPCComponents(WatchWolfComponent component, String stubsOutPackage, String stubsOutPath, String subsystemOutPackage, String subsystemOutPath) {
        // ----------------------
        // 1. Generate the events
        // ----------------------

        for (Event e : component.getAsyncReturns()) generateRPCEvent(e, component.getName(), stubsOutPackage, stubsOutPath);

        // ---------------------------------
        // 2. Generate the methods interface
        // ---------------------------------

        String methodsInterfaceName = component.getName().replaceAll("\\s", "") + "Petitions";
        generateMethodsInterface(component.getName(), component.getPetitions(), stubsOutPackage, methodsInterfaceName, stubsOutPath);

        // ---------------------
        // 3. Get the local stub
        // ---------------------

        String localName = component.getName().replaceAll("\\s", "") + "LocalStub";
        List<String> localImplements = new ArrayList<>();
        localImplements.add(RPCImplementer.class.getName());
        for (Event e : component.getAsyncReturns()) localImplements.add(stubsOutPackage + "." + e.getClassName()); // the LocalStub implements all events
        JavaClass localClass = getCommonClass(stubsOutPackage, localName, localImplements.toArray(new String[0]), new String[]{stubsOutPackage + "." + methodsInterfaceName}, component);
        localClass.addModifier(Modifier.PUBLIC);

        Method pLocalForwardMethod = new Method(void.class, "forwardCall");
        pLocalForwardMethod.addModifier(Modifier.PRIVATE);
        // TODO add "sub-group" for Server
        pLocalForwardMethod.addParameter(new Parameter(byte.class, "origin"))
                            .addParameter(new Parameter(boolean.class, "isReturn"))
                            .addParameter(new Parameter(short.class, "operation"))
                            .addParameter(new Parameter(MessageChannel.class.getName(), "channel"))
                            .addParameter(new Parameter(RPCConverter.class.getName() + "<?>", "converter"));
        if (component.getName().equals("Servers Manager")) {
            // legacy call
            pLocalForwardMethod.addContent("\tif (origin == 1 /* WW server is the origin */ && isReturn && operation == 2 /* 'server started' return */) {")
                                .addContent("\t\t// legacy call; read arguments and do nothing")
                                .addContent("\t\tconverter.unmarshall(channel, " + RPCString.class.getName() + ".class);")
                                .addContent("\t\treturn;")
                                .addContent("\t}");
        }
        // exceptions
        pLocalForwardMethod.addContent("\tif (origin != " + component.getDestinyId() + ") throw new " + RuntimeException.class.getName() + "(\"Got a request targeting a different component\");")
                            .addContent("\tif (isReturn) throw new " + RuntimeException.class.getName() + "(\"Got a return instead of a request\");");
        // TODO read requests
        localClass.addElement(pLocalForwardMethod);

        Method localForwardMethod = getForwardMethod(localClass);
        if (localForwardMethod == null) throw new RuntimeException("Couldn't get the 'forward' method");
        localForwardMethod.addContent("\tsynchronized (this) {")
                        .addContent("\t\tshort info = converter.unmarshall(channel, Short.class);")
                        .addContent("\t\tbyte origin = (byte)(info & 0b111);")
                        .addContent("\t\tboolean isReturn = (info & 0b1_000) > 0;")
                        .addContent("\t\tshort operation = (short)(info >> 4);")
                        .addContent()
                        .addContent("\t\tthis.forwardCall(origin, isReturn, operation, channel, converter);")
                        .addContent("\t}");

        List<Method> throwableMethods = new ArrayList<>();
        for (Method petition : getClassPetitionMethod(component.getPetitions())) {
            localClass.addElement(petition);
            throwableMethods.add(petition);
        }

        for (Event event : component.getAsyncReturns()) {
            Method eventMethod = getClassEventMethod(event);
            eventMethod.addContent(fillEventCall(component, event));
            localClass.addElement(eventMethod);
            throwableMethods.add(eventMethod);
        }

        // write to file
        String localPath = stubsOutPath + "/" + localName + ".java";
        try (FileWriter writer = new FileWriter(localPath);
             BufferedWriter bwr = new BufferedWriter(writer);) {
            String classText = localClass.toString();
            // throws
            for (Method eventMethod : throwableMethods) {
                Pattern p = Pattern.compile("(" + eventMethod.getName() + "\\([^\\)]*\\))(\\s*\\{)");
                Matcher m = p.matcher(classText);
                if (m.find()) {
                    classText = m.replaceAll("$1 throws java.io.IOException$2");
                }
            }
            Pattern p = Pattern.compile("(forwardCall\\([^\\)]*\\))(\\s*\\{)");
            Matcher m = p.matcher(classText);
            if (m.find()) {
                classText = m.replaceAll("$1 throws java.io.IOException$2");
            }

            bwr.write(classText);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // ----------------------
        // 4. Get the remote stub
        // ----------------------
        // TODO
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
