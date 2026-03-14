package laifu.fu.lai.desktop.web;

import laifu.fu.lai.desktop.DesktopWindowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/desktop/window")
public class DesktopWindowController {
    private final DesktopWindowService desktopWindowService;

    public DesktopWindowController(DesktopWindowService desktopWindowService) {
        this.desktopWindowService = desktopWindowService;
    }

    @PostMapping("/minimize")
    public ResponseEntity<Void> minimize() {
        desktopWindowService.minimize();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/maximize-toggle")
    public ResponseEntity<Void> toggleMaximize() {
        desktopWindowService.toggleMaximize();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/close")
    public ResponseEntity<Void> close() {
        desktopWindowService.close();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/devtools")
    public ResponseEntity<Void> devTools() {
        desktopWindowService.openDevTools();
        return ResponseEntity.noContent().build();
    }
}
