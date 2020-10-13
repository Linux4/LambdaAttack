package org.spacehq.mc.protocol.packet.ingame.server.window;

import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

import org.spacehq.mc.protocol.packet.MinecraftPacket;

public class ServerPreparedCraftingGridPacket extends MinecraftPacket {
    private int windowId;
    private String recipeId;

    @SuppressWarnings("unused")
    private ServerPreparedCraftingGridPacket() {
    }

    public ServerPreparedCraftingGridPacket(int windowId, String recipeId) {
        this.windowId = windowId;
        this.recipeId = recipeId;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public String getRecipeId() {
        return this.recipeId;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.windowId = in.readByte();
        this.recipeId = in.readString();
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeByte(this.windowId);
        out.writeString(this.recipeId);
    }
}