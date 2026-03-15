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
        WebClient wc = WebClient.builder().build();
        return new OpenAiCompatibleProvider("openai", wc, props);
    }

    @Bean
    public ChatProvider openaiCompatibleProvider(AiProperties props) {
        WebClient wc = WebClient.builder().build();
        return new OpenAiCompatibleProvider("openaiCompatible", wc, props);
    }

    @Bean
    public ChatProvider anthropicProvider(AiProperties props) {
        WebClient wc = WebClient.builder().build();
        return new AnthropicProvider(wc, props);
    }

    @Bean
    public ChatProvider anthropicCompatibleProvider(AiProperties props) {
        WebClient wc = WebClient.builder().build();
        return new AnthropicCompatibleProvider(wc, props);
    }
}
