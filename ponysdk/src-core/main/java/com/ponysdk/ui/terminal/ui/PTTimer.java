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
package com.ponysdk.ui.terminal.ui;

import com.google.gwt.user.client.Timer;
import com.ponysdk.ui.terminal.HandlerType;
import com.ponysdk.ui.terminal.Property;
import com.ponysdk.ui.terminal.PropertyKey;
import com.ponysdk.ui.terminal.UIService;
import com.ponysdk.ui.terminal.instruction.Create;
import com.ponysdk.ui.terminal.instruction.EventInstruction;
import com.ponysdk.ui.terminal.instruction.GC;
import com.ponysdk.ui.terminal.instruction.Remove;
import com.ponysdk.ui.terminal.instruction.Update;

public class PTTimer extends PTObject {

    private Timer timer;

    @Override
    public void create(final Create create, final UIService uiService) {
        timer = new Timer() {

            @Override
            public void run() {
                uiService.triggerEvent(new EventInstruction(create.getObjectID(), HandlerType.TIMER));
            }
        };
    }

    @Override
    public void remove(Remove remove, UIService uiService) {
        timer.cancel();
    }

    @Override
    public void update(Update update, UIService uiService) {
        Property property = update.getMainProperty();
        if(property.getKey().equals(PropertyKey.REPEATING_DELAY)) timer.scheduleRepeating(update.getMainProperty().getIntValue());
        else timer.schedule(update.getMainProperty().getIntValue());
    }

    @Override
    public void gc(GC gc, UIService uiService) {
        timer.cancel();
    }
}
