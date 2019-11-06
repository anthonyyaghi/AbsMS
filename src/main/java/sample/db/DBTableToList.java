package sample.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DBTableToList<T> {
    public List<T> getList(PreparedStatement statement, Function<ResultSet, T> builder) {
        List<T> values = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                values.add(builder.apply(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }
}
