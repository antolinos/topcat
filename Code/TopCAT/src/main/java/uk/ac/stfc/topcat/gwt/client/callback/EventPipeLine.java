/**
 * 
 * Copyright (c) 2009-2010
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer
 * in the documentation and/or other materials provided with the distribution.
 * Neither the name of the STFC nor the names of its contributors may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY 
 * OF SUCH DAMAGE.
 */
package uk.ac.stfc.topcat.gwt.client.callback;

/**
 * Imports
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uk.ac.stfc.topcat.core.gwt.module.TAdvancedSearchDetails;
import uk.ac.stfc.topcat.core.gwt.module.TFacility;
import uk.ac.stfc.topcat.core.gwt.module.TInvestigation;
import uk.ac.stfc.topcat.gwt.client.Constants;
import uk.ac.stfc.topcat.gwt.client.LoginInterface;
import uk.ac.stfc.topcat.gwt.client.LoginService;
import uk.ac.stfc.topcat.gwt.client.LoginServiceAsync;
import uk.ac.stfc.topcat.gwt.client.SearchService;
import uk.ac.stfc.topcat.gwt.client.SearchServiceAsync;
import uk.ac.stfc.topcat.gwt.client.TOPCATOnline;
import uk.ac.stfc.topcat.gwt.client.UtilityService;
import uk.ac.stfc.topcat.gwt.client.UtilityServiceAsync;
import uk.ac.stfc.topcat.gwt.client.exception.WindowsNotAvailableExcecption;
import uk.ac.stfc.topcat.gwt.client.manager.HistoryManager;
import uk.ac.stfc.topcat.gwt.client.manager.TopcatWindowManager;
import uk.ac.stfc.topcat.gwt.client.model.DatafileModel;
import uk.ac.stfc.topcat.gwt.client.model.DatasetModel;
import uk.ac.stfc.topcat.gwt.client.model.DownloadModel;
import uk.ac.stfc.topcat.gwt.client.model.Facility;
import uk.ac.stfc.topcat.gwt.client.model.Instrument;
import uk.ac.stfc.topcat.gwt.client.model.InvestigationType;
import uk.ac.stfc.topcat.gwt.client.model.TopcatInvestigation;
import uk.ac.stfc.topcat.gwt.client.widget.DatafileWindow;
import uk.ac.stfc.topcat.gwt.client.widget.DatasetWindow;
import uk.ac.stfc.topcat.gwt.client.widget.DownloadWindow;
import uk.ac.stfc.topcat.gwt.client.widget.LoginInfoPanel;
import uk.ac.stfc.topcat.gwt.client.widget.LoginPanel;
import uk.ac.stfc.topcat.gwt.client.widget.LoginWidget;
import uk.ac.stfc.topcat.gwt.client.widget.ParameterDownloadForm;
import uk.ac.stfc.topcat.gwt.client.widget.ParameterWindow;
import uk.ac.stfc.topcat.gwt.client.widget.WaitDialog;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * This is import class which holds all the events. currently doesn't directly
 * inherit from handler manager but a beginning to move in right direction.
 * 
 * This is a singleton and Most of the widget will call this instance to login,
 * search, download, history etc..
 * <p>
 * 
 * @author Mr. Srikanth Nagella
 * @version 1.0, &nbsp; 30-APR-2010
 * @since iCAT Version 3.3
 */
public class EventPipeLine implements LoginInterface {
    private final LoginServiceAsync loginService = GWT.create(LoginService.class);
    private final UtilityServiceAsync utilityService = GWT.create(UtilityService.class);
    private final SearchServiceAsync searchService = GWT.create(SearchService.class);
    ArrayList<TFacility> facilityNames;
    HashMap<String, ListStore<Instrument>> facilityInstrumentMap;
    ParameterDownloadForm paramDownloadForm;
    private Set<String> loadedFacilities = new HashSet<String>();
    private TopcatEvents tcEvents;

    LoginWidget loginWidget;
    private WaitDialog retrievingDataDialog;
    private WaitDialog waitDialog;
    private int downloadCount = 0;
    private int retrievingDataDialogCount = 0;

    // Panels from Main Window
    TOPCATOnline mainWindow = null;
    LoginPanel loginPanel = null;
    TopcatWindowManager tcWindowManager = null;
    HistoryManager historyManager = null;

    private static EventPipeLine eventBus = new EventPipeLine();

    /**
     * Private constructor to make a singleton
     */
    private EventPipeLine() {
        loginWidget = new LoginWidget();
        tcWindowManager = new TopcatWindowManager();
        historyManager = new HistoryManager(tcWindowManager);
        retrievingDataDialog = new WaitDialog();
        retrievingDataDialog.setMessage("  Retrieving data...");
        waitDialog = new WaitDialog();
        facilityInstrumentMap = new HashMap<String, ListStore<Instrument>>();
        tcEvents = new TopcatEvents();
        // Initialise
        loginWidget.setLoginHandler(this);
    }

    public static EventPipeLine getInstance() {
        return eventBus;
    }

    public void setMainWindow(TOPCATOnline topcatOnline) {
        mainWindow = topcatOnline;
    }

    public void initDownloadParameter() {
        paramDownloadForm = new ParameterDownloadForm();
        RootPanel.get().add(paramDownloadForm);
    }

    /**
     * @return history manager
     */
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    /**
     * @return the login panel
     */
    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    /**
     * Set the login info panel
     * 
     * @param loginPanel
     */
    public void setLoginPanel(LoginPanel loginPanel) {
        this.loginPanel = loginPanel;
    }

    /**
     * Callback on login dialog cancel button
     */
    @Override
    public void onLoginCancel() {
        loginWidget.hide();
    }

    /**
     * Callback on login dialog ok button.
     */
    @Override
    public void onLoginOk(String facilityName, String username, String password) {
        loginWidget.hide();
        waitDialog.setMessage(" Logging In...");
        waitDialog.show();
        // login to the given facility using username and password
        loginService.login(username, password, facilityName, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                // Show the RPC error message to the user
                waitDialog.hide();
                failureLogin(loginWidget.getFacilityName());
            }

            @Override
            public void onSuccess(String result) {
                waitDialog.hide();
                successLogin(loginWidget.getFacilityName());
            }
        });
    }

    /**
     * This method checks whether the user has logged into servers or not.
     */
    public void checkLoginStatus() {
        if (facilityNames != null) {
            for (TFacility facilityName : facilityNames) {
                loginService.isUserLoggedIn(facilityName.getName(), new LoginValidCallback(facilityName.getName()));
            }
        }
    }

    /**
     * This method invokes the AJAX call to get the server logo
     */
    public void getLogoURL() {
        utilityService.getLogoURL(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(String result) {
                mainWindow.getHeaderPanel().setLogoURL(result);
            }
        });
    }

    /**
     * This method invokes the AJAX call to get the links for the footer
     */
    public void getLinks() {
        utilityService.getLinks(new AsyncCallback<Map<String, String>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Map<String, String> result) {
                mainWindow.getFooterPanel().setLinks(result);
            }
        });
    }

    /**
     * This method shows login dialog box for a input facility
     * 
     * @param facilityName
     */
    public void showLoginWidget(String facilityName) {
        loginWidget.setFacilityName(facilityName);
        loginWidget.show();
    }

    /**
     * This method is called after the success of logging into facility to
     * update instrument and investigation type list for the facility and user
     * investigations and downloads in the facility
     * 
     * @param facilityName
     */
    public void successLogin(String facilityName) {
        updateLoginPanelStatus(facilityName, Boolean.TRUE);
    }

    /**
     * This method will update the login button of facility name to show that
     * its logged in or not. In the case of the browser refresh button having
     * been pressed facility data will be reloaded.
     * 
     * @param facilityName
     */
    public void updateLoginPanelStatus(String facilityName, Boolean status) {
        LoginInfoPanel infoPanel = loginPanel.getFacilityLoginInfoPanel(facilityName);
        if (status.booleanValue() == true) {
            infoPanel.successLogin();
            // Handle case when browser refresh button is pressed.
            // Without this check the data will be reloaded every time you click
            // on a tab in the main panel.
            if (!loadedFacilities.contains(facilityName)) {
                loadedFacilities.add(facilityName);
                loadFacilityData(facilityName);
            }
        } else {
            successLogout(facilityName);
        }
        if (facilityNames.size() > 0 && facilityNames.get(0).getName().compareToIgnoreCase(facilityName) == 0)
            showInitialLoginWindow();
    }

    private void loadFacilityData(String facilityName) {
        showRetrievingData();
        loadInstrumentNames(facilityName);
        loadInvestigationTypes(facilityName);
        getMyInvestigationsInMyDataPanel(facilityName);
        getMyDownloadsInMyDownloadsPanel(facilityName);
        historyManager.updateHistory();
        // if the facility search has not been set, set it
        if (mainWindow.getMainPanel().getSearchPanel().getFacilitiesSearchSubPanel().getFacilityWidget().getItemCount() == 0) {
            mainWindow.getMainPanel().getSearchPanel().getFacilitiesSearchSubPanel().setFacilitySearch(facilityName);
        }
        hideRetrievingData();
    }

    /**
     * This method is called for the success of logging out facility
     * 
     * @param facilityName
     */
    private void successLogout(String facilityName) {
        LoginInfoPanel infoPanel = loginPanel.getFacilityLoginInfoPanel(facilityName);
        infoPanel.successLogout();
        mainWindow.getMainPanel().getMyDataPanel().clearInvestigationList(facilityName);
        mainWindow.getMainPanel().getMyDownloadPanel().clearDownloads(facilityName);
        loadedFacilities.remove(facilityName);
    }

    /**
     * This method is invoked for the failure to login to facility service,
     * shows an error dialog to check the login details.
     * 
     * @param facilityName
     */
    public void failureLogin(String facilityName) {
        // Process the failure of login
        LoginInfoPanel infoPanel = loginPanel.getFacilityLoginInfoPanel(facilityName);
        infoPanel.successLogout();
        loginWidget.show();
        // Show an error message.
        showErrorDialog("Error logging in.  Please check username and password");
    }

    /**
     * This method logs out user from a input facility
     * 
     * @param facilityName
     */
    public void facilityLogout(String facilityName) {
        waitDialog.setMessage(" Logging Out...");
        waitDialog.show();
        loginWidget.setFacilityName(facilityName);

        // logout of the given facility
        loginService.logout(facilityName, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                // Show the RPC error message to the user
                waitDialog.hide();
            }

            @Override
            public void onSuccess(Void result) {
                waitDialog.hide();
                successLogout(loginWidget.getFacilityName());
            }
        });
    }

    /**
     * This method loads available facility from TopCAT service
     */
    public void loadFacilityNames() {
        utilityService.getFacilities(new AsyncCallback<ArrayList<TFacility>>() {
            @Override
            public void onFailure(Throwable caught) {
                showErrorDialog("Error while downloading facility names");
            }

            @Override
            public void onSuccess(ArrayList<TFacility> result) {
                facilityNames = result;
                // Update widgets
                updateFacilityListWidgets();
                loadLoginPanel();
                loadInstrumentNames();
                loadInvestigationTypes();
                checkLoginStatus();
            }

        });
    }

    /**
     * This method loads instrument names from TopCAT service
     */
    private void loadInstrumentNames() {
        for (TFacility facility : facilityNames) {
            utilityService.getInstrumentNames(facility.getName(), new InstrumentCallback(facility.getName(), this));
        }
    }

    /**
     * This method loads investigation types from TopCAT service
     */
    private void loadInvestigationTypes() {
        for (TFacility facility : facilityNames) {
            utilityService.getInvestigationTypes(facility.getName(), new InvestigationTypeCallback(facility.getName(),
                    this));
        }
    }

    /**
     * This method loads instrument names for a facility from TopCAT service
     * 
     * @param facilityName
     */
    private void loadInstrumentNames(String facilityName) {
        for (TFacility facility : facilityNames) {
            if (facility.getName().compareToIgnoreCase(facilityName) == 0)
                utilityService.getInstrumentNames(facility.getName(), new InstrumentCallback(facility.getName(), this));
        }
    }

    /**
     * This method loads investigation types for a facility from TopCAT service
     * 
     * @param facilityName
     */
    private void loadInvestigationTypes(String facilityName) {
        for (TFacility facility : facilityNames) {
            if (facility.getName().compareToIgnoreCase(facilityName) == 0)
                utilityService.getInvestigationTypes(facility.getName(),
                        new InvestigationTypeCallback(facility.getName(), this));
        }
    }

    /**
     * Updates all the facility list widgets
     */
    private void updateFacilityListWidgets() {
        ComboBox<Facility> cf = mainWindow.getMainPanel().getSearchPanel().getFacilitiesSearchSubPanel()
                .getComboBoxFacility();
        ArrayList<Facility> facilitiesList = new ArrayList<Facility>();
        for (TFacility facility : facilityNames) {
            facilitiesList.add(new Facility(facility.getName(), facility.getPluginName()));
        }
        mainWindow.getMainPanel().getSearchPanel().getAdvancedSearchSubPanel().setFacilityList(facilitiesList);
        cf.getStore().add(facilitiesList);
    }

    /**
     * This method sets the instrument list for a facility
     * 
     * @param facility
     * @param instruments
     */
    public void setInstrumentList(String facility, ArrayList<Instrument> instruments) {
        ListStore<Instrument> fInstruments = getFacilityInstruments(facility);
        fInstruments.removeAll();
        fInstruments.add(instruments);
    }

    /**
     * This method gets all the instrument details for input facility
     * 
     * @param facility
     * @return
     */
    public ListStore<Instrument> getFacilityInstruments(String facility) {
        ListStore<Instrument> fInstruments = facilityInstrumentMap.get(facility);
        if (fInstruments == null) { // New Facility
            fInstruments = new ListStore<Instrument>();
            facilityInstrumentMap.put(facility, fInstruments);
        }
        return fInstruments;
    }

    /**
     * This method updates the facility instrument list for the input facility
     * 
     * @param facility
     * @param instruments
     */
    public void updateFacilityInstrumentListWidgets(String facility, ArrayList<Instrument> instruments) {
        mainWindow.getMainPanel().getSearchPanel().getAdvancedSearchSubPanel()
                .setFacilityInstrumentList(facility, instruments);
        setInstrumentList(facility, instruments);
    }

    /**
     * This method updates Investigation types list widgets
     * 
     * @param facility
     * @param investigationTypes
     */
    public void updateFacilityInvestigationTypeListWidgets(String facility,
            ArrayList<InvestigationType> investigationTypes) {
        mainWindow.getMainPanel().getSearchPanel().getAdvancedSearchSubPanel()
                .setFacilityInvestigationTypeList(facility, investigationTypes);
    }

    /**
     * This method creates the facility login information panels
     */
    private void loadLoginPanel() {
        mainWindow.getHeaderPanel().getLoginPanel().createICATLoginLinks(this, facilityNames);
    }

    /**
     * This method searches for all the investigations that matches given search
     * details
     * 
     * @param searchDetails
     */
    public void searchForInvestigation(TAdvancedSearchDetails searchDetails) {
        waitDialog.setMessage("  Searching...");
        waitDialog.show();
        searchService.getAdvancedSearchResultsInvestigation(null, searchDetails,
                new AsyncCallback<List<TInvestigation>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        waitDialog.hide();
                        showErrorDialog("Error retrieving data from server");
                    }

                    @Override
                    public void onSuccess(List<TInvestigation> result) {
                        ArrayList<TopcatInvestigation> invList = new ArrayList<TopcatInvestigation>();
                        if (result != null) {
                            for (TInvestigation inv : result)
                                invList.add(new TopcatInvestigation(inv.getServerName(), inv.getInvestigationId(), inv
                                        .getInvestigationName(), inv.getTitle(), inv.getVisitId(), inv.getStartDate(),
                                        inv.getEndDate()));
                        }
                        waitDialog.hide();
                        mainWindow.getMainPanel().getSearchPanel().setInvestigations(invList);
                    }
                });
    }

    /**
     * This method searches for the user investigations that matches given
     * search details
     * 
     * @param searchDetails
     */
    public void searchForMyInvestigation(TAdvancedSearchDetails searchDetails) {
        waitDialog.setMessage("  Searching...");
        waitDialog.show();
        searchService.getSearchResultsMyInvestigationFromKeywords(null, searchDetails.getKeywords(),
                new AsyncCallback<List<TInvestigation>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        waitDialog.hide();
                        showErrorDialog("Error retrieving data from server");
                    }

                    @Override
                    public void onSuccess(List<TInvestigation> result) {
                        ArrayList<TopcatInvestigation> invList = new ArrayList<TopcatInvestigation>();
                        if (result != null) {
                            for (TInvestigation inv : result)
                                invList.add(new TopcatInvestigation(inv.getServerName(), inv.getInvestigationId(), inv
                                        .getInvestigationName(), inv.getTitle(), inv.getVisitId(), inv.getStartDate(),
                                        inv.getEndDate()));
                        }
                        waitDialog.hide();
                        mainWindow.getMainPanel().getSearchPanel().setInvestigations(invList);
                    }
                });
    }

    /**
     * This method searches for the user investigations that belongs to user
     */
    public void getMyInvestigationsInMyDataPanel() {
        waitDialog.setMessage("Getting Investigations...");
        waitDialog.show();
        // NOTE: Working without this
        for (TFacility facilityName : facilityNames) {
            getMyInvestigationsInMyDataPanel(facilityName.getName());
        }

    }

    /**
     * This method searches for the user investigations that belongs to user in
     * given facility
     * 
     * @param facilityName
     */
    public void getMyInvestigationsInMyDataPanel(final String facilityName) {
        showRetrievingData();
        utilityService.getMyInvestigationsInServer(facilityName, new AsyncCallback<ArrayList<TInvestigation>>() {
            @Override
            public void onFailure(Throwable caught) {
                hideRetrievingData();
                showErrorDialog("Error retrieving data from server");
            }

            @Override
            public void onSuccess(ArrayList<TInvestigation> result) {
                ArrayList<TopcatInvestigation> invList = new ArrayList<TopcatInvestigation>();
                if (result != null) {
                    for (TInvestigation inv : result)
                        invList.add(new TopcatInvestigation(inv.getServerName(), inv.getInvestigationId(), inv
                                .getInvestigationName(), inv.getTitle(), inv.getVisitId(), inv.getStartDate(), inv
                                .getEndDate()));
                }
                hideRetrievingData();
                mainWindow.getMainPanel().getMyDataPanel().addInvestigations(facilityName, invList);
            }
        });

    }

    /**
     * This method loads download data for a facility from TopCAT service
     * 
     * @param facilityName
     */
    private void getMyDownloadsInMyDownloadsPanel(String facilityName) {
        final Set<String> facilities = new HashSet<String>(1);
        facilities.add(facilityName);
        showRetrievingData();
        utilityService.getMyDownloadList(facilities, new AsyncCallback<ArrayList<DownloadModel>>() {
            @Override
            public void onFailure(Throwable caught) {
                hideRetrievingData();
                showMessageDialog("Error retrieving download history from server");
            }

            @Override
            public void onSuccess(ArrayList<DownloadModel> result) {
                mainWindow.getMainPanel().getMyDownloadPanel().addDownloads(result);
                hideRetrievingData();
                for (Iterator<DownloadModel> it = result.iterator(); it.hasNext();) {
                    if (!it.next().getStatus().equals(Constants.STATUS_IN_PROGRESS)) {
                        it.remove();
                    }
                }
                if (result.size() > 0) {
                    waitForDownloads(result);
                }
            }
        });
    }

    /**
     * Show an alert Dialog box
     * 
     * @param msg
     *            message in the dialog box
     */
    public void showErrorDialog(String msg) {
        MessageBox.alert("Error", msg, null);
    }

    /**
     * Show an info Dialog box
     * 
     * @param msg
     *            message in the dialog box
     */
    public void showMessageDialog(String msg) {
        MessageBox.info("Information", msg, null);
    }

    /**
     * Download all the datafiles from the facility for the given ids.
     * 
     * @param facilityName
     *            a string containing the facility name
     * @param datafileList
     *            a list containing data file ids
     */
    public void downloadDatafiles(final String facilityName, final List<Long> datafileList, final String downloadName) {
        utilityService.getDatafilesDownloadURL(facilityName, (ArrayList<Long>) datafileList, downloadName,
                new AsyncCallback<DownloadModel>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        showErrorDialog("Error retrieving data from server");
                    }

                    @Override
                    public void onSuccess(DownloadModel result) {
                        mainWindow.getMainPanel().getMyDownloadPanel().addDownload(result);
                        if (result.getStatus().equalsIgnoreCase(Constants.STATUS_IN_PROGRESS)) {
                            waitForDownload(result);
                        } else {
                            download(facilityName, result.getUrl());
                        }
                    }
                });
    }

    /**
     * Download the dataset from the facility.
     * 
     * @param facilityName
     *            a string containing the facility name
     * @param datasetId
     *            the data set id
     * @param downloadName
     *            a string containing a user defined name
     */
    public void downloadDatasets(final String facilityName, final Long datasetId, final String downloadName) {
        utilityService.getDatasetDownloadURL(facilityName, datasetId, downloadName, new AsyncCallback<DownloadModel>() {
            @Override
            public void onFailure(Throwable caught) {
                showErrorDialog("Error retrieving data from server");
            }

            @Override
            public void onSuccess(DownloadModel result) {
                mainWindow.getMainPanel().getMyDownloadPanel().addDownload(result);
                if (result.getStatus().equalsIgnoreCase(Constants.STATUS_IN_PROGRESS)) {
                    waitForDownload(result);
                } else {
                    download(facilityName, result.getUrl());
                }
            }
        });
    }

    /**
     * Call the server which will poll the download service until a status of
     * 'available' or 'error' is received or the call times out.
     * 
     * @param downloadModel
     */
    private void waitForDownload(final DownloadModel downloadModel) {
        utilityService.waitForFinalDownloadStatus(downloadModel, new AsyncCallback<DownloadModel>() {
            @Override
            public void onFailure(Throwable caught) {
                if (!loadedFacilities.contains(downloadModel.getFacilityName())) {
                    // session logged out so do not continue
                    return;
                }
                showErrorDialog("Error retrieving data from server for download " + downloadModel.getDownloadName());
            }

            @Override
            public void onSuccess(DownloadModel result) {
                if (!loadedFacilities.contains(result.getFacilityName())) {
                    // session logged out so do not continue
                    return;
                }
                if (result.getStatus().equalsIgnoreCase(Constants.STATUS_AVAILABLE)) {
                    download(result.getFacilityName(), result.getUrl());
                    mainWindow.getMainPanel().getMyDownloadPanel().refresh();
                } else if (result.getStatus().equalsIgnoreCase(Constants.STATUS_ERROR)) {
                    showErrorDialog("Error retrieving data from server for download " + result.getDownloadName());
                    mainWindow.getMainPanel().getMyDownloadPanel().refresh();
                } else if (result.getStatus().equalsIgnoreCase(Constants.STATUS_IN_PROGRESS)) {
                    waitForDownload(result);
                }
            }
        });
    }

    /**
     * Call the server which will poll the download service until a status of
     * 'available' or 'error' is received for one or more of the downloads or
     * the call times out. At which point the server will return a list of
     * downloads that have not finished. This allows the client to upload the
     * status of those that have finished, a new waitForDownloads has to called
     * with the outstanding downloads.
     * 
     * @param downloadModel
     */
    private void waitForDownloads(final List<DownloadModel> downloadModels) {
        utilityService.waitForFinalDownloadStati(downloadModels, new AsyncCallback<List<DownloadModel>>() {
            @Override
            public void onFailure(Throwable caught) {
                // remove items if we have logged out of the facility
                for (Iterator<DownloadModel> it = downloadModels.iterator(); it.hasNext();) {
                    if (!loadedFacilities.contains(it.next().getFacilityName())) {
                        it.remove();
                    }
                }
                if (downloadModels.size() > 0) {
                    showErrorDialog("Error retrieving data from server" + caught.toString());
                }
            }

            @Override
            public void onSuccess(List<DownloadModel> remainingDownloads) {
                // only refresh if item has been downloaded
                if (remainingDownloads.size() < downloadModels.size()) {
                    mainWindow.getMainPanel().getMyDownloadPanel().refresh();
                }
                // remove items if we have logged out of the facility
                for (Iterator<DownloadModel> it = remainingDownloads.iterator(); it.hasNext();) {
                    if (!loadedFacilities.contains(it.next().getFacilityName())) {
                        it.remove();
                    }
                }
                // issue callback to get remaining status
                if (remainingDownloads.size() > 0) {
                    waitForDownloads(remainingDownloads);
                }
            }
        });
    }

    /**
     * Download the url in a separate window.
     * 
     * @param url
     */
    public void download(String facilityName, final String url) {
        if (!loadedFacilities.contains(facilityName)) {
            // session logged out so do not download
            return;
        }
        DOM.setElementAttribute(RootPanel.get("__download" + downloadCount).getElement(), "src", url);
        downloadCount = downloadCount + 1;
        if (downloadCount > (Constants.MAX_DOWNLOAD_FRAMES - 1)) {
            downloadCount = 0;
        }
    }

    /**
     * AJAX call to search of datafiles for the input search details in the
     * given facility
     * 
     * @param facilityName
     *            facility name
     * @param searchDetails
     *            search details
     */
    public void searchForDatafiles(String facilityName, TAdvancedSearchDetails searchDetails) {
        waitDialog.setMessage("  Searching...");
        waitDialog.show();
        searchService.getAdvancedSearchResultsDatafile(null, facilityName, searchDetails,
                new AsyncCallback<ArrayList<DatafileModel>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        waitDialog.hide();
                        showErrorDialog("Error retrieving data from server");
                    }

                    @Override
                    // On success opens a datafile window to show the results
                    public void onSuccess(ArrayList<DatafileModel> result) {
                        waitDialog.hide();
                        try {
                            DatafileWindow datafileWindow = tcWindowManager.createDatafileWindow();
                            datafileWindow.setDatafileList(result);
                            datafileWindow.show();
                            datafileWindow.setHistoryVerified(true);
                        } catch (WindowsNotAvailableExcecption e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * This method will show the dataset window for the given facility name,
     * investigation id
     * 
     * @param facilityName
     *            facility name
     * @param investigationId
     *            investigation id
     * @param investigationName
     *            investigation name
     */
    public void showDatasetWindow(String facilityName, String investigationId, String investigationName) {
        try {
            DatasetWindow datasetWindow = tcWindowManager.createDatasetWindow();
            datasetWindow.setInvestigationTitle(investigationName);
            datasetWindow.setDataset(facilityName, investigationId);
            datasetWindow.show();
            datasetWindow.setHistoryVerified(true);
        } catch (WindowsNotAvailableExcecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method will show dataset window for the given input facility name,
     * investigation id and also updates the browser history
     * 
     * @param facilityName
     *            facility name
     * @param investigationId
     *            investigation id
     * @param investigationName
     *            investigation name
     */
    public void showDatasetWindowWithHistory(String facilityName, String investigationId, String investigationName) {
        showDatasetWindow(facilityName, investigationId, investigationName);
        historyManager.updateHistory();
    }

    /**
     * This method will show the datafile window for the given input dataset
     * models.
     * 
     * @param datasetModel
     *            list of dataset models
     */
    public void showDatafileWindow(ArrayList<DatasetModel> datasetModel) {
        try {
            DatafileWindow datafileWindow = tcWindowManager.createDatafileWindow();
            datafileWindow.setDatasets(datasetModel);
            datafileWindow.show();
            datafileWindow.setHistoryVerified(true);
        } catch (WindowsNotAvailableExcecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method will show the datafile window and also update the browser
     * history.
     * 
     * @param datasetModel
     *            list of dataset models
     */
    public void showDatafileWindowWithHistory(ArrayList<DatasetModel> datasetModel) {
        showDatafileWindow(datasetModel);
        historyManager.updateHistory();
    }

    /**
     * This method will show the parameter window for the given inputs of
     * facility name, datafile id
     * 
     * @param facilityName
     *            facility name
     * @param datafileId
     *            datafile id
     * @param datafileName
     *            datafile name
     */
    public void showParameterWindow(String facilityName, String datafileId, String datafileName) {
        try {
            ParameterWindow paramWindow = tcWindowManager.createParameterWindow();
            paramWindow.setDatafileName(datafileName);
            paramWindow.setDatafileInfo(facilityName, datafileId);
            paramWindow.show();
            paramWindow.setHistoryVerified(true);
        } catch (WindowsNotAvailableExcecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method will call show parameter window and also update the browser
     * history.
     * 
     * @param facilityName
     *            facility name
     * @param datafileId
     *            datafile id
     * @param datafileName
     *            datafile name
     */
    public void showParameterWindowWithHistory(String facilityName, String datafileId, String datafileName) {
        showParameterWindow(facilityName, datafileId, datafileName);
        historyManager.updateHistory();
    }

    /**
     * This method will show a download window for the given url and also
     * updates the browser history
     * 
     * @param url
     *            url of the download page
     */
    public void showDownloadWindow(String url) {
        try {
            ;
            DownloadWindow downloadWindow = tcWindowManager.createDownloadWindow();
            downloadWindow.setDownloadUrl(url);
            downloadWindow.show();
            downloadWindow.setHistoryVerified(true);
        } catch (WindowsNotAvailableExcecption e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * This method will show a download window for the given url and also
     * updates the browser history
     * 
     * @param url
     *            url of the download page
     */
    public void showDownloadWindowWithHistory(String url) {
        showDownloadWindow(url);
        historyManager.updateHistory();
    }

    /**
     * This method will take the input facility name and datafile id and
     * downloads the parameter files in CSV format.
     * 
     * @param facilityName
     *            facility name
     * @param datafileId
     *            datafile id
     */
    public void downloadParametersData(String facilityName, String datafileId) {
        // set the invisible form for parameter download
        // RootPanel.get("__downloadParameterForm").add(paramDownloadForm);
        // construct servlet request to call copydatatocsvfile servlet
        paramDownloadForm.setFacilityName(facilityName);
        paramDownloadForm.setDatafileId(datafileId);
        paramDownloadForm.submit();
        // RootPanel.get("__downloadParameterForm").remove(paramDownloadForm);
    }

    /**
     * @return the main window
     */
    public TOPCATOnline getMainWindow() {
        return mainWindow;
    }

    /**
     * This method will show the login window when the application starts up.
     */
    private void showInitialLoginWindow() {
        // Check all login's whether at least one of them is logged in
        boolean loggedIn = false;
        for (TFacility facility : facilityNames) {
            if (loginPanel.getFacilityLoginInfoPanel(facility.getName()).isValidLogin()) {
                loggedIn = true;
                break;
            }

        }
        if (facilityNames.size() > 0 && !loggedIn)
            showLoginWidget(facilityNames.get(0).getName());
    }

    /**
     * Show the dialog box.
     */
    public void showRetrievingData() {
        if (retrievingDataDialogCount == 0) {
            retrievingDataDialog.show();
        }
        retrievingDataDialogCount = retrievingDataDialogCount + 1;
    }

    /**
     * Hide the dialog box.
     */
    public void hideRetrievingData() {
        retrievingDataDialogCount = retrievingDataDialogCount - 1;
        if (retrievingDataDialogCount < 1) {
            retrievingDataDialog.hide();
            retrievingDataDialogCount = 0;
        }
    }

    public ArrayList<TFacility> getFacilityNames() {
        return facilityNames;
    }

    /**
     * Get the topcat events object. Use this to listen for and fire events.
     * 
     * @return the topcat events object
     */
    public TopcatEvents getTcEvents() {
        return tcEvents;
    }

}
