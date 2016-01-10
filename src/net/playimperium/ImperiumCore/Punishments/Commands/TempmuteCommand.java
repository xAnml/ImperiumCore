package net.playimperium.ImperiumCore.Punishments.Commands;

import net.playimperium.ImperiumCore.ImperiumPlayer.IPlayer;
import net.playimperium.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.playimperium.ImperiumCore.Main;
import net.playimperium.ImperiumCore.Punishments.Punishment;
import net.playimperium.ImperiumCore.Punishments.PunishmentManager;
import net.playimperium.ImperiumCore.Punishments.PunishmentType;
import net.playimperium.ImperiumCore.Utils.Utils;
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
    private Utils utils;

    public TempmuteCommand(Main instance) {
        this.instance = instance;
        iPlayerManager = instance.getIPlayerManager();
        punishmentManager = instance.getPunishmentManager();
        utils = instance.getUtils();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {

        if (!sender.hasPermission("imperiumcore.tempmute")) {
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

        long length = utils.longLength(args[1]);

        if (length == 0) {
            sender.sendMessage("§cYou must enter a correct length.");
            return false;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
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
        iPlayerManager.staff("§9[STAFF] " + sName + " §7has globally temp-muted " + iPlayer.getTag() + " §7for §3" + utils.actualLength(args[1]) + " §7with reason: §a" + reason + "§7.");

        return true;
    }
}
