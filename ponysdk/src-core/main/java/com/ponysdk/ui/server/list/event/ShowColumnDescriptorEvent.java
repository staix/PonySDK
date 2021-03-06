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

package com.ponysdk.ui.server.list.event;

import com.ponysdk.core.event.SystemEvent;

public class ShowColumnDescriptorEvent extends SystemEvent<ShowColumnDescriptorHandler> {

    public static final Type<ShowColumnDescriptorHandler> TYPE = new Type<ShowColumnDescriptorHandler>();

    private final String key;

    private final boolean show;

    private final String tableName;

    public ShowColumnDescriptorEvent(Object sourceComponent, String key, boolean show, String tableName) {
        super(sourceComponent);
        this.key = key;
        this.show = show;
        this.tableName = tableName;
    }

    @Override
    protected void dispatch(ShowColumnDescriptorHandler handler) {
        handler.onShowColumnDescriptor(this);
    }

    @Override
    public Type<ShowColumnDescriptorHandler> getAssociatedType() {
        return TYPE;
    }

    public String getKey() {
        return key;
    }

    public boolean isShow() {
        return show;
    }

    public String getTableName() {
        return tableName;
    }

}
