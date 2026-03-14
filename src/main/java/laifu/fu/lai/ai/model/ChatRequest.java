package laifu.fu.lai.ai.model;

import java.util.List;

public record ChatRequest(
        String provider,
        List<ChatMessage> messages
) {
}
