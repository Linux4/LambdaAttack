package org.spacehq.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.window.WindowType;
import org.spacehq.mc.protocol.packet.MinecraftPacket;

public class ServerOpenWindowPacket extends MinecraftPacket {
    private int windowId;
    private WindowType type;
    private String name;

    @SuppressWarnings("unused")
    private ServerOpenWindowPacket() {
    }

    public ServerOpenWindowPacket(int windowId, WindowType type, String name) {
        this.windowId = windowId;
        this.type = type;
        this.name = name;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public WindowType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readVarInt();
        this.type = MagicValues.key(WindowType.class, in.readVarInt());
        this.name = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.windowId);
        out.writeVarInt(MagicValues.value(Integer.class, this.type));
        out.writeString(this.name);
    }
}
