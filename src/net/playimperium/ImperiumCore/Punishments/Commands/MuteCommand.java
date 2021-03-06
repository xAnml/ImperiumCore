package net.playimperium.ImperiumCore.Punishments.Commands;

import net.playimperium.ImperiumCore.ImperiumPlayer.IPlayer;
import net.playimperium.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.playimperium.ImperiumCore.Main;
import net.playimperium.ImperiumCore.Punishments.Punishment;
import net.playimperium.ImperiumCore.Punishments.PunishmentManager;
import net.playimperium.ImperiumCore.Punishments.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Created by Anml on 1/7/16.
 */
public class MuteCommand implements CommandExecutor {

    private Main instance;
    private IPlayerManager iPlayerManager;
    private PunishmentManager punishmentManager;

    public MuteCommand(Main instance) {
        this.instance = instance;
        iPlayerManager = instance.getIPlayerManager();
        punishmentManager = instance.getPunishmentManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {

        if (!sender.hasPermission("imperiumcore.mute")) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return false;
        }

        String usage = "§4Usage: §c/mute <player> <reason>";

        if (args.length < 2) {
            sender.sendMessage(usage);
            return false;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);

        if (offlinePlayer == null) {
            sender.sendMessage("§cNo player with the given name found.");
            return false;
        }

        IPlayer iPlayer = iPlayerManager.getIPlayer(offlinePlayer);

        List<Punishment> punishments = punishmentManager.getPunishments(offlinePlayer.getUniqueId());

        for (Punishment punishment : punishments) {
            if (punishment.getType().equals(PunishmentType.MUTE) || punishment.getType().equals(PunishmentType.TEMPMUTE)) {
                if (!punishment.hasExpired()) {
                    sender.sendMessage("§cThe target player is already muted.");
                    return false;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            if (i != args.length - 1)
                sb.append(args[i] + " ");
            else
                sb.append(args[i]);
        }

        String reason = sb.toString();
        UUID creator = (sender instanceof Player) ? ((Player) sender).getUniqueId() : null;

        Punishment mute = new Punishment(PunishmentType.MUTE, offlinePlayer.getUniqueId(), creator, -1, reason);
        punishmentManager.addPunishment(mute);

        String sName = creator == null ? "§6Console" : iPlayerManager.getIPlayer(Bukkit.getOfflinePlayer(creator)).getTag();
        iPlayerManager.staff("§9[STAFF] " + sName + " §7has globally muted " + iPlayer.getTag() + " §7with reason: §a" + reason + "§7.");

        return true;
    }
}
