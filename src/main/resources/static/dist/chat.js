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

  async function refreshProfiles() {
    const sel = el("provider");
    if (!sel) return;

    const resp = await fetch("/api/ai/profiles", { method: "GET", credentials: "same-origin" });
    if (!resp.ok) return;

    const profiles = await resp.json();

    // 清空 options（不用 innerHTML，避免 XSS/被安全 hook 擋）
    while (sel.firstChild) sel.removeChild(sel.firstChild);

    for (const p of profiles) {
      const opt = document.createElement("option");
      opt.value = p.name;
      opt.textContent = p.name + (p.active ? " (active)" : "");
      if (p.active) opt.selected = true;
      sel.appendChild(opt);
    }
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
      body: JSON.stringify({ provider: provider, messages: [{ role: "user", content: msg }], params: {}, headers: {} }),
    });

    // 若你在 Settings 切換了 active profile，可以手動刷新：
    // await refreshProfiles();
    //（此處不自動刷新以減少額外請求）


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

    refreshProfiles().catch(() => {});

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
