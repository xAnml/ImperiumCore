package net.anmlmc.ImperiumCore.ImperiumPlayer;

import net.anmlmc.ImperiumCore.Main;
import net.anmlmc.ImperiumCore.MySQL.MySQL;
import net.anmlmc.ImperiumCore.Ranks.Rank;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Anml on 1/4/16.
 */
public class IPlayer {

    private Main instance;
    private IPlayerManager iPlayerManager;
    private MySQL mySQL;
    private OfflinePlayer offlinePlayer;
    private Player player;

    public IPlayer(OfflinePlayer offlinePlayer) {
        instance = Main.getInstance();
        iPlayerManager = instance.getIPlayerManager();
        mySQL = instance.getMySQL();
        this.offlinePlayer = offlinePlayer;
        player = offlinePlayer.getPlayer();
    }

    public Rank getSQLRank() {
        try {
            ResultSet resultSet = mySQL.getResultSet("SELECT Rank FROM PlayerInfo WHERE UUID='" + offlinePlayer.getUniqueId() + "'");
            if (resultSet.next()) {
                return Rank.valueOf(resultSet.getString("Rank"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Rank.DEFAULT;
    }

    public void setSQLRank(Rank rank) {
        if (!hasRank()) {
            try {

                mySQL.executeUpdate("INSERT INTO PlayerInfo (`UUID`, `Rank`) VALUES ('" + offlinePlayer.getUniqueId() + "','" + rank.name() + "')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mySQL.executeUpdate("UPDATE PlayerInfo SET Rank='" + rank.name() + "' WHERE UUID='" + offlinePlayer.getUniqueId() + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Rank getRank() {
        if (offlinePlayer.isOnline()) {
            return iPlayerManager.getRanks().get(player);
        }

        return getSQLRank();
    }

    public void setRank(Rank rank) {
        if (offlinePlayer.isOnline()) {
            iPlayerManager.getRanks().replace(player, rank);
            return;
        }

        setSQLRank(rank);
    }

    public boolean hasRank() {

        try {
            ResultSet rs = mySQL.getResultSet("SELECT Rank FROM PlayerInfo WHERE UUID='" + offlinePlayer.getUniqueId() + "'");
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getTag() {
        return getRank().getTag().replace("%s", offlinePlayer.getName());
    }

    public String getTabTag() {
        return getRank().getTabTag().replace("%s", offlinePlayer.getName());
    }

    public int getSQLTokens() {
        try {
            ResultSet resultSet = mySQL.getResultSet("SELECT Tokens FROM PlayerInfo WHERE UUID='" + offlinePlayer.getUniqueId() + "'");
            if (resultSet.next()) {
                return resultSet.getInt("Tokens");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void setSQLTokens(int value) {
        try {
            mySQL.executeUpdate("UPDATE PlayerInfo SET Tokens='" + value + "' WHERE UUID='" + offlinePlayer.getUniqueId() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTokens() {
        if (offlinePlayer.isOnline()) {
            return iPlayerManager.getTokens().get(player);
        }

        return getSQLTokens();
    }

    public void setTokens(int value) {
        if (offlinePlayer.isOnline()) {
            iPlayerManager.getTokens().replace(player, value);
            return;
        }

        setSQLTokens(value);
    }

}
