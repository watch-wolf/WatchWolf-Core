package dev.watchwolf.core.entities.files.plugins;

public class UsualPlugin extends Plugin {
    private final String name;
    private final String version;
    private final Boolean isPremium;

    /**
     * A plugin that is already on the ServersManager
     * @param name Plugin name
     * @param version Plugin version (empty string or null if you don't care)
     * @param isPremium If the plugin is premium, or free
     */
    public UsualPlugin(String name, String version, boolean isPremium) {
        this.name = name;
        this.version = version;
        this.isPremium = isPremium;
    }

    public UsualPlugin(String name, String version) {
        this.name = name;
        this.version = version;
        this.isPremium = null;
    }

    public UsualPlugin(String name) {
        this(name, null);
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public Boolean isPremium() {
        return this.isPremium;
    }

    @Override
    public String toString() {
        return "UsualPlugin{" + this.name + (this.version != null ? (" v" + this.version) : "") + "}";
    }
}
