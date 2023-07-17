package icesi.plantapiloto.driverAsset.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public interface Repository<T> {

    public T entityFromResult(ResultSet resultSet) throws Exception;

    public void insert(T entity);

    public default List<T> executeQuery(String query) {
        Connection connection = DataManager.getConnection();

        List<T> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            // Recorrer los resultados de la consulta
            while (resultSet.next()) {
                tasks.add(entityFromResult(resultSet));
            }
            resultSet.close();
        } catch (Exception e) {
            System.err.println("Error en la consulta: " + e.getMessage());
        }

        DataManager.closeConnection(connection);
        return tasks;
    }

}
