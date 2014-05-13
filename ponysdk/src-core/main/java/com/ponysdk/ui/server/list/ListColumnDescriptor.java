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

package com.ponysdk.ui.server.list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ponysdk.ui.server.basic.IsPWidget;
import com.ponysdk.ui.server.basic.PLabel;
import com.ponysdk.ui.server.list.renderer.cell.CellRenderer;
import com.ponysdk.ui.server.list.renderer.cell.ObjectCellRenderer;
import com.ponysdk.ui.server.list.renderer.header.HeaderCellRenderer;
import com.ponysdk.ui.server.list.renderer.header.StringHeaderCellRenderer;
import com.ponysdk.ui.server.list.valueprovider.ValueProvider;

public class ListColumnDescriptor<D, V> {

    protected static final Logger log = LoggerFactory.getLogger(ListColumnDescriptor.class);

    protected String caption;

    protected HeaderCellRenderer headerCellRenderer;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected CellRenderer<D, V> cellRenderer = new ObjectCellRenderer();

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected CellRenderer<D, V> subCellRenderer = new ObjectCellRenderer();

    protected ValueProvider<D, V> valueProvider;

    protected boolean viewable = true;

    protected boolean isCustom = false;

    protected String width;

    public ListColumnDescriptor() {}

    public ListColumnDescriptor(final String caption) {
        this.caption = caption;
    }

    public IsPWidget renderCell(final int row, final D data) {
        return renderCell(row, data, cellRenderer);
    }

    public IsPWidget renderSubCell(final int row, final D data) {
        return renderCell(row, data, subCellRenderer);
    }

    protected IsPWidget renderCell(final int row, final D data, final CellRenderer<D, V> renderer) {
        V value;
        try {
            value = valueProvider.getValue(data);
        } catch (final Exception e) {
            log.error("cannot get value", e);
            return new PLabel("failed");
        }
        return renderer.render(row, data, value);
    }

    public IsPWidget renderHeader() {
        if (headerCellRenderer == null) headerCellRenderer = new StringHeaderCellRenderer(caption);
        return headerCellRenderer.render();
    }

    public void setHeaderCellRenderer(final HeaderCellRenderer headerCellRender) {
        this.headerCellRenderer = headerCellRender;
        if (caption == null) {
            caption = headerCellRender.getCaption();
        }
    }

    public void setCellRenderer(final CellRenderer<D, V> cellRenderer) {
        if (cellRenderer == null) throw new RuntimeException("cellRender cannot be null");
        this.cellRenderer = cellRenderer;
    }

    public void setValueProvider(final ValueProvider<D, V> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public HeaderCellRenderer getHeaderCellRenderer() {
        return headerCellRenderer;
    }

    public CellRenderer<D, V> getCellRenderer() {
        return cellRenderer;
    }

    public CellRenderer<D, V> getSubCellRenderer() {
        return subCellRenderer;
    }

    public void setSubCellRenderer(final CellRenderer<D, V> subCellRenderer) {
        this.subCellRenderer = subCellRenderer;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(final String width) {
        this.width = width;
    }

    public String getCaption() {
        return caption;
    }

    public void setViewable(final boolean viewable) {
        this.viewable = viewable;
    }

    public boolean isViewable() {
        return viewable;
    }

    public boolean isCustom() {
        return isCustom;
    }

    public void setCustom(final boolean isCustom) {
        this.isCustom = isCustom;
    }

}
