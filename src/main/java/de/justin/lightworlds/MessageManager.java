package de.justin.lightworlds;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MessageManager {
    public static void sendMessage(CommandSender commandSender, String message) {
        TextComponent textComponent = Component.text("§6[LightWorlds] §7" + message);
        if (commandSender instanceof  ConsoleCommandSender) {
            String content1 = textComponent.content().replace("§3", "").replace("§7", "").replace("§c", "").replace("§6", "");
            TextComponent textComponent1 = Component.text(content1);
            commandSender.sendMessage(textComponent1);
            return;
        }

        commandSender.sendMessage(textComponent);
        if (commandSender instanceof Player player) {
            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
        }
    }

    public static void sendMessageNewLine(CommandSender commandSender, String message) {
        TextComponent textComponent = Component.text("§7" + message);
        if (commandSender instanceof  ConsoleCommandSender) {
            String content1 = textComponent.content().replace("§3", "").replace("§7", "").replace("§c", "").replace("§6", "");
            TextComponent textComponent1 = Component.text(content1);
            commandSender.sendMessage(textComponent1);
            return;
        }
        commandSender.sendMessage(textComponent);
    }
}
