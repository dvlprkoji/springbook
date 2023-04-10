package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;


public class UserDaoJdbc implements UserDao{

    private String sqlAdd;

    public void setSqlAdd(String sqlAdd) {
        this.sqlAdd = sqlAdd;
    }

    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user){
        jdbcTemplate.update(
                sqlAdd,
//                "insert into users(id, name, password, email, level, login, recommend) values(?, ?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String id){
        return jdbcTemplate.queryForObject(
            "select * from users where id = ?",
                new Object[] {id},
                new RowMapper<User>() {
                    @Override
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        return new User(
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                resultSet.getString("password"),
                                resultSet.getString("email"),
                                Level.valueOf(resultSet.getInt("level")),
                                resultSet.getInt("login"),
                                resultSet.getInt("recommend")
                        );
                    }
        });
    }

    public List<User> getAll() {
        return jdbcTemplate.query("select * from users", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                return new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        Level.valueOf(resultSet.getInt("level")),
                        resultSet.getInt("login"),
                        resultSet.getInt("recommend")
                );
            }
        });
    }

    public void deleteAll(){
        jdbcTemplate.execute("delete from users");
    }

    public int getCount(){
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "update users set name = ?, password = ?, email = ?,  level = ?, login = ?, recommend = ? where id = ?",
                user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId()
        );
    }
}
