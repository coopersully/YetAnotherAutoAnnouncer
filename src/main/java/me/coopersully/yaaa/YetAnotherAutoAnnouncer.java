package me.coopersully.yaaa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class YetAnotherAutoAnnouncer extends JavaPlugin implements CommandExecutor {

    public static final Component prefix =
            Component.text("YAAA ", NamedTextColor.YELLOW)
                    .append(Component.text("\u00BB", Style.style(NamedTextColor.WHITE)))
                    .append(Component.text(" ", Style.style(NamedTextColor.GRAY)));

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Announcer announcer = new Announcer(this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, announcer, 0L, getConfig().getLong("interval") * 20);

        // Register commands
        getCommand("yaaa").setExecutor(new YaaaCommandExecutor(announcer, this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void sendInfo(CommandSender sender, String message) {
        sender.sendMessage(YetAnotherAutoAnnouncer.prefix.append(Component.text(message, NamedTextColor.GRAY)));
    }

    public static void sendError(CommandSender sender, String message) {
        sender.sendMessage(YetAnotherAutoAnnouncer.prefix.append(Component.text(message, NamedTextColor.RED)));
    }
}
