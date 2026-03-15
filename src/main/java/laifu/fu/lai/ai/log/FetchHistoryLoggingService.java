package laifu.fu.lai.ai.log;

import laifu.fu.lai.ai.config.ProviderProfile;
import org.springframework.stereotype.Service;

@Service
public class FetchHistoryLoggingService {
    private final FetchHistoryLogRepository repo;

    public FetchHistoryLoggingService(FetchHistoryLogRepository repo) {
        this.repo = repo;
    }

    public void log(ProviderProfile profile, String endpoint, int status, long latencyMs, String requestBody) {
        if (profile == null) return;
        repo.insert(profile.id(), profile.name(), endpoint, status, latencyMs, requestBody);
    }
}
