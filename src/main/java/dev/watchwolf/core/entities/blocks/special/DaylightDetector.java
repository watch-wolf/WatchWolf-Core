package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DaylightDetector extends Block implements Invertable {
	/*   --- INVERTABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean inverted;
	@Override
	public boolean isInverted() {
		return this.inverted;
	}
	@Override

	public Invertable setInvert(boolean val) {
		DaylightDetector current = new DaylightDetector(this);
		current.inverted = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public DaylightDetector(short id) {
		super(id, "DAYLIGHT_DETECTOR", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.inverted = false;
	}

	public DaylightDetector(int id) {
		this((short) id);
	}

	private DaylightDetector(DaylightDetector old) {
		this(old.id);
		this.inverted = old.inverted;
	}

}