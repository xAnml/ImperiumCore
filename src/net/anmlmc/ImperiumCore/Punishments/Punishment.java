package net.anmlmc.ImperiumCore.Punishments;

import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayer;
import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.anmlmc.ImperiumCore.Main;
import net.anmlmc.ImperiumCore.MySQL.MySQL;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Anml on 1/8/16.
 */
public class Punishment {

    Main instance;
    PunishmentManager punishmentManager;
    IPlayerManager iPlayerManager;
    MySQL mySQL;

    PunishmentType type;
    UUID target;
    UUID punisher;
    long created;
    long expires;
    String reason;
    boolean executed = false;

    public Punishment(PunishmentType type, UUID target, UUID punisher, long expires, String reason) {
        this.type = type;
        this.target = target;
        this.punisher = punisher;
        created = System.currentTimeMillis();
        this.expires = type.equals(PunishmentType.BAN) || type.equals(PunishmentType.MUTE) ? -1 :
                type.equals(PunishmentType.WARNING) || type.equals(PunishmentType.KICK) ? 0 :
                        expires;
        this.reason = reason;

        instance = Main.getInstance();
        punishmentManager = instance.getPunishmentManager();
        iPlayerManager = instance.getIPlayerManager();
        mySQL = instance.getMySQL();
    }

    public PunishmentType getType() { return type; }
    public UUID getTarget() { return target; }
    public UUID getPunisher() { return punisher; }
    public long getCreated() { return created; }
    public void setCreated(long value) { created = value; }
    public long getExpires() { return expires; }
    public boolean hasExpired() { return expires == 0 || (expires != -1 && expires <= System.currentTimeMillis()); }
    public String getEndTimestamp() {
        final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return DATE_FORMAT.format(new Date(created + expires));
    }
    public String getReason() { return reason; }
    public String getMessage() {
        String message = "";
        IPlayer pun = iPlayerManager.getIPlayer(Bukkit.getOfflinePlayer(punisher));
        switch (type.name()) {
            case "BAN":
                message =  "§7You are permanently banned from §cImperium Network§7:\n\n" +
                        "§7Reason: §f" + reason + " §8- " + pun.getTag();
            case "TEMPBAN":
                message = "§7You are temporarily banned from §cImperium Network§7:\n\n" +
                        "§7Reason: §f" + reason + " §8- " + pun.getTag() + "\n" +
                        "§7Unban Time: §f" + getEndTimestamp() + "\n\n";
            case "MUTE":
                message = "§7You have been muted by " + pun.getTag() + " §7for: §a" + reason + "§7.";
            case "TEMPMUTE":
                message = "§7You have been temporarily muted until §c" + getEndTimestamp() + " §7by " + pun.getTag() + " §7for: §a" + reason + "§7.";
            case "WARNING":
                message = "§7You have been warned by " + pun.getTag() + " §7for: §a" + reason + "§7.";
            case "KICK":
                message = "§7You have been kicked from §cImperium Network§7:\n\n" +
                        "§7Reason: §f" + reason + " §8- " + pun.getTag();
            default:
                message =  "§cError in Ban Type! Contact an administrator of Imperium Network.";
        }

        return message;
    }
    public boolean isExecuted() { return executed; }
    public void setExecuted(boolean value) { executed = value; }
    public void execute() {

        if(type.equals(PunishmentType.KICK) || type.equals(PunishmentType.WARNING))
            return;

        if (executed) {
            try {
                mySQL.executeUpdate("UPDATE Punishments SET Expires='" + expires + "' WHERE Target='" + target + "' AND Created='" + created + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            String punisherUUID = punisher == null ? "Console" : punisher.toString();
            mySQL.executeUpdate("INSERT INTO `Punishments` (`Type`, `Target`, `Punisher`, `Created`, `Expires`, `Reason`) " +
                    "VALUES ('" + type.name() + "','" + target + "', '" + punisherUUID + "', '" + created + "', '" + expires + "', '" + reason + "')");
            executed = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
