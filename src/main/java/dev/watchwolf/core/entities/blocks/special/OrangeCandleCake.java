package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class OrangeCandleCake extends Block implements Ignitable {
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		OrangeCandleCake current = new OrangeCandleCake(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public OrangeCandleCake(short id) {
		super(id, "ORANGE_CANDLE_CAKE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.ignited = true;
	}

	public OrangeCandleCake(int id) {
		this((short) id);
	}

	private OrangeCandleCake(OrangeCandleCake old) {
		this(old.id);
		this.ignited = old.ignited;
	}

}