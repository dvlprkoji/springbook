package springbook.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) { try{ ps.close(); } catch(SQLException e){}}
            if (c != null) { try { c.close(); } catch (SQLException e){}}
        }
    }

    public void executeSql(String query) throws SQLException {
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(query);
            }
        });
    }

    public void insert(String table, String... args) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        workWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement(
                        stringBuilder.
                                append("insert into ").
                                append(table).
                                append(" values (").
                                append(buildStringWithArgs(args)).
                                append(")").
                                toString()
                                );
            }
        });
    }

    public String buildStringWithArgs(String... args) {
        StringBuilder stringBuilder = new StringBuilder();
        String returnString = null;
        for (String arg : args) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",").toString();
            }
            stringBuilder.append("'");
            stringBuilder.append(arg).toString();
            stringBuilder.append("'");
        }
        return stringBuilder.toString();
    }
}
