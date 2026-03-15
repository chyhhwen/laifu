package laifu.fu.lai.ai.model;

import java.util.List;

public record ChatRequest(
        // providerProfileName：使用者建立的 profile 名稱（例如「公司 OpenAI」「本機 Ollama」）
        String provider,
        List<ChatMessage> messages,
        // per-request overrides (由前端控制)
        java.util.Map<String, Object> params,
        java.util.Map<String, String> headers
) {
}
