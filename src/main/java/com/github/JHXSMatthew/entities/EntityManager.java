package com.github.JHXSMatthew.entities;

import com.github.JHXSMatthew.sql.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Matthew on 27/02/2017.
 */
public class EntityManager {
    private static EntityManager instance;

    private Map<Integer, String> entities;

    public EntityManager() {
        entities = new HashMap<>();
        init();
    }

    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    private void init() {
        entities.clear();
        MySQLConnection connection = new MySQLConnection();

        try {
            Connection sqlConnection = connection.connect();
            sqlConnection.prepareStatement("CREATE TABLE IF NOT EXISTS Entities " +
                    "(" +
                    "id int NOT NULL AUTO_INCREMENT ," +
                    "name varchar(255) NOT NULL," +
                    "PRIMARY KEY (id)" +
                    ")").execute();

            PreparedStatement s = sqlConnection.prepareStatement("SELECT * FROM Entities");
            ResultSet set = s.executeQuery();
            while (set.next())
                entities.put(set.getInt(1), set.getString(2));
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.disconnect();
    }

    private void save() {
        //you don't save this, too dangerous to save!
    }

    public String[] getEntities() {
        return entities.values().toArray(new String[entities.size()]);
    }

    public int getNumberByName(String name) {
        try {
            return entities.entrySet()
                    .stream()
                    .filter(integerStringEntry -> Objects.equals(integerStringEntry.getValue(), name))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList()).get(0);
        } catch (Exception e) {
            return -1;
        }

    }


}
