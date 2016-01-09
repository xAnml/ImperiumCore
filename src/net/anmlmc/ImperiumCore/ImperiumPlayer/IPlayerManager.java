package net.anmlmc.ImperiumCore.ImperiumPlayer;

import net.anmlmc.ImperiumCore.Main;
import net.anmlmc.ImperiumCore.Ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anml on 1/4/16.
 */
public class IPlayerManager {

    Main instance;
    private Map<Player, IPlayer> players;
    private Map<Player, Rank> ranks;
    private Map<Player, Integer> tokens;

    public IPlayerManager(Main instance) {
        this.instance = instance;
        players = new HashMap<>();
        ranks = new HashMap<>();
        tokens = new HashMap<>();
    }

    public Map<Player, IPlayer> getPlayers() {
        return players;
    }
    public Map<Player, Rank> getRanks() {
        return ranks;
    }
    public Map<Player, Integer> getTokens() {
        return tokens;
    }

    public void addIPlayer(Player player) {
        IPlayer iPlayer = new IPlayer(player);
        players.put(player, iPlayer);

        if (!iPlayer.hasRank()) {
            ranks.put(player, Rank.DEFAULT);
        } else {
            ranks.put(player, iPlayer.getSQLRank());
        }
    }

    public void removeIPlayer(Player player) {
        IPlayer iPlayer = getIPlayer(player);

        iPlayer.setSQLRank(iPlayer.getRank());
        players.remove(player);
    }

    public void loadIPlayers() {
        players.clear();
        ranks.clear();
        tokens.clear();

        for (Player player : Bukkit.getOnlinePlayers()) {
            addIPlayer(player);
        }
    }

    public IPlayer getIPlayer(OfflinePlayer player) {
        if(player.isOnline()) {
            if(players.containsKey(player.getPlayer())) {
                return players.get(player.getPlayer());
            }

            addIPlayer(player.getPlayer());
              return getIPlayer(player);
        }

        return new IPlayer(player);
    }

    public void staff(String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("imperiumcore.staff")) {
                p.sendMessage(message);
            }
        }
    }






}
