package springbook.user.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.sqlservice.SqlService;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@Repository
public class UserDaoJdbc implements UserDao{

    @Autowired
    private SqlService sqlService;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user){
        jdbcTemplate.update(
                this.sqlService.getSql("userAdd"),
//                sqlMap.get("add"),
//                "insert into users(id, name, password, email, level, login, recommend) values(?, ?, ?, ?, ?, ?, ?)",
                user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    public User get(String id){
        return jdbcTemplate.queryForObject(
                this.sqlService.getSql("userGet"),
//                "select * from users where id = ?",
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
        return jdbcTemplate.query(
                this.sqlService.getSql("userGetAll"),
//                "select * from users",
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

    public void deleteAll(){
        jdbcTemplate.execute(
                this.sqlService.getSql("userDeleteAll")
//                "delete from users"
        );
    }

    public int getCount(){
        return jdbcTemplate.queryForObject(
                this.sqlService.getSql("userGetCount"),
//                "select count(*) from users",
                Integer.class
        );
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                this.sqlService.getSql("userUpdate"),
//                "update users set name = ?, password = ?, email = ?,  level = ?, login = ?, recommend = ? where id = ?",
                user.getName(), user.getPassword(), user.getEmail(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId()
        );
    }
}
