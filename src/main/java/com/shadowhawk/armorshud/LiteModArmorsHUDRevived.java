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
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;

@ExposableOptions(strategy = ConfigStrategy.Unversioned, filename = "armorshudrevived.json", aggressive = true)

/**
 * Main class containing the backbone of the Armors HUD Revived mod
 * 
 * @author Zachary Cook
 */
public class LiteModArmorsHUDRevived implements HUDRenderListener, Tickable, Configurable {
	public static KeyBinding cycleLocation;
	public static LiteModArmorsHUDRevived instance;
	public static float partialTicks;
	public static KeyBinding toggleArmors;
	@Expose
	@SerializedName("hud_enabled")
	private boolean enabled = true;
	@Expose
	@SerializedName("hud_location")
	private int location;

	public ArmorsHUDRenderer renderer = new ArmorsHUDRenderer();

	public LiteModArmorsHUDRevived() {
		partialTicks = 0.0F;
	}

	/**
	 * Tells the renderer to cycle through the valid locations to display the
	 * overlay and saves that location to the config
	 */
	public void cycleLocation() {
		this.renderer.cycleLocation();
		location = getLocation();
		LiteLoader.getInstance().writeConfig(this);
	}

	@Override
	public Class<? extends ConfigPanel> getConfigPanelClass() {

		return ArmorsHUDConfigPanel.class;
	}

	/**
	 * Gets the index of the location where the renderer is set to show the
	 * overlay;
	 * 
	 * This is only used for setting the index in the config file
	 * 
	 * @return index of the render location
	 */
	public int getLocation() {
		return this.renderer.getLocation();
	}

	/**
	 * Gets the unlocalized string describing where on the screen the renderer
	 * is set to show the overlay;
	 * 
	 * This is only used by the config panel for button text
	 * 
	 * @return unlocalized string describing render location
	 */
	public String getLocationString() {
		return this.renderer.getLocationString();
	}

	@Override
	public String getName() {
		return "Armors HUD Revived";//I18n.format("armorshud.config.name");
	}

	@Override
	public String getVersion() {
		return "1.2.0";
	}

	@Override
	public void init(File configPath) {
		instance = this;
		toggleArmors = new KeyBinding(I18n.format("armorshud.control.toggle"), 0, I18n.format("armorshud.config.name"));
		LiteLoader.getInput().registerKeyBinding(toggleArmors);
		cycleLocation = new KeyBinding(I18n.format("armorshud.control.cycle"), 0, I18n.format("armorshud.config.name"));
		LiteLoader.getInput().registerKeyBinding(cycleLocation);
		this.renderer.setEnabled(enabled);
		this.renderer.setLocation(location);
	}

	/**
	 * Asks the renderer whether it is enabled or not
	 * 
	 * @return if the renderer is enabled
	 */
	public boolean isEnabled() {
		return this.renderer.isEnabled();
	}

	@Override
	public void onPostRenderHUD(int screenWidth, int screenHeight) {
		this.renderer.render(screenWidth, screenHeight);
	}

	@Override
	public void onPreRenderHUD(int screenWidth, int screenHeight) {
	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		if (toggleArmors.isPressed()) {
			toggleArmors();
		}
		if (cycleLocation.isPressed()) {
			cycleLocation();
		}
	}

	/**
	 * Tells the renderer to switch to a specific location to display the
	 * overlay and saves that location to the config
	 * 
	 * @param locationIndex the index of the location to change to
	 */
	public void setLocation(int locationIndex) {
		this.renderer.setLocation(locationIndex);
		location = getLocation();
		LiteLoader.getInstance().writeConfig(this);
	}

	/**
	 * Tells the renderer to toggle whether the armor overlay is enabled and
	 * saves that value to the config
	 */
	public void toggleArmors() {
		this.renderer.toggleArmors();
		enabled = this.renderer.isEnabled();
		LiteLoader.getInstance().writeConfig(this);
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}
}
