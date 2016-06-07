package com.shadowhawk;

import java.io.File;

import com.mumfrey.liteloader.HUDRenderListener;
import com.mumfrey.liteloader.Tickable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldSettings;

public class LiteModArmorsHUDRevived implements HUDRenderListener, Tickable
{
    //private static LiteModArmorsHUD instance;
    public static float partialTicks;

    public LiteModArmorsHUDRevived()
    {
        //instance = this;
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
        return "1.0.2";
    }

    @Override
	public void init(File configPath) {}

    @Override
	public void onPostRenderHUD(int screenWidth, int screenHeight)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer != null && mc.thePlayer.inventory != null)
        {
            if (mc.theWorld.getWorldInfo().getGameType() != WorldSettings.GameType.SPECTATOR)
            {
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                RenderHelper.enableGUIStandardItemLighting();

                for (int i = 0; i < 4; ++i)
                {
                    int y = screenHeight - 40 - i * 20;
                    this.renderArmor(i, screenWidth - 20, y, partialTicks, mc.thePlayer);
                }

                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableBlend();
            }
        }
    }

    @Override
	public void onPreRenderHUD(int screenWidth, int screenHeight) {}

    @Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock)
    {
        //partialTicks = partialTicks;
    }

    private void renderArmor(int var1, int var2, int var3, float var4, EntityPlayer var5)
    {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        ItemStack var6 = var5.inventory.armorInventory[var1];

        if (var6 != null)
        {
            float var7 = var6.animationsToGo - var4;

            if (var7 > 0.0F)
            {
                GlStateManager.pushMatrix();
                float var8 = 1.0F + var7 / 5.0F;
                GlStateManager.translate(var2 + 8, var3 + 12, 0.0F);
                GlStateManager.scale(1.0F / var8, (var8 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((-(var2 + 8)), (-(var3 + 12)), 0.0F);
            }

            itemRenderer.renderItemIntoGUI(var6, var2, var3);

            if (var7 > 0.0F)
            {
                GlStateManager.popMatrix();
            }

            itemRenderer.renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, var6, var2, var3);
        }
    }

    @Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {}
}
