package de.justin.lightworlds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LightWorldsTabCompleter implements TabCompleter {

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            if (commandSender.hasPermission("lightworlds.teleport")) {
                list.add("tp");
            }
            if (commandSender.hasPermission("lightworlds.create")) {
                list.add("create");
            }
            if (commandSender.hasPermission("lightworlds.delete")) {
                list.add("delete");
            }
            if (commandSender.hasPermission("lightworlds.list")) {
                list.add("list");
            }
            if (commandSender.hasPermission("lightworlds.players")) {
                list.add("players");
            }
            if (commandSender.hasPermission("lightworlds.info")) {
                list.add("info");
            }
            return list;
        }

        if (strings.length == 2) {
            if (commandSender.hasPermission("lightworlds.teleport")) {
                if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("teleport")) {
                    Bukkit.getWorlds().forEach(world -> {
                        list.add(world.getName());
                    });
                }
            }

            if (commandSender.hasPermission("lightworlds.create")) {
                if (strings[0].equalsIgnoreCase("create")) {
                    Bukkit.getWorlds().forEach(world -> {
                        list.add("[Name]");
                    });
                }
            }

            if (commandSender.hasPermission("lightworlds.delete")) {
                if (strings[0].equalsIgnoreCase("delete")) {
                    Bukkit.getWorlds().forEach(world -> {
                        list.add(world.getName());
                    });
                }
            }

            if (commandSender.hasPermission("lightworlds.players")) {
                if (strings[0].equalsIgnoreCase("players")) {
                    Bukkit.getWorlds().forEach(world -> {
                        list.add(world.getName());
                    });
                }
            }
            return list;
        }


        if (strings.length == 3) {
            if (commandSender.hasPermission("lightworlds.teleport")) {
                if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("teleport")) {
                    list.add("-c");
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        list.add(player.getName());
                    });
                }
            }

            if (commandSender.hasPermission("lightworlds.create")) {
                if (strings[0].equalsIgnoreCase("create")) {
                    list.add("normal");
                    list.add("nether");
                    list.add("end");
                    list.add("flat");
                    list.add("amplified");
                    list.add("largebiomes");
                    list.add("void");
                }
            }
            return list;
        }

        if (strings.length == 4) {
            if (commandSender.hasPermission("lightworlds.teleport")) {
                if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("teleport")) {
                    if (strings[2].equals("-c")) {
                        return list;
                    }
                    list.add("-c");
                }
            }

            if (commandSender.hasPermission("lightworlds.create")) {
                if (strings[0].equalsIgnoreCase("create")) {
                    list.add("-g");
                    list.add("-s");
                }
            }

            return list;
        }

        if (strings.length == 5) {
            if (commandSender.hasPermission("lightworlds.teleport")) {
                if (strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("teleport")) {
                    if (strings[3].equals("-c")) {
                        return list;
                    }
                }
            }

            return list;
        }

        if (strings.length == 6) {
            if (commandSender.hasPermission("lightworlds.create")) {
                if (strings[0].equalsIgnoreCase("create")) {
                    if (strings[3].equals("-g")) {
                        list.add("-s");
                    }
                    if (strings[3].equals("-s")) {
                        list.add("-g");
                    }
                }
            }
        }
        return list;
    }
}
