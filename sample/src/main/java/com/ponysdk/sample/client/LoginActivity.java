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
package com.ponysdk.sample.client;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.event.dom.client.KeyCodes;
import com.ponysdk.core.PonySession;
import com.ponysdk.core.activity.AbstractActivity;
import com.ponysdk.impl.webapplication.application.ApplicationActivity;
import com.ponysdk.impl.webapplication.login.DefaultLoginPageView;
import com.ponysdk.sample.client.datamodel.User;
import com.ponysdk.sample.client.event.UserLoggedInEvent;
import com.ponysdk.sample.client.place.ApplicationPlace;
import com.ponysdk.ui.server.basic.PAcceptsOneWidget;
import com.ponysdk.ui.server.basic.event.PClickEvent;
import com.ponysdk.ui.server.basic.event.PClickHandler;
import com.ponysdk.ui.server.basic.event.PKeyPressEvent;
import com.ponysdk.ui.server.basic.event.PKeyPressFilterHandler;

public class LoginActivity extends AbstractActivity {

    @Autowired
    private ApplicationActivity applicationActivity;

    private DefaultLoginPageView loginPageView;

    private PAcceptsOneWidget world;

    @Override
    public void start(final PAcceptsOneWidget world) {
        this.world = world;

        loginPageView = new DefaultLoginPageView("PonySDK Sample");

        loginPageView.getLoginTextBox().showLoadingOnRequest(true);
        loginPageView.getLoginTextBox().addClickHandler(new PClickHandler() {

            @Override
            public void onClick(PClickEvent event) {

            }
        });

        loginPageView.addLoginClickHandler(new PClickHandler() {

            @Override
            public void onClick(PClickEvent clickEvent) {
                doLogin();
            }

        });
        world.setWidget(loginPageView);

        loginPageView.asWidget().addDomHandler(new PKeyPressFilterHandler(KeyCodes.KEY_ENTER) {

            @Override
            public void onKeyPress(int keyCode) {
                doLogin();
            }
        }, PKeyPressEvent.TYPE);

    }

    private void doLogin() {
        final User user = new User();
        user.setID(0);
        user.setLogin("guest");
        user.setName("guest");
        user.setPassword("guest");

        PonySession.getCurrent().setAttribute(UISampleEntryPoint.USER, user);

        final UserLoggedInEvent loggedInEvent = new UserLoggedInEvent(LoginActivity.this, user);
        loggedInEvent.setBusinessMessage("guest is now connected");
        fireEvent(loggedInEvent);

        applicationActivity.goTo(new ApplicationPlace(), world);
    }

}