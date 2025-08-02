package laifu.fu.lai.component;

import laifu.fu.lai.event.CGEventBus;
import laifu.fu.lai.gui.GuiManager;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * A component with a simple service life cycle that can be managed by
 * {@link GuiManager}. Implementations typically provide a Swing UI component
 * returned by {@link #getComponent()} which can be added to a {@link JFrame}.
 */
public interface CGComponent {
    /** States for the component. */
    enum Status { ENABLED, DISABLED, ERROR }

    /** Called when the component should attach itself to the provided frame. */
    void onEnable(JFrame frame);

    /** Called when the component should clean up resources. */
    void onDisable();

    /** Called when the component should reload its state. */
    void onReload();

    /** Called when an error occurred during a lifecycle phase. */
    void onError(Throwable throwable);

    /**
     * @return the event bus that manages listeners for this component
     */
    CGEventBus getEventBus();

    /**
     * Register a listener for the given event name.
     */
    default void on(String event, Consumer<Object[]> listener) {
        getEventBus().on(event, listener);
    }

    /**
     * Trigger an event with optional arguments.
     */
    default void trigger(String event, Object... args) {
        getEventBus().trigger(event, args);
    }

    /**
     * @return the Swing component to add to the frame, or {@code null} if none
     */
    Component getComponent();

    /**
     * @return the current lifecycle status
     */
    Status getStatus();
}
