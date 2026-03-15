package laifu.fu.lai.ai.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AiSettingsRepository {
    private final JdbcTemplate jdbc;

    public AiSettingsRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void ensureSchema() {
        jdbc.execute("""
                CREATE TABLE IF NOT EXISTS ai_settings (
                  k TEXT PRIMARY KEY,
                  v TEXT
                )
                """);
    }

    public String get(String key) {
        return jdbc.query("SELECT v FROM ai_settings WHERE k = ?", rs -> rs.next() ? rs.getString(1) : null, key);
    }

    public void set(String key, String value) {
        jdbc.update("INSERT INTO ai_settings(k, v) VALUES(?, ?) ON CONFLICT(k) DO UPDATE SET v = excluded.v", key, value);
    }
}
