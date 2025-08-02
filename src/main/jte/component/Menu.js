function menu() {
    let menus = document.querySelectorAll('.cg-menu:not(.cg-menu-rendered)');
    for (const menu of menus) {
        menu.classList.add('cg-menu-rendered');
        let dropdown = menu.dataset.dropdown;
        let dropdownElement = document.querySelector(dropdown);
        if(dropdownElement !== null){
            dropdownElement.status = false;
            // menu.addEventListener('onmouseover', () => {
            //     dropdownElement.status = true;
            //     dropdownElement.classList.remove('hidden');
            // });
            // menu.addEventListener('onmouseout', () => {
            //     dropdownElement.status = false;
            //     dropdownElement.classList.add('hidden');
            // });
            // menu.addEventListener('click', () => {
            //     if(dropdownElement.status) {
            //         dropdownElement.status = false;
            //         dropdownElement.classList.add('hidden');
            //     } else {
            //         dropdownElement.status = true;
            //         dropdownElement.classList.remove('hidden');
            //     }
            // });
        }
    }
}

document.addEventListener('DOMContentLoaded', menu);