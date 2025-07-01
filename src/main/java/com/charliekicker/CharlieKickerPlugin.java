package com.charliekicker;

import com.google.inject.Provides;

import javax.inject.Provider;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
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
// 1. add a proper menu entry for ease of kicking
// 2. say stuff randomly when doing so! "DIE DIE DIE" "I HATE U I HATE U I HATE U"
// 3. make it so you can kick players? or all npcs?
// 4. nail beast sounds every click
// 5. allow usage of gloves/gauntlets to punch
// 6. allow dynamic messages to be passed in
// 7. use the config to allow enablement of punches, kicks, use on players, and objects like walls and chairs



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


//	@Subscribe
//	public void onClientTick(ClientTick e) {}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (!isKickQueued) { return; } // Removing this allows any chat message to trigger the kick, which is not desired.
		if (event.getType() == ChatMessageType.ENGINE && event.getMessage().contains("Nothing interesting happens"))
		{
			log.info("Charlie the Tramp kicked successfully.");
			executeKick();
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event){
		// MenuOptionClicked(getParam0=0, getParam1=0, getMenuOption=Use, getMenuTarget=<col=ff9040>Leather boots</col><col=ffffff> -> <col=ffff00>Charlie the Tramp, getMenuAction=WIDGET_TARGET_ON_NPC, getId=26381)
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
		client.addChatMessage(ChatMessageType.PUBLICCHAT, local.getName(), "DIE DIE DIE", null);
		log.info("Kick executed.");
		// Reset the kick queue
		isKickQueued = false;
	}

	private void executePunch() {

	}

	@Provides
	CharlieKickerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CharlieKickerConfig.class);
	}
}
