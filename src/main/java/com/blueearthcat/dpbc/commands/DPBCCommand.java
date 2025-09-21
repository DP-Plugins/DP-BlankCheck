package com.blueearthcat.dpbc.commands;

import com.blueearthcat.dpbc.functions.DPBCFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import static com.blueearthcat.dpbc.BlankCheck.plugin;

public class DPBCCommand {
    private final CommandBuilder builder;

    public DPBCCommand() {
        builder = new CommandBuilder(plugin);

        builder.addSubCommand("item", "dpbc.admin", plugin.getLang().get("help_item"), true, (p, args) -> {
            if (args.length == 1) DPBCFunction.openItemSettingGUI((Player) p);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_item"));
            return true;
        });
        builder.addSubCommand("config", "dpbc.admin", plugin.getLang().get("help_config"), true, (p, args) -> {
            if (args.length == 1) DPBCFunction.openConfigSettingGUI((Player) p);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_item"));
            return true;
        });
        builder.addSubCommand("check", plugin.getLang().get("help_check"), true, (p, args) -> {
            if (args.length == 2) DPBCFunction.giveCheck((Player) p, args[1], null);
            else if (args.length == 3) DPBCFunction.giveCheck((Player) p, args[1], args[2]);
            else p.sendMessage(plugin.getPrefix() + plugin.getLang().get("help_check"));
            return true;
        });
    }

    public CommandExecutor getExecuter() {
        return builder;
    }
}
