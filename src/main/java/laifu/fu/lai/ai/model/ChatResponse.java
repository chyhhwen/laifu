package laifu.fu.lai.ai.model;

public record ChatResponse(
        String provider,
        String content,
        String errorCode,
        String errorMessage
) {
    public static ChatResponse ok(String provider, String content) {
        return new ChatResponse(provider, content, null, null);
    }

    public static ChatResponse error(String provider, String errorCode, String errorMessage) {
        return new ChatResponse(provider, null, errorCode, errorMessage);
    }
}
