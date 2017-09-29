package com.shadowhawk.armorshud;

import com.mumfrey.liteloader.modconfig.AbstractConfigPanel;
import com.mumfrey.liteloader.modconfig.ConfigPanelHost;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;

public class ArmorsHUDConfigPanel extends AbstractConfigPanel
{
	/** Line spacing, in points. */
	private final static int SPACING = 24;
	
	@Override
    protected void addOptions(ConfigPanelHost host)
    {
        final LiteModArmorsHUDRevived mod = host.<LiteModArmorsHUDRevived>getMod();
        
        int id = 0, line = 0;
        
        this.addControl(new GuiButton(id++, 10, SPACING * line++, I18n.format("armorshud.config.toggle."+ mod.isEnabled())), new ConfigOptionListener<GuiButton>()
        {
        	@Override
        	public void actionPerformed(GuiButton control)
        	{
        		mod.toggleArmors();
        		control.displayString = (I18n.format("armorshud.config.toggle."+ mod.isEnabled()));
        	}
        });
        this.addControl(new GuiButton(id++, 10, SPACING * line++, I18n.format(mod.getLocationString())), new ConfigOptionListener<GuiButton>()
        {
        	@Override
        	public void actionPerformed(GuiButton control)
        	{
        		mod.cycleLocation();
        		control.displayString = (I18n.format(mod.getLocationString()));
        	}
        });
    }
    
    @Override
    public String getPanelTitle()
    {
        return I18n.format("armorshud.config.title");
    }

    @Override
    public void onPanelHidden() {}
}