package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class HornCoral extends Block {

	/*   --- CONSTRUCTORS ---   */
	public HornCoral(short id) {
		super(id, "HORN_CORAL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public HornCoral(int id) {
		this((short) id);
	}

	private HornCoral(HornCoral old) {
		this(old.id);
	}

}