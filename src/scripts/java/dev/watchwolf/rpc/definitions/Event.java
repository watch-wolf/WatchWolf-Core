package dev.watchwolf.rpc.definitions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Event {
    private String name;
    private String description;
    @SerializedName(value = "FunctionName")
    private String functionName;
    @SerializedName(value = "RelatesTo")
    private String relatesTo;
    private List<Content> contents;

    public String getClassName() {
        return this.getFunctionName().substring(0, 1).toUpperCase() + this.getFunctionName().substring(1) + "Event";
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFunctionName() {
        return functionName;
    }

    public String getRelatesTo() {
        return this.relatesTo;
    }

    public List<Content> getContents() {
        return contents;
    }
}
