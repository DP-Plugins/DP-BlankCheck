package com.blueearthcat.dpbc;

import com.blueearthcat.dpbc.commands.DPBCCommand;
import com.blueearthcat.dpbc.events.DPBCEvent;
import com.blueearthcat.dpbc.functions.DPBCFunction;
import com.darksoldier1404.dppc.data.DPlugin;
import com.darksoldier1404.dppc.utils.PluginUtil;

public class BlankCheck extends DPlugin {
    public static BlankCheck plugin;

    public BlankCheck() {
        super(true);
        plugin = this;
        init();
    }

    public static BlankCheck getInstance() { return plugin; }

    @Override
    public void onLoad(){
        PluginUtil.addPlugin(plugin, 25953);
    }

    @Override
    public void onEnable(){
        plugin.getServer().getPluginManager().registerEvents(new DPBCEvent(), plugin);
        getCommand("dpbc").setExecutor(new DPBCCommand().getExecuter());
        DPBCFunction.init();
    }

    @Override
    public void onDisable(){
        saveDataContainer();
    }
}
