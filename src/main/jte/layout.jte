@import gg.jte.Content
@param String title
@param Content content
@param Content[] heads = new gg.jte.Content[]{}
@param Boolean authed

<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.jsdelivr.net/npm/htmx.org@2.0.6/dist/htmx.min.js" integrity="sha384-Akqfrbj/HpNVo8k11SXBb6TlBWmXXlYQrCSqEWmyKJe+hDm3Z/B2WVG4smwBkRVm" crossorigin="anonymous"></script>
    <link href="/output.css" rel="stylesheet">
    @for(var head : heads)
    ${head}
    @endfor
    <title>${title}</title>
</head>
<body>
<div class="window w-96 h-72">
    <!-- 視窗標題欄 -->
    <div class="window-titlebar">
        <div class="window-title">${title}</div>

        <!-- 視窗控制按鈕 -->
        <div class="window-controls">
            <div class="control-btn minimize">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M4 10a1 1 0 011-1h10a1 1 0 110 2H5a1 1 0 01-1-1z" clip-rule="evenodd" />
                </svg>
            </div>
            <div class="control-btn maximize">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M4 4a1 1 0 011-1h10a1 1 0 011 1v10a1 1 0 01-1 1H5a1 1 0 01-1-1V4zm1 0v10h10V4H5z" clip-rule="evenodd" />
                </svg>
            </div>
            <div class="control-btn close">
                <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
                </svg>
            </div>
        </div>
    </div>

    <!-- 視窗內容區 -->
    <div class="window-content">
        <div class="relative flex size-full min-h-screen flex-col bg-[#121417] dark group/design-root overflow-x-hidden"
             style='font-family: Manrope, "Noto Sans", sans-serif;'>
            <div class="layout-container flex h-full grow flex-col">
                <header class="flex items-center justify-between whitespace-nowrap border-b border-solid border-b-[#2b3036] px-10 py-3">
                    <div class="flex items-center gap-4 text-white">
                        <div class="size-4">
                            <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path fill-rule="evenodd" clip-rule="evenodd"
                                      d="M24 4H6V17.3333V30.6667H24V44H42V30.6667V17.3333H24V4Z" fill="currentColor"></path>
                            </svg>
                        </div>
                        <h2 class="text-white text-lg font-bold leading-tight tracking-[-0.015em]">Fu</h2>
                    </div>
                    @template.menu(authed = authed)
                </header>
                <div class="px-40 flex flex-1 justify-center py-5">
                    <div class="layout-content-container flex flex-col max-w-[960px] flex-1">
                        <div class="flex flex-wrap justify-between gap-3 p-4"><p
                                    class="text-white tracking-light text-[32px] font-bold leading-tight min-w-72">Welcome user!!</p>
                        </div>
                        <div class="grid grid-cols-[repeat(auto-fit,minmax(158px,1fr))] gap-3 p-4">
                            <div class="flex flex-1 gap-3 rounded-lg border border-[#3f4750] bg-[#1d2125] p-4 items-center">
                                <div class="text-white" data-icon="Plus" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor"
                                         viewBox="0 0 256 256">
                                        <path d="M224,128a8,8,0,0,1-8,8H136v80a8,8,0,0,1-16,0V136H40a8,8,0,0,1,0-16h80V40a8,8,0,0,1,16,0v80h80A8,8,0,0,1,224,128Z"></path>
                                    </svg>
                                </div>
                                <h2 class="text-white text-base font-bold leading-tight">Create Service</h2>
                            </div>
                            <div class="flex flex-1 gap-3 rounded-lg border border-[#3f4750] bg-[#1d2125] p-4 items-center">
                                <div class="text-white" data-icon="ListBullets" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor"
                                         viewBox="0 0 256 256">
                                        <path
                                                d="M80,64a8,8,0,0,1,8-8H216a8,8,0,0,1,0,16H88A8,8,0,0,1,80,64Zm136,56H88a8,8,0,0,0,0,16H216a8,8,0,0,0,0-16Zm0,64H88a8,8,0,0,0,0,16H216a8,8,0,0,0,0-16ZM44,52A12,12,0,1,0,56,64,12,12,0,0,0,44,52Zm0,64a12,12,0,1,0,12,12A12,12,0,0,0,44,116Zm0,64a12,12,0,1,0,12,12A12,12,0,0,0,44,180Z"
                                        ></path>
                                    </svg>
                                </div>
                                <h2 class="text-white text-base font-bold leading-tight">Select Service</h2>
                            </div>
                            <div class="flex flex-1 gap-3 rounded-lg border border-[#3f4750] bg-[#1d2125] p-4 items-center">
                                <div class="text-white" data-icon="Clock" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor"
                                         viewBox="0 0 256 256">
                                        <path
                                                d="M128,24A104,104,0,1,0,232,128,104.11,104.11,0,0,0,128,24Zm0,192a88,88,0,1,1,88-88A88.1,88.1,0,0,1,128,216Zm64-88a8,8,0,0,1-8,8H128a8,8,0,0,1-8-8V72a8,8,0,0,1,16,0v48h48A8,8,0,0,1,192,128Z"
                                        ></path>
                                    </svg>
                                </div>
                                <h2 class="text-white text-base font-bold leading-tight">Recent Services</h2>
                            </div>
                            <div class="flex flex-1 gap-3 rounded-lg border border-[#3f4750] bg-[#1d2125] p-4 items-center">
                                <div class="text-white" data-icon="Gear" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor"
                                         viewBox="0 0 256 256">
                                        <path
                                                d="M128,80a48,48,0,1,0,48,48A48.05,48.05,0,0,0,128,80Zm0,80a32,32,0,1,1,32-32A32,32,0,0,1,128,160Zm88-29.84q.06-2.16,0-4.32l14.92-18.64a8,8,0,0,0,1.48-7.06,107.21,107.21,0,0,0-10.88-26.25,8,8,0,0,0-6-3.93l-23.72-2.64q-1.48-1.56-3-3L186,40.54a8,8,0,0,0-3.94-6,107.71,107.71,0,0,0-26.25-10.87,8,8,0,0,0-7.06,1.49L130.16,40Q128,40,125.84,40L107.2,25.11a8,8,0,0,0-7.06-1.48A107.6,107.6,0,0,0,73.89,34.51a8,8,0,0,0-3.93,6L67.32,64.27q-1.56,1.49-3,3L40.54,70a8,8,0,0,0-6,3.94,107.71,107.71,0,0,0-10.87,26.25,8,8,0,0,0,1.49,7.06L40,125.84Q40,128,40,130.16L25.11,148.8a8,8,0,0,0-1.48,7.06,107.21,107.21,0,0,0,10.88,26.25,8,8,0,0,0,6,3.93l23.72,2.64q1.49,1.56,3,3L70,215.46a8,8,0,0,0,3.94,6,107.71,107.71,0,0,0,26.25,10.87,8,8,0,0,0,7.06-1.49L125.84,216q2.16.06,4.32,0l18.64,14.92a8,8,0,0,0,7.06,1.48,107.21,107.21,0,0,0,26.25-10.88,8,8,0,0,0,3.93-6l2.64-23.72q1.56-1.48,3-3L215.46,186a8,8,0,0,0,6-3.94,107.71,107.71,0,0,0,10.87-26.25,8,8,0,0,0-1.49-7.06Zm-16.1-6.5a73.93,73.93,0,0,1,0,8.68,8,8,0,0,0,1.74,5.48l14.19,17.73a91.57,91.57,0,0,1-6.23,15L187,173.11a8,8,0,0,0-5.1,2.64,74.11,74.11,0,0,1-6.14,6.14,8,8,0,0,0-2.64,5.1l-2.51,22.58a91.32,91.32,0,0,1-15,6.23l-17.74-14.19a8,8,0,0,0-5-1.75h-.48a73.93,73.93,0,0,1-8.68,0,8,8,0,0,0-5.48,1.74L100.45,215.8a91.57,91.57,0,0,1-15-6.23L82.89,187a8,8,0,0,0-2.64-5.1,74.11,74.11,0,0,1-6.14-6.14,8,8,0,0,0-5.1-2.64L46.43,170.6a91.32,91.32,0,0,1-6.23-15l14.19-17.74a8,8,0,0,0,1.74-5.48,73.93,73.93,0,0,1,0-8.68,8,8,0,0,0-1.74-5.48L40.2,100.45a91.57,91.57,0,0,1,6.23-15L69,82.89a8,8,0,0,0,5.1-2.64,74.11,74.11,0,0,1,6.14-6.14A8,8,0,0,0,82.89,69L85.4,46.43a91.32,91.32,0,0,1,15-6.23l17.74,14.19a8,8,0,0,0,5.48,1.74,73.93,73.93,0,0,1,8.68,0,8,8,0,0,0,5.48-1.74L155.55,40.2a91.57,91.57,0,0,1,15,6.23L173.11,69a8,8,0,0,0,2.64,5.1,74.11,74.11,0,0,1,6.14,6.14,8,8,0,0,0,5.1,2.64l22.58,2.51a91.32,91.32,0,0,1,6.23,15l-14.19,17.74A8,8,0,0,0,199.87,123.66Z"
                                        ></path>
                                    </svg>
                                </div>
                                <h2 class="text-white text-base font-bold leading-tight">Settings</h2>
                            </div>
                            <div class="flex flex-1 gap-3 rounded-lg border border-[#3f4750] bg-[#1d2125] p-4 items-center">
                                <div class="text-white" data-icon="ShieldCheck" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor"
                                         viewBox="0 0 256 256">
                                        <path
                                                d="M208,40H48A16,16,0,0,0,32,56v58.78c0,89.61,75.82,119.34,91,124.39a15.53,15.53,0,0,0,10,0c15.2-5.05,91-34.78,91-124.39V56A16,16,0,0,0,208,40Zm0,74.79c0,78.42-66.35,104.62-80,109.18-13.53-4.51-80-30.69-80-109.18V56H208ZM82.34,141.66a8,8,0,0,1,11.32-11.32L112,148.68l50.34-50.34a8,8,0,0,1,11.32,11.32l-56,56a8,8,0,0,1-11.32,0Z"
                                        ></path>
                                    </svg>
                                </div>
                                <h2 class="text-white text-base font-bold leading-tight">Privacy</h2>
                            </div>
                            <div class="flex flex-1 gap-3 rounded-lg border border-[#3f4750] bg-[#1d2125] p-4 items-center">
                                <div class="text-white" data-icon="Ticket" data-size="24px" data-weight="regular">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24px" height="24px" fill="currentColor"
                                         viewBox="0 0 256 256">
                                        <path
                                                d="M227.19,104.48A16,16,0,0,0,240,88.81V64a16,16,0,0,0-16-16H32A16,16,0,0,0,16,64V88.81a16,16,0,0,0,12.81,15.67,24,24,0,0,1,0,47A16,16,0,0,0,16,167.19V192a16,16,0,0,0,16,16H224a16,16,0,0,0,16-16V167.19a16,16,0,0,0-12.81-15.67,24,24,0,0,1,0-47ZM32,167.2a40,40,0,0,0,0-78.39V64H88V192H32Zm192,0V192H104V64H224V88.8a40,40,0,0,0,0,78.39Z"
                                        ></path>
                                    </svg>
                                </div>
                                <h2 class="text-white text-base font-bold leading-tight">Support Tickets</h2>
                            </div>
                        </div>
                        <div class="gap-3 p-4">
                            ${content}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- 調整視窗大小的把手 -->
    <div class="resize-handle"></div>
</div>
</body>
</html>