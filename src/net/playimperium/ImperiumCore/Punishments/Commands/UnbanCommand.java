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

/**
 * Created by Anml on 1/7/16.
 */
public class UnbanCommand implements CommandExecutor {

    private Main instance;
    private IPlayerManager iPlayerManager;
    private PunishmentManager punishmentManager;

        public UnbanCommand(Main instance) {
        this.instance = instance;
        iPlayerManager = instance.getIPlayerManager();
        punishmentManager = instance.getPunishmentManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {

        if (!sender.hasPermission("imperiumcore.unban")) {
            sender.sendMessage("§cYou do not have permission to execute this command.");
            return false;
        }

        String usage = "§4Usage: §c/unban <player>";

        if (args.length < 1) {
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
            if (punishment.getType().equals(PunishmentType.BAN) || punishment.getType().equals(PunishmentType.TEMPBAN)) {
                if (!punishment.hasExpired()) {
                    punishment.setExpires(0);
                    punishment.execute();

                    String sName = !(sender instanceof Player) ? "§6Console" : iPlayerManager.getIPlayer(Bukkit.getOfflinePlayer(((Player) sender).getUniqueId())).getTag();
                    iPlayerManager.staff("§9[STAFF] " + sName + " §7has globally unbanned " + iPlayer.getTag() + "§7.");
                    return true;
                }
            }
        }

        sender.sendMessage("§cThe target player is currently not banned.");
        return false;
    }
}
