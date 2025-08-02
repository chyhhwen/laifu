package laifu.fu.lai.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefContextMenuParams;
import org.cef.callback.CefMenuModel;
import org.cef.handler.CefContextMenuHandlerAdapter;

public class MenuHandler extends CefContextMenuHandlerAdapter
{
    private static final int MENU_ID_RELOAD = 10001;


    public MenuHandler() {
    }

    @Override
    public void onBeforeContextMenu(CefBrowser browser, CefFrame frame, CefContextMenuParams params, CefMenuModel model) {
        model.addItem(MENU_ID_RELOAD, "重新整理頁面"); // 新增這行
    }


    @Override
    public boolean onContextMenuCommand(CefBrowser browser, CefFrame frame, CefContextMenuParams params, int commandId, int eventFlags) {
        switch (commandId) {
            case MENU_ID_RELOAD:
                browser.reload(); // 執行重新整理
                return true;
        }
        return false;
    }

}
