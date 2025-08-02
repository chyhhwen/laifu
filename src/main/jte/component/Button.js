function ripple() {
    let elementNodeListOf = document.querySelectorAll('.cg-buttons');
    for (let elementNodeListOfElement of elementNodeListOf) {
        //button.addEventListener('click', createRipple);
        if (!elementNodeListOfElement.classList.contains('cg-buttons-rippled')) {
            elementNodeListOfElement.classList.add('cg-buttons-rippled');
            elementNodeListOfElement.onmouseup = createRipple;
        }
    }
    //console.log('ripper')
}

function createRipple(event) {
    //console.log(event)
    const button = event.currentTarget;

    const circle = document.createElement("span");
    let b = button.getBoundingClientRect();
    const diameter = Math.max(button.clientWidth, button.clientHeight);
    const radius = diameter / 2;

    circle.style.width = circle.style.height = `${diameter}px`;
    circle.style.left = `${event.clientX - b.x - radius}px`;
    circle.style.top = `${event.clientY - b.y - radius}px`;
    circle.classList.add("ripple");

    setTimeout(() => {
        circle.remove();
    }, 600)

    button.appendChild(circle);
}

function createRipple2(button) {
    const circle = document.createElement("span");
    let b = button.getBoundingClientRect();
    const diameter = Math.max(button.clientWidth, button.clientHeight);
    const radius = diameter / 2;

    circle.style.width = circle.style.height = `${diameter}px`;
    circle.style.left = `${event.clientX - b.x - radius}px`;
    circle.style.top = `${event.clientY - b.y - radius}px`;
    circle.classList.add("ripple");

    setTimeout(() => {
        circle.remove();
    }, 600)

    button.appendChild(circle);
}

document.addEventListener('CG::BtnRipple', (event) => {
    createRipple2(event.detail);
});

document.addEventListener('BtnLoad', () => {
    ripple();
    //console.log('BtnLoad');
});

document.addEventListener('DOMContentLoaded', ripple);