package laifu.fu.lai.ai.web;

import laifu.fu.lai.ai.config.ProviderProfile;
import laifu.fu.lai.ai.config.ProviderProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai/profiles")
public class ProviderProfileController {
    private final ProviderProfileService service;

    public ProviderProfileController(ProviderProfileService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProviderProfile> list() {
        return service.list();
    }

    @GetMapping("/active")
    public ProviderProfile active() {
        return service.active();
    }

    @PostMapping("/{id}/activate")
    public void activate(@PathVariable long id) {
        service.setActive(id);
    }
}
