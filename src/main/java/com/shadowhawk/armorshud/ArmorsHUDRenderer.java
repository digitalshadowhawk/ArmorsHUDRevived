package com.shadowhawk.armorshud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameType;

public class ArmorsHUDRenderer {
	
	private boolean enabled = true;
	public static final int BOTTOM_RIGHT = 0;
	public static final int BOTTOM_LEFT = 1;
	public static final int TOP_CENTER = 2;
	private int location = 0;
	
	public boolean isVisible()
	{
		return enabled;
	}
	
	public void cycleLocation()
	{
		location = ((location + 1)%3);
	}
	
	public int getLocation()
	{
		return location;
	}
	
	public void render(int screenWidth, int screenHeight)
	{
		Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer != null && mc.thePlayer.inventory != null && enabled)
        {
            if (mc.theWorld.getWorldInfo().getGameType() != GameType.SPECTATOR)
        	{
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();
                if(location == BOTTOM_RIGHT)
                {
                	for (int i = 0; i < 4; ++i)
                	{
                		int y = screenHeight - 40 - i * 20;
                		this.renderArmor(i, screenWidth - 20, y, mc.getRenderPartialTicks(), mc.thePlayer);
                	}
                } else if (location == BOTTOM_LEFT)
                {
                	for (int i = 0; i < 4; ++i)
                	{
                		int y = screenHeight - 40 - i * 20;
                		this.renderArmor(i, 5, y, mc.getRenderPartialTicks(), mc.thePlayer);
                	}
                } else if (location == TOP_CENTER)
                {
                	for (int i = 0; i < 4; ++i)
                	{
                		int x = screenWidth / 2 + (((10*(3-i))-15) * 2) - 10;
                		this.renderArmor(i, x, 5, mc.getRenderPartialTicks(), mc.thePlayer);
                	}
                }
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
	}
	
	private void renderArmor(int armorSlot, int x, int y, float passedPartialTicks, EntityPlayer player)
    {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        ItemStack armorItem = player.inventory.armorInventory[armorSlot];

        if (armorItem != null)
        {
            float animationFrames = armorItem.animationsToGo - passedPartialTicks;

            if (animationFrames > 0.0F)
            {
                GlStateManager.pushMatrix();
                float var8 = 1.0F + animationFrames / 5.0F;
                GlStateManager.translate(x + 8, y + 12, 0.0F);
                GlStateManager.scale(1.0F / var8, (var8 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((-(x + 8)), (-(y + 12)), 0.0F);
            }

            itemRenderer.renderItemIntoGUI(armorItem, x, y);

            if (animationFrames > 0.0F)
            {
                GlStateManager.popMatrix();
            }

            itemRenderer.renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, armorItem, x, y);
        }
    }

	public void toggleArmors() {
		this.enabled = !this.enabled;
	}

	public void setLocation(int locationIndex) {
		location = locationIndex;
	}

	public void setVisible(boolean status) {
		enabled = status;		
	}

}
