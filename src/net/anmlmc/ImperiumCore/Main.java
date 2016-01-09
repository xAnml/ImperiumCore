package net.anmlmc.ImperiumCore;

import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.anmlmc.ImperiumCore.MySQL.MySQL;
import net.anmlmc.ImperiumCore.Punishments.Commands.*;
import net.anmlmc.ImperiumCore.Punishments.PunishmentListeners;
import net.anmlmc.ImperiumCore.Punishments.PunishmentManager;
import net.anmlmc.ImperiumCore.Ranks.Commands.RankCommand;
import net.anmlmc.ImperiumCore.Ranks.Commands.TokensCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Anml on 1/04/15.
 */

public class Main extends JavaPlugin implements Listener {

    private static Main instance;
    private MySQL mySQL;
    private IPlayerManager iPlayerManager;
    private PunishmentManager punishmentManager;

    public static Main getInstance() {
        return instance;
    }
    public MySQL getMySQL() {
        return mySQL;
    }
    public IPlayerManager getIPlayerManager() {
        return iPlayerManager;
    }
    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    @Override
    public void onEnable() {

        instance = this;
        saveDefaultConfig();

        registerManagers();
        registerEvents();
        registerCommands();

        iPlayerManager.loadIPlayers();

        this.getLogger().info("[ImperiumCore] Plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        instance = null;

        this.getLogger().info("[ImperiumCore] Plugin has been disabled.");
    }

    public void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();

        pm.registerEvents(this, this);
        pm.registerEvents(new PunishmentListeners(this), this);
        pm.registerEvents(iPlayerManager, this);
    }

    public void registerCommands() {
        getCommand("rank").setExecutor(new RankCommand(this));
        getCommand("tokens").setExecutor(new TokensCommand(this));
        getCommand("ban").setExecutor(new BanCommand(this));
        getCommand("tempban").setExecutor(new TempbanCommand(this));
        getCommand("mute").setExecutor(new MuteCommand(this));
        getCommand("tempmute").setExecutor(new TempmuteCommand(this));
        getCommand("kick").setExecutor(new KickCommand(this));
        getCommand("warn").setExecutor(new WarnCommand(this));
    }

    public void registerManagers() {
        mySQL = new MySQL(this);
        iPlayerManager = new IPlayerManager(this);
        punishmentManager = new PunishmentManager(this);
    }
}