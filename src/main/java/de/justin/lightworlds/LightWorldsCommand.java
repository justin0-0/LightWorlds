package de.justin.lightworlds;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class LightWorldsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        //Help(No Arguments) Command
        if (strings.length == 0) {
            if (!commandSender.hasPermission("lightworlds.help")) {
                MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                return false;
            }
            MessageManager.sendMessage(commandSender, "Help for LightWorlds:");
            MessageManager.sendMessageNewLine(commandSender, "Use §3/lightworlds§7 or §3/lw§7 as an Alias for all Commands");
            MessageManager.sendMessageNewLine(commandSender, "Use §3/lw teleport§7 or §3/lw tp§7 as an Alias to teleport you or other Players to another World");
            MessageManager.sendMessageNewLine(commandSender, "Use §3/lw create§7 to create a new world");
            MessageManager.sendMessageNewLine(commandSender, "Use §3/lw delete§7 to delete a world");
            MessageManager.sendMessageNewLine(commandSender, "Use §3/lw list§7 to list all of your worlds");
            MessageManager.sendMessageNewLine(commandSender, "Use §3/lw players§7 to list all players in your worlds");
            MessageManager.sendMessageNewLine(commandSender, "Use §3/lw info§7 to get the name of the world you are in");

            return false;
        }

        //Teleport Command
        if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")){
            //Teleport Command Help
            if (strings.length == 1) {
                if (!commandSender.hasPermission("lightworlds.teleport")) {
                    MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                    return false;
                }
                MessageManager.sendMessage(commandSender, "Use §3/lw tp [WorldName] [(Optional) PlayerName] [(Optional) -c X Y Z Yaw Pitch]§7");
                return false;
            }

            //Teleport Command Self without Cords
            if (strings.length == 2) {
                if (!commandSender.hasPermission("lightworlds.teleport")) {
                    MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    MessageManager.sendMessage(commandSender, "§cYou need to be a player to teleport yourself. §3Use §3/lw tp [WorldName] [PlayerName] [(Optional) -c X Y Z (Optional) Yaw Pitch]§7 to teleport a player on your server");
                    return false;
                }
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe world §3" + strings[1] +"§c doesn't exist");
                    return false;
                }
                Player player = (Player) commandSender;
                World world = Bukkit.getWorld(strings[1]);
                player.teleport(new Location(world, world.getSpawnLocation().getBlockX(), world.getSpawnLocation().getBlockY(), world.getSpawnLocation().getBlockZ(), world.getSpawnLocation().getYaw(), world.getSpawnLocation().getPitch()));
                MessageManager.sendMessage(commandSender, "You got teleported to your provided world");
                return false;
            }

            //Teleport Command Others without Cords
            if (strings.length == 3) {
                if (!commandSender.hasPermission("lightworlds.teleport")) {
                    MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                    return false;
                }
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe world §3" + strings[1] +"§c doesn't exist");
                    return false;
                }
                if (Bukkit.getPlayer(strings[2]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe player §3" + strings[2] +"§c isn't online");
                    return false;
                }

                Player player = Bukkit.getPlayer(strings[2]);
                World world = Bukkit.getWorld(strings[1]);
                player.teleport(new Location(world, world.getSpawnLocation().getBlockX(), world.getSpawnLocation().getBlockY(), world.getSpawnLocation().getBlockZ(), world.getSpawnLocation().getYaw(), world.getSpawnLocation().getPitch()));
                MessageManager.sendMessage(commandSender, "You teleported §3" + player.getName() + "§7 to your provided world");
                return false;
            }

            //Teleport Command Self with Cords without Yaw and Pitch
            if (strings.length == 6) {
                if (!commandSender.hasPermission("lightworlds.teleport")) {
                    MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    MessageManager.sendMessage(commandSender, "§cYou need to be a player to teleport yourself. §3Use §3/lw tp [WorldName] [PlayerName] [(Optional) -c X Y Z (Optional) Yaw Pitch]§7 to teleport a player on your server");
                    return false;
                }
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe world §3" + strings[1] +"§7 doesn't exist");
                    return false;
                }
                if (!strings[2].equals("-c")) {
                    MessageManager.sendMessage(commandSender, "Use §3/lw tp [WorldName] [(Optional) PlayerName] [(Optional) -c X Y Z Yaw Pitch]§7");
                    return false;
                }

                try {
                    Double.parseDouble(strings[3]);
                    Double.parseDouble(strings[4]);
                    Double.parseDouble(strings[5]);
                } catch(NumberFormatException | NullPointerException e) {
                    MessageManager.sendMessage(commandSender, "§cYou didn't provide valid coordination's");
                    return false;
                }

                Player player = (Player) commandSender;
                Double x = Double.parseDouble(strings[3]);
                Double y = Double.parseDouble(strings[4]);
                Double z = Double.parseDouble(strings[5]);
                World world = Bukkit.getWorld(strings[1]);
                player.teleport(new Location(world, x, y, z));
                MessageManager.sendMessage(commandSender, "You got teleported to your provided coordination's");
                return false;
            }

            //Teleport Command Self with Cords with Yaw and Pitch
            if (strings.length == 8) {
                if (!commandSender.hasPermission("lightworlds.teleport")) {
                    MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    MessageManager.sendMessage(commandSender, "§cYou need to be a player to teleport yourself. §3Use §3/lw tp [WorldName] [PlayerName] [(Optional) -c X Y Z (Optional) Yaw Pitch]§7 to teleport a player on your server");
                    return false;
                }
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe world §3" + strings[1] +"§c doesn't exist");
                    return false;
                }
                if (!strings[2].equals("-c")) {
                    MessageManager.sendMessage(commandSender, "Use §3/lw tp [WorldName] [(Optional) PlayerName] [(Optional) -c X Y Z Yaw Pitch]§7");
                    return false;
                }

                try {
                    Double.parseDouble(strings[3]);
                    Double.parseDouble(strings[4]);
                    Double.parseDouble(strings[5]);
                    Float.parseFloat(strings[6]);
                    Float.parseFloat(strings[7]);
                } catch(NumberFormatException | NullPointerException e) {
                    MessageManager.sendMessage(commandSender, "§cYou didn't provide valid coordination's");
                    return false;
                }

                Player player = (Player) commandSender;
                Double x = Double.parseDouble(strings[3]);
                Double y = Double.parseDouble(strings[4]);
                Double z = Double.parseDouble(strings[5]);
                Float yaw = Float.parseFloat(strings[6]);
                Float pitch = Float.parseFloat(strings[7]);
                World world = Bukkit.getWorld(strings[1]);
                player.teleport(new Location(world, x, y, z,yaw,pitch));
                MessageManager.sendMessage(commandSender, "You got teleported to your provided coordination's");
                return false;
            }

            //Teleport Command Others with Cords without Yaw and Pitch
            if (strings.length == 7) {
                if (!commandSender.hasPermission("lightworlds.teleport")) {
                    MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                    return false;
                }
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe world §3" + strings[1] +"§c doesn't exist");
                    return false;
                }

                if (Bukkit.getPlayer(strings[2]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe player §3" + strings[2] +"§c isn't online");
                    return false;
                }

                if (!strings[3].equals("-c")) {
                    MessageManager.sendMessage(commandSender, "Use §3/lw tp [WorldName] [(Optional) PlayerName] [(Optional) -c X Y Z Yaw Pitch]§7");
                    return false;
                }

                try {
                    Double.parseDouble(strings[4]);
                    Double.parseDouble(strings[5]);
                    Double.parseDouble(strings[6]);
                } catch(NumberFormatException | NullPointerException e) {
                    MessageManager.sendMessage(commandSender, "§cYou didn't provide valid coordination's");
                    return false;
                }

                Player player = Bukkit.getPlayer(strings[2]);
                Double x = Double.parseDouble(strings[4]);
                Double y = Double.parseDouble(strings[5]);
                Double z = Double.parseDouble(strings[6]);
                World world = Bukkit.getWorld(strings[1]);
                player.teleport(new Location(world, x, y, z));
                MessageManager.sendMessage(commandSender, "You teleported §3" + player.getName() + "§7 to your provided coordination's");
                return false;
            }

            //Teleport Command Others with Cords with Yaw and Pitch
            if (strings.length == 9) {
                if (!commandSender.hasPermission("lightworlds.teleport")) {
                    MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                    return false;
                }
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe world §3" + strings[1] +"§c doesn't exist");
                    return false;
                }

                if (Bukkit.getPlayer(strings[2]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe player §3" + strings[2] +"§c isn't online");
                    return false;
                }

                if (!strings[3].equals("-c")) {
                    MessageManager.sendMessage(commandSender, "Use §3/lw tp [WorldName] [(Optional) PlayerName] [(Optional) -c X Y Z Yaw Pitch]§7");
                    return false;
                }

                try {
                    Double.parseDouble(strings[4]);
                    Double.parseDouble(strings[5]);
                    Double.parseDouble(strings[6]);
                    Float.parseFloat(strings[7]);
                    Float.parseFloat(strings[8]);
                } catch(NumberFormatException | NullPointerException e) {
                    MessageManager.sendMessage(commandSender, "§cYou didn't provide valid coordination's");
                    return false;
                }

                Player player = Bukkit.getPlayer(strings[2]);
                Double x = Double.parseDouble(strings[4]);
                Double y = Double.parseDouble(strings[5]);
                Double z = Double.parseDouble(strings[6]);
                Float yaw = Float.parseFloat(strings[7]);
                Float pitch = Float.parseFloat(strings[8]);
                World world = Bukkit.getWorld(strings[1]);
                player.teleport(new Location(world, x, y, z, yaw ,pitch));
                MessageManager.sendMessage(commandSender, "You teleported §3" + player.getName() + "§7 to your provided coordination's");
                return false;
            }

            MessageManager.sendMessage(commandSender, "Use §3/lw tp [WorldName] [(Optional) PlayerName] [(Optional) -c X Y Z Yaw Pitch]§7");
            return false;
        }

        //Create Command
        if (strings[0].equalsIgnoreCase("create")){
            //Create Command Help
            if (!commandSender.hasPermission("lightworlds.create")) {
                MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                return false;
            }
            if (strings.length == 1) {
                MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                return false;
            }
            if (strings.length == 2) {
                MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                return false;
            }

            //Create Command without gen and seed
            if (strings.length == 3) {
                List<World> worldList = Bukkit.getWorlds();
                for (World world : worldList) {
                    if (world.getName().equalsIgnoreCase(strings[1])) {
                        MessageManager.sendMessage(commandSender, "§cThe world name is already in use");
                        return false;
                    }
                }

                if (!strings[1].matches("^[a-zA-Z0-9_.-]*$")) {
                    MessageManager.sendMessage(commandSender, "§cThe world name can only contain §3letters§7, §3numbers§7 or §3_-.§7");
                    return false;
                }
                String s1 = strings[2];
                WorldCreator worldCreator = new WorldCreator(strings[1]);
                switch (s1) {
                    case "normal":
                        worldCreator.environment(World.Environment.NORMAL);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        addWorldToDatabase(worldCreator);
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "nether":
                        worldCreator.environment(World.Environment.NETHER);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        addWorldToDatabase(worldCreator);
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "end":
                        worldCreator.environment(World.Environment.THE_END);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        addWorldToDatabase(worldCreator);
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "amplified":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.AMPLIFIED);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        addWorldToDatabase(worldCreator);
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "largebiomes":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.LARGE_BIOMES);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        addWorldToDatabase(worldCreator);
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "flat":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.FLAT);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        addWorldToDatabase(worldCreator);
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "void":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.generator(new VoidGenerator());
                        worldCreator.biomeProvider(new VoidBiomes());
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        addWorldToDatabase(worldCreator, "LightWorldsOwnVoidGenerator1");
                        MessageManager.sendMessage(commandSender, "Your world was created");

                        return false;
                    default:
                        MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                        return false;
                }
            }

            //Create Command with gen or seed
            if (strings.length == 5) {
                List<World> worldList = Bukkit.getWorlds();
                for (World world : worldList) {
                    if (world.getName().equalsIgnoreCase(strings[1])) {
                        MessageManager.sendMessage(commandSender, "§cThe world name is already in use");
                        return false;
                    }
                }

                if (!strings[1].matches("^[a-zA-Z0-9_.-]*$")) {
                    MessageManager.sendMessage(commandSender, "§cThe world name can only contain §3letters§7, §3numbers§7 or §3_-.§7");
                    return false;
                }
                if (!strings[3].equals("-g") && !strings[3].equals("-s")) {
                    MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                    return false;
                }

                Boolean isGen = false;
                String s1 = strings[2];
                WorldCreator worldCreator = new WorldCreator(strings[1]);

                if (strings[3].equals("-g")) {
                    isGen = true;
                    if (Bukkit.getPluginManager().getPlugin(strings[4]) == null) {
                        MessageManager.sendMessage(commandSender, "§cGenerator not found");
                        return false;
                    }
                    if (Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultWorldGenerator(strings[4], "0") == null) {
                        MessageManager.sendMessage(commandSender, "§cGenerator not found");
                        return false;
                    }
                    ChunkGenerator chunkGenerator = Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultWorldGenerator(strings[4], "0");
                    worldCreator.generator(chunkGenerator);
                    if (Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultBiomeProvider(strings[4], "0") != null) {
                        worldCreator.biomeProvider(Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultBiomeProvider(strings[4], "0"));
                    }
                }

                if (strings[3].equals("-s")) {
                    try {
                        Long.parseLong(strings[4]);
                    } catch(NumberFormatException | NullPointerException e) {
                        MessageManager.sendMessage(commandSender, "§cYou didn't provide a valid seed");
                        return false;
                    }
                    worldCreator.seed(Long.parseLong(strings[4]));
                }

                switch (s1) {
                    case "normal":
                        worldCreator.environment(World.Environment.NORMAL);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGen) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "nether":
                        worldCreator.environment(World.Environment.NETHER);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGen) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "end":
                        worldCreator.environment(World.Environment.THE_END);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGen) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "amplified":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.AMPLIFIED);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGen) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "largebiomes":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.LARGE_BIOMES);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGen) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "flat":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.FLAT);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGen) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "void":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.generator(new VoidGenerator());
                        worldCreator.biomeProvider(new VoidBiomes());
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGen) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, "LightWorldsOwnVoidGenerator1");
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    default:
                        MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                        return false;
                }
            }

            //Create Command with gen and seed
            if (strings.length == 7) {
                List<World> worldList = Bukkit.getWorlds();
                for (World world : worldList) {
                    if (world.getName().equalsIgnoreCase(strings[1])) {
                        MessageManager.sendMessage(commandSender, "§cThe world name is already in use");
                        return false;
                    }
                }
                if (!strings[1].matches("^[a-zA-Z0-9_.-]*$")) {
                    MessageManager.sendMessage(commandSender, "§cThe world name can only contain §3letters§7, §3numbers§7 or §3_-.§7");
                    return false;
                }
                if (!strings[3].equals("-g") && !strings[3].equals("-s")) {
                    MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                    return false;
                }
                if (!strings[5].equals("-g") && !strings[5].equals("-s")) {
                    MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                    return false;
                }

                Boolean isGenOne = false;
                String s1 = strings[2];
                WorldCreator worldCreator = new WorldCreator(strings[1]);

                if (strings[3].equals("-g")) {
                    isGenOne = true;
                    if (Bukkit.getPluginManager().getPlugin(strings[4]) == null) {
                        MessageManager.sendMessage(commandSender, "§cGenerator not found");
                        return false;
                    }
                    if (Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultWorldGenerator(strings[4], "0") == null) {
                        MessageManager.sendMessage(commandSender, "§cGenerator not found");
                        return false;
                    }
                    ChunkGenerator chunkGenerator = Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultWorldGenerator(strings[4], "0");
                    worldCreator.generator(chunkGenerator);
                    if (Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultBiomeProvider(strings[4], "0") != null) {
                        worldCreator.biomeProvider(Bukkit.getPluginManager().getPlugin(strings[4]).getDefaultBiomeProvider(strings[4], "0"));
                    }
                }

                if (strings[3].equals("-s")) {
                    try {
                        Long.parseLong(strings[4]);
                    } catch(NumberFormatException | NullPointerException e) {
                        MessageManager.sendMessage(commandSender, "§cYou didn't provide a valid seed");
                        return false;
                    }
                    worldCreator.seed(Long.parseLong(strings[4]));
                }

                if (strings[5].equals("-g")) {
                    if (Bukkit.getPluginManager().getPlugin(strings[6]) == null) {
                        MessageManager.sendMessage(commandSender, "§cGenerator not found");
                        return false;
                    }
                    if (Bukkit.getPluginManager().getPlugin(strings[6]).getDefaultWorldGenerator(strings[6], "0") == null) {
                        MessageManager.sendMessage(commandSender, "§cGenerator not found");
                        return false;
                    }
                    ChunkGenerator chunkGenerator = Bukkit.getPluginManager().getPlugin(strings[6]).getDefaultWorldGenerator(strings[4], "0");
                    worldCreator.generator(chunkGenerator);
                    if (Bukkit.getPluginManager().getPlugin(strings[6]).getDefaultBiomeProvider(strings[6], "0") != null) {
                        worldCreator.biomeProvider(Bukkit.getPluginManager().getPlugin(strings[6]).getDefaultBiomeProvider(strings[4], "0"));
                    }
                }

                if (strings[5].equals("-s")) {
                    try {
                        Long.parseLong(strings[6]);
                    } catch(NumberFormatException | NullPointerException e) {
                        MessageManager.sendMessage(commandSender, "§cYou didn't provide a valid seed");
                        return false;
                    }
                    worldCreator.seed(Long.parseLong(strings[6]));
                }

                switch (s1) {
                    case "normal":
                        worldCreator.environment(World.Environment.NORMAL);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGenOne) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, strings[6]);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "nether":
                        worldCreator.environment(World.Environment.NETHER);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGenOne) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, strings[6]);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "end":
                        worldCreator.environment(World.Environment.THE_END);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGenOne) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, strings[6]);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "amplified":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.AMPLIFIED);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGenOne) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, strings[6]);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "largebiomes":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.LARGE_BIOMES);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGenOne) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, strings[6]);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "flat":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.type(WorldType.FLAT);
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGenOne) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, strings[6]);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    case "void":
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.generator(new VoidGenerator());
                        worldCreator.biomeProvider(new VoidBiomes());
                        MessageManager.sendMessage(commandSender, "Creating world...");
                        worldCreator.createWorld();
                        if (isGenOne) {
                            addWorldToDatabase(worldCreator, strings[4]);
                        }else {
                            addWorldToDatabase(worldCreator, strings[6]);
                        }
                        MessageManager.sendMessage(commandSender, "Your world was created");
                        return false;
                    default:
                        MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
                        return false;
                }
            }
            MessageManager.sendMessage(commandSender, "Use §3/lw create [WorldName] [WorldType] [(Optional) -s Seed] [(Optiona) -g GeneratorName]§7");
            return false;
        }

        //Delete Command
        if (strings[0].equalsIgnoreCase("delete")){
            //Delete Command Help
            if (!commandSender.hasPermission("lightworlds.delete")) {
                MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                return false;
            }

            if (strings.length == 1) {
                MessageManager.sendMessage(commandSender, "Use §3/lw delete [WorldName]§7");
            }

            if (strings.length == 2) {
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cThe world §3" + strings[1] + "§c doens't exist");
                    return false;
                }
                World world = Bukkit.getWorld(strings[1]);
                String worldname = world.getName();
                File path = world.getWorldFolder();
                MessageManager.sendMessage(commandSender, "Start deleting the world...");
                if (world.getName().equals("world")) {
                    MessageManager.sendMessage(commandSender, "§cYou can't delete your main world");
                    return false;
                }
                world.getPlayers().forEach(player -> {
                    player.teleport(Bukkit.getWorld("world").getSpawnLocation());
                });
                Bukkit.getScheduler().runTaskTimer(LightWorlds.getPlugin(), bukkitTask -> {
                    if (!Bukkit.isTickingWorlds()) {
                        bukkitTask.cancel();
                        Boolean bool = Bukkit.unloadWorld(world, false);
                        deleteWorld(path);
                        if (bool) {
                            MessageManager.sendMessage(commandSender, "The world §3" + worldname + "§7 got deleted");
                            removeWorldFromTDatabase(worldname);
                        }else {
                            MessageManager.sendMessage(commandSender, "§cFailed to delete world §3" + worldname + "§7");
                        }
                    }
                },0,5);
                return false;
            }
        }

        //List Command
        if (strings[0].equalsIgnoreCase("list")){
            //List Command Help
            if (!commandSender.hasPermission("lightworlds.list")) {
                MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                return false;
            }

            if (strings.length > 1) {
                MessageManager.sendMessage(commandSender, "Use §3/lw list§7");
                return false;
            }
            MessageManager.sendMessage(commandSender, "Your worlds:");
            Bukkit.getWorlds().forEach(world -> {
                MessageManager.sendMessageNewLine(commandSender, "§3" + world.getName() + "§7");
            });
        }

        //Players Command
        if (strings[0].equalsIgnoreCase("players")){
            //List Command Help
            if (!commandSender.hasPermission("lightworlds.players")) {
                MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                return false;
            }

            if (strings.length > 2) {
                MessageManager.sendMessage(commandSender, "Use §3/lw players [(Optional) WorldName]§7");
            }

            if (strings.length == 1) {
                Integer totalPlayers = Bukkit.getOnlinePlayers().size();
                MessageManager.sendMessage(commandSender, "Players in your worlds:");
                MessageManager.sendMessageNewLine(commandSender, "§7Total of all Worlds§7: §3" + totalPlayers + "§7 Players");
                Bukkit.getWorlds().forEach(world -> {
                    MessageManager.sendMessageNewLine(commandSender, "§7" + world.getName() + "§7: §3" + world.getPlayers().size() + "§7 Players");
                });
                return false;
            }
            if (strings.length == 2) {
                if (Bukkit.getWorld(strings[1]) == null) {
                    MessageManager.sendMessage(commandSender, "§cYou didn't provide a valid world name");
                    return false;
                }
                World world = Bukkit.getWorld(strings[1]);
                Integer totalPlayers = world.getPlayers().size();
                MessageManager.sendMessage(commandSender, "Players in world " + world.getName() + ": §3" + totalPlayers + "§7 Players");
                world.getPlayers().forEach(player -> {
                    MessageManager.sendMessageNewLine(commandSender, "§3" + player.getName() + "§7");
                });
                return false;
            }
        }

        //Info Command
        if (strings[0].equalsIgnoreCase("info")){
            //Info Command Help
            if (!commandSender.hasPermission("lightworlds.info")) {
                MessageManager.sendMessage(commandSender, "§cYou don't have the required permission for this command");
                return false;
            }

            if (strings.length > 1) {
                MessageManager.sendMessage(commandSender, "Use §3/lw info");
            }
            if (strings.length == 1) {
                if (commandSender instanceof Player player) {
                    Integer totalPlayers = Bukkit.getOnlinePlayers().size();
                    MessageManager.sendMessage(commandSender, "Info for your world:");
                    MessageManager.sendMessageNewLine(commandSender, "Name: §3" + player.getWorld().getName());
                    return false;
                }else {
                    MessageManager.sendMessage(commandSender, "You need to be a Player to use this command");
                }

            }
        }
        return false;
    }








    public boolean deleteWorld(File path) {
        if(path.exists()) {
            File files[] = path.listFiles();
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        path.delete();
        return(path.delete());
    }

    public void addWorldToDatabase(WorldCreator worldCreator, String gen) {
        Bukkit.getScheduler().runTaskAsynchronously(LightWorlds.getPlugin(), bukkitTask -> {
            PreparedStatement preparedStatement = Database.prepareStatement("INSERT INTO worlds(worldname, env, type, gen, seed) VALUES(?, ?, ?, ?, ?);");
            try {
                preparedStatement.setString(1, worldCreator.name());
                preparedStatement.setString(2, worldCreator.environment().name());
                preparedStatement.setString(3, worldCreator.type().name());
                preparedStatement.setString(4, gen);
                preparedStatement.setLong(5, worldCreator.seed());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Database.runStatement(preparedStatement);
        });
    }

    public void addWorldToDatabase(WorldCreator worldCreator) {
        Bukkit.getScheduler().runTaskAsynchronously(LightWorlds.getPlugin(), bukkitTask -> {
            PreparedStatement preparedStatement = Database.prepareStatement("INSERT INTO worlds(worldname, env, type,seed) VALUES(?, ?, ?, ?);");
            try {
                preparedStatement.setString(1, worldCreator.name());
                preparedStatement.setString(2, worldCreator.environment().name());
                preparedStatement.setString(3, worldCreator.type().name());
                preparedStatement.setLong(4, worldCreator.seed());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Database.runStatement(preparedStatement);
        });
    }

    public void removeWorldFromTDatabase(String name) {
        Bukkit.getScheduler().runTaskAsynchronously(LightWorlds.getPlugin(), bukkitTask -> {
            PreparedStatement preparedStatement = Database.prepareStatement("DELETE FROM worlds WHERE worldname=?;");
            try {
                preparedStatement.setString(1, name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Database.runStatement(preparedStatement);
        });

    }
}
