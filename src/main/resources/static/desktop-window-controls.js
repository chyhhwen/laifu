// Desktop window controls bridge (HTMX -> Spring -> Swing/JCEF)
// Keep it minimal: just intercept clicks and send POST requests.

(function () {
  function post(url) {
    fetch(url, { method: 'POST', credentials: 'same-origin' }).catch(() => {});
  }

  document.addEventListener('click', function (e) {
    const btn = e.target.closest('.control-btn');
    if (!btn) return;

    if (btn.classList.contains('minimize')) {
      e.preventDefault();
      post('/api/desktop/window/minimize');
    } else if (btn.classList.contains('maximize')) {
      e.preventDefault();
      post('/api/desktop/window/maximize-toggle');
    } else if (btn.classList.contains('close')) {
      e.preventDefault();
      post('/api/desktop/window/close');
    }
  });

  // DevTools hotkey: Cmd+Opt+I (mac) / Ctrl+Shift+I (win/linux)
  document.addEventListener('keydown', function (e) {
    const mac = navigator.platform.toUpperCase().indexOf('MAC') >= 0;
    const open = (mac && e.metaKey && e.altKey && (e.key === 'i' || e.key === 'I')) ||
                 (!mac && e.ctrlKey && e.shiftKey && (e.key === 'i' || e.key === 'I'));
    if (!open) return;
    e.preventDefault();
    post('/api/desktop/window/devtools');
  });
})();
