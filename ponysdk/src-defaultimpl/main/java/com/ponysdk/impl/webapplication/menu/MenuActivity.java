/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *	Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *	Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
 *  
 *  WebSite:
 *  http://code.google.com/p/pony-sdk/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ponysdk.impl.webapplication.menu;

import com.ponysdk.core.place.Place;
import com.ponysdk.impl.webapplication.page.PageActivity;
import com.ponysdk.impl.webapplication.page.PageProvider;
import com.ponysdk.impl.webapplication.page.place.HasPageName;
import com.ponysdk.impl.webapplication.page.place.PagePlace;
import com.ponysdk.ui.server.basic.IsPWidget;
import com.ponysdk.ui.server.basic.event.PSelectionEvent;
import com.ponysdk.ui.server.basic.event.PSelectionHandler;

public class MenuActivity extends com.ponysdk.core.activity.AbstractActivity implements PSelectionHandler<MenuItem> {

    private MenuView menuView;
    private PageProvider pageProvider;

    @Override
    public IsPWidget buildView() {
        for (final PageActivity pageActivity : pageProvider.getPageActivities()) {
            menuView.addItem(new MenuItem(pageActivity.getPageName(), pageActivity.getPageCategories()));
        }

        return menuView;
    }

    @Override
    public void updateView(final Place place) {
        if (place instanceof HasPageName) {
            final String pageName = ((HasPageName) place).getPageName();
            final PageActivity pageActivity = pageProvider.getPageActivity(pageName);

            if (pageActivity == null) throw new RuntimeException("The page hasn't been loaded : " + pageName);

            if (pageActivity.getPageName() != null) {
                menuView.selectItem(new MenuItem(pageActivity.getPageName(), pageActivity.getPageCategories()));
            }
        }
    }

    @Override
    public void onSelection(final PSelectionEvent<MenuItem> event) {

        menuView.selectItem(event.getSelectedItem());

        goTo(new PagePlace(event.getSelectedItem().getName()));
    }

    public void setMenuView(final MenuView menuView) {
        this.menuView = menuView;
        this.menuView.addSelectionHandler(this);
    }

    public MenuView getMenuView() {
        return menuView;
    }

    public void setPageProvider(final PageProvider pageProvider) {
        this.pageProvider = pageProvider;
    }

}
