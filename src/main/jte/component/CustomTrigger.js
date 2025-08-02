function customTrigger() {
    var cts = document.querySelectorAll('.ct:not(.ct-rendered)');
    for (let ct of cts) {
        //console.log(ct)
        if (ct.dataset.fn !== null && ct.dataset.target !== null) {
            let target = ct.dataset.target;
            //console.log(target)
            ct.classList.add("ct-rendered")
            switch (ct.dataset.fn) {
                default:
                    ct.classList.remove("ct-rendered");
                    break;
            }
        }
    }
}

document.addEventListener("CGCT::init", customTrigger);
document.addEventListener('DOMContentLoaded', customTrigger);