package dev.zoid.ping;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Ping extends JavaPlugin implements CommandExecutor {

    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        Objects.requireNonNull(getCommand("ping")).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("zoid.ping")) {
            player.sendMessage(Component.text("You don't have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            sendPingMessage(player, player);
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                if (!player.hasPermission("zoid.ping.reload")) {
                    player.sendMessage(Component.text("You don't have permission to reload the config."));
                    return true;
                }
                reloadConfig();
                config = getConfig();
                String reloadMessage = config.getString("reload-message", "<green>Configuration reloaded.");
                sendMessage(player, reloadMessage, true);
                playSound(player, "config-reload");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                String notFoundMessage = config.getString("player-not-found-message", "<red>The player does not exist on the server");
                sendMessage(player, notFoundMessage, false);
                playSound(player, "player-not-found");
                return true;
            }
            sendPingMessage(player, target);
        } else {
            sender.sendMessage("Usage: /ping [player]");
        }

        return true;
    }

    private void sendPingMessage(Player sender, Player target) {
        int ping = target.getPing();
        String message;

        if (sender == target) {
            message = config.getString("message-1-argument", "&aYour ping is #0ecc1e%ping%ms");
        } else {
            message = config.getString("message-2-argument", "&a%player%'s ping is #0ecc1e%ping%ms");
            message = message.replace("%player%", target.getName());
        }

        message = message.replace("%ping%", String.valueOf(ping));

        sendMessage(sender, message, true);
        playSound(sender, "ping-check");
    }

    private void sendMessage(Player player, String message, boolean isInfoMessage) {
        Component component = MiniMessage.miniMessage().deserialize(message);

        if (config.getBoolean("actionbar", true)) {
            player.sendActionBar(component);
        }

        if (config.getBoolean("chat", true) || !isInfoMessage) {
            player.sendMessage(component);
        }
    }

    private void playSound(Player player, String soundKey) {
        if (config.getBoolean("sound", true)) {
            String soundName = config.getString("sounds." + soundKey);
            if (soundName != null) {
                try {
                    // Try to play as Bukkit Sound enum first
                    Sound sound = Sound.valueOf(soundName.toUpperCase());
                    player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
                } catch (IllegalArgumentException e) {
                    // If it's not a Bukkit Sound enum, try to play as Minecraft sound ID
                    player.playSound(player.getLocation(), soundName.toLowerCase(), 1.0f, 1.0f);
                }
            } else {
                getLogger().warning("Sound not defined for key: " + soundKey);
            }
        }
    }
}