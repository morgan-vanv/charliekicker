package com.charliekicker;

import com.google.inject.Provides;

import javax.inject.Provider;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
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

// getTarget = Charlie the Tramp
// getType = 1003
// getIdentifier = 26381

// TODO:
// 1. make the message appear above player's head


public class CharlieKickerPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private Provider<MenuManager> menuManager;

	@Inject
	private CharlieKickerConfig config;

	private boolean isKickQueued;

	@Override
	protected void startUp() throws Exception
	{
		log.info("CharlieKicker started!");
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("CharlieKicker stopped!");
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (!isKickQueued) { return; } // Removing this allows any chat message to trigger the kick, which is not desired.
		if (event.getType() == ChatMessageType.ENGINE && event.getMessage().contains("Nothing interesting happens"))
		{
			executeKick();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event){
		if (event.getMenuOption().equals("Use") && event.getMenuTarget().contains("boots") && event.getMenuTarget().endsWith("Tramp"))
		{
			log.info("Kick queued for Charlie the Tramp with boots: " + event.getMenuTarget());
			isKickQueued = true;
		}

	}

	private void executeKick() {
		Player local = client.getLocalPlayer();
		client.playSoundEffect(2565);
		local.setAnimation(423);
		local.setActionFrame(0);
		// TODO: make this an above head chat message
		client.addChatMessage(ChatMessageType.PUBLICCHAT, local.getName(), config.message(), null);
		log.info("Kick executed.");
		// Reset the kick queue
		isKickQueued = false;
	}

	@Provides
	CharlieKickerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CharlieKickerConfig.class);
	}
}
