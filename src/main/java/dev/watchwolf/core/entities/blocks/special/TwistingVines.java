package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class TwistingVines extends Block {

	/*   --- CONSTRUCTORS ---   */
	public TwistingVines(short id) {
		super(id, "TWISTING_VINES", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public TwistingVines(int id) {
		this((short) id);
	}

	private TwistingVines(TwistingVines old) {
		this(old.id);
	}

}