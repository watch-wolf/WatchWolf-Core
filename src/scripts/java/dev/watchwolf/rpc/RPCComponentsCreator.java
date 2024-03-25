package dev.watchwolf.rpc;

import de.dev.eth0.jcodegen.constants.Modifier;
import de.dev.eth0.jcodegen.elements.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

        // Logger
        Field loggerNode = new Field(Logger.class.getName(), "logger");
        loggerNode.addModifier(Modifier.PRIVATE);
        loggerNode.addModifier(Modifier.FINAL);
        loggerNode.addCommentLine("Used to trace method enter/exit");
        clazz.addField(loggerNode);

        // RPC node
        Field rpcNode = new Field(RPC.class.getName(), "rpc");
        rpcNode.addModifier(Modifier.PRIVATE);
        rpcNode.addCommentLine("The RPC node will be the one that will run the events raised");

        Method setRPCMethod = new Method(void.class, "setHandler");
        setRPCMethod.addParameter(new Parameter(RPC.class.getName(), "handler"));
        setRPCMethod.addModifier(Modifier.PUBLIC);
        setRPCMethod.addContent("\tthis.logger.traceEntry(null, handler);");
        setRPCMethod.addContent("\tthis.rpc = handler;");
        setRPCMethod.addContent("\tthis.logger.traceExit();");

        clazz.addElement(setRPCMethod);
        clazz.addField(rpcNode);

        // constructor
        Constructor constructor = new Constructor(localName);
        constructor.addContent("\tthis.logger = " + LogManager.class.getName() + ".getLogger(\"" + pckge + "." + localName + "\");");
        clazz.addElement(constructor);

        // Runner node
        Field runnerNode = new Field(runners[0], "runner");
        runnerNode.addModifier(Modifier.PRIVATE);
        runnerNode.addCommentLine("The runner will be the one that will run the petitions captured");

        Method setRunnerMethod = new Method(void.class, "setRunner");
        setRunnerMethod.addParameter(new Parameter(runners[0], "runner"));
        setRunnerMethod.addModifier(Modifier.PUBLIC);
        setRunnerMethod.addContent("\tthis.logger.traceEntry(null, runner);");
        setRunnerMethod.addContent("\tthis.runner = runner;");
        setRunnerMethod.addContent("\tthis.logger.traceExit();");

        clazz.addElement(setRunnerMethod);
        clazz.addField(runnerNode);

        return clazz;
    }

    private static Method getForwardMethod(JavaClass clazz) {
        return (Method)clazz.getElements().stream().filter(e -> e instanceof Method)
                                .filter(m -> ((Method)m).getName().equals("forwardCall"))
                                .findFirst().orElse(null);
    }

    /**
     * Gets the properties that an event sends
     * @param event Event to extract the information
     * @return list of tuple (argument type,(variable name, description))
     */
    private static List<Map.Entry<String,Map.Entry<String, String>>> getEventArguments(Event event) {
        List<Map.Entry<String,Map.Entry<String, String>>> arguments = new ArrayList<>();
        for (Content content : event.getContents()) {
            if (content.getType().startsWith("_")) continue; // internal use

            arguments.add(new AbstractMap.SimpleEntry<>(TypeToRPCType.typeToName(TypeToRPCType.getType(content.getType())), new AbstractMap.SimpleEntry<>(content.getVariableName(), content.getDescription())));
        }
        return arguments;
    }

    private static Method getInterfaceEventMethod(Event event) {
        Method interfaceMethod = new Method(void.class, event.getFunctionName());

        interfaceMethod.addCommentLine(event.getDescription());
        if (event.getRelatesTo() != null) interfaceMethod.addCommentLine("Relates to `" + event.getRelatesTo() + "` method");

        for (Map.Entry<String,Map.Entry<String,String>> argument : getEventArguments(event)) {
            Parameter param = new Parameter(argument.getKey(), argument.getValue().getKey());
            if (argument.getValue().getValue() != null) interfaceMethod.addCommentLine("@param " + argument.getValue().getKey() + ": " + argument.getValue().getValue());
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
            Content returnType = (petitions.get(n).getReturns() == null || petitions.get(n).getReturns().getContents().isEmpty()) ? null : petitions.get(n).getReturns().getContents().get(0);
            Method interfaceMethod = new Method(returnType == null ? "void" : TypeToRPCType.typeToName(TypeToRPCType.getType(returnType.getType())), petitions.get(n).getFunctionName());

            interfaceMethod.addCommentLine(petitions.get(n).getDescription());

            for (Content content : petitions.get(n).getContents()) {
                if (content.getType().startsWith("_")) continue; // internal use

                Parameter param = new Parameter(TypeToRPCType.typeToName(TypeToRPCType.getType(content.getType())), content.getVariableName());
                if (content.getDescription() != null) interfaceMethod.addCommentLine("@param " + content.getVariableName() + ": " + content.getDescription());
                interfaceMethod.addParameter(param);
            }
            if (returnType != null) interfaceMethod.addCommentLine("@return " + returnType.getDescription());

            interfaceMethods[n] = interfaceMethod;
        }

        return interfaceMethods;
    }

    private static Method []getClassPetitionMethod(int destiny, List<Petition> petitions) {
        Method []classMethods = new Method[petitions.size()];
        for (int n = 0; n < petitions.size(); n++) {
            Content returnType = (petitions.get(n).getReturns() == null || petitions.get(n).getReturns().getContents().isEmpty()) ? null : petitions.get(n).getReturns().getContents().get(0);
            Method classMethod = new Method(void.class, petitions.get(n).getFunctionName());
            classMethod.addModifier(Modifier.PRIVATE);

            // comments
            classMethod.addCommentLine(petitions.get(n).getDescription());
            StringBuilder sb = new StringBuilder();
            for (Content content : petitions.get(n).getContents()) {
                if (content.getType().startsWith("_")) continue; // internal use

                sb.append("* - ").append(content.getVariableName()).append((content.getDescription() != null) ? (": " + content.getDescription()) : "").append("\n");
            }
            if (sb.length() > 0) {
                sb.insert(0, "This method will extract the following parameters for the `runner`:\n");
                sb.setLength(sb.length()-1); // remove last '\n'
                classMethod.addCommentLine(sb.toString());
            }
            if (returnType != null) classMethod.addCommentLine("This method will return: " + returnType.getName() + " - " + returnType.getDescription());

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
                String unmarshallClassParam = TypeToRPCType.typeToName(nativeType) + ".class";
                if (nativeType instanceof TemplateClassType) {
                    unmarshallClassParam = ClassTypeFactory.class.getName() + ".getTemplateType(" + nativeType.getName() /* don't use `typeToName` here; we need the raw class */ + ".class, " + ((TemplateClassType)nativeType).getSubtype().getName() + ".class)";
                }
                classMethod.addContent("\t" + TypeToRPCType.typeToName(nativeType) + " " + content.getVariableName() + " = " + "converter.unmarshall(channel, " + unmarshallClassParam + ");");
                params.append(content.getVariableName()).append(", ");
            }
            if (params.length() > 0) {
                params.setLength(params.length()-2); // remove last ', '
                classMethod.addContent(""); // leave some space
            }
            String varAssignation = (returnType == null) ? "" : (TypeToRPCType.typeToName(TypeToRPCType.getType(returnType.getType())) + " " + returnType.getVariableName() + " = ");
            classMethod.addContent("\tthis.logger.traceEntry(" + ((params.length() == 0) ? "" : ("null, " + params.toString())) + ");")
                        .addContent("\t" + varAssignation + "this.runner." + petitions.get(n).getFunctionName() + "(" + params.toString() + ");")
                        .addContent("\tthis.logger.traceExit(" + ((returnType == null) ? "" : returnType.getVariableName()) + ");");

            if (returnType != null) {
                // header
                classMethod.addContent("\n\tnew " + RPCByte.class.getName() + "(" + getLSBOperationByte(destiny, true, petitions.get(n).getOperationId()) + ").send(channel);");
                classMethod.addContent("\tnew " + RPCByte.class.getName() + "(" + getMSBOperationByte(petitions.get(n).getOperationId()) + ").send(channel);");

                // arguments
                ClassType<? extends RPCObject> rpcArgType = TypeToRPCType.getRPCType(returnType.getType());
                if (rpcArgType == null) throw new IllegalArgumentException("Couldn't parse " + returnType.getType() + " into RPC!\n");
                classMethod.addContent("\tnew " + TypeToRPCType.typeToName(rpcArgType) + "(" + returnType.getVariableName() + ").send(channel);");
            }

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

    private static String getLSBOperationByte(int destiny, boolean isReturn, int operation) {
        int header_lsb = destiny | (isReturn ? 0b0000_1_000 : 0)
                | ((operation & 0b1111) << 4);

        String header_lsb_str = Integer.toBinaryString(header_lsb);
        while (header_lsb_str.length() < 8) header_lsb_str = "0" + header_lsb_str;

        return "(byte) 0b" + header_lsb_str.substring(0, 4) + "_" + header_lsb_str.charAt(4) + "_" + header_lsb_str.substring(5, 8);
    }

    private static String getMSBOperationByte(int operation) {
        int header_msb = operation >> 4;

        String header_msb_str = Integer.toBinaryString(header_msb);
        while (header_msb_str.length() < 8) header_msb_str = "0" + header_msb_str;

        return "(byte) 0b" + header_msb_str;
    }

    private static String fillEventCall(WatchWolfComponent component, Event event) {
        StringBuilder sb = new StringBuilder();

        List<Content> arguments = event.getContents();
        Content operation = arguments.get(0);
        if (!operation.getType().equals("_operation")) throw new IllegalArgumentException("Event must have a first content of type '_operation'");
        arguments = arguments.subList(1, arguments.size());

        List<String> sentArguments = new ArrayList<>();
        for (Map.Entry<String,Map.Entry<String,String>> args : getEventArguments(event)) {
            sentArguments.add(args.getValue().getKey());
        }

        sb.append("\tthis.logger.traceEntry(" + String.join(", ", sentArguments) + ");\n")
            .append("\tif (this.rpc == null) throw this.logger.throwing(new ").append(RuntimeException.class.getName()).append("(\"Got 'send event' call before RPC instance\"));\n")
            .append("\tsynchronized (this) {\n")
            .append("\t\tthis.rpc.sendEvent(\n")
        // header
            .append("\t\t\t\tnew " + RPCByte.class.getName() + "(" + getLSBOperationByte(component.getDestinyId(), true, operation.getIntValue()) + "),\n")
            .append("\t\t\t\tnew " + RPCByte.class.getName() + "(" + getMSBOperationByte(operation.getIntValue()) + "),\n");
        // arguments
        for (Content arg : arguments) {
            ClassType<? extends RPCObject> rpcArgType = TypeToRPCType.getRPCType(arg.getType());
            if (rpcArgType == null) throw new IllegalArgumentException("Couldn't parse " + arg.getType() + " into RPC!\n");
            sb.append("\t\t\t\tnew " + TypeToRPCType.typeToName(rpcArgType) + "(" + arg.getName() + "),\n");
        }
        sb.setLength(sb.length()-2); // remove last ',\n'

        // done
        sb.append("\n\t\t);\n\t}\n")
            .append("\tthis.logger.traceExit();"); // the "return" is set as the argument

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

        pLocalForwardMethod.addContent("\tthis.logger.traceEntry(null, origin, isReturn, operation, channel, converter);");

        if (component.getName().equals("Servers Manager")) {
            // legacy call
            pLocalForwardMethod.addContent("\tif (origin == 1 /* WW server is the origin */ && isReturn && operation == 2 /* 'server started' return */) {")
                                .addContent("\t\t// legacy call; read arguments and do nothing")
                                .addContent("\t\tconverter.unmarshall(channel, " + RPCString.class.getName() + ".class);")
                                .addContent("\t\tthis.logger.traceExit();")
                                .addContent("\t\treturn;")
                                .addContent("\t}");
        }

        // exceptions
        pLocalForwardMethod.addContent("")
                            .addContent("\t// arg guards")
                            .addContent("\tif (origin != " + component.getDestinyId() + ") throw this.logger.throwing(new " + RuntimeException.class.getName() + "(\"Got a request targeting a different component\"));")
                            .addContent("\tif (isReturn) throw this.logger.throwing(new " + RuntimeException.class.getName() + "(\"Got a return instead of a request\"));");

        // ServersManager needs the source IP to reply
        if (component.getName().equals("Servers Manager")) {
            // add the variable
            Field latestMessageChannelPetitionNode = new Field(MessageChannel.class.getName(), "latestMessageChannelPetition");
            latestMessageChannelPetitionNode.addModifier(Modifier.PRIVATE);
            latestMessageChannelPetitionNode.addCommentLine("Latest MessageChannel petition got");
            localClass.addField(latestMessageChannelPetitionNode);

            // save the variable
            pLocalForwardMethod.addContent("\n\tthis." + latestMessageChannelPetitionNode.getName() + " = channel;");

            // get the variable method
            Method getLatestMessageChannelPetitionNodeMethod = new Method(MessageChannel.class.getName(), "getLatestMessageChannelPetitionNode");
            getLatestMessageChannelPetitionNodeMethod.addModifier(Modifier.PUBLIC);
            getLatestMessageChannelPetitionNodeMethod.addModifier(Modifier.SYNCHRONIZED);
            getLatestMessageChannelPetitionNodeMethod.addContent("\tthis.logger.traceEntry();");
            getLatestMessageChannelPetitionNodeMethod.addContent("\treturn this.logger.traceExit(this." + latestMessageChannelPetitionNode.getName() + ");");
            localClass.addElement(getLatestMessageChannelPetitionNodeMethod);
        }

        // operations
        pLocalForwardMethod.addContent("\n\t// operation calls");
        boolean firstIf = true;
        for (Petition petition : component.getPetitions()) {
            String msg = "\t";
            if (firstIf) firstIf = false;
            else msg += "else ";

            Content operationNumber = petition.getContents().get(0);
            if (!operationNumber.getType().equals("_operation")) throw new RuntimeException("Expected 'operation' type in first content");
            msg += "if (operation == " + ((Number)operationNumber.getValue()).intValue() + ") " + petition.getFunctionName() + "(channel, converter);";
            pLocalForwardMethod.addContent(msg);
        }
        String msg = "\t";
        if (!firstIf) msg += "else ";
        msg += "throw this.logger.throwing(new " + UnsupportedOperationException.class.getName() + "(\"Got unsupported operation: \" + operation)); // no match";
        pLocalForwardMethod.addContent(msg)
                        .addContent("\tthis.logger.traceExit();");
        localClass.addElement(pLocalForwardMethod);

        Method localForwardMethod = getForwardMethod(localClass);
        if (localForwardMethod == null) throw new RuntimeException("Couldn't get the 'forward' method");
        localForwardMethod.addContent("\tthis.logger.traceEntry();")
                        .addContent("\tsynchronized (this) {")
                        .addContent("\t\tshort info = converter.unmarshall(channel, Short.class);")
                        .addContent("\t\tbyte origin = (byte)(info & 0b111);")
                        .addContent("\t\tboolean isReturn = (info & 0b1_000) > 0;")
                        .addContent("\t\tshort operation = (short)(info >> 4);")
                        .addContent("\t\t")
                        .addContent("\t\tthis.forwardCall(origin, isReturn, operation, channel, converter);")
                        .addContent("\t}")
                        .addContent("\tthis.logger.traceExit();");

        List<Method> throwableMethods = new ArrayList<>();
        for (Method petition : getClassPetitionMethod(component.getDestinyId(), component.getPetitions())) {
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
