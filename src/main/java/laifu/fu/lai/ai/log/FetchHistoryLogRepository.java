package laifu.fu.lai.ai.log;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class FetchHistoryLogRepository {
    private final JdbcTemplate jdbc;

    public FetchHistoryLogRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void ensureSchema() {
        jdbc.execute("""
                CREATE TABLE IF NOT EXISTS fetch_history_log (
                  id INTEGER PRIMARY KEY AUTOINCREMENT,
                  profile_id INTEGER NOT NULL,
                  profile_name TEXT,
                  endpoint TEXT NOT NULL,
                  status INTEGER NOT NULL,
                  latency_ms INTEGER NOT NULL,
                  request_body TEXT,
                  created_at TEXT NOT NULL,
                  FOREIGN KEY(profile_id) REFERENCES ai_provider_profiles(id)
                )
                """);
        jdbc.execute("CREATE INDEX IF NOT EXISTS idx_fetch_history_profile_time ON fetch_history_log(profile_id, created_at)");
    }

    public void insert(long profileId, String profileName, String endpoint, int status, long latencyMs, String requestBody) {
        ensureSchema();
        jdbc.update(
                "INSERT INTO fetch_history_log(profile_id, profile_name, endpoint, status, latency_ms, request_body, created_at) VALUES(?, ?, ?, ?, ?, ?, ?)",
                profileId,
                profileName,
                endpoint,
                status,
                latencyMs,
                requestBody,
                Instant.now().toString()
        );
    }
}
