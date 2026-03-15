package laifu.fu.lai.ai.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import laifu.fu.lai.ai.AiChatHub;
import laifu.fu.lai.ai.config.ProviderProfile;
import laifu.fu.lai.ai.config.ProviderProfileService;
import laifu.fu.lai.ai.log.FetchHistoryLoggingService;
import laifu.fu.lai.ai.model.ChatMessage;
import laifu.fu.lai.ai.model.ChatRequest;
import laifu.fu.lai.ai.model.ChatResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final AiChatHub hub;
    private final ProviderProfileService profileService;
    private final FetchHistoryLoggingService history;
    private final ObjectMapper om;

    public AiChatController(AiChatHub hub,
                            ProviderProfileService profileService,
                            FetchHistoryLoggingService history,
                            ObjectMapper om) {
        this.hub = hub;
        this.profileService = profileService;
        this.history = history;
        this.om = om;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatRequest normalized = normalize(request);

        String providerProfileName = (normalized.provider() == null || normalized.provider().isBlank()) ? "default" : normalized.provider();
        ProviderProfile profile = resolveProfile(normalized);
        String endpoint = endpointForProfile(profile);
        long start = System.currentTimeMillis();

        try {
            // TODO: 下一步會在 provider 層真正使用 headers/params（think mode / 1m context 等）
            String content = hub.chat(normalized);
            log(profile, endpoint, 200, start, normalized);
            return ResponseEntity.ok(ChatResponse.ok(providerProfileName, content));
        } catch (WebClientResponseException e) {
            HttpStatusCode status = e.getStatusCode();
            String code = status.value() == 401 ? "UNAUTHORIZED" : "UPSTREAM_ERROR";
            String msg = status.value() == 401
                    ? "上游服務回覆 401（通常是 API Key 未設定或無效）"
                    : ("上游服務錯誤：" + status.value() + " " + status);
            log(profile, endpoint, status.value(), start, normalized);
            return ResponseEntity.status(status).body(ChatResponse.error(providerProfileName, code, msg));
        } catch (Exception e) {
            log(profile, endpoint, 500, start, normalized);
            return ResponseEntity.status(500).body(ChatResponse.error(providerProfileName, "INTERNAL_ERROR", "伺服器內部錯誤"));
        }
    }

    /** 簡化：給前端快速測試的表單式介面 */
    @PostMapping("/chat/simple")
    public ResponseEntity<ChatResponse> simple(@RequestParam String message, @RequestParam(required = false) String provider) {
        ChatRequest req = new ChatRequest(
                provider,
                List.of(new ChatMessage("user", message)),
                java.util.Map.of(),
                java.util.Map.of()
        );
        return chat(req);
    }

    private ChatRequest normalize(ChatRequest request) {
        return new ChatRequest(
                request.provider(),
                request.messages(),
                request.params() == null ? java.util.Map.of() : request.params(),
                request.headers() == null ? java.util.Map.of() : request.headers()
        );
    }

    private ProviderProfile resolveProfile(ChatRequest request) {
        String name = request.provider();
        if (name == null || name.isBlank()) return profileService.active();
        return profileService.findByName(name);
    }

    private String endpointForProfile(ProviderProfile p) {
        if (p == null) return "/api/ai/chat";
        return p.type() + " " + p.baseUrl();
    }

    private void log(ProviderProfile profile, String endpoint, int status, long startMs, ChatRequest request) {
        if (profile == null) return;
        history.log(profile, endpoint, status, System.currentTimeMillis() - startMs, safeJson(request));
    }

    private String safeJson(Object o) {
        try {
            return om.writeValueAsString(o);
        } catch (Exception e) {
            return null;
        }
    }
}
