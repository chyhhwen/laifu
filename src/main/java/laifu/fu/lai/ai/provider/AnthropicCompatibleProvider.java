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
 * 「Anthropic 相容端點」：假設它相容 Anthropic Messages API（/v1/messages）與 header。
 * 目前先做非串流。
 */
public class AnthropicCompatibleProvider implements ChatProvider {

    private final WebClient webClient;
    private final AiProperties props;

    public AnthropicCompatibleProvider(WebClient webClient, AiProperties props) {
        this.webClient = webClient;
        this.props = props;
    }

    @Override
    public String name() {
        return "anthropicCompatible";
    }

    @Override
    public String chat(ChatRequest request) {
        String userText = request.messages().isEmpty() ? "" : request.messages().getLast().content();

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", props.getAnthropicCompatible().getModel());
        payload.put("max_tokens", 1024);
        payload.put("messages", List.of(Map.of(
                "role", "user",
                "content", List.of(Map.of("type", "text", "text", userText))
        )));

        AnthropicProvider.AnthropicMessagesResponse resp = webClient.post()
                .uri("/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("x-api-key", props.getAnthropicCompatible().getApiKey())
                .header("anthropic-version", "2023-06-01")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(AnthropicProvider.AnthropicMessagesResponse.class)
                .block();

        if (resp == null || resp.content == null || resp.content.isEmpty() || resp.content.getFirst().text == null) {
            return "";
        }
        return resp.content.getFirst().text;
    }
}
