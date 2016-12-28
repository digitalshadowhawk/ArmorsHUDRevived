package com.shadowhawk.armorshud;

import java.io.File;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mumfrey.liteloader.Configurable;
import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.Tickable;
import com.mumfrey.liteloader.core.LiteLoader;
import com.mumfrey.liteloader.modconfig.ConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

@ExposableOptions(
		strategy = ConfigStrategy.Unversioned,
		filename = "armorshudrevived.json",
		aggressive = true
)

public class LiteModArmorsHUDRevived implements HUDRenderListener, Tickable, Configurable
{
    public static float partialTicks;
    public static KeyBinding toggleArmors;
    public static KeyBinding cycleLocation;
    public ArmorsHUDRenderer renderer = new ArmorsHUDRenderer();
    public static LiteModArmorsHUDRevived instance;
	@Expose
	@SerializedName("hud_location")
	private int location;
	
	@Expose
	@SerializedName("hud_enabled")
	private boolean enabled = true;
    
    public LiteModArmorsHUDRevived()
    {
        partialTicks = 0.0F;
    }

    @Override
	public String getName()
    {
        return "Armors HUD Revived";
    }

    @Override
	public String getVersion()
    {
        return "1.1.0-beta";
    }

    @Override
	public void init(File configPath) {
    	instance = this;
    	toggleArmors = new KeyBinding("Toggle Armors HUD", 0, "Armors HUD Revived");
    	LiteLoader.getInput().registerKeyBinding(toggleArmors);
    	cycleLocation = new KeyBinding("Cycle HUD Location", 0, "Armors HUD Revived");
    	LiteLoader.getInput().registerKeyBinding(cycleLocation);
    	this.renderer.setVisible(enabled);
    	this.renderer.setLocation(location);
    }

    @Override
	public void onPostRenderHUD(int screenWidth, int screenHeight)
    {
        this.renderer.render(screenWidth, screenHeight);
    }

    @Override
	public void onPreRenderHUD(int screenWidth, int screenHeight) {}

    @Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
    	if (toggleArmors.isPressed())
        {
            toggleArmors();            
        }
    	if (cycleLocation.isPressed())
        {
            cycleLocation();            
        }
    }
    
    public void toggleArmors()
    {
    	this.renderer.toggleArmors();
        enabled = this.renderer.isVisible();
        LiteLoader.getInstance().writeConfig(this);
    }
    
    public void cycleLocation()
    {
    	this.renderer.cycleLocation();            
    	location = getLocation();
        LiteLoader.getInstance().writeConfig(this);
    }
    
    public void setLocation(int locationIndex)
    {
    	this.renderer.setLocation(locationIndex);            
    	location = getLocation();
        LiteLoader.getInstance().writeConfig(this);
    }
    
    public boolean isVisible()
    {
    	return this.renderer.isVisible();
    }
    
    public int getLocation()
    {
    	return this.renderer.getLocation();
    }

    @Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {
		
		return ConfigArmorsHUD.class;
	}
}
