package org.spacehq.mc.protocol.packet.login.server;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;

import java.io.IOException;

public class LoginSuccessPacket extends MinecraftPacket {
    private GameProfile profile;

    @SuppressWarnings("unused")
    private LoginSuccessPacket() {
    }

    public LoginSuccessPacket(GameProfile profile) {
        this.profile = profile;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.profile = new GameProfile(in.readString(), in.readString());
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeString(this.profile.getIdAsString());
        out.writeString(this.profile.getName());
    }

    @Override
    public boolean isPriority() {
        return true;
    }
}
