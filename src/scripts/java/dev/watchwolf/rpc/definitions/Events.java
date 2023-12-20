package dev.watchwolf.rpc.definitions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Events {
    private String name;
    private String description;
    @SerializedName(value = "FunctionName")
    private String functionName;
    private String RelatesTo;
    private List<Content> contents;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<Content> getContents() {
        return contents;
    }
}
