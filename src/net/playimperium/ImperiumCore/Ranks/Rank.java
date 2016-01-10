package net.playimperium.ImperiumCore.Ranks;

/**
 * Created by Anml on 1/4/16.
 */

public enum Rank {

    OWNER("§7[§4Owner§7] %s", "§4Owner", "§4%s"),
    ADMIN("§7[§cAdmin§7] %s", "§cAdmin", "§c%s"),
    DEV("§7[§cDev§7] %s", "§cDev", "Developer", "§c%s"),
    MOD("§7[§3Mod§7] %s", "§3Mod", "Moderator", "§3%s"),
    JRMOD("§7[§dJrMod§7] %s", "§dJrMod", "JrModerator", "§d%s"),
    YOUTUBER("§7[§6YouTuber§7] %s", "§6YouTuber", "§6%s"),
    MVPPLUS("§7[§bMVP§c+§7] %s", "§bMVP§c+", "MVP+", "§b%s"),
    MVP("§7[§bMVP§7] %s", "§bMVP", "§b%s"),
    VIPPLUS("§7[§aVIP§c+§7] %s", "§aVIP§c+", "VIP+", "§a%s"),
    VIP("§7[§aVIP§7] %s", "§aVIP", "§a%s"),
    DEFAULT("§7%s", "§7Default", "§7%s");

    String tag;
    String name;
    String alias;
    String tabTag;

    Rank(String tag, String name, String alias, String tabTag) {
        this.tag = tag;
        this.name = name;
        this.alias = alias;
        this.tabTag = tabTag;
    }

    Rank(String tag, String name, String tabTag) {
        this.tag = tag;
        this.name = name;
        this.alias = name();
        this.tabTag = tabTag;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getTag() {
        return tag;
    }

    public String getTabTag() {
        return tabTag;
    }
}