package de.justin.lightworlds;

import de.justin.lightworlds.LightWorlds;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Base64;

public class Database {

    public static Connection connectToDatabase() {
        Connection conn;
        conn = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection("jdbc:h2:" + LightWorlds.getPlugin().getDataFolder().getAbsolutePath() + "/data/" + "database");
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static PreparedStatement prepareStatement(String statement) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connectToDatabase().prepareStatement(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return preparedStatement;
    }

    public static void runStatement(PreparedStatement preparedStatement) {
        try {
            preparedStatement.execute();
            connectToDatabase().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void runStatement(String preparedStatementString) {
        try {
            PreparedStatement preparedStatement = Database.prepareStatement(preparedStatementString);
            preparedStatement.execute();
            connectToDatabase().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static ResultSet runSelectStatement(PreparedStatement preparedStatement) {
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
            connectToDatabase().close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public static ResultSet runSelectStatement(String preparedStatementString) {
        PreparedStatement preparedStatement = Database.prepareStatement(preparedStatementString);
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
            connectToDatabase().close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public static String convertObjectToString(Object object) {
        String encodedOnject;
        try {
            ByteArrayOutputStream io = new ByteArrayOutputStream();
            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);
            os.writeObject(object);
            os.flush();
            byte[] serializedObject = io.toByteArray();
            encodedOnject = Base64.getEncoder().encodeToString(serializedObject);
            return encodedOnject;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object convertStringToObject(String string) {
        try {
            byte[] serializedObject;
            serializedObject = Base64.getDecoder().decode(string);
            ByteArrayInputStream in = new ByteArrayInputStream(serializedObject);
            BukkitObjectInputStream is = new BukkitObjectInputStream(in);
            return is.readObject();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
