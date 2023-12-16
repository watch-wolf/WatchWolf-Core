package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class BrownCandleCake extends Block implements Ignitable {
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		BrownCandleCake current = new BrownCandleCake(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public BrownCandleCake(short id) {
		super(id, "BROWN_CANDLE_CAKE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.ignited = true;
	}

	public BrownCandleCake(int id) {
		this((short) id);
	}

	private BrownCandleCake(BrownCandleCake old) {
		this(old.id);
		this.ignited = old.ignited;
	}

}