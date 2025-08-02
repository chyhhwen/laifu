import tippy from "tippy.js";

/**
 * 初始化頁面上所有帶有 `.tippyer` 類別的元素，用 `tippy.js` 套件加載提示工具（Tooltip）。
 * 依據 data-* 屬性設定每個元素的提示工具參數。
 *
 * @return {boolean} 若某些必需參數（content、placement、trigger）缺失，則返回 `false`。
 */
function tippyLoader() {
    let tippyer = document.querySelectorAll(".tippyer:not(.tippy-loaded)");
    for (let tippyerEl of tippyer) {
        tippyerEl.classList.add("tippy-loaded");
        // settings [source, default value]
        let settings = {
            htmlable: [tippyerEl.dataset.htmlable, false],
            arrow: [tippyerEl.dataset.arrow, true],
            delay: [tippyerEl.dataset.delay, 200],
            delayForShow: [tippyerEl.dataset.delayshow, undefined],
            delayForHidden: [tippyerEl.dataset.delayhide, undefined],
            content: [tippyerEl.dataset.content, undefined], //allow target, text,
            animation: [tippyerEl.dataset.animation, false],
            placement: [tippyerEl.dataset.placement, 'top'],
            zIndex: [tippyerEl.dataset.zindex, 50],
            trigger: [tippyerEl.dataset.trigger, "mouseenter focus"],
            interactive: [tippyerEl.dataset.interactive, true],
            offset: [tippyerEl.dataset.offset, [0, 0]],
            offsetX: [tippyerEl.dataset.offsetx, 0],
            offsetY: [tippyerEl.dataset.offsety, 0],
            theme: [tippyerEl.dataset.theme, "material"],
            touch: [tippyerEl.dataset.touch, true],
        };
        let denyEmptys = [settings.content, settings.placement, settings.trigger];

        for (let denyEmpty of denyEmptys) {
            if (denyEmpty === undefined) return false;
        }

        for (let settingsKey in settings) {
            if (settingsKey === "delay" || settingsKey === "delayForShow" || settingsKey === "delayForHidden") continue;
            if (settingsKey === "offset" || settingsKey === "offsetX" || settingsKey === "offsetY") continue;
            if (settings[settingsKey][0] === undefined) settings[settingsKey][0] = settings[settingsKey][1];
        }

        let delayOptions;
        if (settings.delay[0] !== undefined) {
            delayOptions = parseInt(settings.delay[0]);
        } else if (settings.delayForShow[0] !== undefined && settings.delayForHidden[0] !== undefined) {
            delayOptions = [parseInt(settings.delayForShow[0]), parseInt(settings.delayForHidden[0])];
        } else if (settings.delayForHidden[0] !== undefined) {
            delayOptions = [0, parseInt(settings.delayForHidden[0])];
        } else if (settings.delayForShow[0] !== undefined) {
            delayOptions = [parseInt(settings.delayForShow[0]), 0];
        }
        let offsetOption;
        if (settings.offset[0] !== undefined) {
            let split = settings.offset[0].split(',');
            for (let splitKey in split) {
                split[splitKey] = parseInt(split[splitKey]);
            }
            if (Array.isArray(split) && split > 1) {
                offsetOption = split;
            }
        } else if (settings.offsetX[0] !== undefined && settings.offsetY[0] !== undefined) {
            offsetOption = [parseInt(settings.offsetX[0]), parseInt(settings.offsetY[0])];
        } else if (settings.offsetX[0] !== undefined) {
            offsetOption = [parseInt(settings.offsetX[0]), 0];
        } else if (settings.offsetY[0] !== undefined) {
            offsetOption = [0, parseInt(settings.offsetY[0])];
        }
        if (typeof settings.content[0] === "string") {
            if (settings.content[0].startsWith("#")) {
                let element = document.querySelector(settings.content[0]);
                if (element !== null) {
                    settings.content[0] = element.innerHTML;
                }
            }
        }


        let options = {
            allowHTML: settings.htmlable[0],
            arrow: settings.arrow[0],
            content: settings.content[0],
            animation: settings.animation[0],
            placement: settings.placement[0],
            zIndex: settings.zIndex[0],
            trigger: settings.trigger[0],
            interactive: settings.interactive[0],
            theme: settings.theme[0],
            touch: settings.touch[0],
            onShow() {
                document.dispatchEvent(new CustomEvent('BtnLoad'));
            },
            onShown() {
                document.dispatchEvent(new CustomEvent('BtnLoad'));
            },
            onAfterUpdate() {
                document.dispatchEvent(new CustomEvent('BtnLoad'));
            },
        };

        if (delayOptions !== null) {
            options['delay'] = delayOptions;
        }
        if (offsetOption !== null) {
            options['offset'] = offsetOption;
        }

        tippyerEl.tippy = tippy(tippyerEl, options);
    }
}

document.addEventListener('CGTIPPYER::init', tippyLoader);
document.addEventListener('DOMContentLoaded', tippyLoader);
