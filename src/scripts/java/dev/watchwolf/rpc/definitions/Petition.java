package dev.watchwolf.rpc.definitions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Petition {
    private String name;
    private String description;
    @SerializedName(value = "FunctionName")
    private String functionName;
    private String RelatesTo;
    private List<Content> contents;
    @SerializedName(value = "return")
    private Petition returns;

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

    public Petition getReturns() {
        return this.returns;
    }
}
