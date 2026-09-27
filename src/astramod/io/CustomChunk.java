package astramod.io;

import java.io.*;
import arc.func.*;
import mindustry.io.*;

public class CustomChunk implements SaveFileReader.CustomChunk {
	public ConsInput readCons;
	public ConsOutput writeCons;
	public Boolp shouldWrite;

	public CustomChunk(ConsInput read, ConsOutput write, Boolp shouldWrite) {
		readCons = read;
		writeCons = write;
		this.shouldWrite = shouldWrite;
	}

	public CustomChunk(ConsInput read, ConsOutput write) {
		this(read, write, () -> true);
	}

	@Override public void read(DataInput stream) throws IOException {
		readCons.read(stream);
	}

	@Override public void write(DataOutput stream) throws IOException {
		writeCons.write(stream);
	}

	@Override public boolean shouldWrite() {
		return shouldWrite.get();
	}
}