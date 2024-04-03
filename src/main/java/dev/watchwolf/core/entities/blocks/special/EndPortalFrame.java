package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class EndPortalFrame extends Block implements Orientable, Eyeable {
	/*   --- ORIENTABLE INTERFACE ---   */
	@RelevantBlockData
	private final Map<Orientable.Orientation,Boolean> orientation = new HashMap<>();
	@Override
	public boolean isOrientationSet(Orientable.Orientation o) throws IllegalArgumentException {
		Boolean result = this.orientation.get(o);
		return result;
	}
	@Override

	public Orientable setOrientation(Orientable.Orientation o, boolean value) throws IllegalArgumentException {
		EndPortalFrame current = new EndPortalFrame(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}
	/*   --- EYEABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean hasEye;
	@Override

	public Eyeable setEyePlaced(boolean placed) {
		EndPortalFrame current = new EndPortalFrame(this);
		current.hasEye = placed;
		return current;
	}
	@Override

	public boolean isEyePlaced() {
		return this.hasEye;
	}

	/*   --- CONSTRUCTORS ---   */
	public EndPortalFrame(short id) {
		super(id, "END_PORTAL_FRAME", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.D, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
		this.hasEye = false;
	}

	public EndPortalFrame(int id) {
		this((short) id);
	}

	private EndPortalFrame(EndPortalFrame old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
		this.hasEye = old.hasEye;
	}

}