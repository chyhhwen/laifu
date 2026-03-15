package laifu.fu.lai.ai;

import laifu.fu.lai.ai.config.AiProperties;
import laifu.fu.lai.ai.config.ProviderProfile;
import laifu.fu.lai.ai.config.ProviderProfileService;
import laifu.fu.lai.ai.model.ChatRequest;
import laifu.fu.lai.ai.provider.ChatProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiChatHub {
    private final AiProperties props;
    private final List<ChatProvider> providers;
    private final ProviderProfileService profileService;

    public AiChatHub(AiProperties props, List<ChatProvider> providers, ProviderProfileService profileService) {
        this.props = props;
        this.providers = providers;
        this.profileService = profileService;
    }

    /**
     * request.provider() 代表「profile 名稱」，不是 provider 類型。
     * 若為空，則使用 active profile。
     */
    public String chat(ChatRequest request) {
        String profileName = (request.provider() == null || request.provider().isBlank()) ? null : request.provider();

        ProviderProfile profile = (profileName == null) ? profileService.active() : profileService.findByName(profileName);
        if (profile == null) {
            throw new IllegalArgumentException("Unknown provider profile: " + (profileName == null ? "(active)" : profileName));
        }

        String providerType = profile.type();
        if (providerType == null || providerType.isBlank()) {
            throw new IllegalArgumentException("Profile type is blank: " + profile.name());
        }

        ChatProvider provider = providers.stream()
                .filter(p -> p.name().equalsIgnoreCase(providerType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown provider type: " + providerType));

        // 每次請求都用 profile 覆蓋一次 props（確保 baseUrl/apiKey/model 立即生效）
        profileService.applyProfileToProps(profile);

        return provider.chat(request);
    }
}
