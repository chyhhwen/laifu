package laifu.fu.lai.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Simple event bus for CGComponent implementations.
 */
public class CGEventBus {
    private final Map<String, List<Consumer<Object[]>>> listeners = new ConcurrentHashMap<>();

    /** Register a listener for the given event name. */
    public void on(String event, Consumer<Object[]> listener) {
        listeners.computeIfAbsent(event, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    /** Trigger an event with optional arguments. */
    public void trigger(String event, Object... args) {
        for (Consumer<Object[]> listener : listeners.getOrDefault(event, List.of())) {
            listener.accept(args);
        }
    }
}
