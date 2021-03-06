
package com.ponysdk.ui.server.list2.selector;

import com.ponysdk.core.tools.ListenerCollection;
import com.ponysdk.impl.theme.PonySDKTheme;
import com.ponysdk.ui.server.basic.PCommand;
import com.ponysdk.ui.server.basic.PMenuBar;
import com.ponysdk.ui.server.basic.PMenuItem;
import com.ponysdk.ui.server.basic.PWidget;

public class DefaultActionSelectorView extends PMenuBar implements SelectorView {

    private final ListenerCollection<SelectorViewListener> selectorViewListeners = new ListenerCollection<SelectorViewListener>();
    private final PMenuItem selectAllMenuItem;
    private final PMenuItem selectNoneMenuItem;

    public DefaultActionSelectorView() {
        addStyleName(PonySDKTheme.MENUBAR_LIGHT);
        final PMenuBar menuBarAction = new PMenuBar(true);
        addItem("", menuBarAction);
        selectAllMenuItem = new PMenuItem("All");
        selectAllMenuItem.setCommand(getSelectAllCommand());
        addItem(selectAllMenuItem);
        selectNoneMenuItem = new PMenuItem("None");
        selectNoneMenuItem.setCommand(getSelectNoneCommand());

        menuBarAction.addItem(selectAllMenuItem);
        menuBarAction.addItem(selectNoneMenuItem);
    }

    @Override
    public PWidget asWidget() {
        return this;
    }

    private PCommand getSelectAllCommand() {
        return new PCommand() {

            @Override
            public void execute() {
                for (final SelectorViewListener selectorListener : selectorViewListeners) {
                    selectorListener.onSelectionChange(SelectionMode.PAGE);
                }
            }
        };
    }

    private PCommand getSelectNoneCommand() {
        return new PCommand() {

            @Override
            public void execute() {
                for (final SelectorViewListener selectorListener : selectorViewListeners) {
                    selectorListener.onSelectionChange(SelectionMode.NONE);
                }
            }
        };
    }

    @Override
    public void addSelectorViewListener(final SelectorViewListener selectorViewListener) {
        selectorViewListeners.register(selectorViewListener);
    }

    @Override
    public void update(final SelectionMode selectionMode, final int numberOfSelectedItems, final int fullSize, final int pageSize) {
        // Nothing
    }

}
