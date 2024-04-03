package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class StructureBlock extends Block implements Stateful {
	/*   --- STATEFUL INTERFACE ---   */
	@RelevantBlockData
	private Stateful.Mode state;
	private final HashSet<Stateful.Mode> allowedStates = new HashSet<>();
	@Override
	public Stateful.Mode getMode() {
		return this.state;
	}
	@Override

	public Stateful setMode(Stateful.Mode mode) throws IllegalArgumentException {
		if (!this.allowedStates.contains(mode)) throw new IllegalArgumentException("StructureBlock block doesn't allow the state " + mode.name());
		StructureBlock current = new StructureBlock(this);
		current.state = mode;
		return current;
	}
	@Override
	public Set<Stateful.Mode> getValidModes() {
		return (HashSet)this.allowedStates.clone();
	}

	/*   --- CONSTRUCTORS ---   */
	public StructureBlock(short id) {
		super(id, "STRUCTURE_BLOCK", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.allowedStates.add(Stateful.Mode.LOAD);
		this.allowedStates.add(Stateful.Mode.SAVE);
		this.allowedStates.add(Stateful.Mode.CORNER);
		this.state = Stateful.Mode.LOAD;
	}

	public StructureBlock(int id) {
		this((short) id);
	}

	private StructureBlock(StructureBlock old) {
		this(old.id);
		this.state = old.state;
	}

}