package laifu.fu.lai.ai.provider;

import laifu.fu.lai.ai.model.ChatRequest;

public interface ChatProvider {
    String name();

    String chat(ChatRequest request);
}
