package laifu.fu.lai.web;

import laifu.fu.lai.ai.config.AiSettingsForm;
import laifu.fu.lai.ai.config.AiSettingsService;
import laifu.fu.lai.ai.config.ProviderProfile;
import laifu.fu.lai.ai.config.ProviderProfileForm;
import laifu.fu.lai.ai.config.ProviderProfileService;
import laifu.fu.lai.data.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static java.util.Objects.requireNonNull;

@Controller
public class PageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PageController.class);

    private final AiSettingsService aiSettingsService;
    private final ProviderProfileService providerProfileService;

    public PageController(AiSettingsService aiSettingsService, ProviderProfileService providerProfileService) {
        this.aiSettingsService = aiSettingsService;
        this.providerProfileService = providerProfileService;
    }

    @GetMapping("/chat")
    public String chat(Model model) {
        Page page = new Page(
                "AI Chat Hub",
                "guest",
                "",
                false,
                ""
        );
        model.addAttribute("page", page);
        model.addAttribute("title", page.getTitle());
        model.addAttribute("authed", page.getAuthed());
        return "chat";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        Page page = new Page(
                "設定",
                "guest",
                "",
                false,
                ""
        );
        model.addAttribute("page", page);
        model.addAttribute("title", page.getTitle());
        model.addAttribute("authed", page.getAuthed());

        ProviderProfile active = providerProfileService.active();
        model.addAttribute("active", active);
        model.addAttribute("activeApiKeyMasked", providerProfileService.maskKey(active == null ? null : active.apiKey()));

        model.addAttribute("profiles", providerProfileService.list());
        model.addAttribute("profileForm", new ProviderProfileForm());

        // 舊版 key-value 設定表單仍保留（避免你現有資料直接消失）
        model.addAttribute("form", aiSettingsService.getForm());
        return "settings";
    }

    @PostMapping("/settings")
    public String saveSettings(AiSettingsForm form, RedirectAttributes ra) {
        try {
            aiSettingsService.apply(form);
            ra.addFlashAttribute("saved", true);
        } catch (Exception e) {
            LOGGER.warn("Failed to save AI settings", e);
            ra.addFlashAttribute("error", "儲存設定失敗：" + (e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
        }
        return "redirect:/settings";
    }

    @PostMapping("/settings/profiles")
    public String saveProfile(ProviderProfileForm profileForm, RedirectAttributes ra) {
        if (profileForm.getName() == null || profileForm.getName().isBlank()) {
            ra.addFlashAttribute("error", "Profile 名稱不能為空");
            return "redirect:/settings";
        }

        try {
            providerProfileService.saveProfile(profileForm);
            ra.addFlashAttribute("saved", true);
        } catch (Exception e) {
            LOGGER.warn("Failed to save provider profile", e);
            ra.addFlashAttribute("error", "儲存 Profile 失敗：" + (e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
        }
        return "redirect:/settings";
    }

    @PostMapping("/settings/profiles/activate")
    public String activateProfile(long id, RedirectAttributes ra) {
        try {
            providerProfileService.setActive(id);
            ra.addFlashAttribute("saved", true);
        } catch (Exception e) {
            LOGGER.warn("Failed to activate provider profile id={}", id, e);
            ra.addFlashAttribute("error", "切換 Profile 失敗：" + (e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
        }
        return "redirect:/settings";
    }

    @PostMapping("/settings/profiles/delete")
    public String deleteProfile(@RequestParam("id") long id, RedirectAttributes ra) {
        try {
            providerProfileService.deleteProfile(id);
            ra.addFlashAttribute("saved", true);
        } catch (Exception e) {
            LOGGER.warn("Failed to delete provider profile id={}", id, e);
            ra.addFlashAttribute("error", "刪除 Profile 失敗：" + (e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()));
        }
        return "redirect:/settings";
    }

    @GetMapping("/login")
    public String login(Model model) {
        Page page = new Page(
                "登入",
                "guest",
                "",
                false,
                ""
        );
        model.addAttribute("page", page);
        model.addAttribute("title", page.getTitle());
        model.addAttribute("authed", page.getAuthed());
        return "login";
    }
}
