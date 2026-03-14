package laifu.fu.lai.web;

public class Page {
    private final String title;
    private final Boolean authed;
    private final String user;

    public Page(String title, Boolean authed, String user) {
        this.title = title;
        this.authed = authed;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getAuthed() {
        return authed;
    }

    public String getUser() {
        return user;
    }
}
