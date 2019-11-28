package ru.javabegin.tutorial.androidfinance.core.dao.impls;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ru.javabegin.tutorial.androidfinance.core.dao.interfaces.SourceDAO;
import ru.javabegin.tutorial.androidfinance.core.database.SQLiteConnection;
import ru.javabegin.tutorial.androidfinance.core.impls.DefaultSource;
import ru.javabegin.tutorial.androidfinance.core.interfaces.Source;
import ru.javabegin.tutorial.androidfinance.core.objects.OperationType;

public class SourceDAOImpl implements SourceDAO {

    private static final String SOURCE_TABLE = "source";
    private List<Source> sourceList = new ArrayList<>();

    @Override
    public List<Source> getAll() {
        sourceList.clear();

        String query = "select * from " + SOURCE_TABLE + " order by parent_id asc";
        try (Statement statement = SQLiteConnection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                DefaultSource source = new DefaultSource();
                source.setId(resultSet.getLong("id"));
                source.setName(resultSet.getString("name"));
                source.setParentId(resultSet.getLong("parent_id"));
                int operationTypeId = resultSet.getInt("operation_type_id");
                source.setOperationType(OperationType.getType(operationTypeId));

                sourceList.add(source);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sourceList;
    }

    @Override
    public Source get(long id) {
        String query = "select * from " + SOURCE_TABLE +" where id=?";
        try(PreparedStatement pstmt = SQLiteConnection.getConnection().prepareStatement(query)) {
            pstmt.setLong(1, id);

            try(ResultSet rs = pstmt.executeQuery()) {
                DefaultSource source = null;
                if(rs.next()) {
                    source = new DefaultSource();
                    source.setId(rs.getLong("id"));
                    source.setName(rs.getString("name"));
                    source.setParentId(rs.getLong("parent_id"));
                    source.setOperationType(OperationType.getType(rs.getInt("operation_type_id")));
                }
                return source;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Source source) {
        String query = "update " + SOURCE_TABLE + " set name=? where id=?";
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement(query)) {
            statement.setString(1, source.getName());
            statement.setLong(2, source.getId());

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Source source) {
        String query = "delete from " + SOURCE_TABLE + " where id=?";
        try (PreparedStatement statement = SQLiteConnection.getConnection().prepareStatement(query)) {
            statement.setLong(1, source.getId());

            if (statement.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean add(Source source) {
        String query = "insert into " + SOURCE_TABLE + " (name, parent_id, operation_type_id) values(?,?,?)";
        try(PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, source.getName());

            if(source.hasParent()) {
                stmt.setLong(2, source.getParent().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }
            stmt.setLong(3, source.getOperationType().getId());

            if(stmt.executeUpdate() == 1) {
                try(ResultSet resultSet = stmt.getGeneratedKeys()) {
                    if(resultSet.next()) {
                        source.setId(resultSet.getLong(1));
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Source> getList(OperationType operationType) {
        sourceList.clear();

        String query = "select * from " + SOURCE_TABLE + " where operation_type_id=?";
        try(PreparedStatement stmt = SQLiteConnection.getConnection().prepareStatement(query)) {

            stmt.setLong(1, operationType.getId());

            try(ResultSet rs = stmt.executeQuery()) {
                DefaultSource source;

                while (rs.next()) {
                    source = new DefaultSource();
                    source.setId(rs.getLong("id"));
                    source.setName(rs.getString("name"));
                    source.setParentId(rs.getLong("parent_id"));
                    source.setOperationType(OperationType.getType(rs.getInt("operation_type_id")));
                    sourceList.add(source);
                }
            }
            return sourceList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
