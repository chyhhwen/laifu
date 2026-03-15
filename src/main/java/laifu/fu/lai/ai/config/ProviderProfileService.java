package laifu.fu.lai.ai.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderProfileService {
    private final ProviderProfileRepository repo;
    private final AiProperties props;

    public ProviderProfileService(ProviderProfileRepository repo, AiProperties props) {
        this.repo = repo;
        this.props = props;
    }

    @PostConstruct
    public void initDefaults() {
        repo.ensureSchema();

        // 若 DB 還沒有任何 profile，就用目前 AiProperties 建兩個預設 profile。
        if (repo.list().isEmpty()) {
            ProviderProfileForm openai = new ProviderProfileForm();
            openai.setName("OpenAI");
            openai.setType("openai");
            openai.setBaseUrl(props.getOpenai().getBaseUrl());
            openai.setApiKey(props.getOpenai().getApiKey());
            openai.setModel(props.getOpenai().getModel());
            repo.upsert(openai);

            ProviderProfileForm anthropic = new ProviderProfileForm();
            anthropic.setName("Anthropic");
            anthropic.setType("anthropic");
            anthropic.setBaseUrl(props.getAnthropic().getBaseUrl());
            anthropic.setApiKey(props.getAnthropic().getApiKey());
            anthropic.setModel(props.getAnthropic().getModel());
            repo.upsert(anthropic);

            // 讓 OpenAI 當預設 active
            ProviderProfile p = repo.findByName("OpenAI");
            if (p != null) repo.setActive(p.id());
        }

        // 讀取 active profile，回寫到 AiProperties（讓其他既有邏輯也跟著吃到目前 active 設定）
        ProviderProfile active = repo.findActive();
        if (active != null) {
            applyProfileToProps(active);
        }
    }

    public List<ProviderProfile> list() {
        repo.ensureSchema();
        return repo.list();
    }

    public ProviderProfile active() {
        repo.ensureSchema();
        return repo.findActive();
    }

    public ProviderProfile findByName(String name) {
        repo.ensureSchema();
        return repo.findByName(name);
    }

    public void saveProfile(ProviderProfileForm form) {
        repo.ensureSchema();
        if (form == null) throw new IllegalArgumentException("Profile 表單為空");
        if (form.getName() == null || form.getName().isBlank()) throw new IllegalArgumentException("Profile Name 必填");
        if (form.getType() == null || form.getType().isBlank()) throw new IllegalArgumentException("Type 必填");
        if (form.getBaseUrl() == null || form.getBaseUrl().isBlank()) throw new IllegalArgumentException("API Base URL 必填");
        if (form.getModel() == null || form.getModel().isBlank()) throw new IllegalArgumentException("Model 必填");

        // 去掉前後空白，避免出現看似有填其實是空白
        form.setName(form.getName().trim());
        form.setType(form.getType().trim());
        form.setBaseUrl(form.getBaseUrl().trim());
        form.setModel(form.getModel().trim());
        if (form.getApiKey() != null) form.setApiKey(form.getApiKey().trim());

        repo.upsert(form);
    }

    public void setActive(long id) {
        repo.ensureSchema();
        repo.setActive(id);
        ProviderProfile active = repo.findActive();
        if (active != null) applyProfileToProps(active);
    }

    public void deleteProfile(long id) {
        repo.ensureSchema();
        ProviderProfile p = repo.findById(id);
        if (p != null && p.active()) {
            throw new IllegalArgumentException("無法刪除正在使用中的 Profile");
        }
        repo.deleteById(id);
    }

    public String maskKey(String apiKey) {
        if (apiKey == null || apiKey.isBlank()) return "(未設定)";
        if (apiKey.length() <= 8) return "********";
        return apiKey.substring(0, 3) + "***" + apiKey.substring(apiKey.length() - 4);
    }

    public void applyProfileToProps(ProviderProfile p) {
        if (p == null) return;
        // 只回寫已知 type，避免把未知 profile type 寫壞
        String type = p.type() == null ? "" : p.type().trim();
        props.setProvider(type);

        if (type.equalsIgnoreCase("openai") || type.equalsIgnoreCase("openaiCompatible")) {
            props.getOpenai().setBaseUrl(p.baseUrl());
            props.getOpenai().setApiKey(p.apiKey());
            if (p.model() != null && !p.model().isBlank()) props.getOpenai().setModel(p.model());
        } else if (type.equalsIgnoreCase("anthropic")) {
            props.getAnthropic().setBaseUrl(p.baseUrl());
            props.getAnthropic().setApiKey(p.apiKey());
            if (p.model() != null && !p.model().isBlank()) props.getAnthropic().setModel(p.model());
        } else if (type.equalsIgnoreCase("anthropicCompatible")) {
            props.getAnthropicCompatible().setBaseUrl(p.baseUrl());
            props.getAnthropicCompatible().setApiKey(p.apiKey());
            if (p.model() != null && !p.model().isBlank()) props.getAnthropicCompatible().setModel(p.model());
        }
    }
}
