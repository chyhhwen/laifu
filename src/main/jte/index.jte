@import laifu.fu.lai.data.Page
@import gg.jte.Content
@param Page page

@template.layout(title = page.getTitle(), authed = page.getAuthed(), heads = new Content[]{@`<script src="/dist/bundle.js"></script>`}, content = @`Welcome, ${page.getUser()}.`)