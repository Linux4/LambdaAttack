package org.spacehq.mc.protocol.packet.status.client;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.packet.MinecraftPacket;

public class StatusQueryPacket extends MinecraftPacket {
    public StatusQueryPacket() {
    }

    @Override
    public void read(NetInput in) throws IOException {
    }

    @Override
    public void write(NetOutput out) throws IOException {
    }
}
