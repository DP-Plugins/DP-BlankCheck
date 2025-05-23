package com.blueearthcat.dpbc;

import com.blueearthcat.dpbc.commands.DPBCCommand;
import com.blueearthcat.dpbc.events.DPBCEvent;
import com.blueearthcat.dpbc.functions.DPBCFunction;
import com.darksoldier1404.dppc.utils.DataContainer;
import com.darksoldier1404.dppc.utils.PluginUtil;
import org.bukkit.plugin.java.JavaPlugin;

public class BlankCheck extends JavaPlugin {
    private static BlankCheck plugin;
    public static DataContainer data;

    public static BlankCheck getInstance() { return plugin; }

    @Override
    public void onLoad(){
        plugin = this;
        PluginUtil.addPlugin(plugin, 25953);
    }

    @Override
    public void onEnable(){
        data = new DataContainer(plugin, true);
        plugin.getServer().getPluginManager().registerEvents(new DPBCEvent(), plugin);
        getCommand("dpbc").setExecutor(new DPBCCommand().getExecuter());
        DPBCFunction.init();
    }

    @Override
    public void onDisable(){
        data.save();
    }
}
