package org.spacehq.mc.protocol.packet.ingame.server;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.setting.Difficulty;
import org.spacehq.mc.protocol.packet.MinecraftPacket;

public class ServerDifficultyPacket extends MinecraftPacket {
    private Difficulty difficulty;
    private boolean difficultyLocked;

    @SuppressWarnings("unused")
    private ServerDifficultyPacket() {
    }

    public ServerDifficultyPacket(Difficulty difficulty, boolean difficultyLocked) {
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public boolean isDifficultyLocked() {
        return difficultyLocked;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.difficulty = MagicValues.key(Difficulty.class, in.readUnsignedByte());
        this.difficultyLocked = in.readBoolean();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(MagicValues.value(Integer.class, this.difficulty));
        out.writeBoolean(difficultyLocked);
    }
}
