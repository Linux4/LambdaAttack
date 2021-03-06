package com.github.games647.lambdaattack.bot;

import com.github.games647.lambdaattack.LambdaAttack;
import com.github.games647.lambdaattack.UniversalProtocol;
import com.github.games647.lambdaattack.bot.listener.SessionListener110;
import com.github.games647.lambdaattack.bot.listener.SessionListener111;
import com.github.games647.lambdaattack.bot.listener.SessionListener112;
import com.github.games647.lambdaattack.bot.listener.SessionListener114;
import com.github.games647.lambdaattack.bot.listener.SessionListener17;
import com.github.games647.lambdaattack.bot.listener.SessionListener18;
import com.github.games647.lambdaattack.bot.listener.SessionListener19;

import java.net.Proxy;
import java.util.logging.Logger;

import org.spacehq.mc.auth.data.GameProfile;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

public class Bot {

	public static final char COMMAND_IDENTIFIER = '/';

	private final Proxy proxy;
	private final Logger logger;
	private final UniversalProtocol account;

	private Session session;
	private EntitiyLocation location;
	private float health = -1;
	private float food = -1;

	public Bot(UniversalProtocol account) {
		this(account, Proxy.NO_PROXY);
	}

	public Bot(UniversalProtocol account, Proxy proxy) {
		this.account = account;
		this.proxy = proxy;

		this.logger = Logger.getLogger(account.getGameProfile().getName());
		this.logger.setParent(LambdaAttack.getLogger());
	}

	public void connect(String host, int port) throws RequestException {
		Client client = new Client(host, port, account.getProtocol(), new TcpSessionFactory(proxy));
		this.session = client.getSession();

		switch (account.getGameVersion()) {
		case VERSION_1_14:
			client.getSession().addListener(new SessionListener114(this));
			break;
		case VERSION_1_12:
			client.getSession().addListener(new SessionListener112(this));
			break;
		case VERSION_1_11:
			client.getSession().addListener(new SessionListener111(this));
			break;
		case VERSION_1_10:
			client.getSession().addListener(new SessionListener110(this));
			break;
		case VERSION_1_9:
			client.getSession().addListener(new SessionListener19(this));
			break;
		case VERSION_1_8:
			client.getSession().addListener(new SessionListener18(this));
			break;
		case VERSION_1_7:
			client.getSession().addListener(new SessionListener17(this));
			break;
		default:
			throw new IllegalStateException("Unknown session listener");
		}

		client.getSession().connect();
	}

	public void sendMessage(String message) {
		if (session != null) {
			switch (account.getGameVersion()) {
			case VERSION_1_14:
				session.send(new org.spacehq.mc.protocol.v1_14.packet.ingame.client.ClientChatPacket(message));
				break;
			case VERSION_1_12:
				session.send(new org.spacehq.mc.protocol.v1_12.packet.ingame.client.ClientChatPacket(message));
				break;
			case VERSION_1_11:
				session.send(new org.spacehq.mc.protocol.v1_11.packet.ingame.client.ClientChatPacket(message));
				break;
			case VERSION_1_10:
				session.send(new org.spacehq.mc.protocol.v1_10.packet.ingame.client.ClientChatPacket(message));
				break;
			case VERSION_1_9:
				session.send(new org.spacehq.mc.protocol.v1_9.packet.ingame.client.ClientChatPacket(message));
				break;
			case VERSION_1_8:
				session.send(new org.spacehq.mc.protocol.v1_8.packet.ingame.client.ClientChatPacket(message));
				break;
			case VERSION_1_7:
				session.send(new org.spacehq.mc.protocol.v1_7.packet.ingame.client.ClientChatPacket(message));
				break;
			default:
				throw new IllegalArgumentException("Invalid game version");
			}
		}
	}

	public boolean isOnline() {
		return session != null && session.isConnected();
	}

	public Session getSession() {
		return session;
	}

	public EntitiyLocation getLocation() {
		return location;
	}

	public void setLocation(EntitiyLocation location) {
		this.location = location;
	}

	public double getHealth() {
		return health;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	public float getFood() {
		return food;
	}

	public void setFood(float food) {
		this.food = food;
	}

	public Logger getLogger() {
		return logger;
	}

	public GameProfile getGameProfile() {
		return account.getGameProfile();
	}

	public Proxy getProxy() {
		return proxy;
	}

	public void disconnect() {
		if (session != null) {
			session.disconnect("Disconnect");
		}
	}
}
