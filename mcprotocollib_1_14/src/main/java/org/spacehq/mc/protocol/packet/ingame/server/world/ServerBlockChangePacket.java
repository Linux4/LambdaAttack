package org.spacehq.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.data.game.world.block.BlockChangeRecord;
import org.spacehq.mc.protocol.packet.MinecraftPacket;
import org.spacehq.mc.protocol.util.NetUtil;

public class ServerBlockChangePacket extends MinecraftPacket {
    private BlockChangeRecord record;

    @SuppressWarnings("unused")
    private ServerBlockChangePacket() {
    }

    public ServerBlockChangePacket(BlockChangeRecord record) {
        this.record = record;
    }

    public BlockChangeRecord getRecord() {
        return this.record;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.record = new BlockChangeRecord(NetUtil.readPosition(in), NetUtil.readBlockState(in));
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.record.getPosition());
        NetUtil.writeBlockState(out, this.record.getBlock());
    }
}
