package org.spacehq.mc.protocol.data.status.handler;

import org.spacehq.mc.protocol.data.status.ServerStatusInfo;

import com.github.steveice10.packetlib.Session;

public interface ServerInfoHandler {
    public void handle(Session session, ServerStatusInfo info);
}
