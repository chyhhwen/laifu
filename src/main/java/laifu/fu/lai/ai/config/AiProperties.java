package laifu.fu.lai.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ai")
public class AiProperties {
    /** 預設 provider：openai | anthropic | openaiCompatible */
    private String provider = "openai";

    private final OpenAi openai = new OpenAi();
    private final Anthropic anthropic = new Anthropic();
    private final AnthropicCompatible anthropicCompatible = new AnthropicCompatible();

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public OpenAi getOpenai() {
        return openai;
    }

    public Anthropic getAnthropic() {
        return anthropic;
    }

    public AnthropicCompatible getAnthropicCompatible() {
        return anthropicCompatible;
    }

    public static class OpenAi {
        /** 例如：https://api.openai.com（或 Jan 的 OpenAI 相容 baseUrl） */
        private String baseUrl = "https://api.openai.com";
        private String apiKey;
        private String model = "gpt-4o-mini";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }

    public static class Anthropic {
        private String baseUrl = "https://api.anthropic.com";
        private String apiKey;
        private String model = "claude-3-5-sonnet-latest";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }

    public static class AnthropicCompatible {
        /** 例如：自架/第三方 Claude 相容端點（Anthropic Messages API 相容） */
        private String baseUrl;
        private String apiKey;
        private String model = "claude-3-5-sonnet-latest";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }
    }
}
