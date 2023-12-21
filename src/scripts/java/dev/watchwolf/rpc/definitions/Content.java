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

    public String getDescription() {
        return description;
    }

    public Object getValue() {
        return this.value;
    }
}
