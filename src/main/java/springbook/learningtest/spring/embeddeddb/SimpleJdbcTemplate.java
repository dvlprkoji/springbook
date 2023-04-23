package springbook.learningtest.spring.embeddeddb;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import java.util.List;
import java.util.Map;

public class SimpleJdbcTemplate extends JdbcTemplate {

    public SimpleJdbcTemplate(EmbeddedDatabase db) {
        super(db);
    }

    public Object queryForInt(String sql) {
        return super.queryForObject(sql, Integer.class);
    }

    public List<Map<String, Object>> queryForList(String sql) {
        return super.queryForList(sql);
    }

    public void update(String sql, String key, String value) {
        super.update(sql, key, value);
    }
}
