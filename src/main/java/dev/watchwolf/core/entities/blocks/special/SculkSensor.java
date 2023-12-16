package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class SculkSensor extends Block {

	/*   --- CONSTRUCTORS ---   */
	public SculkSensor(short id) {
		super(id, "SCULK_SENSOR", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
	}

	public SculkSensor(int id) {
		this((short) id);
	}

	private SculkSensor(SculkSensor old) {
		this(old.id);
	}

}