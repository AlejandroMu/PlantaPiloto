package icesi.plantapiloto.driverAsset.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class MeasurementRepository implements Repository<Measurements> {

    private static final Logger logger = Logger.getLogger(MeasurementRepository.class.getName());

    private static MeasurementRepository instance;

    public static MeasurementRepository getInstance() {
        if (instance == null) {
            instance = new MeasurementRepository();
        }
        return instance;
    }

    private MeasurementRepository() {

    }

    static {
        Connection connection = DataManager.getConnection();
        try (Statement statement = connection.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS measure (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "assetId INTEGER, " +
                    "assetName VARCHAR, " +
                    "value DOUBLE, " +
                    "serverProxy VARCHAR, " +
                    "execId INTEGER," +
                    "timeStamp BIGINT)";
            statement.execute(createTableQuery);
        } catch (SQLException ex) {
            logger.severe("Error al iniciar la base de datos: " + ex.getMessage());
        }
        DataManager.closeConnection(connection);

    }

    @Override
    public Measurements entityFromResult(ResultSet resultSet) throws Exception {
        int id = resultSet.getInt("id");
        int assetId = resultSet.getInt("assetId");
        double value = resultSet.getDouble("value");
        String assetName = resultSet.getString("assetName");
        String server = resultSet.getString("serverProxy");
        int execId = resultSet.getInt("execId");
        long timeStamp = resultSet.getLong("timeStamp");

        return new Measurements(id, assetId, assetName, value, execId, timeStamp, server);

    }

    @Override
    public void insert(Measurements entity) {
        Connection connection = DataManager.getConnection();
        String insertQuery = "INSERT INTO measure (assetId, assetName, value, serverProxy, execId, timeStamp) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            statement.setInt(1, entity.assetId);
            statement.setString(2, entity.assetName);
            statement.setDouble(3, entity.value);
            statement.setString(4, entity.getServerProxy());
            statement.setInt(5, entity.execId);
            statement.setLong(6, entity.timeStamp);
            statement.executeUpdate();
        } catch (Exception e) {
            logger.severe("Error en la inserción" + e.getMessage());
        }
        DataManager.closeConnection(connection);
    }

    public Map<String, List<Measurements>> getMeasurements() {
        String query = "select * from measure";
        List<Measurements> result = executeQuery(query);

        Map<String, List<Measurements>> ret = new HashMap<>();
        for (Measurements measurements : result) {
            List<Measurements> temp = ret.get(measurements.getServerProxy());
            if (temp == null) {
                temp = new ArrayList<>();
            }
            temp.add(measurements);
            ret.put(measurements.getServerProxy(), temp);
        }

        return ret;
    }

    public void remove(Measurements... measurements) {
        Connection connection = DataManager.getConnection();

        try (Statement statement = connection.createStatement()) {
            for (Measurements measurements2 : measurements) {
                String deleteQuery = "DELETE FROM measure WHERE id = " + measurements2.getId();
                statement.executeUpdate(deleteQuery);
            }
        } catch (Exception e) {
            logger.severe("Error en la eliminación " + e.getMessage());
        }
        DataManager.closeConnection(connection);
    }

}
