package laifu.fu.lai.data

data class Page(
    val title: String,
    val user: String,
    val description: String,
    val authed: Boolean = false,
    val content: String = ""
)
