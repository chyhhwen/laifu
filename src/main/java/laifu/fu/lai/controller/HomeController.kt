package laifu.fu.lai.controller

import laifu.fu.lai.data.Page
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.File


@Controller
class HomeController {

    @ResponseBody
    @GetMapping("/output.css")
    fun outputCss(): String {
        return File("src/main/jte/output.css").readText()
    }

    @ResponseBody
    @GetMapping("/dist/bundle.js")
    fun outputDistBundle(): String {
        return File("src/main/jte/dist/bundle.js").readText()
    }

    @GetMapping("/")
    fun index(model: Model): String {
        val page = Page(
            title = "JTE 測試",
            user = "cream god",
            description = "這是說明內容",
            authed = false,
            content = ""
        )
        model.addAttribute("page", page) // ✅ 這一行很重要
        return "index"  // 對應到 /templates/index.jte 或 .html
    }
}