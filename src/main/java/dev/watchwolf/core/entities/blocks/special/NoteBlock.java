package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class NoteBlock extends Block implements Playable, Powerable {
	/*   --- PLAYABLE INTERFACE ---   */
	@RelevantBlockData
	private int note;
	@Override

	public Playable setNote(int note) throws IllegalArgumentException {
		if (note < 0 || note > 24) throw new IllegalArgumentException("NoteBlock block allows notes from 0 to 24");
		NoteBlock current = new NoteBlock(this);
		current.note = note;
		return current;
	}
	@Override

	public int getNote() {
		return this.note;
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
		NoteBlock current = new NoteBlock(this);
		current.powered = val;
		return current;
	}

	/*   --- CONSTRUCTORS ---   */
	public NoteBlock(short id) {
		super(id, "NOTE_BLOCK", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.note = 0;
		this.powered = false;
	}

	public NoteBlock(int id) {
		this((short) id);
	}

	private NoteBlock(NoteBlock old) {
		this(old.id);
		this.note = old.note;
		this.powered = old.powered;
	}

}