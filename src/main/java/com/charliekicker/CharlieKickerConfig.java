package com.charliekicker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("general")
public interface CharlieKickerConfig extends Config
{
	// GENERAL SETTINGS
	@ConfigSection(
			name = "General Settings",
			description = "General Settings",
			position = 0,
			closedByDefault = false
	)
	String generalSettings = "generalSettings";

	@ConfigItem(
			keyName = "message",
			name = "Kick Message",
			description = "The message that is sent when kicking Charlie"
	)
	default String message()
	{
		return "I hate you and your dumb clue scrolls Charlie! Take this!!";
	}
}
