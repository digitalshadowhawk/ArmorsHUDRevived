package com.shadowhawk.armorshud;

import org.lwjgl.input.Keyboard;

import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;
import com.shadowhawk.armorshud.LiteModArmorsHUDRevived;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ConfigArmorsHUD implements ConfigPanel{
	
	/** Line spacing, in points. */
	private final static int SPACING = 24;
	  
	public static final String[] locationNames = {"Bottom Right", "Bottom Left", "Top Center"};
	private GuiButton location;
	private GuiButton activeButton;
	private GuiButton toggleArmors;
	private Minecraft minecraft;
	private LiteModArmorsHUDRevived shell = LiteModArmorsHUDRevived.instance;
	

	@Override
	public String getPanelTitle() {
		
		return "Armors HUD Settings";
	}

	@Override
	public int getContentHeight() {
		return SPACING * 3;
	}

	@Override
	public void onPanelShown(ConfigPanelHost host) {
		minecraft = Minecraft.getMinecraft();
	    int id = 0;
	    int line = 0;
	    toggleArmors = new GuiButton(id++, 10, SPACING * line++, "AHUDR: "+ shell.isVisible());
	    location = new GuiButton(id++, 10, SPACING * line++, locationNames[shell.getLocation()]);
	    
		
	}

	@Override
	public void onPanelResize(ConfigPanelHost host) {}

	@Override
	public void onPanelHidden()
	{
		LiteLoader.getInstance().writeConfig(shell);
	}

	@Override
	public void onTick(ConfigPanelHost host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawPanel(ConfigPanelHost host, int mouseX, int mouseY, float partialTicks) {
		toggleArmors.drawButton(minecraft, mouseX, mouseY);
		location.drawButton(minecraft, mouseX, mouseY);		
	}

	@Override
	public void mousePressed(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton) {
		if (toggleArmors.mousePressed(minecraft, mouseX, mouseY))
		{
			activeButton = toggleArmors;
			shell.toggleArmors();
			toggleArmors.displayString = ("AHUDR: "+ shell.isVisible());
			toggleArmors.playPressSound(minecraft.getSoundHandler());
		}
		if (location.mousePressed(minecraft, mouseX, mouseY))
		  {
			  activeButton = location;
			  shell.cycleLocation();
			  location.displayString = (locationNames[shell.getLocation()]);
			  location.playPressSound(minecraft.getSoundHandler());
		 }
		
	}

	@Override
	public void mouseReleased(ConfigPanelHost host, int mouseX, int mouseY, int mouseButton)
	{
		if (activeButton != null) {
		      activeButton.mouseReleased(mouseX, mouseY);
		      activeButton = null;
		    }
	}

	@Override
	public void mouseMoved(ConfigPanelHost host, int mouseX, int mouseY) {}

	@Override
	public void keyPressed(ConfigPanelHost host, char keyChar, int keyCode)
	{
		if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RETURN) {
		      host.close();
		}
	}

}
