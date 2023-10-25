package me.coopersully.yaaa;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Announcer implements Runnable {

    private final JavaPlugin plugin;
    private final List<JSONObject> messages = new ArrayList<>();
    private int messageIndex = 0;
    private long lastAnnouncementTime;
    private final long interval;

    public Announcer(JavaPlugin plugin) {
        this.plugin = plugin;
        this.interval = plugin.getConfig().getLong("interval") * 20;  // Convert seconds to ticks
        loadMessages();
    }

    public long getInterval() {
        return this.interval;
    }

    private void loadMessages() {
        try {
            JSONParser parser = new JSONParser();
            JSONArray messageArray = (JSONArray) parser.parse(plugin.getConfig().getString("messages"));
            for (Object obj : messageArray) {
                messages.add((JSONObject) obj);
            }

            if (plugin.getConfig().getBoolean("random")) {
                Collections.shuffle(messages);
            }
        } catch (ParseException e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        File errorDirectory = new File(plugin.getDataFolder(), "errors");
        if (!errorDirectory.exists()) errorDirectory.mkdirs();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        File errorFile = new File(errorDirectory, "error_" + timestamp + ".txt");

        try (FileWriter writer = new FileWriter(errorFile)) {
            e.printStackTrace(new PrintWriter(writer));
        } catch (IOException ioException) {
            plugin.getLogger().severe("Failed to save error stack trace to file: " + ioException.getMessage());
        }

        plugin.getLogger().severe("An error occurred while loading messages. The error stack trace has been saved to " + errorFile.getPath());
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    @Override
    public void run() {
        if (messageIndex >= messages.size()) {
            messageIndex = 0;
            if (plugin.getConfig().getBoolean("random")) {
                // Reshuffle if random is enabled
                Collections.shuffle(messages);
            }
        }

        JSONObject message = messages.get(messageIndex);
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tellraw @a " + message.toJSONString());
        messageIndex++;

        // Update the time of the last announcement
        lastAnnouncementTime = System.currentTimeMillis();
    }

    public long getTimeUntilNext() {
        long currentTime = System.currentTimeMillis();
        long timeSinceLastAnnouncement = (currentTime - lastAnnouncementTime) / 1000;  // Convert milliseconds to seconds
        long timeUntilNext = interval - timeSinceLastAnnouncement;
        return Math.max(0, timeUntilNext);  // Ensure the result is non-negative
    }

    public List<JSONObject> getMessages() {
        return messages;
    }
}
