package com.shadowhawk.armorshud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;

public class ArmorsHUDRenderer {
	
	/**
	 * An enum of locations for the overlay to render at on-screen:
	 * <pre>
	 * BOTTOM_RIGHT: 0
	 * BOTTOM_LEFT: 1
	 * TOP_CENTER: 2
	 * </pre>
	 */
	private enum Location
	{
		BOTTOM_LEFT(1),
		BOTTOM_RIGHT(0),
		TOP_CENTER(2);
		
		public final static int locations = 3;
		private int value;

        /**
         * instantiates the enums
         * @param newValue
         */
		Location(final int newValue) {
            value = newValue;
        }
        
        /**
         * Returns a processed location based on screen width and index slot
         * @param screenWidth	width of the minecraft screen
         * @param i				index of the armor slot being processed
         * @return				the location the item should be rendered
         */
		public int processX(int screenWidth, int i) {
        	if(value == 0) {
        		return screenWidth - 20;
        	} else if(value == 1) {
        		return 5;
        	} else if(value == 2) {
        		//return screenWidth/2+(((10*(3-i))-15)*2)-10;
        		return screenWidth/2 - 20*(i-1);
        	} else {
        		return -1;
        	}
        }
		
		/**
		 * Returns a processed location based on screen height and index slot
		 * @param screenHeight	height of the minecraft screen
         * @param i				index of the armor slot being processed
         * @return				the location the item should be rendered
		 */
        public int processY(int screenHeight, int i) {
        	if(value == 0 || value ==1) {
        		return screenHeight - 40 - i * 20;
        	} else if(value == 2) {
        		return 5;
        	} else {
        		return -1;
        	}
        }
        
        /**
         * changes the enum to i
         * @param i	the new enum index
         */
		public void set(int i)
        {
        	value = i;
        }
        
		@Override
		public String toString()
		{
			if(value == 0)
			{
				return "armorshud.config.bottomright";
			} else if (value == 1) {
				return "armorshud.config.bottomleft";
			} else if (value == 2) {
				return "armorshud.config.topcenter";
			} else {
				return "Internal Error 001";
			}
		}
	}
	private boolean enabled = true;
	
	private Location location = Location.BOTTOM_RIGHT;
	
	/**
	 * Increments the index of the location the overlay should be rendered
	 * on-screen by one, resetting to zero when it reaches the max index
	 */
	public void cycleLocation()
	{
		location.set(((location.value + 1) % Location.locations));
	}
	
	public int getLocation()
	{
		return location.value;
	}
	
	/**
	 * Returns the unlocalized string for the current location
	 * @return the unlocalized string for the current location
	 */
	public String getLocationString()
	{
		return location.toString();
	}
	
	/**
	 * Tells whether the armor overlay is enabled
	 * @return	whether or not the armor overlay is enabled
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/**
	 * Renders the overlay based on information about the size of the Minecraft window
	 * @param screenWidth	the width of the window
	 * @param screenHeight	the height of the window
	 */
	public void render(int screenWidth, int screenHeight)
	{
		Minecraft mc = Minecraft.getMinecraft();

        //This will only work if the player exists and has an inventory, and if the overlay is enabled
		//Essentially has to be loaded into a world
		if (mc.player != null && mc.player.inventory != null && enabled)
        {
            //Check to make sure armor overlay doesn't render while in spectator
			if (mc.world.getWorldInfo().getGameType() != GameType.SPECTATOR)
        	{
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                for (int i = 0; i < 4; ++i)
                {
                	int x = location.processX(screenWidth, i);
                	int y = location.processY(screenHeight, i);
                	this.renderArmor(i, x, y, mc.getRenderPartialTicks(), mc.player);
                }
               
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
	}

	/**
	 * Renders an armor item based on which armor slot and a specific set of coordinates
	 * @param armorSlot				The index of armor slot to render the item from
	 * @param xPos					The x-coordinate to render on-screen
	 * @param yPos					The y-coordinate to render on-screen
	 * @param passedPartialTicks	The tick count used for animation of enchanted items
	 * @param player				The player wearing the armor
	 */
	private void renderArmor(int armorSlot, int xPos, int yPos, float passedPartialTicks, EntityPlayer player)
    {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        ItemStack armorItem = player.inventory.armorInventory.get(armorSlot);

        if (armorItem != null)
        {
            float animationFrames = armorItem.getAnimationsToGo() - passedPartialTicks;

            if (animationFrames > 0.0F)
            {
                GlStateManager.pushMatrix();
                float var8 = 1.0F + animationFrames / 5.0F;
                GlStateManager.translate(xPos + 8, yPos + 12, 0.0F);
                GlStateManager.scale(1.0F / var8, (var8 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((-(xPos + 8)), (-(yPos + 12)), 0.0F);
            }

            itemRenderer.renderItemIntoGUI(armorItem, xPos, yPos);

            if (animationFrames > 0.0F)
            {
                GlStateManager.popMatrix();
            }

            itemRenderer.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, armorItem, xPos, yPos);
        }
    }

	/**
	 * Turns the armor overlay on or off based on the boolean value given
	 * @param status whether to render the overlay or not
	 */
	public void setEnabled(boolean status) {
		enabled = status;		
	}

	/**
	 * Sets the location of the armor overlay on the screen manually
	 * @param loc the index of the overlay position
	 */
	public void setLocation(int loc) {
		location.set(loc);
	}
	
	/**
	 * Toggles the armor overlay on or off
	 */
	public void toggleArmors() {
		this.enabled = !this.enabled;
	}
}
