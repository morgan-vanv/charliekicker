package com.charliekicker;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CharlieKickerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CharlieKickerPlugin.class);
		RuneLite.main(args);
	}
}