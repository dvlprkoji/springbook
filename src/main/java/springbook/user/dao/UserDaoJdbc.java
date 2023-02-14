package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;


public class UserDaoJdbc implements UserDao{

    private JdbcTemplate jdbcTemplate;
    private javax.sql.DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);

        this.dataSource = dataSource;
    }

    public void add(User user){
        jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
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
                "update users set name = ?, password = ?, level = ?, login = ?, recommend = ?",
                user.getName(), user.getPassword(), user.getLevel(), user.getLogin(), user.getRecommend()
        );
    }
}
