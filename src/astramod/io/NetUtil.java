package astramod.io;

import java.nio.*;
import arc.func.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

public final class NetUtil {
	public static String modName = "astramod-";

	// region CLIENT HANDLERS

	public static void clientStringHandler(String name, Cons<String> handler) {
		netClient.addPacketHandler(modName + name, data -> {
			if (!net.server()) handler.get(data);
		});
	}

	public static void clientIntHandler(String name, Intc handler) {
		netClient.addBinaryPacketHandler(modName + name, data -> {
			if (!net.server()) {
				handler.get(ByteBuffer.wrap(data).getInt());
			}
		});
	}

	public static void clientIntsHandler(String name, Cons<IntBuffer> handler) {
		netClient.addBinaryPacketHandler(modName + name, data -> {
			if (!net.server()) {
				handler.get(ByteBuffer.wrap(data).asIntBuffer());
			}
		});
	}

	public static void clientFloatHandler(String name, Floatc handler) {
		netClient.addBinaryPacketHandler(modName + name, data -> {
			if (!net.server()) {
				handler.get(ByteBuffer.wrap(data).getFloat());
			}
		});
	}

	public static void clientFloatsHandler(String name, Cons<FloatBuffer> handler) {
		netClient.addBinaryPacketHandler(modName + name, data -> {
			if (!net.server()) {
				handler.get(ByteBuffer.wrap(data).asFloatBuffer());
			}
		});
	}

	// region SERVER HANDLERS

	public static void serverStringHandler(String name, Cons2<Player, String> handler) {
		netServer.addPacketHandler(modName + name, (player, data) -> {
			if (net.server()) handler.get(player, data);
		});
	}

	public static void serverIntHandler(String name, Cons2<Player, Integer> handler) {
		netServer.addBinaryPacketHandler(modName + name, (player, data) -> {
			if (net.server()) {
				handler.get(player, ByteBuffer.wrap(data).getInt());
			}
		});
	}

	public static void serverIntsHandler(String name, Cons2<Player, IntBuffer> handler) {
		netServer.addBinaryPacketHandler(modName + name, (player, data) -> {
			if (net.server()) {
				handler.get(player, ByteBuffer.wrap(data).asIntBuffer());
			}
		});
	}

	public static void serverFloatHandler(String name, Cons2<Player, Float> handler) {
		netServer.addBinaryPacketHandler(modName + name, (player, data) -> {
			if (net.server()) {
				handler.get(player, ByteBuffer.wrap(data).getFloat());
			}
		});
	}

	public static void serverFloatsHandler(String name, Cons2<Player, FloatBuffer> handler) {
		netServer.addBinaryPacketHandler(modName + name, (player, data) -> {
			if (net.server()) {
				handler.get(player, ByteBuffer.wrap(data).asFloatBuffer());
			}
		});
	}

	// region CLIENT PACKETS

	public static void clientIntReliable(String name, int value) {
		Call.clientBinaryPacketReliable(modName + name, serialize(value));
	}

	public static void clientIntUnreliable(String name, int value) {
		Call.clientBinaryPacketUnreliable(modName + name, serialize(value));
	}

	public static void clientIntsReliable(String name, int... values) {
		Call.clientBinaryPacketReliable(modName + name, serialize(values));
	}

	public static void clientIntsUnreliable(String name, int... values) {
		Call.clientBinaryPacketUnreliable(modName + name, serialize(values));
	}

	public static void clientFloatReliable(String name, float value) {
		Call.clientBinaryPacketReliable(modName + name, serialize(value));
	}

	public static void clientFloatUnreliable(String name, float value) {
		Call.clientBinaryPacketUnreliable(modName + name, serialize(value));
	}

	public static void clientFloatsReliable(String name, float... values) {
		Call.clientBinaryPacketReliable(modName + name, serialize(values));
	}

	public static void clientFloatsUnreliable(String name, float... values) {
		Call.clientBinaryPacketUnreliable(modName + name, serialize(values));
	}

	// region SERVER PACKETS

	public static void serverIntReliable(String name, int value) {
		Call.serverBinaryPacketReliable(modName + name, serialize(value));
	}

	public static void serverIntUnreliable(String name, int value) {
		Call.serverBinaryPacketUnreliable(modName + name, serialize(value));
	}

	public static void serverIntsReliable(String name, int... values) {
		Call.serverBinaryPacketReliable(modName + name, serialize(values));
	}

	public static void serverIntsUnreliable(String name, int... values) {
		Call.serverBinaryPacketUnreliable(modName + name, serialize(values));
	}

	public static void serverFloatReliable(String name, float value) {
		Call.serverBinaryPacketReliable(modName + name, serialize(value));
	}

	public static void serverFloatUnreliable(String name, float value) {
		Call.serverBinaryPacketUnreliable(modName + name, serialize(value));
	}

	public static void serverFloatsReliable(String name, float... values) {
		Call.serverBinaryPacketReliable(modName + name, serialize(values));
	}

	public static void serverFloatsUnreliable(String name, float... values) {
		Call.serverBinaryPacketUnreliable(modName + name, serialize(values));
	}

	// region SERIALIZATION

	public static byte[] serialize(int value) {
		return ByteBuffer.allocate(Integer.BYTES).putInt(value).array();
	}

	public static byte[] serialize(float value) {
		return ByteBuffer.allocate(Float.BYTES).putFloat(value).array();
	}

	public static byte[] serialize(int[] values) {
		ByteBuffer buffer = ByteBuffer.allocate(values.length * Integer.BYTES);
		buffer.asIntBuffer().put(values);
		return buffer.array();
	}

	public static byte[] serialize(float[] values) {
		ByteBuffer buffer = ByteBuffer.allocate(values.length * Float.BYTES);
		buffer.asFloatBuffer().put(values);
		return buffer.array();
	}
}