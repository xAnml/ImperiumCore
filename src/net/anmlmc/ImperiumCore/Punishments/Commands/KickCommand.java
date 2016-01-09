package net.anmlmc.ImperiumCore.Punishments.Commands;

import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayer;
import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.anmlmc.ImperiumCore.Main;
import net.anmlmc.ImperiumCore.Punishments.Punishment;
import net.anmlmc.ImperiumCore.Punishments.PunishmentManager;
import net.anmlmc.ImperiumCore.Punishments.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by Anml on 1/7/16.
 */
public class KickCommand implements CommandExecutor {

    private Main instance;
    private IPlayerManager iPlayerManager;
    private PunishmentManager punishmentManager;

    public KickCommand(Main instance) {
        this.instance = instance;
        iPlayerManager = instance.getIPlayerManager();
        punishmentManager = instance.getPunishmentManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {

        if (!sender.hasPermission("imperiumcore.kick")) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return false;
        }

        String usage = "§4Usage: §c/kick <player> <reason>";

        if (args.length < 2) {
            sender.sendMessage(usage);
            return false;
        }

        Player player = Bukkit.getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage("§cNo player online with the given name found.");
            return false;
        }

        IPlayer iPlayer = iPlayerManager.getIPlayer(player);

        List<Punishment> punishments = punishmentManager.getPunishments(player.getUniqueId());

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i != args.length - 1)
                sb.append(args[i] + " ");
            else
                sb.append(args[i]);
        }

        String reason = sb.toString();
        UUID creator = (sender instanceof Player) ? ((Player) sender).getUniqueId() : null;

        Punishment kick = new Punishment(PunishmentType.KICK, player.getUniqueId(), creator, 0, reason);
        punishmentManager.addPunishment(kick);

        String sName = creator == null ? "§6Console" : iPlayerManager.getIPlayer(Bukkit.getOfflinePlayer(creator)).getTag();
        iPlayerManager.staff("§9[STAFF] " + sName + " §7has kicked " + iPlayer.getTag() + " §7with reason: §a" + reason + "§7.");

        player.kickPlayer(kick.getMessage());

        return true;
    }
}
