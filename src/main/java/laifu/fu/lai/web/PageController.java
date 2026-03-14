package laifu.fu.lai.web;

import laifu.fu.lai.data.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

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
        // Thymeleaf layout/menu 需要的通用欄位
        model.addAttribute("title", page.getTitle());
        model.addAttribute("authed", page.getAuthed());
        return "chat";
    }
}
