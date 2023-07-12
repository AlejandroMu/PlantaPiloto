package icesi.plantapiloto.driverAsset.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class DataManager {

    public static Queue<Connection> connections = new ArrayDeque<>();

    static {
        try (Connection connection = DriverManager
                .getConnection("jdbc:h2:./taskdb", "i40dev", "SwEng")) {

            try (Statement statement = connection.createStatement()) {
                String createTableQuery = "CREATE TABLE IF NOT EXISTS task (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "period BIGINT, " +
                        "execid INT, " +
                        "assets VARCHAR, " +
                        "server VARCHAR, " +
                        "share INT," +
                        "state VARCHAR(10))";
                statement.execute(createTableQuery);
            }

        } catch (SQLException ex) {
            System.out.println("Error al iniciar la base de datos: " + ex.getMessage());
        }
        for (int i = 0; i < 5; i++) {
            try {
                Connection c = DriverManager.getConnection("jdbc:h2:./taskdb", "i40dev", "SwEng");
                connections.add(c);

            } catch (Exception e) {
                System.out.println("Error al establecer la conexión con la base de datos: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        synchronized (connections) {
            return connections.poll();
        }
    }

    public void closeConnection(Connection c) {
        synchronized (connections) {
            connections.add(c);
        }
    }

    public List<Task> executeQuery(String selectQuery) {
        Connection connection = getConnection();

        List<Task> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);

            // Recorrer los resultados de la consulta
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                int period = resultSet.getInt("period");
                int execId = resultSet.getInt("execid");
                String assets = resultSet.getString("assets");
                String server = resultSet.getString("server");
                int isShare = resultSet.getInt("share");
                String state = resultSet.getString("state");

                Task t = new Task(id, period, execId, assets, server, isShare, state);
                tasks.add(t);
            }

            resultSet.close();
        } catch (Exception e) {
            System.err.println("Error en la consulta: " + e.getMessage());
        }

        closeConnection(connection);
        return tasks;
    }

    public List<Task> getTasks() {
        return executeQuery("SELECT * FROM task");
    }

    public List<Task> getTasksByPeriod(int period) {
        return executeQuery("SELECT * FROM task t WHERE t.period = " + period);
    }

    public List<Task> getTasksByExecId(int execId) {
        return executeQuery("SELECT * FROM task t WHERE t.execid = " + execId);
    }

    public List<Task> getTasksByExecIdAndPeriod(int execId, int period) {
        return executeQuery("SELECT * FROM task t WHERE t.execid = " + execId + " AND t.period = " + period);
    }

    public void saveTask(Task task) {
        Connection connection = getConnection();
        String insertQuery = "INSERT INTO task (period, execid, assets, server, share, state) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setLong(1, task.getPeriod());
            statement.setInt(2, task.getExecId());
            statement.setString(3, task.getAssets());
            statement.setString(4, task.getServer());
            statement.setInt(5, task.getIsShare());
            statement.setString(6, task.getState());
            int rowsAffected = statement.executeUpdate();
            System.out.println("Filas afectadas: " + rowsAffected);
        } catch (Exception e) {
            System.err.println("Error en la inserción" + e.getMessage());
        }
        closeConnection(connection);
    }

    public void removeByExecId(int exeId) {
        Connection connection = getConnection();

        try (Statement statement = connection.createStatement()) {
            String deleteQuery = "DELETE FROM task WHERE execid = " + exeId;
            int rowsAffected = statement.executeUpdate(deleteQuery);
            System.out.println("Filas borradas: " + rowsAffected);
        } catch (Exception e) {
            System.err.println("Error en la eliminación");
        }

        closeConnection(connection);
    }

    public void setState(int execId, String string) {
        Connection connection = getConnection();
        try (Statement statement = connection.createStatement()) {
            String updateQuery = "UPDATE task SET state = '" + string + "' WHERE execid = " + execId;
            int rowsAffected = statement.executeUpdate(updateQuery);
            System.out.println("Filas actualizadas: " + rowsAffected);
        } catch (Exception e) {
            System.err.println("Error en la actualización");
        }
        closeConnection(connection);

    }

}
