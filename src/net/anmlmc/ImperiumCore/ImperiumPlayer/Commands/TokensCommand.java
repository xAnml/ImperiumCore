package net.anmlmc.ImperiumCore.ImperiumPlayer.Commands;

import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayer;
import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.anmlmc.ImperiumCore.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Anml on 12/28/15.
 */
public class TokensCommand implements CommandExecutor {

    Main instance;
    IPlayerManager iPlayerManager;

    public TokensCommand(Main instance) {
        this.instance = instance;
        iPlayerManager = instance.getIPlayerManager();
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String command, final String[] args) {

        if (!sender.hasPermission("imperiumcore.tokens")) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return false;
        }

        String usage = "§4Usage: §c/tokens set <player> <rank>\n         /tokens get <player>";

        if (args.length == 0 || args.length < getMinArgs(args[0])) {
            sender.sendMessage(usage);
            return false;

        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);

        if (offlinePlayer == null) {
            sender.sendMessage("§cNo player with the given name found.");
            return false;
        }

        IPlayer iPlayer = iPlayerManager.getIPlayer(offlinePlayer);

        if (args[0].equalsIgnoreCase("set")) {

            int amount;
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("§cYou must enter a valid number value.");
                return false;
            }

            String commander = sender instanceof Player ? iPlayerManager.getIPlayer((Player) sender).getTag() : "§6CONSOLE";
            iPlayerManager.staff("§9[STAFF] " + commander + " §7has set " + iPlayer.getTag() + "§7's tokens balance to §e" + amount + "§7.");
            return true;

        } else if (args[0].equalsIgnoreCase("get")) {
            sender.sendMessage("§a" + iPlayer.getTag() + "'s current tokens balance is §e" + iPlayer.getTokens() + "§a.");
            return true;
        } else {
            sender.sendMessage(usage);
            return false;
        }
    }

    public int getMinArgs(String subcommand) {
        switch (subcommand.toLowerCase()) {
            case "set":
                return 3;
            case "get":
                return 2;
            default:
                return 100;
        }
    }
}