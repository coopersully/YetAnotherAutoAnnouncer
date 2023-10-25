package me.coopersully.yaaa;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.time.Duration;
import java.util.List;

public class YaaaCommandExecutor implements CommandExecutor {

    private final Announcer announcer;
    private final JavaPlugin plugin;

    public YaaaCommandExecutor(Announcer announcer, JavaPlugin plugin) {
        this.announcer = announcer;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            YetAnotherAutoAnnouncer.sendError(sender, "Nope. Usage: /yaaa <reload|next|messages> [args]");
            return true;
        }

        if (!sender.hasPermission("yaaa.admin")) {
            YetAnotherAutoAnnouncer.sendError(sender, "You don't have permission to do that, little man.");
            return true;
        }

        return switch (args[0].toLowerCase()) {
            case "reloadconfig", "reload", "rel", "r" -> handleReload(sender);
            case "next" -> handleNext(sender);
            case "messages", "list", "message", "msgs", "msg" -> handleMessages(sender, args);
            default -> {
                YetAnotherAutoAnnouncer.sendError(sender, "Unknown subcommand: " + args[0]);
                yield true;
            }
        };
    }

    private boolean handleReload(CommandSender sender) {
        plugin.reloadConfig();
        announcer.reload();
        YetAnotherAutoAnnouncer.sendInfo(sender, "Plugin configuration reloaded.");
        return true;
    }

    private boolean handleNext(CommandSender sender) {
        long timeUntilNext = announcer.getTimeUntilNext();
        Duration duration = Duration.ofSeconds(timeUntilNext);

        long years = timeUntilNext / (365 * 24 * 3600);
        timeUntilNext %= (365 * 24 * 3600);
        long months = timeUntilNext / (30 * 24 * 3600);
        timeUntilNext %= (30 * 24 * 3600);
        long days = duration.toDaysPart();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();

        StringBuilder timeBuilder = new StringBuilder("Next announcement in ");
        if (years > 0) timeBuilder.append(years).append(years == 1 ? " year, " : " years, ");
        if (months > 0) timeBuilder.append(months).append(months == 1 ? " month, " : " months, ");
        if (days > 0) timeBuilder.append(days).append(days == 1 ? " day, " : " days, ");
        if (hours > 0) timeBuilder.append(hours).append(hours == 1 ? " hour, " : " hours, ");
        if (minutes > 0) timeBuilder.append(minutes).append(minutes == 1 ? " minute, " : " minutes, ");
        timeBuilder.append(seconds).append(seconds == 1 ? " second" : " seconds");

        YetAnotherAutoAnnouncer.sendInfo(sender, timeBuilder.toString());
        return true;
    }

    private boolean handleMessages(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            YetAnotherAutoAnnouncer.sendError(sender, "This command can only be used by a player.");
            return true;
        }

        List<JSONObject> messages = announcer.getMessages();

        if (args.length != 2) {
            YetAnotherAutoAnnouncer.sendInfo(player, "There are " + messages.size() + " messages. Specify an index you'd like to display.");
            return true;
        }

        try {
            int index = Integer.parseInt(args[1]);
            if (index >= 0 && index < messages.size()) {
                JSONObject message = messages.get(index);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + message.toJSONString());
            } else {
                YetAnotherAutoAnnouncer.sendInfo(player, "Invalid index. Please specify an index between 0 and " + (messages.size() - 1) + ".");
            }
        } catch (NumberFormatException e) {
            YetAnotherAutoAnnouncer.sendInfo(player, "Invalid index. Please specify a valid number.");
        }
        return true;
    }
}
