package de.justin.lightworlds;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class LightWorlds extends JavaPlugin {

    private static JavaPlugin javaPlugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        javaPlugin = this;
        Database.runStatement("CREATE TABLE IF NOT EXISTS worlds(worldname VARCHAR(255), env VARCHAR(255),type VARCHAR(255), gen VARCHAR(255), seed BIGINT);");
        getCommand("lightworlds").setExecutor(new LightWorldsCommand());
        getCommand("lightworlds").setTabCompleter(new LightWorldsTabCompleter());

        Integer worldsLoaded = 0;

        ResultSet resultSet = Database.runSelectStatement("SELECT * FROM worlds;");
        try {
            while (resultSet.next()) {
                worldsLoaded++;
                WorldCreator worldCreator = new WorldCreator(resultSet.getString("worldname"));
                worldCreator.seed(resultSet.getLong("seed"));
                worldCreator.type(WorldType.valueOf(resultSet.getString("type")));
                worldCreator.environment(World.Environment.valueOf(resultSet.getString("env")));
                if (resultSet.getString("gen") != null) {
                    if (resultSet.getString("gen").equals("LightWorldsOwnVoidGenerator1")) {
                        worldCreator.environment(World.Environment.NORMAL);
                        worldCreator.generator(new VoidGenerator());
                        worldCreator.biomeProvider(new VoidBiomes());
                    }else {
                        worldCreator.generator(Bukkit.getPluginManager().getPlugin(resultSet.getString("gen")).getDefaultWorldGenerator(resultSet.getString("worldname"), "0"));
                    }
                }
                Bukkit.createWorld(worldCreator);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (worldsLoaded != 0) {
            MessageManager.sendMessage(Bukkit.getConsoleSender(), "§3" + worldsLoaded + "§7 worlds loaded");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static JavaPlugin getPlugin() {
        return javaPlugin;
    }
}
