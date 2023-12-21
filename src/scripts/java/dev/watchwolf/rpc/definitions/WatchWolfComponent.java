package dev.watchwolf.rpc.definitions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WatchWolfComponent {
    private String name;
    private String description;
    private String version;

    @SerializedName(value = "DestinyId")
    private int destinyId;

    private List<Petition> petitions;

    @SerializedName(value = "AsyncReturns")
    private List<Event> asyncReturns;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVersion() {
        return version;
    }

    public int getDestinyId() {
        return destinyId;
    }

    public List<Petition> getPetitions() {
        return petitions;
    }

    public List<Event> getAsyncReturns() {
        return asyncReturns;
    }
}
