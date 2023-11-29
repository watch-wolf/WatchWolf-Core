package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import dev.watchwolf.core.entities.SocketHelper;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class RespawnAnchor extends Block implements Staggered {
	/*   --- STAGGERED INTERFACE ---   */
	@RelevantBlockData
	private int stage;
	private final int maxStage, minStage;
	@Override

	public Staggered setStage(int stage) throws IllegalArgumentException {
		if (stage < this.getMinStage() || stage > this.getMaxStage()) throw new IllegalArgumentException("RespawnAnchor block only allows stages from " + this.getMinStage() + " to " + this.getMaxStage());
		RespawnAnchor current = new RespawnAnchor(this);
		current.stage = stage;
		return current;
	}
	@Override

	public int getStage() {
		return this.stage;
	}
	@Override

	public int getMaxStage() {
		return this.maxStage;
	}
	@Override

	public int getMinStage() {
		return this.minStage;
	}

	/*   --- SOCKET DATA OVERRIDE ---   */
	@Override
	public void sendSocketData(ArrayList<Byte> out) {
		SocketHelper.addShort(out, this.id);
		out.add((byte)(0)); // age
		out.add((byte)(0)); // directionable & orientable
		out.add((byte)0); // reserved
		out.add((byte)(0)); // group_count & delay & eye & hinge & open
		out.add((byte)((byte)(this.stage))); // stage
		out.add((byte)(0)); // part & rotation
		out.add((byte)(0)); // note
		out.add((byte)(0)); // mode & leaves & lit & locked
		out.add((byte)0); // reserved
		out.add((byte)(0)); // cond & inv & pow
		SocketHelper.fill(out, 44); // reserved
	}

	/*   --- CONSTRUCTORS ---   */
	public RespawnAnchor(short id) {
		super(id, "RESPAWN_ANCHOR", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.stage = 0;
		this.maxStage = 4;
		this.minStage = 0;
	}

	public RespawnAnchor(int id) {
		this((short) id);
	}

	private RespawnAnchor(RespawnAnchor old) {
		this(old.id);
		this.stage = old.stage;
	}

}