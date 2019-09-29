package org.spacehq.mc.protocol;

import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.auth.service.AuthenticationService;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Server;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.crypt.AESEncryption;
import com.github.steveice10.packetlib.crypt.PacketEncryption;
import com.github.steveice10.packetlib.packet.DefaultPacketHeader;
import com.github.steveice10.packetlib.packet.PacketHeader;
import com.github.steveice10.packetlib.packet.PacketProtocol;

import java.net.Proxy;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.util.UUID;

import org.spacehq.mc.protocol.data.SubProtocol;
import org.spacehq.mc.protocol.packet.handshake.client.HandshakePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientLockDifficultyPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientPluginMessagePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientSetDifficultyPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerAbilitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerMovementPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPlaceBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerSwingArmPacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.ClientPlayerUseItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientAdvancementTabPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientClickWindowButtonPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCraftingBookDataPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientEditBookPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientMoveItemToHotbarPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientPrepareCraftingGridPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientRenameItemPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientSelectTradePacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientSetBeaconEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientUpdateCommandBlockMinecartPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientUpdateCommandBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientUpdateJigsawBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientUpdateStructureBlockPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientBlockNBTRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientEntityNBTRequestPacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientSpectatePacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientSteerBoatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientSteerVehiclePacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientTeleportConfirmPacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientUpdateSignPacket;
import org.spacehq.mc.protocol.packet.ingame.client.world.ClientVehicleMovePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerAdvancementTabPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerAdvancementsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerBossBarPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerCombatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDeclareCommandsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDeclareRecipesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDeclareTagsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerEntitySoundEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerPluginMessagePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSetCooldownPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerStatisticsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerStopSoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerSwitchCameraPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerUnlockRecipesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityAttachPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMovementPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityPropertiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRemoveEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerAbilitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerActionAckPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerFacingPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerDisplayScoreboardPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerScoreboardObjectivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerTeamPacket;
import org.spacehq.mc.protocol.packet.ingame.server.scoreboard.ServerUpdateScorePacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenBookPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenHorseWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerPreparedCraftingGridPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerTradeListPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockBreakAnimPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNBTResponsePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerOpenTileEntityEditorPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateLightPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateViewDistancePacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateViewPositionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerWorldBorderPacket;
import org.spacehq.mc.protocol.packet.login.client.EncryptionResponsePacket;
import org.spacehq.mc.protocol.packet.login.client.LoginPluginResponsePacket;
import org.spacehq.mc.protocol.packet.login.client.LoginStartPacket;
import org.spacehq.mc.protocol.packet.login.server.EncryptionRequestPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginDisconnectPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginPluginRequestPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSetCompressionPacket;
import org.spacehq.mc.protocol.packet.login.server.LoginSuccessPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusPingPacket;
import org.spacehq.mc.protocol.packet.status.client.StatusQueryPacket;
import org.spacehq.mc.protocol.packet.status.server.StatusPongPacket;
import org.spacehq.mc.protocol.packet.status.server.StatusResponsePacket;

public class MinecraftProtocol extends PacketProtocol {
    private SubProtocol subProtocol = SubProtocol.HANDSHAKE;
    private PacketHeader header = new DefaultPacketHeader();
    private AESEncryption encrypt;

    private GameProfile profile;
    private String clientToken = "";
    private String accessToken = "";

    @SuppressWarnings("unused")
    private MinecraftProtocol() {
    }

    public MinecraftProtocol(SubProtocol subProtocol) {
        if(subProtocol != SubProtocol.LOGIN && subProtocol != SubProtocol.STATUS) {
            throw new IllegalArgumentException("Only login and status modes are permitted.");
        }

        this.subProtocol = subProtocol;
        if(subProtocol == SubProtocol.LOGIN) {
            this.profile = new GameProfile((UUID) null, "Player");
        }
    }

    public MinecraftProtocol(String username) {
        this(SubProtocol.LOGIN);
        this.profile = new GameProfile((UUID) null, username);
    }

    public MinecraftProtocol(String username, String password) throws RequestException {
        this(username, password, Proxy.NO_PROXY);
    }

    public MinecraftProtocol(String username, String clientToken, String accessToken) throws RequestException {
        this(username, clientToken, accessToken, Proxy.NO_PROXY);
    }

    public MinecraftProtocol(String username, String password, Proxy proxy) throws RequestException {
        this(username, UUID.randomUUID().toString(), password, false, proxy);
    }

    public MinecraftProtocol(String username, String clientToken, String accessToken, Proxy proxy) throws RequestException {
        this(username, clientToken, accessToken, true, proxy);
    }

    private MinecraftProtocol(String username, String clientToken, String using, boolean token, Proxy authProxy) throws RequestException {
        this(SubProtocol.LOGIN);

        AuthenticationService auth = new AuthenticationService(clientToken, authProxy);
        auth.setUsername(username);
        if(token) {
            auth.setAccessToken(using);
        } else {
            auth.setPassword(using);
        }

        auth.login();
        this.profile = auth.getSelectedProfile();
        this.clientToken =  auth.getClientToken();
        this.accessToken = auth.getAccessToken();
    }

    public MinecraftProtocol(GameProfile profile, String clientToken, String accessToken) {
        this(SubProtocol.LOGIN);
        this.profile = profile;
        this.clientToken = clientToken;
        this.accessToken = accessToken;
    }

    public GameProfile getProfile() {
        return this.profile;
    }

    public String getClientToken() {
        return this.clientToken;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String getSRVRecordPrefix() {
        return "_minecraft";
    }

    @Override
    public PacketHeader getPacketHeader() {
        return this.header;
    }

    @Override
    public PacketEncryption getEncryption() {
        return this.encrypt;
    }

    @Override
    public void newClientSession(Client client, Session session) {
        if(this.profile != null) {
            session.setFlag(MinecraftConstants.PROFILE_KEY, this.profile);
            session.setFlag(MinecraftConstants.ACCESS_TOKEN_KEY, this.accessToken);
        }

        this.setSubProtocol(this.subProtocol, true, session);
        session.addListener(new ClientListener());
    }

    @Override
    public void newServerSession(Server server, Session session) {
        this.setSubProtocol(SubProtocol.HANDSHAKE, false, session);
        session.addListener(new ServerListener());
    }

    protected void enableEncryption(Key key) {
        try {
            this.encrypt = new AESEncryption(key);
        } catch(GeneralSecurityException e) {
            throw new Error("Failed to enable protocol encryption.", e);
        }
    }

    public SubProtocol getSubProtocol() {
        return this.subProtocol;
    }

    protected void setSubProtocol(SubProtocol subProtocol, boolean client, Session session) {
        this.clearPackets();
        switch(subProtocol) {
            case HANDSHAKE:
                if(client) {
                    this.initClientHandshake(session);
                } else {
                    this.initServerHandshake(session);
                }

                break;
            case LOGIN:
                if(client) {
                    this.initClientLogin(session);
                } else {
                    this.initServerLogin(session);
                }

                break;
            case GAME:
                if(client) {
                    this.initClientGame(session);
                } else {
                    this.initServerGame(session);
                }

                break;
            case STATUS:
                if(client) {
                    this.initClientStatus(session);
                } else {
                    this.initServerStatus(session);
                }

                break;
        }

        this.subProtocol = subProtocol;
    }

    private void initClientHandshake(Session session) {
        this.registerOutgoing(0, HandshakePacket.class);
    }

    private void initServerHandshake(Session session) {
        this.registerIncoming(0, HandshakePacket.class);
    }

    private void initClientLogin(Session session) {
        this.registerIncoming(0x00, LoginDisconnectPacket.class);
        this.registerIncoming(0x01, EncryptionRequestPacket.class);
        this.registerIncoming(0x02, LoginSuccessPacket.class);
        this.registerIncoming(0x03, LoginSetCompressionPacket.class);
        this.registerIncoming(0x04, LoginPluginRequestPacket.class);

        this.registerOutgoing(0x00, LoginStartPacket.class);
        this.registerOutgoing(0x01, EncryptionResponsePacket.class);
        this.registerOutgoing(0x02, LoginPluginResponsePacket.class);
    }

    private void initServerLogin(Session session) {
        this.registerIncoming(0x00, LoginStartPacket.class);
        this.registerIncoming(0x01, EncryptionResponsePacket.class);
        this.registerIncoming(0x02, LoginPluginResponsePacket.class);

        this.registerOutgoing(0x00, LoginDisconnectPacket.class);
        this.registerOutgoing(0x01, EncryptionRequestPacket.class);
        this.registerOutgoing(0x02, LoginSuccessPacket.class);
        this.registerOutgoing(0x03, LoginSetCompressionPacket.class);
        this.registerOutgoing(0x04, LoginPluginRequestPacket.class);
    }

    private void initClientGame(Session session) {
        this.registerIncoming(0x00, ServerSpawnObjectPacket.class);
        this.registerIncoming(0x01, ServerSpawnExpOrbPacket.class);
        this.registerIncoming(0x02, ServerSpawnGlobalEntityPacket.class);
        this.registerIncoming(0x03, ServerSpawnMobPacket.class);
        this.registerIncoming(0x04, ServerSpawnPaintingPacket.class);
        this.registerIncoming(0x05, ServerSpawnPlayerPacket.class);
        this.registerIncoming(0x06, ServerEntityAnimationPacket.class);
        this.registerIncoming(0x07, ServerStatisticsPacket.class);
        this.registerIncoming(0x08, ServerBlockBreakAnimPacket.class);
        this.registerIncoming(0x09, ServerUpdateTileEntityPacket.class);
        this.registerIncoming(0x0A, ServerBlockValuePacket.class);
        this.registerIncoming(0x0B, ServerBlockChangePacket.class);
        this.registerIncoming(0x0C, ServerBossBarPacket.class);
        this.registerIncoming(0x0D, ServerDifficultyPacket.class);
        this.registerIncoming(0x0E, ServerChatPacket.class);
        this.registerIncoming(0x0F, ServerMultiBlockChangePacket.class);
        this.registerIncoming(0x10, ServerTabCompletePacket.class);
        this.registerIncoming(0x11, ServerDeclareCommandsPacket.class);
        this.registerIncoming(0x12, ServerConfirmTransactionPacket.class);
        this.registerIncoming(0x13, ServerCloseWindowPacket.class);
        this.registerIncoming(0x14, ServerWindowItemsPacket.class);
        this.registerIncoming(0x15, ServerWindowPropertyPacket.class);
        this.registerIncoming(0x16, ServerSetSlotPacket.class);
        this.registerIncoming(0x17, ServerSetCooldownPacket.class);
        this.registerIncoming(0x18, ServerPluginMessagePacket.class);
        this.registerIncoming(0x19, ServerPlaySoundPacket.class);
        this.registerIncoming(0x1A, ServerDisconnectPacket.class);
        this.registerIncoming(0x1B, ServerEntityStatusPacket.class);
        this.registerIncoming(0x1C, ServerExplosionPacket.class);
        this.registerIncoming(0x1D, ServerUnloadChunkPacket.class);
        this.registerIncoming(0x1E, ServerNotifyClientPacket.class);
        this.registerIncoming(0x1F, ServerOpenHorseWindowPacket.class);
        this.registerIncoming(0x20, ServerKeepAlivePacket.class);
        this.registerIncoming(0x21, ServerChunkDataPacket.class);
        this.registerIncoming(0x22, ServerPlayEffectPacket.class);
        this.registerIncoming(0x23, ServerSpawnParticlePacket.class);
        this.registerIncoming(0x24, ServerUpdateLightPacket.class);
        this.registerIncoming(0x25, ServerJoinGamePacket.class);
        this.registerIncoming(0x26, ServerMapDataPacket.class);
        this.registerIncoming(0x27, ServerTradeListPacket.class);
        this.registerIncoming(0x28, ServerEntityPositionPacket.class);
        this.registerIncoming(0x29, ServerEntityPositionRotationPacket.class);
        this.registerIncoming(0x2A, ServerEntityRotationPacket.class);
        this.registerIncoming(0x2B, ServerEntityMovementPacket.class);
        this.registerIncoming(0x2C, ServerVehicleMovePacket.class);
        this.registerIncoming(0x2D, ServerOpenBookPacket.class);
        this.registerIncoming(0x2E, ServerOpenWindowPacket.class);
        this.registerIncoming(0x2F, ServerOpenTileEntityEditorPacket.class);
        this.registerIncoming(0x30, ServerPreparedCraftingGridPacket.class);
        this.registerIncoming(0x31, ServerPlayerAbilitiesPacket.class);
        this.registerIncoming(0x32, ServerCombatPacket.class);
        this.registerIncoming(0x33, ServerPlayerListEntryPacket.class);
        this.registerIncoming(0x34, ServerPlayerFacingPacket.class);
        this.registerIncoming(0x35, ServerPlayerPositionRotationPacket.class);
        this.registerIncoming(0x36, ServerUnlockRecipesPacket.class);
        this.registerIncoming(0x37, ServerEntityDestroyPacket.class);
        this.registerIncoming(0x38, ServerEntityRemoveEffectPacket.class);
        this.registerIncoming(0x39, ServerResourcePackSendPacket.class);
        this.registerIncoming(0x3A, ServerRespawnPacket.class);
        this.registerIncoming(0x3B, ServerEntityHeadLookPacket.class);
        this.registerIncoming(0x3C, ServerAdvancementTabPacket.class);
        this.registerIncoming(0x3D, ServerWorldBorderPacket.class);
        this.registerIncoming(0x3E, ServerSwitchCameraPacket.class);
        this.registerIncoming(0x3F, ServerPlayerChangeHeldItemPacket.class);
        this.registerIncoming(0x40, ServerUpdateViewPositionPacket.class);
        this.registerIncoming(0x41, ServerUpdateViewDistancePacket.class);
        this.registerIncoming(0x42, ServerDisplayScoreboardPacket.class);
        this.registerIncoming(0x43, ServerEntityMetadataPacket.class);
        this.registerIncoming(0x44, ServerEntityAttachPacket.class);
        this.registerIncoming(0x45, ServerEntityVelocityPacket.class);
        this.registerIncoming(0x46, ServerEntityEquipmentPacket.class);
        this.registerIncoming(0x47, ServerPlayerSetExperiencePacket.class);
        this.registerIncoming(0x48, ServerPlayerHealthPacket.class);
        this.registerIncoming(0x49, ServerScoreboardObjectivePacket.class);
        this.registerIncoming(0x4A, ServerEntitySetPassengersPacket.class);
        this.registerIncoming(0x4B, ServerTeamPacket.class);
        this.registerIncoming(0x4C, ServerUpdateScorePacket.class);
        this.registerIncoming(0x4D, ServerSpawnPositionPacket.class);
        this.registerIncoming(0x4E, ServerUpdateTimePacket.class);
        this.registerIncoming(0x4F, ServerTitlePacket.class);
        this.registerIncoming(0x50, ServerEntitySoundEffectPacket.class);
        this.registerIncoming(0x51, ServerPlayBuiltinSoundPacket.class);
        this.registerIncoming(0x52, ServerStopSoundPacket.class);
        this.registerIncoming(0x53, ServerPlayerListDataPacket.class);
        this.registerIncoming(0x54, ServerNBTResponsePacket.class);
        this.registerIncoming(0x55, ServerEntityCollectItemPacket.class);
        this.registerIncoming(0x56, ServerEntityTeleportPacket.class);
        this.registerIncoming(0x57, ServerAdvancementsPacket.class);
        this.registerIncoming(0x58, ServerEntityPropertiesPacket.class);
        this.registerIncoming(0x59, ServerEntityEffectPacket.class);
        this.registerIncoming(0x5A, ServerDeclareRecipesPacket.class);
        this.registerIncoming(0x5B, ServerDeclareTagsPacket.class);
        this.registerIncoming(0x5C, ServerPlayerActionAckPacket.class);

        this.registerOutgoing(0x00, ClientTeleportConfirmPacket.class);
        this.registerOutgoing(0x01, ClientBlockNBTRequestPacket.class);
        this.registerOutgoing(0x02, ClientSetDifficultyPacket.class);
        this.registerOutgoing(0x03, ClientChatPacket.class);
        this.registerOutgoing(0x04, ClientRequestPacket.class);
        this.registerOutgoing(0x05, ClientSettingsPacket.class);
        this.registerOutgoing(0x06, ClientTabCompletePacket.class);
        this.registerOutgoing(0x07, ClientConfirmTransactionPacket.class);
        this.registerOutgoing(0x08, ClientClickWindowButtonPacket.class);
        this.registerOutgoing(0x09, ClientWindowActionPacket.class);
        this.registerOutgoing(0x0A, ClientCloseWindowPacket.class);
        this.registerOutgoing(0x0B, ClientPluginMessagePacket.class);
        this.registerOutgoing(0x0C, ClientEditBookPacket.class);
        this.registerOutgoing(0x0D, ClientEntityNBTRequestPacket.class);
        this.registerOutgoing(0x0E, ClientPlayerInteractEntityPacket.class);
        this.registerOutgoing(0x0F, ClientKeepAlivePacket.class);
        this.registerOutgoing(0x10, ClientLockDifficultyPacket.class);
        this.registerOutgoing(0x11, ClientPlayerPositionPacket.class);
        this.registerOutgoing(0x12, ClientPlayerPositionRotationPacket.class);
        this.registerOutgoing(0x13, ClientPlayerRotationPacket.class);
        this.registerOutgoing(0x14, ClientPlayerMovementPacket.class);
        this.registerOutgoing(0x15, ClientVehicleMovePacket.class);
        this.registerOutgoing(0x16, ClientSteerBoatPacket.class);
        this.registerOutgoing(0x17, ClientMoveItemToHotbarPacket.class);
        this.registerOutgoing(0x18, ClientPrepareCraftingGridPacket.class);
        this.registerOutgoing(0x19, ClientPlayerAbilitiesPacket.class);
        this.registerOutgoing(0x1A, ClientPlayerActionPacket.class);
        this.registerOutgoing(0x1B, ClientPlayerStatePacket.class);
        this.registerOutgoing(0x1C, ClientSteerVehiclePacket.class);
        this.registerOutgoing(0x1D, ClientCraftingBookDataPacket.class);
        this.registerOutgoing(0x1E, ClientRenameItemPacket.class);
        this.registerOutgoing(0x1F, ClientResourcePackStatusPacket.class);
        this.registerOutgoing(0x20, ClientAdvancementTabPacket.class);
        this.registerOutgoing(0x21, ClientSelectTradePacket.class);
        this.registerOutgoing(0x22, ClientSetBeaconEffectPacket.class);
        this.registerOutgoing(0x23, ClientPlayerChangeHeldItemPacket.class);
        this.registerOutgoing(0x24, ClientUpdateCommandBlockPacket.class);
        this.registerOutgoing(0x25, ClientUpdateCommandBlockMinecartPacket.class);
        this.registerOutgoing(0x26, ClientCreativeInventoryActionPacket.class);
        this.registerOutgoing(0x27, ClientUpdateJigsawBlockPacket.class);
        this.registerOutgoing(0x28, ClientUpdateStructureBlockPacket.class);
        this.registerOutgoing(0x29, ClientUpdateSignPacket.class);
        this.registerOutgoing(0x2A, ClientPlayerSwingArmPacket.class);
        this.registerOutgoing(0x2B, ClientSpectatePacket.class);
        this.registerOutgoing(0x2C, ClientPlayerPlaceBlockPacket.class);
        this.registerOutgoing(0x2D, ClientPlayerUseItemPacket.class);
    }

    private void initServerGame(Session session) {
        this.registerIncoming(0x00, ClientTeleportConfirmPacket.class);
        this.registerIncoming(0x01, ClientBlockNBTRequestPacket.class);
        this.registerIncoming(0x02, ClientSetDifficultyPacket.class);
        this.registerIncoming(0x03, ClientChatPacket.class);
        this.registerIncoming(0x04, ClientRequestPacket.class);
        this.registerIncoming(0x05, ClientSettingsPacket.class);
        this.registerIncoming(0x06, ClientTabCompletePacket.class);
        this.registerIncoming(0x07, ClientConfirmTransactionPacket.class);
        this.registerIncoming(0x08, ClientClickWindowButtonPacket.class);
        this.registerIncoming(0x09, ClientWindowActionPacket.class);
        this.registerIncoming(0x0A, ClientCloseWindowPacket.class);
        this.registerIncoming(0x0B, ClientPluginMessagePacket.class);
        this.registerIncoming(0x0C, ClientEditBookPacket.class);
        this.registerIncoming(0x0D, ClientEntityNBTRequestPacket.class);
        this.registerIncoming(0x0E, ClientPlayerInteractEntityPacket.class);
        this.registerIncoming(0x0F, ClientKeepAlivePacket.class);
        this.registerIncoming(0x10, ClientLockDifficultyPacket.class);
        this.registerIncoming(0x11, ClientPlayerPositionPacket.class);
        this.registerIncoming(0x12, ClientPlayerPositionRotationPacket.class);
        this.registerIncoming(0x13, ClientPlayerRotationPacket.class);
        this.registerIncoming(0x14, ClientPlayerMovementPacket.class);
        this.registerIncoming(0x15, ClientVehicleMovePacket.class);
        this.registerIncoming(0x16, ClientSteerBoatPacket.class);
        this.registerIncoming(0x17, ClientMoveItemToHotbarPacket.class);
        this.registerIncoming(0x18, ClientPrepareCraftingGridPacket.class);
        this.registerIncoming(0x19, ClientPlayerAbilitiesPacket.class);
        this.registerIncoming(0x1A, ClientPlayerActionPacket.class);
        this.registerIncoming(0x1B, ClientPlayerStatePacket.class);
        this.registerIncoming(0x1C, ClientSteerVehiclePacket.class);
        this.registerIncoming(0x1D, ClientCraftingBookDataPacket.class);
        this.registerIncoming(0x1E, ClientRenameItemPacket.class);
        this.registerIncoming(0x1F, ClientResourcePackStatusPacket.class);
        this.registerIncoming(0x20, ClientAdvancementTabPacket.class);
        this.registerIncoming(0x21, ClientSelectTradePacket.class);
        this.registerIncoming(0x22, ClientSetBeaconEffectPacket.class);
        this.registerIncoming(0x23, ClientPlayerChangeHeldItemPacket.class);
        this.registerIncoming(0x24, ClientUpdateCommandBlockPacket.class);
        this.registerIncoming(0x25, ClientUpdateCommandBlockMinecartPacket.class);
        this.registerIncoming(0x26, ClientCreativeInventoryActionPacket.class);
        this.registerIncoming(0x27, ClientUpdateJigsawBlockPacket.class);
        this.registerIncoming(0x28, ClientUpdateStructureBlockPacket.class);
        this.registerIncoming(0x29, ClientUpdateSignPacket.class);
        this.registerIncoming(0x2A, ClientPlayerSwingArmPacket.class);
        this.registerIncoming(0x2B, ClientSpectatePacket.class);
        this.registerIncoming(0x2C, ClientPlayerPlaceBlockPacket.class);
        this.registerIncoming(0x2D, ClientPlayerUseItemPacket.class);

        this.registerOutgoing(0x00, ServerSpawnObjectPacket.class);
        this.registerOutgoing(0x01, ServerSpawnExpOrbPacket.class);
        this.registerOutgoing(0x02, ServerSpawnGlobalEntityPacket.class);
        this.registerOutgoing(0x03, ServerSpawnMobPacket.class);
        this.registerOutgoing(0x04, ServerSpawnPaintingPacket.class);
        this.registerOutgoing(0x05, ServerSpawnPlayerPacket.class);
        this.registerOutgoing(0x06, ServerEntityAnimationPacket.class);
        this.registerOutgoing(0x07, ServerStatisticsPacket.class);
        this.registerOutgoing(0x08, ServerBlockBreakAnimPacket.class);
        this.registerOutgoing(0x09, ServerUpdateTileEntityPacket.class);
        this.registerOutgoing(0x0A, ServerBlockValuePacket.class);
        this.registerOutgoing(0x0B, ServerBlockChangePacket.class);
        this.registerOutgoing(0x0C, ServerBossBarPacket.class);
        this.registerOutgoing(0x0D, ServerDifficultyPacket.class);
        this.registerOutgoing(0x0E, ServerChatPacket.class);
        this.registerOutgoing(0x0F, ServerMultiBlockChangePacket.class);
        this.registerOutgoing(0x10, ServerTabCompletePacket.class);
        this.registerOutgoing(0x11, ServerDeclareCommandsPacket.class);
        this.registerOutgoing(0x12, ServerConfirmTransactionPacket.class);
        this.registerOutgoing(0x13, ServerCloseWindowPacket.class);
        this.registerOutgoing(0x14, ServerWindowItemsPacket.class);
        this.registerOutgoing(0x15, ServerWindowPropertyPacket.class);
        this.registerOutgoing(0x16, ServerSetSlotPacket.class);
        this.registerOutgoing(0x17, ServerSetCooldownPacket.class);
        this.registerOutgoing(0x18, ServerPluginMessagePacket.class);
        this.registerOutgoing(0x19, ServerPlaySoundPacket.class);
        this.registerOutgoing(0x1A, ServerDisconnectPacket.class);
        this.registerOutgoing(0x1B, ServerEntityStatusPacket.class);
        this.registerOutgoing(0x1C, ServerExplosionPacket.class);
        this.registerOutgoing(0x1D, ServerUnloadChunkPacket.class);
        this.registerOutgoing(0x1E, ServerNotifyClientPacket.class);
        this.registerOutgoing(0x1F, ServerOpenHorseWindowPacket.class);
        this.registerOutgoing(0x20, ServerKeepAlivePacket.class);
        this.registerOutgoing(0x21, ServerChunkDataPacket.class);
        this.registerOutgoing(0x22, ServerPlayEffectPacket.class);
        this.registerOutgoing(0x23, ServerSpawnParticlePacket.class);
        this.registerOutgoing(0x24, ServerUpdateLightPacket.class);
        this.registerOutgoing(0x25, ServerJoinGamePacket.class);
        this.registerOutgoing(0x26, ServerMapDataPacket.class);
        this.registerOutgoing(0x27, ServerTradeListPacket.class);
        this.registerOutgoing(0x28, ServerEntityPositionPacket.class);
        this.registerOutgoing(0x29, ServerEntityPositionRotationPacket.class);
        this.registerOutgoing(0x2A, ServerEntityRotationPacket.class);
        this.registerOutgoing(0x2B, ServerEntityMovementPacket.class);
        this.registerOutgoing(0x2C, ServerVehicleMovePacket.class);
        this.registerOutgoing(0x2D, ServerOpenBookPacket.class);
        this.registerOutgoing(0x2E, ServerOpenWindowPacket.class);
        this.registerOutgoing(0x2F, ServerOpenTileEntityEditorPacket.class);
        this.registerOutgoing(0x30, ServerPreparedCraftingGridPacket.class);
        this.registerOutgoing(0x31, ServerPlayerAbilitiesPacket.class);
        this.registerOutgoing(0x32, ServerCombatPacket.class);
        this.registerOutgoing(0x33, ServerPlayerListEntryPacket.class);
        this.registerOutgoing(0x34, ServerPlayerFacingPacket.class);
        this.registerOutgoing(0x35, ServerPlayerPositionRotationPacket.class);
        this.registerOutgoing(0x36, ServerUnlockRecipesPacket.class);
        this.registerOutgoing(0x37, ServerEntityDestroyPacket.class);
        this.registerOutgoing(0x38, ServerEntityRemoveEffectPacket.class);
        this.registerOutgoing(0x39, ServerResourcePackSendPacket.class);
        this.registerOutgoing(0x3A, ServerRespawnPacket.class);
        this.registerOutgoing(0x3B, ServerEntityHeadLookPacket.class);
        this.registerOutgoing(0x3C, ServerAdvancementTabPacket.class);
        this.registerOutgoing(0x3D, ServerWorldBorderPacket.class);
        this.registerOutgoing(0x3E, ServerSwitchCameraPacket.class);
        this.registerOutgoing(0x3F, ServerPlayerChangeHeldItemPacket.class);
        this.registerOutgoing(0x40, ServerUpdateViewPositionPacket.class);
        this.registerOutgoing(0x41, ServerUpdateViewDistancePacket.class);
        this.registerOutgoing(0x42, ServerDisplayScoreboardPacket.class);
        this.registerOutgoing(0x43, ServerEntityMetadataPacket.class);
        this.registerOutgoing(0x44, ServerEntityAttachPacket.class);
        this.registerOutgoing(0x45, ServerEntityVelocityPacket.class);
        this.registerOutgoing(0x46, ServerEntityEquipmentPacket.class);
        this.registerOutgoing(0x47, ServerPlayerSetExperiencePacket.class);
        this.registerOutgoing(0x48, ServerPlayerHealthPacket.class);
        this.registerOutgoing(0x49, ServerScoreboardObjectivePacket.class);
        this.registerOutgoing(0x4A, ServerEntitySetPassengersPacket.class);
        this.registerOutgoing(0x4B, ServerTeamPacket.class);
        this.registerOutgoing(0x4C, ServerUpdateScorePacket.class);
        this.registerOutgoing(0x4D, ServerSpawnPositionPacket.class);
        this.registerOutgoing(0x4E, ServerUpdateTimePacket.class);
        this.registerOutgoing(0x4F, ServerTitlePacket.class);
        this.registerOutgoing(0x50, ServerEntitySoundEffectPacket.class);
        this.registerOutgoing(0x51, ServerPlayBuiltinSoundPacket.class);
        this.registerOutgoing(0x52, ServerStopSoundPacket.class);
        this.registerOutgoing(0x53, ServerPlayerListDataPacket.class);
        this.registerOutgoing(0x54, ServerNBTResponsePacket.class);
        this.registerOutgoing(0x55, ServerEntityCollectItemPacket.class);
        this.registerOutgoing(0x56, ServerEntityTeleportPacket.class);
        this.registerOutgoing(0x57, ServerAdvancementsPacket.class);
        this.registerOutgoing(0x58, ServerEntityPropertiesPacket.class);
        this.registerOutgoing(0x59, ServerEntityEffectPacket.class);
        this.registerOutgoing(0x5A, ServerDeclareRecipesPacket.class);
        this.registerOutgoing(0x5B, ServerDeclareTagsPacket.class);
        this.registerOutgoing(0x5C, ServerPlayerActionAckPacket.class);
    }

    private void initClientStatus(Session session) {
        this.registerIncoming(0x00, StatusResponsePacket.class);
        this.registerIncoming(0x01, StatusPongPacket.class);

        this.registerOutgoing(0x00, StatusQueryPacket.class);
        this.registerOutgoing(0x01, StatusPingPacket.class);
    }

    private void initServerStatus(Session session) {
        this.registerIncoming(0x00, StatusQueryPacket.class);
        this.registerIncoming(0x01, StatusPingPacket.class);

        this.registerOutgoing(0x00, StatusResponsePacket.class);
        this.registerOutgoing(0x01, StatusPongPacket.class);
    }
}
