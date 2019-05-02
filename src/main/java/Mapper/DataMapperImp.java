package Mapper;

import Database.DataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public abstract class DataMapperImp<T, I> implements DataMapper<T, I> {
    protected Map<I, T> loadedMap = new HashMap<>();
    abstract protected String findStatement();

    public String getDataFromServer(String urlPath) throws IOException {
        StringBuilder response = new StringBuilder();
        String inputLine;
        URL project = new URL(urlPath);
        URLConnection pc = project.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(pc.getInputStream()));

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();
        return response.toString();
    }

    protected T abstractFind(String id) {
        T result = loadedMap.get(id);
        if (result != null) return result;
        PreparedStatement findStatement = null;
        try (Connection conn = DataSource.getConnection()) {
            findStatement = conn.prepareStatement(findStatement());
            findStatement.setString(1, id);
            ResultSet rs = findStatement.executeQuery();
            rs.next();
            result = load(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected T load(ResultSet rs) throws SQLException {
        Long id = new Long(rs.getLong(1));
        if (loadedMap.containsKey(id)) return loadedMap.get(id);
        T result = doLoad(id, rs);
        loadedMap.put((I)id, result);
        return result;
    }
    abstract protected T doLoad(Long id, ResultSet rs) throws SQLException;

    public String insert(T subject) {
        PreparedStatement insertStatement = null;
        try (Connection conn = DataSource.getConnection()) {
        insertStatement = conn.prepareStatement(findStatement());
        String id = doInsert(subject, insertStatement);
        insertStatement.execute();
        loadedMap.put((I)id, subject);
        return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
    abstract protected String insertStatement();
    abstract protected String doInsert(T subject, PreparedStatement insertStatement)
            throws SQLException;
}
