package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class LightGrayCandleCake extends Block implements Ignitable {
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		LightGrayCandleCake current = new LightGrayCandleCake(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public LightGrayCandleCake(short id) {
		super(id, "LIGHT_GRAY_CANDLE_CAKE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.ignited = true;
	}

	public LightGrayCandleCake(int id) {
		this((short) id);
	}

	private LightGrayCandleCake(LightGrayCandleCake old) {
		this(old.id);
		this.ignited = old.ignited;
	}

}