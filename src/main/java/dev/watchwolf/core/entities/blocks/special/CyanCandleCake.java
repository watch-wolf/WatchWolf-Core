package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class CyanCandleCake extends Block implements Ignitable {
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		CyanCandleCake current = new CyanCandleCake(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public CyanCandleCake(short id) {
		super(id, "CYAN_CANDLE_CAKE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.ignited = true;
	}

	public CyanCandleCake(int id) {
		this((short) id);
	}

	private CyanCandleCake(CyanCandleCake old) {
		this(old.id);
		this.ignited = old.ignited;
	}

}