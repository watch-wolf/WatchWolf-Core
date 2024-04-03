package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class Light extends Block {

	/*   --- CONSTRUCTORS ---   */
	public Light(short id) {
		super(id, "LIGHT", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public Light(int id) {
		this((short) id);
	}

	private Light(Light old) {
		this(old.id);
	}

}