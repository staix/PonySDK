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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.ponysdk.core.command.Command;
import com.ponysdk.core.deprecated.AbstractActivity;
import com.ponysdk.core.event.EventBus;
import com.ponysdk.core.event.EventBusAware;
import com.ponysdk.core.event.SimpleEventBus;
import com.ponysdk.core.export.ExportContext;
import com.ponysdk.core.export.Exporter;
import com.ponysdk.core.query.Criterion;
import com.ponysdk.core.query.Query;
import com.ponysdk.core.query.Query.QueryMode;
import com.ponysdk.core.query.Result;
import com.ponysdk.core.query.SortingType;
import com.ponysdk.impl.theme.PonySDKTheme;
import com.ponysdk.ui.server.basic.IsPWidget;
import com.ponysdk.ui.server.basic.PAcceptsOneWidget;
import com.ponysdk.ui.server.basic.PAnchor;
import com.ponysdk.ui.server.basic.PCheckBox;
import com.ponysdk.ui.server.basic.PCommand;
import com.ponysdk.ui.server.basic.PConfirmDialogHandler;
import com.ponysdk.ui.server.basic.PDialogBox;
import com.ponysdk.ui.server.basic.PHTML;
import com.ponysdk.ui.server.basic.PHorizontalPanel;
import com.ponysdk.ui.server.basic.PLabel;
import com.ponysdk.ui.server.basic.PMenuBar;
import com.ponysdk.ui.server.basic.PMenuItem;
import com.ponysdk.ui.server.basic.PNotificationManager;
import com.ponysdk.ui.server.basic.PNotificationManager.Notification;
import com.ponysdk.ui.server.basic.PPanel;
import com.ponysdk.ui.server.basic.PSimplePanel;
import com.ponysdk.ui.server.basic.PVerticalPanel;
import com.ponysdk.ui.server.basic.event.PClickEvent;
import com.ponysdk.ui.server.basic.event.PClickHandler;
import com.ponysdk.ui.server.basic.event.PValueChangeEvent;
import com.ponysdk.ui.server.basic.event.PValueChangeHandler;
import com.ponysdk.ui.server.form.DefaultFormView;
import com.ponysdk.ui.server.form.FormActivity;
import com.ponysdk.ui.server.form.FormConfiguration;
import com.ponysdk.ui.server.form.FormField;
import com.ponysdk.ui.server.form.FormView;
import com.ponysdk.ui.server.form.renderer.DateBoxFormFieldRenderer;
import com.ponysdk.ui.server.form.renderer.ListBoxFormFieldRenderer;
import com.ponysdk.ui.server.list.event.AddCustomColumnDescriptorEvent;
import com.ponysdk.ui.server.list.event.AddCustomColumnDescriptorHandler;
import com.ponysdk.ui.server.list.event.ColumnDescriptorMovedEvent;
import com.ponysdk.ui.server.list.event.ColumnDescriptorRemovedEvent;
import com.ponysdk.ui.server.list.event.ColumnDescriptorShownEvent;
import com.ponysdk.ui.server.list.event.ComparatorTypeChangeEvent;
import com.ponysdk.ui.server.list.event.ComparatorTypeChangeHandler;
import com.ponysdk.ui.server.list.event.CustomColumnDescriptorAddedEvent;
import com.ponysdk.ui.server.list.event.MoveColumnDescriptorEvent;
import com.ponysdk.ui.server.list.event.MoveColumnDescriptorHandler;
import com.ponysdk.ui.server.list.event.PreferenceChangedEvent;
import com.ponysdk.ui.server.list.event.RefreshListEvent;
import com.ponysdk.ui.server.list.event.RefreshListHandler;
import com.ponysdk.ui.server.list.event.RemoveColumnDescriptorEvent;
import com.ponysdk.ui.server.list.event.RemoveColumnDescriptorHandler;
import com.ponysdk.ui.server.list.event.ShowColumnDescriptorEvent;
import com.ponysdk.ui.server.list.event.ShowColumnDescriptorHandler;
import com.ponysdk.ui.server.list.event.ShowCustomColumnDescriptorFormEvent;
import com.ponysdk.ui.server.list.event.ShowCustomColumnDescriptorFormHandler;
import com.ponysdk.ui.server.list.event.ShowSubListEvent;
import com.ponysdk.ui.server.list.event.ShowSubListHandler;
import com.ponysdk.ui.server.list.event.SortColumnEvent;
import com.ponysdk.ui.server.list.event.SortColumnHandler;
import com.ponysdk.ui.server.list.form.AddCustomColumnDescriptorForm;
import com.ponysdk.ui.server.list.form.PreferenceForm;
import com.ponysdk.ui.server.list.paging.MenuBarPagingView;
import com.ponysdk.ui.server.list.paging.PagingActivity;
import com.ponysdk.ui.server.list.paging.PagingView;
import com.ponysdk.ui.server.list.paging.event.PagingSelectionChangeEvent;
import com.ponysdk.ui.server.list.paging.event.PagingSelectionChangeHandler;
import com.ponysdk.ui.server.list.renderer.cell.CellRenderer;
import com.ponysdk.ui.server.list.renderer.cell.DetailsCellRenderer;
import com.ponysdk.ui.server.list.renderer.cell.EmptyCellRenderer;
import com.ponysdk.ui.server.list.renderer.header.ComplexHeaderCellRenderer;
import com.ponysdk.ui.server.list.renderer.header.HeaderCellRenderer;
import com.ponysdk.ui.server.list.valueprovider.BeanValueProvider;
import com.ponysdk.ui.server.list.valueprovider.BooleanValueProvider;
import com.ponysdk.ui.server.list.valueprovider.ValueProvider;
import com.ponysdk.ui.server.rich.PConfirmDialog;
import com.ponysdk.ui.terminal.basic.PHorizontalAlignment;

public class ComplexListActivity<D> extends AbstractActivity implements PagingSelectionChangeHandler, SortColumnHandler, ComparatorTypeChangeHandler, RefreshListHandler, ShowSubListHandler<D>, ShowCustomColumnDescriptorFormHandler,
        ShowColumnDescriptorHandler, MoveColumnDescriptorHandler, RemoveColumnDescriptorHandler, AddCustomColumnDescriptorHandler {

    protected SimpleListActivity<D> simpleListActivity;

    protected final ComplexListView complexListView;

    protected ComplexListCommandFactory<D> commandFactory;

    protected final List<ListColumnDescriptor<D, ?>> listColumnDescriptors;

    protected SimpleDateFormat dateFormater = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

    protected final Map<Criterion, FormField> formFieldsByCriterionFields = new HashMap<Criterion, FormField>();

    protected final Map<String, Criterion> criterionByPojoProperty = new HashMap<String, Criterion>();

    protected PagingActivity pagingActivity;

    protected final ComplexListConfiguration<D> complexListConfiguration;

    protected final Map<String, ColumnDescriptorFieldHolder> customDescriptorHolderByCaption = new HashMap<String, ColumnDescriptorFieldHolder>();

    protected LinkedHashMap<String, ListColumnDescriptor<?, ?>> descriptorsByCaption = new LinkedHashMap<String, ListColumnDescriptor<?, ?>>();

    protected FormActivity searchFormActivity;

    protected SortingType currentSortingType = SortingType.NONE;

    protected String currentSortingPojoPropertyKey;

    protected int currentPage;

    protected Map<Integer, PRowCheckBox> rowSelectors = new TreeMap<Integer, PRowCheckBox>();

    protected PCheckBox mainCheckBox;

    protected boolean mainSelectorAction;

    protected boolean rowSelectorAction;

    protected final EventBus localEventBus;

    protected EventBus eventBus; // use to forward ShowSubListEvent

    protected PMenuItem refreshButton;

    protected PMenuItem resetButton;

    protected String debugID;

    protected Result<List<D>> findResult;

    protected final Set<D> selectedAndEnabledData = new HashSet<D>();

    protected final Set<D> selectedAndDisabledData = new HashSet<D>();

    protected SelectionMode selectionMode = SelectionMode.NONE;

    protected int beforeIndex = -1;

    protected ShowCustomColumnDescriptorFormHandler columnDescriptorFormHandler = new SHowCustomColumnDescriptorFormHandlerImpl();

    public ComplexListActivity(final ComplexListConfiguration<D> complexListConfiguration, final ComplexListView complexListView) {
        this(complexListConfiguration, complexListView, null);
    }

    public ComplexListActivity(final ComplexListConfiguration<D> complexListConfiguration, final ComplexListView complexListView, final EventBus eventBus) {
        this.eventBus = eventBus;
        this.complexListConfiguration = complexListConfiguration;
        complexListConfiguration.setComplexListActivity(this);
        this.listColumnDescriptors = complexListConfiguration.getColumnDescriptors();
        for (final ListColumnDescriptor<D, ?> descriptor : listColumnDescriptors) {
            descriptorsByCaption.put(descriptor.getCaption(), descriptor);
        }
        if (complexListConfiguration.isSelectionColumnEnabled()) {
            listColumnDescriptors.add(0, getSelectableRow());
            beforeIndex = 0;
        }
        if (complexListConfiguration.isShowSubListColumnEnabled()) {
            beforeIndex++;
            listColumnDescriptors.add(beforeIndex, getShowSubListRow());
        }

        this.complexListView = complexListView;

        this.localEventBus = new SimpleEventBus();
        this.localEventBus.addHandler(PagingSelectionChangeEvent.TYPE, this);
        this.localEventBus.addHandler(SortColumnEvent.TYPE, this);
        this.localEventBus.addHandler(RefreshListEvent.TYPE, this);
        this.localEventBus.addHandler(ShowSubListEvent.TYPE, this);
        this.localEventBus.addHandler(ShowCustomColumnDescriptorFormEvent.TYPE, this);
        this.localEventBus.addHandler(ShowColumnDescriptorEvent.TYPE, this);
        this.localEventBus.addHandler(MoveColumnDescriptorEvent.TYPE, this);
        this.localEventBus.addHandler(RemoveColumnDescriptorEvent.TYPE, this);
        this.localEventBus.addHandler(ComparatorTypeChangeEvent.TYPE, this);
        this.simpleListActivity = new SimpleListActivity<D>(complexListConfiguration.getTableName(), complexListView, listColumnDescriptors, localEventBus);
    }

    protected void initEventBus(final ListColumnDescriptor<D, ?> columnDescriptor) {
        if (columnDescriptor.getHeaderCellRenderer() instanceof EventBusAware) {
            ((EventBusAware) columnDescriptor.getHeaderCellRenderer()).setEventBus(localEventBus);
        }
        if (columnDescriptor.getCellRenderer() instanceof EventBusAware) {
            ((EventBusAware) columnDescriptor.getCellRenderer()).setEventBus(localEventBus);
        }
    }

    protected class PRowCheckBox extends PCheckBox {

        protected int row;

        protected int datasize;

        public void setRow(final int row) {
            this.row = row;
        }

        public int getRow() {
            return row;
        }

        public void setDatasize(final int datasize) {
            this.datasize = datasize;
        }

        public int getDatasize() {
            return datasize;
        }

        @SuppressWarnings("unchecked")
        @Override
        public D getData() {
            return (D) data;
        }

    }

    protected ListColumnDescriptor<D, Boolean> getSelectableRow() {
        final ListColumnDescriptor<D, Boolean> listColumnDescriptor = new ListColumnDescriptor<D, Boolean>();
        listColumnDescriptor.setValueProvider(new BooleanValueProvider<D>(false));
        listColumnDescriptor.setSubCellRenderer(new EmptyCellRenderer<D, Boolean>());
        listColumnDescriptor.setCellRenderer(new CellRenderer<D, Boolean>() {

            @Override
            public IsPWidget render(final int row, final D data, final Boolean value) {
                final PRowCheckBox checkBox = new PRowCheckBox();
                checkBox.setRow(row);
                checkBox.setData(data);

                checkBox.addValueChangeHandler(new PValueChangeHandler<Boolean>() {

                    @Override
                    public void onValueChange(final PValueChangeEvent<Boolean> event) {
                        if (event.getValue()) {
                            if (checkBox.getRow() != 0) {
                                simpleListActivity.selectRow(checkBox.getRow());
                                if (checkBox.isEnabled()) selectedAndEnabledData.add(data);
                                else selectedAndDisabledData.add(data);

                                selectionMode = SelectionMode.PARTIAL;
                            }
                            if (!mainSelectorAction) {
                                boolean allChecked = true;
                                for (final PCheckBox box : rowSelectors.values()) {
                                    if (!box.equals(checkBox)) {
                                        if (!box.getValue()) {
                                            allChecked = false;
                                            break;
                                        }
                                    }
                                }
                                if (allChecked) {
                                    rowSelectorAction = true;
                                    mainCheckBox.setValue(true);
                                    showSelectAllOption();
                                    rowSelectorAction = false;
                                }
                            }
                        } else {
                            if (checkBox.getRow() != 0) {
                                simpleListActivity.unSelectRow(checkBox.getRow());
                                selectedAndEnabledData.remove(data);
                                selectedAndDisabledData.remove(data);
                            }
                            hideSelectAllOption();
                            if (rowSelectors.isEmpty()) {
                                selectionMode = SelectionMode.NONE;
                            } else {
                                selectionMode = SelectionMode.PARTIAL;
                            }
                            if (!mainSelectorAction) {
                                rowSelectorAction = true;
                                mainCheckBox.setValue(false);
                                rowSelectorAction = false;
                            }
                        }
                    }
                });

                rowSelectors.put(row, checkBox);

                if (selectedAndEnabledData.contains(data) || selectedAndDisabledData.contains(data)) {
                    selectRow(row);
                }

                return checkBox;
            }
        });

        listColumnDescriptor.setHeaderCellRenderer(new HeaderCellRenderer() {

            @Override
            public IsPWidget render() {
                mainCheckBox = new PCheckBox();
                mainCheckBox.addStyleName(PonySDKTheme.COMPLEXLIST_HEADERCELLRENDERER_MAINCHECKBOX);
                mainCheckBox.addValueChangeHandler(new PValueChangeHandler<Boolean>() {

                    @Override
                    public void onValueChange(final PValueChangeEvent<Boolean> event) {
                        triggerMainCheckBoxValueChange(event.getValue());
                    }

                });

                return mainCheckBox;
            }

            @Override
            public String getCaption() {
                return null;
            }
        });

        return listColumnDescriptor;
    }

    protected void triggerMainCheckBoxValueChange(final Boolean value) {
        if (!rowSelectorAction) {
            mainSelectorAction = true;
            mainCheckboxChanged(value);
            mainSelectorAction = false;
        }
    }

    protected void mainCheckboxChanged(final Boolean value) {
        changeRowSelectorsState(value);
        if (value) {
            showSelectAllOption();
            selectionMode = SelectionMode.PARTIAL;
        } else {
            hideSelectAllOption();
            selectionMode = SelectionMode.NONE;
        }

        // rowSelectorAction = true;
        mainCheckBox.setValue(value);
        // rowSelectorAction = false;
    }

    protected void showSelectAllOption() {
        final PHorizontalPanel panel = new PHorizontalPanel();
        panel.setHorizontalAlignment(PHorizontalAlignment.ALIGN_CENTER);
        panel.setStyleName("pony-ComplexList-OptionSelectionPanel");
        final PLabel label = new PLabel("All " + rowSelectors.size() + " items on this page are selected.");
        final int fullSize = findResult.getFullSize();
        panel.add(label);
        if (fullSize > complexListConfiguration.getPageSize()) {
            final PAnchor anchor = new PAnchor("Select all " + fullSize + " final items in Inbox");
            anchor.addClickHandler(new PClickHandler() {

                @Override
                public void onClick(final PClickEvent event) {
                    selectionMode = SelectionMode.FULL;
                    showClearSelectAllOption();
                }
            });
            panel.add(anchor);
            panel.setCellHorizontalAlignment(label, PHorizontalAlignment.ALIGN_RIGHT);
            panel.setCellHorizontalAlignment(anchor, PHorizontalAlignment.ALIGN_LEFT);
        }
        complexListView.getTopListLayout().setWidget(panel);
    }

    protected void showClearSelectAllOption() {
        final PHorizontalPanel panel = new PHorizontalPanel();
        panel.setHorizontalAlignment(PHorizontalAlignment.ALIGN_CENTER);
        panel.setStyleName("pony-ComplexList-OptionSelectionPanel");
        final PLabel label = new PLabel("All " + findResult.getFullSize() + " items are selected.");
        final PAnchor anchor = new PAnchor("Clear selection");
        anchor.addClickHandler(new PClickHandler() {

            @Override
            public void onClick(final PClickEvent event) {
                mainCheckboxChanged(false);
            }
        });
        panel.add(label);
        panel.add(anchor);
        panel.setCellHorizontalAlignment(label, PHorizontalAlignment.ALIGN_RIGHT);
        panel.setCellHorizontalAlignment(anchor, PHorizontalAlignment.ALIGN_LEFT);
        complexListView.getTopListLayout().setWidget(panel);
    }

    protected void hideSelectAllOption() {
        complexListView.getTopListLayout().setWidget(new PHTML());
    }

    protected ListColumnDescriptor<D, String> getShowSubListRow() {

        final ListColumnDescriptor<D, String> listColumnDescriptor = new ListColumnDescriptor<D, String>();
        listColumnDescriptor.setValueProvider(new ValueProvider<D, String>() {

            @Override
            public String getValue(final D data) {
                return null;
            }
        });
        listColumnDescriptor.setCellRenderer(new DetailsCellRenderer<D, String>());
        listColumnDescriptor.setSubCellRenderer(new EmptyCellRenderer<D, String>());
        listColumnDescriptor.setHeaderCellRenderer(new HeaderCellRenderer() {

            @Override
            public IsPWidget render() {
                return new PLabel("");
            }

            @Override
            public String getCaption() {
                return "";
            }
        });

        return listColumnDescriptor;
    }

    protected void buildSearchForm() {
        if (complexListConfiguration.isEnableForm()) {
            PPanel formLayout = complexListConfiguration.getFormLayout();
            if (formLayout == null) {
                formLayout = new PVerticalPanel();
            }

            final FormView formView = new DefaultFormView("SearchForm", formLayout);
            final FormConfiguration formConfiguration = new FormConfiguration();
            formConfiguration.setName(complexListConfiguration.getTableName() + "filterForm");

            searchFormActivity = new FormActivity(formConfiguration, formView);
            searchFormActivity.start(complexListView.getFormLayout());
        }
    }

    protected void buildPaging() {
        final PagingView pagingView = new MenuBarPagingView();
        pagingActivity = new PagingActivity(pagingView);
        pagingActivity.setPageSize(complexListConfiguration.getPageSize());
        pagingActivity.setEventBus(localEventBus);
        pagingActivity.start(complexListView.getPagingLayout());
    }

    protected void buildActions() {
        final PMenuBar actionBar = new PMenuBar();
        actionBar.setStyleName("pony-ActionToolbar");

        refreshButton = new PMenuItem("Refresh", new PCommand() {

            @Override
            public void execute() {
                refresh();
            }
        });
        actionBar.addItem(refreshButton);
        actionBar.addSeparator();

        resetButton = new PMenuItem("Reset", new PCommand() {

            @Override
            public void execute() {
                reset();
            }
        });

        actionBar.addItem(resetButton);

        if (complexListConfiguration.getExportConfiguration() != null) {
            actionBar.addSeparator();
            final PMenuBar exportListMenuBar = new PMenuBar(true);

            for (final Exporter<D> exporter : complexListConfiguration.getExportConfiguration().getExporters()) {
                final PMenuItem item = new PMenuItem(exporter.name(), new PCommand() {

                    @Override
                    public void execute() {

                        final SelectionResult<D> selectionResult = getSelectedData();
                        if (selectionResult.getSelectedData() != null && selectionResult.getSelectedData().isEmpty()) {
                            PNotificationManager.notify("Export failed, please select data to export", Notification.WARNING_MESSAGE);
                            return;
                        }
                        final Query query = createQuery(currentPage);
                        if (SelectionMode.FULL.equals(selectionMode)) {
                            query.setQueryMode(QueryMode.FULL_RESULT);
                        }
                        final ExportContext<D> exportContext = new ExportContext<D>(query, complexListConfiguration.getExportConfiguration().getExportableFields(), selectionResult);
                        exportContext.setExporter(exporter);

                        final Command<String> command = commandFactory.newExportCommand(ComplexListActivity.this, exportContext);
                        command.execute();
                    }
                });
                exportListMenuBar.addItem(item);
            }
            actionBar.addItem("Export", exportListMenuBar);

        }

        complexListView.getToolbarLayout().add(actionBar);
        complexListView.getToolbarLayout().addSepararator();
    }

    public void setData(final Result<List<D>> result) {
        findResult = result;
        simpleListActivity.setData(result.getData());

        boolean mainCheckBoxSelected = true;
        for (final PRowCheckBox checkBox : rowSelectors.values()) {
            if (!checkBox.getValue()) {
                mainCheckBoxSelected = false;
                break;
            }
        }

        if (!mainCheckBoxSelected) {
            hideSelectAllOption();
        }

        if (mainCheckBox != null) {
            mainCheckBox.setValue(mainCheckBoxSelected);
        }

        getComplexListView().addHeaderStyle("pony-ComplexList-ColumnHeader");
        final float executionTime = result.getExecutionTime() * 0.000000001f;// TO

        complexListView.setSearchResultInformation("found " + result.getData().size() + " out of " + result.getFullSize() + " records (" + executionTime + " seconds), last refresh: " + dateFormater.format(Calendar.getInstance().getTime()));

        pagingActivity.process(result.getFullSize());
    }

    public Result<List<D>> getData() {
        return findResult;
    }

    public void addDescriptor(final ListColumnDescriptor<D, ?> customDescriptor) {
        addDescriptor(listColumnDescriptors.size(), customDescriptor);
    }

    public void addDescriptor(final int index, final ListColumnDescriptor<D, ?> customDescriptor) {
        if (index > listColumnDescriptors.size() || index < 0) { throw new RuntimeException("Cannot add column#" + customDescriptor.getCaption() + " index out of bound"); }
        listColumnDescriptors.add(index, customDescriptor);
        descriptorsByCaption.put(customDescriptor.getCaption(), customDescriptor);
        initEventBus(customDescriptor);
    }

    public void insertSubList(final int row, final List<D> datas) {
        simpleListActivity.insertSubList(row, datas);

        for (final PRowCheckBox c : rowSelectors.values()) {
            if (c.getRow() == row) {
                c.setDatasize(datas.size());
            }
            if (c.getRow() > row) {
                c.setRow(c.getRow() + datas.size());
            }
        }
    }

    public void removeSubList(final int fatherRow) {
        simpleListActivity.removeSubList(fatherRow);
        int dataSize = 0;

        final Map<Integer, PRowCheckBox> map = new HashMap<Integer, PRowCheckBox>();

        for (final PRowCheckBox c : rowSelectors.values()) {
            if (c.getRow() == fatherRow) {
                dataSize = c.getDatasize();
                c.setDatasize(0);
            }
            if (c.getRow() > fatherRow) {
                c.setRow(c.getRow() - dataSize);
            }

            map.put(c.getRow(), c);
        }

        rowSelectors = map;
    }

    public void setCommandFactory(final ComplexListCommandFactory<D> commandFactory) {
        this.commandFactory = commandFactory;
    }

    protected void changeRowSelectorsState(final boolean selected) {
        for (final PRowCheckBox checkBox : rowSelectors.values()) {
            simpleListActivity.selectRow(checkBox.getRow());
            if (selected) {
                selectRow(checkBox.getRow());
            } else {
                unSelectRow(checkBox.getRow());
            }
        }
    }

    public void resetAllSelectedData() {
        selectedAndDisabledData.clear();
        selectedAndEnabledData.clear();
        if (mainCheckBox != null) {
            triggerMainCheckBoxValueChange(false);
        }
    }

    public void reset() {
        selectedAndEnabledData.clear();
        selectedAndDisabledData.clear();

        for (final FormField formField : formFieldsByCriterionFields.values()) {
            formField.reset();
        }

        if (currentSortingPojoPropertyKey != null) {
            this.localEventBus.fireEvent(new SortColumnEvent(this, SortingType.NONE, currentSortingPojoPropertyKey));
        }

        if (complexListConfiguration.isSelectionColumnEnabled()) {
            changeRowSelectorsState(false);
            mainCheckBox.setValue(false);
            triggerMainCheckBoxValueChange(false);
        }

        currentSortingPojoPropertyKey = null;
        currentSortingType = SortingType.NONE;
    }

    public void refresh() {
        pagingActivity.clear();
        refresh(0);
    }

    public void refresh(final int page) {
        if (complexListConfiguration.isSearchFormMustBeValid()) {
            if (!searchFormActivity.isValid()) { return; }
        }

        final Query query = createQuery(page);
        final Command<Result<List<D>>> command = commandFactory.newFindCommand(ComplexListActivity.this, query);
        if (command == null) { throw new IllegalStateException("FindCommand of the complex list can't be null"); }
        command.execute();
        complexListView.updateView();

    }

    protected Query createQuery(final int page) {
        final List<Criterion> criteria = new ArrayList<Criterion>();
        for (final Entry<Criterion, FormField> entry : formFieldsByCriterionFields.entrySet()) {
            final FormField formField = entry.getValue();

            if (formField.getValue() != null) {
                final Criterion criterionField = entry.getKey();
                criterionField.setValue(formField.getValue());
                criteria.add(criterionField);
            }
        }

        if (currentSortingPojoPropertyKey != null) {
            final Criterion criterionField = new Criterion(currentSortingPojoPropertyKey);
            criterionField.setSortingType(currentSortingType);
            criteria.add(criterionField);
        }

        final Query query = new Query();
        query.setCriteria(criteria);
        query.setPageNum(page);
        query.setPageSize(complexListConfiguration.getPageSize());
        return query;
    }

    public FormActivity getForm() {
        return searchFormActivity;
    }

    @Override
    public void onPageChange(final PagingSelectionChangeEvent event) {
        if (event.getSource().equals(pagingActivity)) {
            currentPage = event.getPage();
            refresh(currentPage);
        }
    }

    public void registerSearchCriteria(final Criterion criterionField, final FormField formField) {
        formFieldsByCriterionFields.put(criterionField, formField);
        criterionByPojoProperty.put(criterionField.getPojoProperty(), criterionField);
    }

    @Override
    public void onColumnSort(final SortColumnEvent event) {
        currentSortingType = event.getSortingType();
        currentSortingPojoPropertyKey = event.getPojoPropertyKey();
        refresh();
    }

    @Override
    public void onComparatorTypeChange(final ComparatorTypeChangeEvent event) {
        final Criterion criterionField = criterionByPojoProperty.get(event.getPojoPropertyKey());
        if (criterionField != null) {
            criterionField.setComparator(event.getComparatorType());
        }
    }

    public SelectionResult<D> getSelectedData() {
        if (!complexListConfiguration.isSelectionColumnEnabled()) { return new SelectionResult<D>(SelectionMode.FULL, new ArrayList<D>()); }

        final List<D> selectedData = new ArrayList<D>();
        selectedData.addAll(selectedAndDisabledData);
        selectedData.addAll(selectedAndEnabledData);

        final SelectionResult<D> selectionResult = new SelectionResult<D>(selectionMode, selectedData);
        return selectionResult;
    }

    public SelectionResult<D> getSelectedAndEnabledData() {
        if (!complexListConfiguration.isSelectionColumnEnabled()) { return new SelectionResult<D>(SelectionMode.FULL, new ArrayList<D>()); }

        final List<D> selectedData = new ArrayList<D>();
        selectedData.addAll(selectedAndEnabledData);

        final SelectionResult<D> selectionResult = new SelectionResult<D>(selectionMode, selectedData);
        return selectionResult;
    }

    public void setSelectedData(final List<D> data) {
        // unselect previously selected data
        if (mainCheckBox != null) {
            mainCheckBox.setValue(false);
            triggerMainCheckBoxValueChange(false);
        }

        // select
        for (final D d : data) {
            selectRow(getRow(d));
        }
    }

    public void enableSelectedData(final D data, final boolean enabled) {
        // // unselect previously selected data
        // if (mainCheckBox != null) {
        // mainCheckBox.setValue(false);
        // triggerMainCheckBoxValueChange(false);
        // }

        // enable
        enableRow(getRow(data), enabled);
    }

    public int getRow(final D data) {
        if (findResult == null) return -1;

        for (final PRowCheckBox checkBox : rowSelectors.values()) {
            if (data.equals(checkBox.getData())) { return checkBox.getRow(); }
        }

        return -1;
    }

    @Override
    public void onRefreshList(final RefreshListEvent event) {
        refresh();
    }

    public void enableRow(final int row, final boolean enabled) {
        if (complexListConfiguration.isSelectionColumnEnabled()) {
            enableCheckBoxRow(row, enabled);
        }
    }

    public void selectRow(final int row) {
        if (complexListConfiguration.isSelectionColumnEnabled()) {
            selectRowCheckBox(row);
        }

        final PRowCheckBox checkBox = rowSelectors.get(row);
        if (checkBox == null) return;

        if (checkBox.isEnabled()) {
            this.selectedAndEnabledData.add(checkBox.getData());
        } else {
            this.selectedAndDisabledData.add(checkBox.getData());
        }

        this.simpleListActivity.selectRow(row);
    }

    public void enableCheckBoxRow(final int row, final boolean enabled) {
        if (complexListConfiguration.isSelectionColumnEnabled()) {
            enableRowCheckBox(row, enabled);
        }

    }

    public void unSelectRow(final int row) {
        if (complexListConfiguration.isSelectionColumnEnabled()) {
            unselectRowCheckBox(row);
        }

        final PRowCheckBox checkBox = rowSelectors.get(row);
        if (checkBox == null) return;
        this.selectedAndEnabledData.remove(checkBox.getData());
        this.selectedAndDisabledData.remove(checkBox.getData());

        this.simpleListActivity.unSelectRow(row);
    }

    protected void enableRowCheckBox(final int row, final boolean enabled) {
        final PRowCheckBox checkBox = rowSelectors.get(row);
        if (checkBox == null) return;
        checkBox.setEnabled(enabled);
    }

    protected void selectRowCheckBox(final int row) {
        final PRowCheckBox checkBox = rowSelectors.get(row);
        if (checkBox == null) return;
        checkBox.setValue(true);
    }

    protected void unselectRowCheckBox(final int row) {
        final PRowCheckBox checkBox = rowSelectors.get(row);
        if (checkBox == null) return;
        checkBox.setValue(false);
    }

    @Override
    public void start(final PAcceptsOneWidget world) {
        for (final ListColumnDescriptor<D, ?> columnDescriptor : listColumnDescriptors) {
            initEventBus(columnDescriptor);
        }
        buildSearchForm();
        buildActions();
        buildPaging();
        buildPreferences();
        world.setWidget(complexListView);

        if (debugID != null) {
            ensureDebugId(debugID);
        }
    }

    protected void buildPreferences() {
        final PMenuBar menuBar = new PMenuBar();
        final PMenuBar menuBarAction = new PMenuBar(true);
        menuBar.addItem("Preferences", menuBarAction);
        menuBar.setStyleName("pony-ActionToolbar");
        boolean hasPreferenceAction = false;
        if (complexListConfiguration.isCustomColumnEnabled()) {
            menuBarAction.addItem("Add Custom Column", new PCommand() {

                @Override
                public void execute() {
                    localEventBus.fireEvent(new ShowCustomColumnDescriptorFormEvent(this));

                }
            });
            hasPreferenceAction = true;
        }

        if (complexListConfiguration.isShowPreferences()) {
            menuBarAction.addItem(new PMenuItem("Order columns", new PCommand() {

                @Override
                public void execute() {

                    PConfirmDialog.show("Column Ordering", new PreferenceForm(descriptorsByCaption.values(), localEventBus, complexListConfiguration.getTableName()), new PConfirmDialogHandler() {

                        @Override
                        public boolean onOK(final PDialogBox dialogBox) {
                            return true;
                        }

                        @Override
                        public void onCancel() {}
                    });

                }
            }));
            hasPreferenceAction = true;
        }

        if (hasPreferenceAction) {
            complexListView.getPreferencesLayout().setWidget(menuBar);
        }

    }

    public void updateData(final D d) {
        final int row = getRow(d);
        eventBus.fireEvent(new ShowSubListEvent<D>(this, d, true, row));
    }

    @Override
    public void onShowSubList(final ShowSubListEvent<D> event) {
        eventBus.fireEvent(new ShowSubListEvent<D>(this, event.getData(), event.isShow(), event.getRow()));
    }

    public ComplexListView getComplexListView() {
        return complexListView;
    }

    public void ensureDebugId(final String debugID) {
        this.debugID = debugID;
        if (refreshButton != null) {
            refreshButton.ensureDebugId(debugID + "[refresh]");
        }
        if (resetButton != null) {
            resetButton.ensureDebugId(debugID + "[reset]");
        }
        if (mainCheckBox != null) {
            mainCheckBox.ensureDebugId(debugID + "[checkAll]");
        }

        if (simpleListActivity != null) {
            simpleListActivity.ensureDebugId(debugID);
        }
    }

    @Override
    public void onShowCustomColumnDescriptorForm(final ShowCustomColumnDescriptorFormEvent event) {
        columnDescriptorFormHandler.onShowCustomColumnDescriptorForm(event);

    }

    protected class SHowCustomColumnDescriptorFormHandlerImpl implements ShowCustomColumnDescriptorFormHandler {

        @Override
        public void onShowCustomColumnDescriptorForm(final ShowCustomColumnDescriptorFormEvent event) {
            final FormView formView = new DefaultFormView("AddCustomColumnDescriptorForm");
            final FormConfiguration formConfiguration = new FormConfiguration();
            formConfiguration.setName("Form");
            final AddCustomColumnDescriptorForm form = new AddCustomColumnDescriptorForm(formConfiguration, formView, complexListConfiguration.getClas(), ComplexListActivity.this);
            final PSimplePanel windowContent = new PSimplePanel();
            form.start(windowContent);
            PConfirmDialog.show("Add custom column", windowContent, "Ok", "Cancel", new PConfirmDialogHandler() {

                @Override
                public boolean onOK(final PDialogBox dialogBox) {
                    if (form.isValid()) {
                        final Class<?> fieldType = form.getFieldType();
                        final String fieldPath = form.getFieldPath();
                        final String caption = form.getCaption();
                        onAddCustomColumnDescriptor(new AddCustomColumnDescriptorEvent(ComplexListActivity.this, new ColumnDescriptorFieldHolder(caption, fieldPath, fieldType, complexListConfiguration.getTableName())));
                        eventBus.fireEvent(new PreferenceChangedEvent(ComplexListActivity.this));
                        return true;
                    }
                    return false;
                }

                @Override
                public void onCancel() {}
            });
        }

    }

    protected void rebuildSimpleList() {
        if (findResult != null) {
            simpleListActivity.rebuild(listColumnDescriptors, findResult.getData());
        } else simpleListActivity.rebuild(listColumnDescriptors, null);
    }

    public void repaint() {
        rebuildSimpleList();
    }

    @Override
    public void onShowColumnDescriptor(final ShowColumnDescriptorEvent event) {
        descriptorsByCaption.get(event.getKey()).setViewable(event.isShow());
        rebuildSimpleList();
        final ColumnDescriptorShownEvent showColumnDescriptorEvent = new ColumnDescriptorShownEvent(this, event.getKey(), event.isShow(), complexListConfiguration.getTableName());
        eventBus.fireEvent(showColumnDescriptorEvent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onMoveColumn(final MoveColumnDescriptorEvent event) {
        if (event.getSource() != this && event.getTableName() != null && event.getTableName().equals(complexListConfiguration.getTableName())) {
            final List<String> caption = event.getColumnOrder();
            listColumnDescriptors.removeAll(new ArrayList<ListColumnDescriptor<D, ?>>(listColumnDescriptors.subList(beforeIndex + 1, listColumnDescriptors.size())));
            for (final String c : caption) {
                final ListColumnDescriptor<?, ?> listColumnDescriptor = descriptorsByCaption.get(c);
                listColumnDescriptors.add((ListColumnDescriptor<D, ?>) listColumnDescriptor);
            }
            descriptorsByCaption.clear();
            for (int i = (beforeIndex + 1); i < listColumnDescriptors.size(); i++) {
                final ListColumnDescriptor<D, ?> d = listColumnDescriptors.get(i);
                descriptorsByCaption.put(d.getCaption(), d);
            }
            rebuildSimpleList();
            eventBus.fireEvent(new ColumnDescriptorMovedEvent(this, event.getColumnOrder(), event.getTableName()));
        }
    }

    @Override
    public void onRemoveColumn(final RemoveColumnDescriptorEvent event) {
        final String caption = event.getCaption();
        final ListColumnDescriptor<?, ?> columnDescriptorToBeRemoved = descriptorsByCaption.remove(caption);
        listColumnDescriptors.remove(columnDescriptorToBeRemoved);
        rebuildSimpleList();
        final ColumnDescriptorRemovedEvent removeColumnEvent = new ColumnDescriptorRemovedEvent(this, caption, event.getTableName());
        customDescriptorHolderByCaption.remove(caption);
        eventBus.fireEvent(removeColumnEvent);
    }

    public Map<String, ListColumnDescriptor<?, ?>> getDescriptorsByCaption() {
        return descriptorsByCaption;
    }

    public Map<String, ColumnDescriptorFieldHolder> getCustomDescriptorHolderByCaption() {
        return customDescriptorHolderByCaption;
    }

    @Override
    public void onAddCustomColumnDescriptor(final AddCustomColumnDescriptorEvent event) {
        FormField formField;
        final Class<?> fieldType = event.getDescriptorHolder().getFieldType();
        final String caption = event.getDescriptorHolder().getCaption();
        final String fieldPath = event.getDescriptorHolder().getFieldPath();
        if (fieldType.equals(Boolean.class)) {
            final ListBoxFormFieldRenderer formFieldRenderer = new ListBoxFormFieldRenderer();
            formFieldRenderer.addItem(Boolean.TRUE.toString(), Boolean.TRUE);
            formFieldRenderer.addItem(Boolean.FALSE.toString(), Boolean.FALSE);
            formField = new FormField(formFieldRenderer);
        } else if (fieldType.equals(Date.class)) {
            formField = new FormField(new DateBoxFormFieldRenderer());
        } else if (fieldType.equals(Integer.class)) {
            formField = new FormField() {

                @Override
                public Object getValue() {
                    if (super.getValue() != null) return getIntegerValue();
                    return null;
                }
            };
        } else if (fieldType.equals(Long.class)) {
            formField = new FormField() {

                @Override
                public Object getValue() {
                    if (super.getValue() != null) return getLongValue();
                    return null;
                }
            };
        } else if (fieldType.equals(Double.class)) {
            formField = new FormField() {

                @Override
                public Object getValue() {
                    if (super.getValue() != null) return getDoubleValue();
                    return null;
                }
            };
        } else formField = new FormField();
        final ComplexHeaderCellRenderer headerCellRenderer = new ComplexHeaderCellRenderer(caption, formField, fieldPath);
        final ListColumnDescriptor<D, Object> columnDescriptor = new ListColumnDescriptor<D, Object>();
        columnDescriptor.setHeaderCellRenderer(headerCellRenderer);
        columnDescriptor.setCustom(true);
        columnDescriptor.setValueProvider(new BeanValueProvider<D, Object>(fieldPath));
        addDescriptor(columnDescriptor);
        registerSearchCriteria(new Criterion(fieldPath), formField);
        final ColumnDescriptorFieldHolder descriptorHolder = new ColumnDescriptorFieldHolder(caption, fieldPath, fieldType, complexListConfiguration.getTableName());
        customDescriptorHolderByCaption.put(caption, descriptorHolder);
        rebuildSimpleList();
        eventBus.fireEvent(new CustomColumnDescriptorAddedEvent(ComplexListActivity.this, descriptorHolder));
    }

    public void setColumnDescriptorFormHandler(final ShowCustomColumnDescriptorFormHandler columnDescriptorFormHandler) {
        this.columnDescriptorFormHandler = columnDescriptorFormHandler;
    }

    public void setDateFormater(final SimpleDateFormat formater) {
        this.dateFormater = formater;
    }

}
