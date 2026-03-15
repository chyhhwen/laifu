package laifu.fu.lai.ai.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class AiSettingsService {
    private static final String OPENAI_BASE_URL = "openai.baseUrl";
    private static final String OPENAI_API_KEY = "openai.apiKey";
    private static final String ANTHROPIC_BASE_URL = "anthropic.baseUrl";
    private static final String ANTHROPIC_API_KEY = "anthropic.apiKey";

    private final AiProperties props;
    private final AiSettingsRepository repo;

    public AiSettingsService(AiProperties props, AiSettingsRepository repo) {
        this.props = props;
        this.repo = repo;
    }

    @PostConstruct
    public void loadFromDb() {
        repo.ensureSchema();

        String openaiBaseUrl = repo.get(OPENAI_BASE_URL);
        if (openaiBaseUrl != null && !openaiBaseUrl.isBlank()) {
            props.getOpenai().setBaseUrl(openaiBaseUrl);
        }
        String openaiApiKey = repo.get(OPENAI_API_KEY);
        if (openaiApiKey != null && !openaiApiKey.isBlank()) {
            props.getOpenai().setApiKey(openaiApiKey);
        }

        String anthropicBaseUrl = repo.get(ANTHROPIC_BASE_URL);
        if (anthropicBaseUrl != null && !anthropicBaseUrl.isBlank()) {
            props.getAnthropic().setBaseUrl(anthropicBaseUrl);
        }
        String anthropicApiKey = repo.get(ANTHROPIC_API_KEY);
        if (anthropicApiKey != null && !anthropicApiKey.isBlank()) {
            props.getAnthropic().setApiKey(anthropicApiKey);
        }
    }

    public AiSettingsForm getForm() {
        AiSettingsForm form = new AiSettingsForm();
        form.setOpenaiBaseUrl(props.getOpenai().getBaseUrl());
        form.setAnthropicBaseUrl(props.getAnthropic().getBaseUrl());
        // API key 不回填（避免把 key 直接渲染回頁面）
        return form;
    }

    public void apply(AiSettingsForm form) {
        repo.ensureSchema();

        if (form.getOpenaiBaseUrl() != null && !form.getOpenaiBaseUrl().isBlank()) {
            String v = form.getOpenaiBaseUrl().trim();
            props.getOpenai().setBaseUrl(v);
            repo.set(OPENAI_BASE_URL, v);
        }
        if (form.getAnthropicBaseUrl() != null && !form.getAnthropicBaseUrl().isBlank()) {
            String v = form.getAnthropicBaseUrl().trim();
            props.getAnthropic().setBaseUrl(v);
            repo.set(ANTHROPIC_BASE_URL, v);
        }

        // 只有輸入了才覆蓋
        if (form.getOpenaiApiKey() != null && !form.getOpenaiApiKey().isBlank()) {
            String v = form.getOpenaiApiKey().trim();
            props.getOpenai().setApiKey(v);
            repo.set(OPENAI_API_KEY, v);
        }
        if (form.getAnthropicApiKey() != null && !form.getAnthropicApiKey().isBlank()) {
            String v = form.getAnthropicApiKey().trim();
            props.getAnthropic().setApiKey(v);
            repo.set(ANTHROPIC_API_KEY, v);
        }
    }
}
