package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class MagentaCandleCake extends Block implements Ignitable {
	/*   --- IGNITABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean ignited;
	@Override
	public boolean isIgnited() {
		return this.ignited;
	}
	@Override

	public Ignitable setIgnited(boolean value) {
		MagentaCandleCake current = new MagentaCandleCake(this);
		current.ignited = value;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public MagentaCandleCake(short id) {
		super(id, "MAGENTA_CANDLE_CAKE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.ignited = true;
	}

	public MagentaCandleCake(int id) {
		this((short) id);
	}

	private MagentaCandleCake(MagentaCandleCake old) {
		this(old.id);
		this.ignited = old.ignited;
	}

}