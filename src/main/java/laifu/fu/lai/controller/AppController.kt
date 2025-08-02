package laifu.fu.lai.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import kotlin.system.exitProcess

@Controller
class AppController {
    @GetMapping("/app/shutdown")
    fun appShutdown(): String {
        exitProcess(0);
    }
}