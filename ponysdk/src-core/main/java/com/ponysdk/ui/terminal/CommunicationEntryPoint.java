/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *  Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *  Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
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
 */package com.ponysdk.ui.terminal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.StatusCodeException;

public class CommunicationEntryPoint implements EntryPoint {

    private final PonyEngineServiceAsync communicationService = GWT.create(PonyEngineService.class);

    @Override
    public void onModuleLoad() {
        try {
            // load all cookies at startup
            final Map<String, String> cookies = new HashMap<String, String>();
            final Collection<String> cookieNames = Cookies.getCookieNames();
            if (cookieNames != null) {
                for (final String cookieName : cookieNames) {
                    cookies.put(cookieName, Cookies.getCookie(cookieName));
                }
            }

            communicationService.startApplication(History.getToken(), cookies, new AsyncCallback<PonySessionContext>() {

                @Override
                public void onFailure(Throwable caught) {
                    GWT.log("Error", caught);
                    if (caught instanceof StatusCodeException) {
                        final StatusCodeException codeException = (StatusCodeException) caught;
                        if (codeException.getStatusCode() == 0)
                            return;

                    }
                    Window.alert("Cannot inititialize the application : " + caught.getMessage() + "\n" + caught + "\nPlease reload your application");
                }

                @Override
                public void onSuccess(PonySessionContext ponySessionContext) {
                    final UIBuilder uiBuilder = new UIBuilder(ponySessionContext.getID());
                    uiBuilder.init();
                    uiBuilder.update(ponySessionContext.getInstructions());
                }
            });

        } catch (final Exception e) {
            Window.alert("Loading application has failed #" + e);
        }
    }
}