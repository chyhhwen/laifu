package laifu.fu.lai.controller

import laifu.fu.lai.data.Page
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class HomeController {

    @GetMapping("/")
    fun index(model: Model): String {
        val page = Page(
            title = "AI Chat Hub",
            user = "cream god",
            description = "這是說明內容",
            authed = false,
            content = ""
        )
        model.addAttribute("page", page)
        // Thymeleaf layout/menu 需要的通用欄位
        model.addAttribute("title", page.title)
        model.addAttribute("authed", page.authed)
        return "index"  // 對應到 /templates/index.html
    }
}
