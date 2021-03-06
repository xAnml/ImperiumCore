package net.playimperium.ImperiumCore.Punishments;

import net.playimperium.ImperiumCore.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by Anml on 1/8/16.
 */
public class PunishmentListeners implements Listener {

    Main instance;
    PunishmentManager punishmentManager;

    public PunishmentListeners(Main instance) {
        this.instance = instance;
        punishmentManager = instance.getPunishmentManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuit(AsyncPlayerPreLoginEvent e) {
        List<Punishment> punishments = punishmentManager.getPunishments(e.getUniqueId());

        for (Punishment punishment : punishments) {
            if (punishment.getType().equals(PunishmentType.BAN) || punishment.getType().equals(PunishmentType.TEMPBAN)) {
                if (!punishment.hasExpired()) {
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, punishment.getMessage());
                    return;
                }
            }
        }

        punishmentManager.getCachedPunishments().put(e.getUniqueId(), punishments);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerQuit(PlayerQuitEvent e) {
        UUID id = e.getPlayer().getUniqueId();

        if(punishmentManager.getCachedPunishments().containsKey(id)) {
            for (Punishment p : punishmentManager.getCachedPunishments().get(id)) {
                p.execute();
            }
            punishmentManager.getCachedPunishments().remove(id);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerChat(AsyncPlayerChatEvent e) {
        List<Punishment> punishments = punishmentManager.getPunishments(e.getPlayer().getUniqueId());

        for (Punishment punishment : punishments) {
            if (punishment.getType().equals(PunishmentType.MUTE)) {
                if (!punishment.hasExpired()) {
                    e.getPlayer().sendMessage("§cYou are permanently muted from speaking on the network.");
                    return;
                }
            }

            if (punishment.getType().equals(PunishmentType.TEMPMUTE)) {
                if (!punishment.hasExpired()) {
                    e.getPlayer().sendMessage("§cYou are temporarily muted until §3" + punishment.getEndTimestamp() + " §cfrom speaking on the network.");
                    return;
                }
            }
        }
    }
}