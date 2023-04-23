package springbook.learningtest.spring.embeddeddb;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

public class SimpleJdbcTemplate extends JdbcTemplate {

    public SimpleJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    public Object queryForInt(String sql) {
        return super.queryForObject(sql, Integer.class);
    }

    public List<Map<String, Object>> queryForList(String sql) {
        return super.queryForList(sql);
    }

    public int update(String sql, String key, String value) {
        return super.update(sql, key, value);
    }
}
