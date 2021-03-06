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

package com.ponysdk.ui.server.basic;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ponysdk.core.instruction.Update;
import com.ponysdk.core.stm.Txn;
import com.ponysdk.ui.terminal.Dictionnary.PROPERTY;

public class PCookies {

    private final Map<String, String> cachedCookies = new ConcurrentHashMap<String, String>();
    private final long objectID = 0; // reserved

    public PCookies() {}

    public void cacheCookie(final String name, final String value) {
        cachedCookies.put(name, value);
    }

    public String getCookie(final String name) {
        return cachedCookies.get(name);
    }

    public String removeCookie(final String name) {

        final Update update = new Update(objectID);
        update.put(PROPERTY.REMOVE, Boolean.TRUE);
        update.put(PROPERTY.NAME, name);
        Txn.get().getTxnContext().save(update);

        return cachedCookies.remove(name);
    }

    public void setCookie(final String name, final String value) {
        setCookie(name, value, null);
    }

    public void setCookie(final String name, final String value, final Date expires) {
        cachedCookies.put(name, value);

        final Update update = new Update(objectID);
        update.put(PROPERTY.ADD, Boolean.TRUE);
        update.put(PROPERTY.NAME, name);
        update.put(PROPERTY.VALUE, value);
        if (expires != null) {
            update.put(PROPERTY.COOKIE_EXPIRE, expires.getTime());
        }
        Txn.get().getTxnContext().save(update);
    }

}
