package Mapper;

import Database.C3poDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class DataMapperImp<T, I> implements DataMapper<T, I> {
    protected Map<I, T> loadedMap = new HashMap<>();
    abstract protected String findStatement();

    public T abstractFind(String id) {
        if (loadedMap.containsKey(id)) return loadedMap.get(id);

        T result = loadedMap.get(id);
        if (result != null) return result;
        PreparedStatement findStatement = null;
        try (Connection conn = C3poDataSource.getConnection()) {

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

    // todo: handle updates;

    public List getAll() {
        ResultSet rs = null;
        PreparedStatement selectAllStatement = null;
        try (Connection conn = C3poDataSource.getConnection()) {
            selectAllStatement = conn.prepareStatement(selectAllStatement());
            rs = selectAllStatement.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String insert(T subject) {
        System.out.println("oops! insert called");
        PreparedStatement insertStatement = null;
        try (Connection conn = C3poDataSource.getConnection()) {
            insertStatement = conn.prepareStatement(insertStatement());
            String id = doInsert(subject, insertStatement);
            System.out.println("oops! in insert with name " + id);
            insertStatement.execute();
            loadedMap.put((I)id, subject);
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    protected List<T> loadAll(ResultSet rs) throws SQLException {
        List<T> result = new ArrayList();
        while (rs.next())
            result.add(load(rs));
        return result;
    }

    protected T load(ResultSet rs) throws SQLException {
        String id = rs.getString(1);
        if (loadedMap.containsKey(id)) return loadedMap.get(id);
        T result = doLoad(id, rs);
        loadedMap.put((I)id, result);
        return result;
    }


    abstract protected T doLoad(String id, ResultSet rs) throws SQLException;
    abstract protected String insertStatement();
    abstract protected String selectAllStatement();
    abstract protected String doInsert(T subject, PreparedStatement insertStatement) throws SQLException;
}
