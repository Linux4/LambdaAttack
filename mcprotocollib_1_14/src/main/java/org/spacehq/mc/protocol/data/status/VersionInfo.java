package org.spacehq.mc.protocol.data.status;

import java.util.Objects;

import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.util.ObjectUtil;

public class VersionInfo {
    public static final VersionInfo CURRENT = new VersionInfo(MinecraftConstants.GAME_VERSION, MinecraftConstants.PROTOCOL_VERSION);

    private String name;
    private int protocol;

    public VersionInfo(String name, int protocol) {
        this.name = name;
        this.protocol = protocol;
    }

    public String getVersionName() {
        return this.name;
    }

    public int getProtocolVersion() {
        return this.protocol;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof VersionInfo)) return false;

        VersionInfo that = (VersionInfo) o;
        return Objects.equals(this.name, that.name) &&
                this.protocol == that.protocol;
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(this.name, this.protocol);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
