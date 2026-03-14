package laifu.fu.lai.ai.config;

import laifu.fu.lai.ai.provider.AnthropicCompatibleProvider;
import laifu.fu.lai.ai.provider.AnthropicProvider;
import laifu.fu.lai.ai.provider.ChatProvider;
import laifu.fu.lai.ai.provider.OpenAiCompatibleProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiConfig {

    @Bean
    public ChatProvider openaiProvider(AiProperties props) {
        WebClient wc = WebClient.builder()
                .baseUrl(props.getOpenai().getBaseUrl())
                .build();
        return new OpenAiCompatibleProvider("openai", wc, props);
    }

    @Bean
    public ChatProvider openaiCompatibleProvider(AiProperties props) {
        WebClient wc = WebClient.builder()
                .baseUrl(props.getOpenai().getBaseUrl())
                .build();
        return new OpenAiCompatibleProvider("openaiCompatible", wc, props);
    }

    @Bean
    public ChatProvider anthropicProvider(AiProperties props) {
        WebClient wc = WebClient.builder()
                .baseUrl(props.getAnthropic().getBaseUrl())
                .build();
        return new AnthropicProvider(wc, props);
    }

    @Bean
    public ChatProvider anthropicCompatibleProvider(AiProperties props) {
        if (props.getAnthropicCompatible().getBaseUrl() == null || props.getAnthropicCompatible().getBaseUrl().isBlank()) {
            // 未設定就不啟用（避免啟動就因為 baseUrl 為 null 而爆掉）
            return new ChatProvider() {
                @Override
                public String name() {
                    return "anthropicCompatible";
                }

                @Override
                public String chat(laifu.fu.lai.ai.model.ChatRequest request) {
                    throw new IllegalStateException("ai.anthropic-compatible.base-url is not configured");
                }
            };
        }

        WebClient wc = WebClient.builder()
                .baseUrl(props.getAnthropicCompatible().getBaseUrl())
                .build();
        return new AnthropicCompatibleProvider(wc, props);
    }
}
