package com.blueearthcat.dpbc.commands;

import com.blueearthcat.dpbc.BlankCheck;
import com.blueearthcat.dpbc.functions.DPBCFunction;
import com.darksoldier1404.dppc.builder.command.CommandBuilder;
import com.darksoldier1404.dppc.lang.DLang;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class DPBCCommand {
    private final CommandBuilder builder;
    private final BlankCheck plugin = BlankCheck.getInstance();
    private final String prefix;
    private final DLang lang;
    public DPBCCommand() {
        prefix = plugin.data.getPrefix();
        lang = plugin.data.getLang();
        builder = new CommandBuilder(plugin.data.getPrefix());

        builder.addSubCommand("item", "dpbc.admin", lang.get("help_item"), true, (p, args) -> {
            if (args.length == 1) DPBCFunction.openItemSettingGUI((Player)p);
            else p.sendMessage(prefix + lang.get("help_item"));
        });
        builder.addSubCommand("config", "dpbc.admin", lang.get("help_config"), true, (p, args) -> {
            if (args.length == 1) DPBCFunction.openConfigSettingGUI((Player)p);
            else p.sendMessage(prefix + lang.get("help_item"));
        });
        builder.addSubCommand("check", lang.get("help_check"), true, (p, args) -> {
            if (args.length == 2) DPBCFunction.giveCheck((Player)p,args[1], null);
            else if (args.length == 3) DPBCFunction.giveCheck((Player)p, args[1], args[2]);
            else p.sendMessage(prefix + lang.get("help_check"));
        });
    }

    public CommandExecutor getExecuter() {
        return builder;
    }
}
