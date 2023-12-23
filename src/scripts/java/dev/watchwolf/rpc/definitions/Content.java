package dev.watchwolf.rpc.definitions;

public class Content {
    private String type;
    private String name;
    private String description;
    private Object value;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    /**
     * Converts a string to lowerCamelCase
     * @author <a href="https://www.baeldung.com/java-string-to-camel-case">https://www.baeldung.com/java-string-to-camel-case</a>
     * @param text Text to convert
     * @return lowerCamelCased string
     */
    private static String toLowerCamelCase(String text) {
        String[] words = text.split("[\\W_]+");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    public String getVariableName() {
        return toLowerCamelCase(this.getName());
    }

    public String getDescription() {
        return description;
    }

    public Object getValue() {
        return this.value;
    }
}
