package net.anmlmc.ImperiumCore;

import net.anmlmc.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.anmlmc.ImperiumCore.MySQL.MySQL;
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

    public static Main getInstance() {
        return instance;
    }
    public MySQL getMySQL() {
        return mySQL;
    }
    public IPlayerManager getIPlayerManager() {
        return iPlayerManager;
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
    }

    public void registerCommands() {
        getCommand("rank").setExecutor(new RankCommand(this));
        getCommand("tokens").setExecutor(new TokensCommand(this));
    }

    public void registerManagers() {
        mySQL = new MySQL(this);
        iPlayerManager = new IPlayerManager(this);
    }
}