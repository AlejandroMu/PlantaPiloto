package icesi.plantapiloto.driverAsset.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

public class TaskRepository implements Repository<Task> {

    private static final Logger logger = Logger.getLogger(TaskRepository.class.getName());

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
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "period BIGINT, " +
                    "execid INTEGER, " +
                    "assets VARCHAR, " +
                    "server VARCHAR, " +
                    "share INTEGER," +
                    "state VARCHAR(10))";
            statement.execute(createTableQuery);
        } catch (SQLException ex) {
            logger.severe("Error al iniciar la base de datos: " + ex.getMessage());
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
            statement.executeUpdate();
        } catch (Exception e) {
            logger.severe("Error en la inserción" + e.getMessage());
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
            statement.executeUpdate(deleteQuery);
        } catch (Exception e) {
            logger.severe("Error en la eliminación " + e.getMessage());
        }
        DataManager.closeConnection(connection);
    }

    public void setState(int execId, String string) {
        Connection connection = DataManager.getConnection();
        try (Statement statement = connection.createStatement()) {
            String updateQuery = "UPDATE task SET state = '" + string + "' WHERE execid = " + execId;
            statement.executeUpdate(updateQuery);
        } catch (Exception e) {
            logger.severe("Error en la actualización: " + e.getMessage());
        }
        DataManager.closeConnection(connection);
    }

}
