package laifu.fu.lai.ai;

import laifu.fu.lai.ai.config.AiProperties;
import laifu.fu.lai.ai.model.ChatRequest;
import laifu.fu.lai.ai.provider.ChatProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiChatHub {
    private final AiProperties props;
    private final List<ChatProvider> providers;

    public AiChatHub(AiProperties props, List<ChatProvider> providers) {
        this.props = props;
        this.providers = providers;
    }

    public String chat(ChatRequest request) {
        String providerName = (request.provider() == null || request.provider().isBlank())
                ? props.getProvider()
                : request.provider();

        ChatProvider provider = providers.stream()
                .filter(p -> p.name().equalsIgnoreCase(providerName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown provider: " + providerName));

        return provider.chat(request);
    }
}
