package laifu.fu.lai.ai.log;

import java.time.Instant;

public record FetchHistoryLog(
        long id,
        long profileId,
        String profileName,
        String endpoint,
        int status,
        long latencyMs,
        String requestBody,
        Instant createdAt
) {
}
