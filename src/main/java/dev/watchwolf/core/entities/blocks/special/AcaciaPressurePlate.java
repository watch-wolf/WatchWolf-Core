package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class AcaciaPressurePlate extends Block implements Powerable {
	/*   --- POWERABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean powered;
	@Override
	public boolean isPowered() {
		return this.powered;
	}
	@Override

	public Powerable setPowered(boolean val) {
		AcaciaPressurePlate current = new AcaciaPressurePlate(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public AcaciaPressurePlate(short id) {
		super(id, "ACACIA_PRESSURE_PLATE", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.powered = false;
	}

	public AcaciaPressurePlate(int id) {
		this((short) id);
	}

	private AcaciaPressurePlate(AcaciaPressurePlate old) {
		this(old.id);
		this.powered = old.powered;
	}

}