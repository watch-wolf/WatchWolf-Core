package dev.watchwolf.rpc;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.watchwolf.rpc.definitions.WatchWolfComponent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DefinitionDataFactory {
    private static final String []LATEST_DEFINITIONS_LIST = {
            "https://raw.githubusercontent.com/watch-wolf/WatchWolf/076678d534f015515f8ee1367b854d9b97cd60f3/API/definitions/servers_manager.json"
    };

    public static List<WatchWolfComponent> get() throws IOException {
        List<WatchWolfComponent> r = new ArrayList<>();
        Gson gson = new Gson();

        for (String url : LATEST_DEFINITIONS_LIST) {
            // get the json
            String json = downloadTextFile(url);
            //System.out.println("[v] Got from " + url + ": " + json);

            // to object
            JsonElement rawComponent = JsonParser.parseString(json).getAsJsonObject().get("WatchWolfComponent");
            WatchWolfComponent component = gson.fromJson(rawComponent, WatchWolfComponent.class);
            r.add(component);
        }

        return r;
    }

    /**
     * Downloads a text file from the url and returns it
     * @author <a href="https://stackoverflow.com/a/7467629/9178470">https://stackoverflow.com/a/7467629/9178470</a>
     * @param urlString URL of the target file
     * @return File contents
     * @throws IOException Exception while downloading the file
     */
    private static String downloadTextFile(String urlString) throws IOException {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
