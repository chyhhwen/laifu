package laifu.fu.lai.ai.provider;

import laifu.fu.lai.ai.config.AiProperties;
import laifu.fu.lai.ai.model.ChatMessage;
import laifu.fu.lai.ai.model.ChatRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OpenAI 官方與「OpenAI 相容端點」（Jan/Ollama/vLLM/LM Studio）共用。
 * 目前先做非串流的 /v1/chat/completions。
 */
public class OpenAiCompatibleProvider implements ChatProvider {

    private final WebClient webClient;
    private final AiProperties props;
    private final String name;

    public OpenAiCompatibleProvider(String name, WebClient webClient, AiProperties props) {
        this.name = name;
        this.webClient = webClient;
        this.props = props;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String chat(ChatRequest request) {
        List<Map<String, String>> messages = request.messages().stream()
                .map(m -> Map.of("role", m.role(), "content", m.content()))
                .toList();

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", props.getOpenai().getModel());
        payload.put("messages", messages);

        OpenAiChatCompletionsResponse resp = webClient.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + props.getOpenai().getApiKey())
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(OpenAiChatCompletionsResponse.class)
                .block();

        if (resp == null || resp.choices == null || resp.choices.isEmpty() || resp.choices.getFirst().message == null) {
            return "";
        }
        return resp.choices.getFirst().message.content == null ? "" : resp.choices.getFirst().message.content;
    }

    // --- minimal DTOs for OpenAI Chat Completions ---
    public static class OpenAiChatCompletionsResponse {
        public List<Choice> choices;

        public static class Choice {
            public Message message;
        }

        public static class Message {
            public String role;
            public String content;
        }
    }
}
