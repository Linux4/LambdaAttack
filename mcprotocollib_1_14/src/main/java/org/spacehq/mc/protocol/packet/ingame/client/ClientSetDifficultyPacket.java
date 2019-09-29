package org.spacehq.mc.protocol.packet.ingame.client;

import java.io.IOException;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.setting.Difficulty;
import org.spacehq.mc.protocol.packet.MinecraftPacket;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

public class ClientSetDifficultyPacket extends MinecraftPacket {

    private Difficulty difficulty;

    @Override
    public void read(NetInput in) throws IOException {
        this.difficulty = MagicValues.key(Difficulty.class, in.readByte());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(MagicValues.key(Byte.class, this.difficulty));
    }
}
