package com.charliekicker;

import com.google.inject.Provides;
import java.util.Arrays;
import javax.inject.Provider;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.menus.MenuManager;

@Slf4j
@PluginDescriptor(
	name = "CharlieKicker"
)
public class CharlieKickerPlugin extends Plugin
{
	private static final String KICK_CHARLIE = "KICK!";

	@Inject
	private Client client;

	@Inject
	private Provider<MenuManager> menuManager;

	@Inject
	private CharlieKickerConfig config;

	private LocalPoint charlieLocation;

	@Override
	protected void startUp() throws Exception
	{
		log.info("CharlieKicker started!");
		addMenuItem();
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("CharlieKicker stopped!");
		removeMenuItem();
	}

	private synchronized void removeMenuItem() {
		menuManager.get().removePlayerMenuItem(KICK_CHARLIE);
	}

	private synchronized void addMenuItem() {
		if (client == null) return;
		if (client.getNpcs().stream().noneMatch(npc -> npc.getName() != null && npc.getName().endsWith("Tramp"))) {
			log.info("the trigger just triggered, adding menu item");
			menuManager.get().addPlayerMenuItem(KICK_CHARLIE);
			menuManager.addNpcMenuOption();

		}
//		if (Arrays.stream(client.getPlayerOptions()).noneMatch(KICK_CHARLIE::equals)) {
//			menuManager.get().addPlayerMenuItem(KICK_CHARLIE);
//		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event) {
		addMenuItem();
	}


	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked e)
	{
		log.info(e.toString());
		log.info(e.getMenuTarget().toString());
		if (e.getMenuTarget().endsWith("Tramp"))
		{
			log.info("charlie location assigned");
			charlieLocation = LocalPoint.fromScene(e.getParam0(), e.getParam1());
		}
		else
		{
			charlieLocation = null;
		}
	}

	@Subscribe
	public void onClientTick(ClientTick e)
	{
		if (charlieLocation != null)
		{
			Player local = client.getLocalPlayer();
			if (charlieLocation.distanceTo(local.getLocalLocation()) <= Perspective.LOCAL_TILE_SIZE)
			{
				local.setAnimation(423);
				local.setActionFrame(0);
				charlieLocation = null;
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}

	@Provides
	CharlieKickerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CharlieKickerConfig.class);
	}
}
