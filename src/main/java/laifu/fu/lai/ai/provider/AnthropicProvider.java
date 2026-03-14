package laifu.fu.lai.ai.provider;

import laifu.fu.lai.ai.config.AiProperties;
import laifu.fu.lai.ai.model.ChatRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Anthropic Messages API（非串流）。
 */
public class AnthropicProvider implements ChatProvider {

    private final WebClient webClient;
    private final AiProperties props;

    public AnthropicProvider(WebClient webClient, AiProperties props) {
        this.webClient = webClient;
        this.props = props;
    }

    @Override
    public String name() {
        return "anthropic";
    }

    @Override
    public String chat(ChatRequest request) {
        String userText = request.messages().isEmpty() ? "" : request.messages().getLast().content();

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", props.getAnthropic().getModel());
        payload.put("max_tokens", 1024);
        payload.put("messages", List.of(Map.of(
                "role", "user",
                "content", List.of(Map.of("type", "text", "text", userText))
        )));

        AnthropicMessagesResponse resp = webClient.post()
                .uri("/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("x-api-key", props.getAnthropic().getApiKey())
                .header("anthropic-version", "2023-06-01")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(AnthropicMessagesResponse.class)
                .block();

        if (resp == null || resp.content == null || resp.content.isEmpty() || resp.content.getFirst().text == null) {
            return "";
        }
        return resp.content.getFirst().text;
    }

    public static class AnthropicMessagesResponse {
        public List<ContentBlock> content;

        public static class ContentBlock {
            public String type;
            public String text;
        }
    }
}
