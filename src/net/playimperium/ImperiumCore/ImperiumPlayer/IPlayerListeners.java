package net.playimperium.ImperiumCore.ImperiumPlayer;

import net.playimperium.ImperiumCore.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Anml on 1/9/16.
 */
public class IPlayerListeners implements Listener {

    Main instance;
    IPlayerManager iPlayerManager;

    public IPlayerListeners(Main instance) {
        this.instance = instance;
        iPlayerManager = instance.getIPlayerManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Player player = e.getPlayer();
        iPlayerManager.addIPlayer(player);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        Player player = e.getPlayer();
        iPlayerManager.removeIPlayer(player);
    }

}
