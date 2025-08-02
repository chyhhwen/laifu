import * as LZString from "lz-string";

/**
 * 顯示確認對話框。
 *
 * @param {string} title 對話框的標題。
 * @param {string} message 對話框的消息內容。
 * @param {string} type 對話框的類型，可選值包括"alert", "warning", "error", "danger", "success", "ok", "color7", "normal", "info"。
 * @param {Function} ok 確認按鈕點擊時的回調函數。
 * @param {Function} cancel 取消按鈕點擊時的回調函數。
 * @return {string} 生成的對話框的唯一ID。
 */
export function confirmDialog(title, message, type, ok, cancel) {
    let dialog = document.createElement("div");
    dialog.id = "dialog_" + generateRandomString(5);
    dialog.classList.add("dialog-message-box");
    document.body.appendChild(dialog);
    document.body.style.overflow = "hidden";
    switch (type) {
        case "alert":
        case "warning":
            dialog.classList.add("dialog-message-box-warning");
            break;
        case "error":
        case "danger":
            dialog.classList.add("dialog-message-box-error");
            break;
        case "success":
        case "ok":
            dialog.classList.add("dialog-message-box-success");
            break;
        case "color7":
            dialog.classList.add("dialog-message-box-color7");
            break;
        case "normal":
        case "info":
            dialog.classList.add("dialog-message-box-info");
            break;
        default:
            dialog.classList.add("dialog-message-box-" + type);
            break;
    }
    let bodycolorEl = document.createElement("div");
    bodycolorEl.classList.add('dialog-message-box-body-color');
    dialog.append(bodycolorEl);
    let body2El = document.createElement("div");
    body2El.classList.add('dialog-message-box-body');
    bodycolorEl.append(body2El);

    let titleEl = document.createElement("div");
    titleEl.classList.add('dialog-message-box-title');
    titleEl.innerText = title;
    body2El.append(titleEl);

    let contentEl = document.createElement("div");
    contentEl.classList.add('dialog-message-box-content');
    contentEl.innerText = message;
    body2El.append(contentEl);

    let buttonFlexGroup = document.createElement("div");
    buttonFlexGroup.classList.add('dialog-message-box-buttonFlexGroup');
    body2El.append(buttonFlexGroup);

    let confirmEl = document.createElement("button");
    confirmEl.classList.add("dialog-message-box-confirm", "btn", "btn-ripple", "btn-md-strip");
    confirmEl.type = 'button';
    confirmEl.innerText = "確認";
    confirmEl.onclick = () => {
        dialog.remove();
        document.body.style.overflow = "";
        ok();
    };
    buttonFlexGroup.append(confirmEl);

    let cancelEl = document.createElement("button");
    cancelEl.classList.add("dialog-message-box-cancel", "btn", "btn-ripple", "btn-ok", "btn-md-strip");
    cancelEl.type = 'button';
    cancelEl.innerText = "取消";
    cancelEl.onclick = () => {
        dialog.remove();
        document.body.style.overflow = "";
        cancel();
    };
    buttonFlexGroup.append(cancelEl);

    document.dispatchEvent(new CustomEvent('BtnLoad'));
    return dialog.id;
}

/**
 * 生成指定長度的隨機字符串。
 *
 * @param {number} length - 要生成的字符串的長度。
 * @return {string} 生成的隨機字符串。
 */
export function generateRandomString(length) {
    const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    let result = '';
    const charactersLength = characters.length;
    for (let i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

/**
 * 生成範圍內（包括起點和終點）的隨機數字數列。
 *
 * @param {number} rangeStart - 數字範圍的起始值。
 * @param {number} rangeEnd - 數字範圍的結束值。
 * @param {number} numNumbers - 需要生成的隨機數字的數量。
 * @return {number[]} 生成的隨機數字數列。
 * @throws {Error} 如果請求的數字數量超過範圍內可用的數字，拋出錯誤信息。
 */
export function generateRandomNumbers(rangeStart, rangeEnd, numNumbers) {
    if (numNumbers > (rangeEnd - rangeStart + 1)) {
        throw new Error("Number of requested numbers exceeds range");
    }

    const randomNumbers = [];
    const availableNumbers = Array.from({length: rangeEnd - rangeStart + 1}, (_, i) => i + rangeStart);

    for (let i = 0; i < numNumbers; i++) {
        const randomIndex = Math.floor(Math.random() * availableNumbers.length);
        const randomNumber = availableNumbers[randomIndex];
        randomNumbers.push(randomNumber);

        // Remove the selected number from the available array
        availableNumbers.splice(randomIndex, 1);
    }

    return randomNumbers;
}

/**
 * 將字符串中的字符位置進行交換。
 *
 * @param {string} str - 要進行操作的字符串
 * @param {number} index - 要交換的第一個字符的位置
 * @param {number} shift_index - 要交換的第二個字符的位置
 * @return {Object} 返回一個包含新字符串、第一個位置和第二個位置的對象
 * @throws {Error} 當 `index` 或 `shift_index` 超出字符串範圍時拋出錯誤
 */
export function string_move_shift(str, index, shift_index) {
    if (index < 0 || index >= str.length || shift_index < 0 || shift_index >= str.length) {
        throw new Error("Invalid indices");
    }

    const chars = str.split("");
    const temp = chars[index];
    chars[index] = chars[shift_index];
    chars[shift_index] = temp;

    const newStr = chars.join("");
    return {
        str: newStr, index: index, shift: shift_index,
    };
}


/**
 * 將給定的文本進行HTML編碼，以確保在網頁中安全顯示。
 * 此方法會轉換特定的字符為HTML實體, 例如將 '<' 轉換為 '&lt;'
 *
 * @param {string} txt - 需要進行HTML編碼的字符串
 * @return {string} - 經過HTML編碼處理後的字符串
 */
export function htmlencode(txt) {
    var div = document.createElement("div");
    div.appendChild(document.createTextNode(txt));
    return div.innerHTML;
}

/**
 * 將 HTML 編碼的字串解碼回普通文字。
 *
 * @param {string} txt - 要解碼的 HTML 字串。
 * @return {string} 返回解碼後的普通文字。
 */
export function htmldecode(txt) {
    var div = document.createElement("div");
    div.innerHTML = txt;
    return div.innerText || div.textContent;
}

/**
 * 根據滑鼠事件的位置旋轉元素。
 *
 * @param {MouseEvent} event - 滑鼠事件，用於取得滑鼠的位置。
 * @param {HTMLElement} element - 需要旋轉的 HTML 元素。
 * @return {void} 不返回任何值。
 */
export function rotateElement(event, element) {
    // get mouse position
    const x = event.clientX;
    const y = event.clientY;
    // console.log(x, y)

    // find the middle
    const middleX = window.innerWidth / 2;
    const middleY = window.innerHeight / 2;
    // console.log(middleX, middleY)

    // get offset from middle as a percentage
    // and tone it down a little
    const offsetX = ((x - middleX) / middleX) * 10;
    const offsetY = ((y - middleY) / middleY) * 10;
    // console.log(offsetX, offsetY);

    // set rotation
    element.style.setProperty("--rotateX", offsetX + "deg");
    element.style.setProperty("--rotateY", -1 * offsetY + "deg");
}

/**
 * 驗證給定的電子郵件地址是否符合標準格式。
 *
 * @param {string} email - 要驗證的電子郵件地址。
 * @return {boolean} 如果電子郵件地址符合正則表示式，回傳 true；否則回傳 false。
 */
export function validateEmail(email) {
    // 定義正則表達式
    var regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    // 測試信箱字串是否符合正則表達式
    return regex.test(email);
}

/**
 * 驗證電話號碼是否有效
 *
 * @param {string} phone - 要驗證的電話號碼
 * @return {boolean} 如果電話號碼有效則返回true，否則返回false
 */
export function validatePhone(phone) {
    var regex = /^\d{10,}$/;
    return regex.test(phone);
}


/**
 * 獲取客戶端指紋信息。
 *
 * 該方法通過多種技術手段(如Canvas Fingerprinting、WebGL Fingerprinting、ClientRects Fingerprinting等)
 * 收集客戶端設備及瀏覽器的各種信息，以期生成一個唯一的指紋標識來識別不同的客戶端。
 *
 * @return {Promise<string>} 返回壓縮後的客戶端指紋信息。
 */
export async function getClientFingerprint() {
    const fingerprintComponents = [];

    // Canvas Fingerprint
    function getCanvasFingerprint() {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        ctx.textBaseline = 'top';
        ctx.font = '14px Arial';
        ctx.fillStyle = '#f60';
        ctx.fillRect(125, 1, 62, 20);
        ctx.fillStyle = '#069';
        ctx.fillText('Hello, world!', 2, 15);
        ctx.fillStyle = 'rgba(102, 204, 0, 0.7)';
        ctx.fillText('Hello, world!', 4, 17);
        return canvas.toDataURL();
    }

    fingerprintComponents.push(getCanvasFingerprint());

    // WebGL Fingerprint
    function getWebGLFingerprint() {
        const canvas = document.createElement('canvas');
        const gl = canvas.getContext('webgl') || canvas.getContext('experimental-webgl');
        if (!gl) return null;
        const debugInfo = gl.getExtension('WEBGL_debug_renderer_info');
        return gl.getParameter(debugInfo.UNMASKED_VENDOR_WEBGL) + '~' + gl.getParameter(debugInfo.UNMASKED_RENDERER_WEBGL);
    }

    fingerprintComponents.push(getWebGLFingerprint());

    // ClientRects Fingerprint
    function getClientRectsFingerprint() {
        const div = document.createElement('div');
        div.style.cssText = 'width: 100px; height: 100px; overflow: scroll; position: absolute; top: -9999px;';
        document.body.appendChild(div);
        const rects = div.getClientRects();
        document.body.removeChild(div);
        return JSON.stringify(rects);
    }

    fingerprintComponents.push(getClientRectsFingerprint());

    // Fonts Fingerprint
    function getFontsFingerprint() {
        const fonts = ['Arial', 'Verdana', 'Times New Roman', 'Courier New', 'Courier', 'Helvetica', 'Garamond', 'Georgia', 'Palatino'];
        const detectedFonts = [];
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        ctx.textBaseline = 'top';
        ctx.font = '72px monospace';
        const reference = ctx.measureText('mmmmmmmmmmmmmlli').width;
        fonts.forEach(font => {
            ctx.font = `72px ${font}, monospace`;
            const width = ctx.measureText('mmmmmmmmmmmmmlli').width;
            if (width !== reference) {
                detectedFonts.push(font);
            }
        });
        return detectedFonts.join(',');
    }

    fingerprintComponents.push(getFontsFingerprint());

    // Navigator Fingerprint
    function getNavigatorFingerprint() {
        return JSON.stringify({
            userAgent: navigator.userAgent,
            language: navigator.language,
            platform: navigator.platform,
            hardwareConcurrency: navigator.hardwareConcurrency,
            deviceMemory: navigator.deviceMemory
        });
    }

    fingerprintComponents.push(getNavigatorFingerprint());

    // Timezone Fingerprint
    function getTimezoneFingerprint() {
        return Intl.DateTimeFormat().resolvedOptions().timeZone;
    }

    fingerprintComponents.push(getTimezoneFingerprint());

    // Clipboard Fingerprint
    //async function getClipboardFingerprint() {
    //    try {
    //        const text = await navigator.clipboard.readText();
    //        return text;
    //    } catch (e) {
    //        return '';
    //    }
    //}
    //fingerprintComponents.push(await getClipboardFingerprint());

    // Random Fingerprint
    //function getRandomFingerprint() {
    //    return Math.random().toString(36).substring(2);
    //}
    //fingerprintComponents.push(getRandomFingerprint());

    const fingerprint = fingerprintComponents.join('~');
    let encodeContext1 = encodeContext(fingerprint);
    console.log(fingerprint);
    let encodeContextElement = encodeContext1['compress'];
    return encodeContextElement;
}
