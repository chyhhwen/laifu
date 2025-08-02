@param Boolean authed

<div class="flex flex-1 justify-end gap-8">
    <button
        data-dropdown="#menu-dropdown"
        class="cg-menu flex max-w-[480px] items-center justify-center overflow-hidden h-10 !bg-[#2b3036] text-white gap-2 text-sm font-bold leading-normal tracking-[0.015em] min-w-0 px-2.5 btn btn-pill btn-border-0 cg-buttons tippyer"
        data-theme="light-border" data-placement="bottom" data-content="#menu-dropdown" data-htmlable="true"
    >
        <div class="text-white" data-icon="List" data-size="20px" data-weight="regular">
            <svg xmlns="http://www.w3.org/2000/svg" width="20px" height="20px" fill="currentColor"
                 viewBox="0 0 256 256">
                <path
                        d="M224,128a8,8,0,0,1-8,8H40a8,8,0,0,1,0-16H216A8,8,0,0,1,224,128ZM40,72H216a8,8,0,0,0,0-16H40a8,8,0,0,0,0,16ZM216,184H40a8,8,0,0,0,0,16H216a8,8,0,0,0,0-16Z"
                ></path>
            </svg>
        </div>
    </button>
    <div id="menu-dropdown" style="display: none;" class="dropdown z-10 origin-top-right absolute right-0 mt-2 min-w-[160px] rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 hidden">
        <div class="py-1 menuitem-panel" role="menu" aria-orientation="vertical" aria-labelledby="dropdownButton">
            <button class="w-full text-left px-4 py-2 text-sm text-white hover:bg-neutral-600 hover:text-white" role="menuitem">設定</button>
            <button class="w-full text-left px-4 py-2 text-sm text-white hover:bg-neutral-600 hover:text-white" role="menuitem">離開</button>
            <hr>
            @if(authed)
                <button class="w-full text-left px-4 py-2 text-sm text-white hover:bg-neutral-600 hover:text-white" role="menuitem">登出</button>
            @else
                <button class="w-full text-left px-4 py-2 text-sm text-white hover:bg-neutral-600 hover:text-white" role="menuitem">登入</button>
            @endif
        </div>

    </div>
    <div
        class="bg-center bg-no-repeat aspect-square bg-cover rounded-full size-10"
        style='background-image: url("https://lh3.googleusercontent.com/aida-public/AB6AXuDt_W6oqcq-8PADiqpScMJAhaqh_Q6sfy8YtUYqqara_ZbdFgW_yZRHaGP0x5D3p8WV0EJAu3eN2PEJkbOR1ejbBdxAjyn78edD0wcEST1v48_WQKLgAzAc8BKJVBSjYsF14YbgBUS0LpaNgy2WTBHXK7Y9x4dYv9sz8EDycm5KebDBS7YraKPaO2_pmC9PJiN3if_gTvizi_LNFBI0A-Oi_7usjdJKRIzus471UgRLv2Y64XZMSZdEj8Y3ZhxLo0vr7nLqeopKXcI");'
    ></div>
</div>