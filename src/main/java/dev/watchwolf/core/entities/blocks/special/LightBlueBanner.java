package dev.watchwolf.core.entities.blocks.special;

import dev.watchwolf.core.entities.blocks.*;
import dev.watchwolf.core.entities.SocketHelper;
import java.util.*;

/* THIS CLASS WAS AUTOMATICALLY GENERATED; DO NOT MODIFY */
public class LightBlueBanner extends Block implements Rotable {
	/*   --- ROTABLE INTERFACE ---   */
	@RelevantBlockData
	private Rotable.Rotation rotation;
	@Override

	public Rotable setRotation(Rotable.Rotation rotation) {
		LightBlueBanner current = new LightBlueBanner(this);
		current.rotation = rotation;
		return current;
	}
	@Override

	public Rotable.Rotation getRotation() {
		return this.rotation;
	}

	/*   --- SOCKET DATA OVERRIDE ---   */
	@Override
	public void sendSocketData(ArrayList<Byte> out) {
		SocketHelper.addShort(out, this.id);
		out.add((byte)(0)); // age
		out.add((byte)(0)); // directionable & orientable
		out.add((byte)0); // reserved
		out.add((byte)(0)); // group_count & delay & eye & hinge & open
		out.add((byte)(0)); // stage
		out.add((byte)((byte)(this.rotation.ordinal()))); // part & rotation
		out.add((byte)(0)); // note
		out.add((byte)(0)); // mode & leaves & lit & locked
		out.add((byte)0); // reserved
		out.add((byte)(0)); // cond & inv & pow
		SocketHelper.fill(out, 44); // reserved
	}

	/*   --- CONSTRUCTORS ---   */
	public LightBlueBanner(short id) {
		super(id, "LIGHT_BLUE_BANNER", (ins, f) -> {
			try { return f.get(ins).toString(); }
			catch (IllegalAccessException ignore) { return "?"; }
		});
		this.rotation = Rotable.Rotation.S;
	}

	public LightBlueBanner(int id) {
		this((short) id);
	}

	private LightBlueBanner(LightBlueBanner old) {
		this(old.id);
		this.rotation = old.rotation;
	}

}