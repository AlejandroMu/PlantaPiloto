package icesi.plantapiloto.driverAsset.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.logging.Logger;

public class DataManager {
    private static final Logger logger = Logger.getLogger(DataManager.class.getName());

    public static Queue<Connection> connections = new ArrayDeque<>();

    static {
        String url = "jdbc:sqlite:./driver.db";

        for (int i = 0; i < 5; i++) {
            try {
                Connection c = DriverManager.getConnection(url, "i40dev", "SwEng");
                connections.add(c);

            } catch (Exception e) {
                logger.severe("Error al establecer la conexión con la base de datos: " + e.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        synchronized (connections) {
            return connections.poll();
        }
    }

    public static void closeConnection(Connection c) {
        synchronized (connections) {
            connections.add(c);
        }
    }

}
