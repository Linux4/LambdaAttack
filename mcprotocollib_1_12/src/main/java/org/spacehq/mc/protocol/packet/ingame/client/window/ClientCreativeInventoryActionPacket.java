package org.spacehq.mc.protocol.packet.ingame.client.window;

import org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack;
import org.spacehq.mc.protocol.packet.MinecraftPacket;
import org.spacehq.mc.protocol.util.NetUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class ClientCreativeInventoryActionPacket extends MinecraftPacket {
    private int slot;
    private ItemStack clicked;

    @SuppressWarnings("unused")
    private ClientCreativeInventoryActionPacket() {
    }

    public ClientCreativeInventoryActionPacket(int slot, ItemStack clicked) {
        this.slot = slot;
        this.clicked = clicked;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getClickedItem() {
        return this.clicked;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.slot = in.readShort();
        this.clicked = NetUtil.readItem(in);
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeShort(this.slot);
        NetUtil.writeItem(out, this.clicked);
    }
}
