// tailwind.config.js
module.exports = {
    content: [
        {
            files: './**/*.jte',
            extract: (content) => {
                // 簡單範例, 根據實際使用狀況調整 Regex
                return content.match(/class:?"([^"]+)"/g) || [];
            }
        }
    ],
    theme: {
        extend: {
            colors: {
                'color1': 'rgb(248, 202, 68)',
                'color1-50': '#e6bc40',
                'color2': '#5866a6',
                'color2-50': '#697ac6',
                'color3': '#405ee6',
                'color3-50': '#4768fa',
                'color4': '#918259',
                'color4-50': '#b2a06e',
                'color5': '#505466',
                'color5-50': '#747a94',
                'color6': '#3c3933',
                'color6-50': '#666056',
                'color7': 'var(--color7)',
                'color7-50': 'var(--color7-50)',
                'menu-bar': 'var(--menu-bar)',
                'line-50': '#4CC764',
                'line': '#06C755',
            }
        }
    }
}