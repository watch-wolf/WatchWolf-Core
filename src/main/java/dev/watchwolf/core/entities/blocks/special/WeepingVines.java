package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class WeepingVines extends Block {

	/*   --- CONSTRUCTORS ---   */
	public WeepingVines(short id) {
		super(id, "WEEPING_VINES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public WeepingVines(int id) {
		this((short) id);
	}

	private WeepingVines(WeepingVines old) {
		this(old.id);
	}

}