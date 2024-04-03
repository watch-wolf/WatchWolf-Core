package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class FireCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public FireCoral(short id) {
		super(id, "FIRE_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public FireCoral(int id) {
		this((short) id);
	}

	private FireCoral(FireCoral old) {
		this(old.id);
	}

}