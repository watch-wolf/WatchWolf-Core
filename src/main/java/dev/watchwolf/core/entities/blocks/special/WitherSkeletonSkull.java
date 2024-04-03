package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class WitherSkeletonSkull extends Block implements Rotable {
	/*   --- ROTABLE INTERFACE ---   */
	@RelevantBlockData
	private Rotable.Rotation rotation;
	@Override

	public Rotable setRotation(Rotable.Rotation rotation) {
		WitherSkeletonSkull current = new WitherSkeletonSkull(this);
		current.rotation = rotation;
		return current;
	}
	@Override

	public Rotable.Rotation getRotation() {
		return this.rotation;
	}

	/*   --- CONSTRUCTORS ---   */
	public WitherSkeletonSkull(short id) {
		super(id, "WITHER_SKELETON_SKULL", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.rotation = Rotable.Rotation.S;
	}

	public WitherSkeletonSkull(int id) {
		this((short) id);
	}

	private WitherSkeletonSkull(WitherSkeletonSkull old) {
		this(old.id);
		this.rotation = old.rotation;
	}

}