package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class DarkOakDoor extends Block implements Orientable, Hinged, Openable, Powerable {
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
		DarkOakDoor current = new DarkOakDoor(this);
		current.orientation.put(o, value);
		return current;
	}
	@Override

	public Set<Orientable.Orientation> getValidOrientations() {
		return this.orientation.keySet();
	}
	/*   --- HINGED INTERFACE ---   */
	@RelevantBlockData
	private Hinged.Hinge hingeDirection;
	@Override

	public Hinged setHinge(Hinged.Hinge hinge) {
		DarkOakDoor current = new DarkOakDoor(this);
		current.hingeDirection = hinge;
		return current;
	}
	@Override

	public Hinge getHinge() {
		return this.hingeDirection;
	}
	/*   --- OPENABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean isOpen;
	@Override

	public Openable setOpened(boolean opened) {
		DarkOakDoor current = new DarkOakDoor(this);
		current.isOpen = opened;
		return current;
	}
	@Override

	public boolean isOpened() {
		return this.isOpen;
	}
	/*   --- POWERABLE INTERFACE ---   */
	@RelevantBlockData
	private boolean powered;
	@Override
	public boolean isPowered() {
		return this.powered;
	}
	@Override

	public Powerable setPowered(boolean val) {
		DarkOakDoor current = new DarkOakDoor(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public DarkOakDoor(short id) {
		super(id, "DARK_OAK_DOOR", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.orientation.put(Orientable.Orientation.U, false);
		this.orientation.put(Orientable.Orientation.S, false);
		this.orientation.put(Orientable.Orientation.N, false);
		this.orientation.put(Orientable.Orientation.D, false);
		this.orientation.put(Orientable.Orientation.W, false);
		this.orientation.put(Orientable.Orientation.E, false);
		this.hingeDirection = Hinged.Hinge.LEFT;
		this.isOpen = false;
		this.powered = false;
	}

	public DarkOakDoor(int id) {
		this((short) id);
	}

	private DarkOakDoor(DarkOakDoor old) {
		this(old.id);
		this.orientation.putAll(old.orientation);
		this.hingeDirection = old.hingeDirection;
		this.isOpen = old.isOpen;
		this.powered = old.powered;
	}

}