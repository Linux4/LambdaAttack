package org.spacehq.mc.protocol.packet.ingame.server.world;

import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.data.MagicValues;
import org.spacehq.mc.protocol.data.game.entity.metadata.Position;
import org.spacehq.mc.protocol.data.game.world.block.UpdatedTileType;
import org.spacehq.mc.protocol.packet.MinecraftPacket;
import org.spacehq.mc.protocol.util.NetUtil;

public class ServerUpdateTileEntityPacket extends MinecraftPacket {
    private Position position;
    private UpdatedTileType type;
    private CompoundTag nbt;

    @SuppressWarnings("unused")
    private ServerUpdateTileEntityPacket() {
    }

    public ServerUpdateTileEntityPacket(Position position, UpdatedTileType type, CompoundTag nbt) {
        this.position = position;
        this.type = type;
        this.nbt = nbt;
    }

    public Position getPosition() {
        return this.position;
    }

    public UpdatedTileType getType() {
        return this.type;
    }

    public CompoundTag getNBT() {
        return this.nbt;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.position = NetUtil.readPosition(in);
        this.type = MagicValues.key(UpdatedTileType.class, in.readUnsignedByte());
        this.nbt = NetUtil.readNBT(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        NetUtil.writePosition(out, this.position);
        out.writeByte(MagicValues.value(Integer.class, this.type));
        NetUtil.writeNBT(out, this.nbt);
    }
}
