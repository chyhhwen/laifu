(function () {
  function el(id) {
    return document.getElementById(id);
  }

  function appendLine(text) {
    const log = el("log");
    const div = document.createElement("div");
    div.style.whiteSpace = "pre-wrap";
    div.textContent = text;
    log.appendChild(div);
    log.scrollTop = log.scrollHeight;
  }

  async function send() {
    const provider = el("provider").value;
    const msg = el("msg").value.trim();
    if (!msg) return;

    el("msg").value = "";
    appendLine("you: " + msg);

    const resp = await fetch("/api/ai/chat", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ provider: provider, messages: [{ role: "user", content: msg }] }),
    });

    const data = await resp.json().catch(async () => ({ errorMessage: await resp.text() }));

    if (!resp.ok) {
      const code = data.errorCode || "ERROR";
      const msgText = data.errorMessage || "請求失敗";
      appendLine("error(" + code + "): " + msgText);
      return;
    }

    if (data.errorCode || data.errorMessage) {
      appendLine("error(" + (data.errorCode || "ERROR") + "): " + (data.errorMessage || ""));
      return;
    }

    appendLine((data.provider || provider) + ": " + (data.content || ""));
  }

  function bind() {
    const btn = el("send");
    const input = el("msg");
    if (!btn || !input) return;

    btn.addEventListener("click", send);
    input.addEventListener("keydown", function (e) {
      if (e.key === "Enter") send();
    });
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", bind);
  } else {
    bind();
  }
})();
