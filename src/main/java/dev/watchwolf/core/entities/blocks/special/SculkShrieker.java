package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class SculkShrieker extends Block {

	/*   --- CONSTRUCTORS ---   */
	public SculkShrieker(short id) {
		super(id, "SCULK_SHRIEKER", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public SculkShrieker(int id) {
		this((short) id);
	}

	private SculkShrieker(SculkShrieker old) {
		this(old.id);
	}

}