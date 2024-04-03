package dev.watchwolf.core.entities.entities;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public enum EntityType {
	DROPPED_ITEM,
	EXPERIENCE_ORB,
	AREA_EFFECT_CLOUD,
	ELDER_GUARDIAN,
	WITHER_SKELETON,
	STRAY,
	EGG,
	LEASH_HITCH,
	PAINTING,
	ARROW,
	SNOWBALL,
	FIREBALL,
	SMALL_FIREBALL,
	ENDER_PEARL,
	ENDER_SIGNAL,
	SPLASH_POTION,
	THROWN_EXP_BOTTLE,
	ITEM_FRAME,
	WITHER_SKULL,
	PRIMED_TNT,
	FALLING_BLOCK,
	FIREWORK,
	HUSK,
	SPECTRAL_ARROW,
	SHULKER_BULLET,
	DRAGON_FIREBALL,
	ZOMBIE_VILLAGER,
	SKELETON_HORSE,
	ZOMBIE_HORSE,
	ARMOR_STAND,
	DONKEY,
	MULE,
	EVOKER_FANGS,
	EVOKER,
	VEX,
	VINDICATOR,
	ILLUSIONER,
	MINECART_COMMAND,
	BOAT,
	MINECART,
	MINECART_CHEST,
	MINECART_FURNACE,
	MINECART_TNT,
	MINECART_HOPPER,
	MINECART_MOB_SPAWNER,
	CREEPER,
	SKELETON,
	SPIDER,
	GIANT,
	ZOMBIE,
	SLIME,
	GHAST,
	ZOMBIFIED_PIGLIN,
	ENDERMAN,
	CAVE_SPIDER,
	SILVERFISH,
	BLAZE,
	MAGMA_CUBE,
	ENDER_DRAGON,
	WITHER,
	BAT,
	WITCH,
	ENDERMITE,
	GUARDIAN,
	SHULKER,
	PIG,
	SHEEP,
	COW,
	CHICKEN,
	SQUID,
	WOLF,
	MUSHROOM_COW,
	SNOWMAN,
	OCELOT,
	IRON_GOLEM,
	HORSE,
	RABBIT,
	POLAR_BEAR,
	LLAMA,
	LLAMA_SPIT,
	PARROT,
	VILLAGER,
	ENDER_CRYSTAL,
	TURTLE,
	PHANTOM,
	TRIDENT,
	COD,
	SALMON,
	PUFFERFISH,
	TROPICAL_FISH,
	DROWNED,
	DOLPHIN,
	CAT,
	PANDA,
	PILLAGER,
	RAVAGER,
	TRADER_LLAMA,
	WANDERING_TRADER,
	FOX,
	BEE,
	HOGLIN,
	PIGLIN,
	STRIDER,
	ZOGLIN,
	PIGLIN_BRUTE,
	AXOLOTL,
	GLOW_ITEM_FRAME,
	GLOW_SQUID,
	GOAT,
	MARKER,
	ALLAY,
	CHEST_BOAT,
	FROG,
	TADPOLE,
	WARDEN,
	FISHING_HOOK,
	LIGHTNING,
	PLAYER;

	/**
     * Get the EntityType using a class
     * @param cls Entity subclass
     * @return EntityType of the class
     * @throws IllegalArgumentException shouldn't happen; the specified class doesn't exist as EntityType
     */
    public static EntityType getType(Class<? extends Entity> cls) throws IllegalArgumentException {
        return EntityType.valueOf(EntityType.classNameToEnumName(cls.getSimpleName()));
    }

    private static String classNameToEnumName(String className) {
        StringBuilder sb = new StringBuilder(className);
        for (int i = 1; i < sb.length() - 1; i++) {
            if (sb.charAt(i) >= 'A' && sb.charAt(i) <= 'Z') {
                sb.insert(i, '_');
                i++; // now the index 'i' is at 'i+1'
            }
        }
        return sb.toString().toUpperCase();
    }

	public String toClassName() {
		StringBuilder sb = new StringBuilder(this.name().toLowerCase()); // enum name as lower
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0))); // first must be upper
		// if '_' present, remove it and change the next as upper
		for (int i = 1; i < sb.length() - 1; i++) {
			if (sb.charAt(i) == '_') {
				sb.setCharAt(i+1, Character.toUpperCase(sb.charAt(i+1)));
				sb.deleteCharAt(i);
				// we're removing the lenght by 1, but we'll increase it too, so we'll skip this "upper character"
			}
		}
		return sb.toString();
	}

    /**
     * Get the EntityType using an instance
     * @param e Entity instance
     * @return EntityType of the instance
     * @throws IllegalArgumentException shouldn't happen; the specified class doesn't exist as EntityType
     */
    public static EntityType getType(Entity e) throws IllegalArgumentException {
        return getType(e.getClass());
    }
}