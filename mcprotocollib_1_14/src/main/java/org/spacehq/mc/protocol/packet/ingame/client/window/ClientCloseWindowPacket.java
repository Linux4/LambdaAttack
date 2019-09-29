package org.spacehq.mc.protocol.packet.ingame.client.window;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.packet.MinecraftPacket;

public class ClientCloseWindowPacket extends MinecraftPacket {
    private int windowId;

    @SuppressWarnings("unused")
    private ClientCloseWindowPacket() {
    }

    public ClientCloseWindowPacket(int windowId) {
        this.windowId = windowId;
    }

    public int getWindowId() {
        return this.windowId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
    }
}
