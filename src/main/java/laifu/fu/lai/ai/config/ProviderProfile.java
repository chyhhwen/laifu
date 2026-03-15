package laifu.fu.lai.ai.config;

public record ProviderProfile(
        long id,
        String name,
        String type,
        String baseUrl,
        String apiKey,
        String model,
        boolean active
) {
}
