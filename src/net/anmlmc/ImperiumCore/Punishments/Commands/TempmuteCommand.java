package net.anmlmc.ImperiumCore.Punishments.Commands;

import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayer;
import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.anmlmc.ImperiumCore.Main;
import net.anmlmc.ImperiumCore.Punishments.Punishment;
import net.anmlmc.ImperiumCore.Punishments.PunishmentManager;
import net.anmlmc.ImperiumCore.Punishments.PunishmentType;
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
public class TempmuteCommand implements CommandExecutor {

    private Main instance;
    private IPlayerManager iPlayerManager;
    private PunishmentManager punishmentManager;

    public TempmuteCommand(Main instance) {
        this.instance = instance;
        iPlayerManager = instance.getIPlayerManager();
        punishmentManager = instance.getPunishmentManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {

        if (!sender.hasPermission("imperiumcore.tempban")) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return false;
        }

        String usage = "§4Usage: §c/tempmute <player> <length> <reason>";

        if (args.length < 3) {
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
                    sender.sendMessage("§cThe target player is already banned.");
                    return false;
                }
            }
        }

        long length = punishmentManager.longValue(args[1]);

        if (length == 0) {
            sender.sendMessage("§cYou must enter a correct length.");
            return false;
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

        Punishment tempmute = new Punishment(PunishmentType.TEMPMUTE, offlinePlayer.getUniqueId(), creator, length, reason);
        punishmentManager.addPunishment(tempmute);

        String sName = creator == null ? "§6Console" : iPlayerManager.getIPlayer(Bukkit.getOfflinePlayer(creator)).getTag();
        iPlayerManager.staff("§9[STAFF] " + sName + " §7has temp-muted " + iPlayer.getTag() + " §7for §3" + args[2].toLowerCase() + " §7with reason: §a" + reason + "§7.");

        return true;
    }
}
