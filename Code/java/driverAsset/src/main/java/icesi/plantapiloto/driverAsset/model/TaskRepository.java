package icesi.plantapiloto.driverAsset.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TaskRepository implements Repository<Task> {

    private static TaskRepository instance;

    public static TaskRepository getInstance() {
        if (instance == null) {
            instance = new TaskRepository();
        }
        return instance;
    }

    private TaskRepository() {

    }

    static {
        Connection connection = DataManager.getConnection();
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
        } catch (SQLException ex) {
            System.out.println("Error al iniciar la base de datos: " + ex.getMessage());
        }
        DataManager.closeConnection(connection);

    }

    @Override
    public Task entityFromResult(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        int period = resultSet.getInt("period");
        int execId = resultSet.getInt("execid");
        String assets = resultSet.getString("assets");
        String server = resultSet.getString("server");
        int isShare = resultSet.getInt("share");
        String state = resultSet.getString("state");

        return new Task(id, period, execId, assets, server, isShare, state);
    }

    @Override
    public void insert(Task task) {
        Connection connection = DataManager.getConnection();
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
        DataManager.closeConnection(connection);
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

    public void removeByExecId(int exeId) {
        Connection connection = DataManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            String deleteQuery = "DELETE FROM task WHERE execid = " + exeId;
            int rowsAffected = statement.executeUpdate(deleteQuery);
            System.out.println("Filas borradas: " + rowsAffected);
        } catch (Exception e) {
            System.err.println("Error en la eliminación");
        }
        DataManager.closeConnection(connection);
    }

    public void setState(int execId, String string) {
        Connection connection = DataManager.getConnection();
        try (Statement statement = connection.createStatement()) {
            String updateQuery = "UPDATE task SET state = '" + string + "' WHERE execid = " + execId;
            int rowsAffected = statement.executeUpdate(updateQuery);
            System.out.println("Filas actualizadas: " + rowsAffected);
        } catch (Exception e) {
            System.err.println("Error en la actualización");
        }
        DataManager.closeConnection(connection);
    }

}
