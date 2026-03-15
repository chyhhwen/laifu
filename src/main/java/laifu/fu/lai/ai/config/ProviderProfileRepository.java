package laifu.fu.lai.ai.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProviderProfileRepository {
    private final JdbcTemplate jdbc;

    public ProviderProfileRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void ensureSchema() {
        jdbc.execute("""
                CREATE TABLE IF NOT EXISTS ai_provider_profiles (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  name TEXT NOT NULL UNIQUE,
                  type TEXT NOT NULL,
                  base_url TEXT NOT NULL,
                  api_key TEXT,
                  model TEXT,
                  active INTEGER NOT NULL DEFAULT 0
                )
                """);
    }

    public List<ProviderProfile> list() {
        return jdbc.query(
                "SELECT id, name, type, base_url, api_key, model, active FROM ai_provider_profiles ORDER BY active DESC, id ASC",
                (rs, i) -> new ProviderProfile(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("base_url"),
                        rs.getString("api_key"),
                        rs.getString("model"),
                        rs.getInt("active") == 1
                )
        );
    }

    public ProviderProfile findActive() {
        List<ProviderProfile> list = jdbc.query(
                "SELECT id, name, type, base_url, api_key, model, active FROM ai_provider_profiles WHERE active = 1 LIMIT 1",
                (rs, i) -> new ProviderProfile(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("base_url"),
                        rs.getString("api_key"),
                        rs.getString("model"),
                        true
                )
        );
        return list.isEmpty() ? null : list.getFirst();
    }

    public ProviderProfile findByName(String name) {
        List<ProviderProfile> list = jdbc.query(
                "SELECT id, name, type, base_url, api_key, model, active FROM ai_provider_profiles WHERE lower(name) = lower(?) LIMIT 1",
                (rs, i) -> new ProviderProfile(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("base_url"),
                        rs.getString("api_key"),
                        rs.getString("model"),
                        rs.getInt("active") == 1
                ),
                name
        );
        return list.isEmpty() ? null : list.getFirst();
    }

    public void deactivateAll() {
        jdbc.update("UPDATE ai_provider_profiles SET active = 0");
    }

    public void setActive(long id) {
        deactivateAll();
        jdbc.update("UPDATE ai_provider_profiles SET active = 1 WHERE id = ?", id);
    }

    public void upsert(ProviderProfileForm form) {
        jdbc.update(
                "INSERT INTO ai_provider_profiles(name, type, base_url, api_key, model, active) VALUES(?, ?, ?, ?, ?, 0) " +
                        "ON CONFLICT(name) DO UPDATE SET type = excluded.type, base_url = excluded.base_url, api_key = COALESCE(excluded.api_key, ai_provider_profiles.api_key), model = excluded.model",
                form.getName(),
                form.getType(),
                form.getBaseUrl(),
                (form.getApiKey() == null || form.getApiKey().isBlank()) ? null : form.getApiKey(),
                form.getModel()
        );
    }

    public void deleteById(long id) {
        jdbc.update("DELETE FROM ai_provider_profiles WHERE id = ?", id);
    }

    public ProviderProfile findById(long id) {
        List<ProviderProfile> list = jdbc.query(
                "SELECT id, name, type, base_url, api_key, model, active FROM ai_provider_profiles WHERE id = ?",
                (rs, i) -> new ProviderProfile(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("base_url"),
                        rs.getString("api_key"),
                        rs.getString("model"),
                        rs.getInt("active") == 1
                ),
                id
        );
        return list.isEmpty() ? null : list.getFirst();
    }
}
