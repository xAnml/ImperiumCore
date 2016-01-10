package net.playimperium.ImperiumCore.Punishments;

import net.playimperium.ImperiumCore.ImperiumPlayer.IPlayerManager;
import net.playimperium.ImperiumCore.Main;
import net.playimperium.ImperiumCore.MySQL.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Anml on 1/7/16.
 */
public class PunishmentManager {

    private Main instance;
    private MySQL mySQL;
    private IPlayerManager iPlayerManager;
    private Map<UUID, List<Punishment>> cachedPunishments;

    public PunishmentManager(Main instance) {
        this.instance = instance;
        mySQL = instance.getMySQL();
        iPlayerManager = instance.getIPlayerManager();
        cachedPunishments = new HashMap<>();
    }

    public Map<UUID, List<Punishment>> getCachedPunishments() {
        return cachedPunishments;
    }

    public List<Punishment> getPunishments(UUID uuid) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);

        if (offlinePlayer == null)
            return null;

        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();

            if (player == null) {
                return null;
            }

            UUID id = player.getUniqueId();
            if (cachedPunishments.containsKey(id)) {
                return cachedPunishments.get(id);
            }
        }

        List<Punishment> list = new ArrayList<>();

        try {
            ResultSet resultSet = mySQL.getResultSet("SELECT * FROM Punishments WHERE Target='" + offlinePlayer.getUniqueId() + "'");

            while (resultSet.next()) {
                PunishmentType type = PunishmentType.valueOf(resultSet.getString("Type"));
                UUID target = UUID.fromString(resultSet.getString("Target"));
                UUID punisher = !resultSet.getString("Punisher").equals("Console") ? UUID.fromString(resultSet.getString("Punisher")) : null;
                long created = resultSet.getLong("Created");
                long expires = resultSet.getLong("Expires");
                String reason = resultSet.getString("Reason");

                Punishment entry = new Punishment(type, target, punisher, expires, reason);
                entry.setCreated(created);
                entry.setExecuted(true);
                list.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void addPunishment(Punishment punishment) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(punishment.getTarget());

        if (offlinePlayer == null)
            return;

        if (offlinePlayer.isOnline()) {
            Player player = offlinePlayer.getPlayer();

            if (player == null) {
                return;
            }

            UUID id = player.getUniqueId();
            if (cachedPunishments.containsKey(id)) {
                cachedPunishments.get(id).add(punishment);
                return;
            }
        }

        punishment.execute();
    }
}
