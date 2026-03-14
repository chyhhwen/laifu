package laifu.fu.lai.ai.web;

import laifu.fu.lai.ai.AiChatHub;
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

    public AiChatController(AiChatHub hub) {
        this.hub = hub;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String provider = (request.provider() == null || request.provider().isBlank()) ? "default" : request.provider();
        try {
            String content = hub.chat(request);
            return ResponseEntity.ok(ChatResponse.ok(provider, content));
        } catch (WebClientResponseException e) {
            HttpStatusCode status = e.getStatusCode();
            String code = status.value() == 401 ? "UNAUTHORIZED" : "UPSTREAM_ERROR";
            String msg = status.value() == 401
                    ? "上游服務回覆 401（通常是 API Key 未設定或無效）"
                    : ("上游服務錯誤：" + status.value() + " " + status);
            return ResponseEntity.status(status).body(ChatResponse.error(provider, code, msg));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ChatResponse.error(provider, "INTERNAL_ERROR", "伺服器內部錯誤"));
        }
    }

    /** 簡化：給前端快速測試的表單式介面 */
    @PostMapping("/chat/simple")
    public ResponseEntity<ChatResponse> simple(@RequestParam String message, @RequestParam(required = false) String provider) {
        ChatRequest req = new ChatRequest(provider, List.of(new ChatMessage("user", message)));
        return chat(req);
    }
}
