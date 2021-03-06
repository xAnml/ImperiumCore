package net.playimperium.ImperiumCore;

import net.playimperium.ImperiumCore.ImperiumPlayer.IPlayerListeners;
import net.playimperium.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.playimperium.ImperiumCore.MySQL.MySQL;
import net.playimperium.ImperiumCore.Punishments.Commands.*;
import net.playimperium.ImperiumCore.Punishments.PunishmentListeners;
import net.playimperium.ImperiumCore.Punishments.PunishmentManager;
import net.playimperium.ImperiumCore.Ranks.Commands.RankCommand;
import net.playimperium.ImperiumCore.ImperiumPlayer.Commands.TokensCommand;
import net.playimperium.ImperiumCore.Utils.Utils;
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
    private Utils utils;

    public static Main getInstance() { return instance; }
    public MySQL getMySQL() {
        return mySQL;
    }
    public IPlayerManager getIPlayerManager() {
        return iPlayerManager;
    }
    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }
    public Utils getUtils() {
        return utils;
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
        pm.registerEvents(new IPlayerListeners(this), this);
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
        getCommand("unban").setExecutor(new UnbanCommand(this));
        getCommand("unmute").setExecutor(new UnmuteCommand(this));
    }

    public void registerManagers() {
        mySQL = new MySQL(this);
        iPlayerManager = new IPlayerManager(this);
        punishmentManager = new PunishmentManager(this);
        utils = new Utils();
    }
}