
(function(){
    'use strict';

    var app = angular.module('topcat');

    app.controller('DownloadsController', function($state, $scope, $translate, $uibModalInstance, $q, $interval, tc, uiGridConstants, helpers){
        var that = this;
        var pagingConfig = tc.config().paging;
        var timeout = $q.defer();
        $scope.$on('$destroy', function(){ timeout.resolve(); });
        this.isScroll = pagingConfig.pagingType == 'scroll';
        this.gridOptions = _.merge({data: [], appScopeProvider: this}, tc.config().myDownloads.gridOptions);
        helpers.setupTopcatGridOptions(this.gridOptions, 'download');
        this.gridOptions.useExternalPagination =  false;
        this.gridOptions.useExternalSorting =  false;
        this.gridOptions.useExternalFiltering =  false;
        this.gridOptions.columnDefs.push({
            name : 'actions',
            title: 'DOWNLOAD.COLUMN.ACTIONS',
            enableFiltering: false,
            enable: false,
            enableColumnMenu: false,
            enableSorting: false,
            enableHiding: false,
            cellTemplate : [
                '<div class="ui-grid-cell-contents">',
                    '<span ng-if="row.entity.transport == \'https\'">',
                        '<a ng-if="row.entity.status == \'COMPLETE\'" href="{{row.entity.transportUrl + \'/ids/getData?preparedId=\' + row.entity.preparedId + \'&outname=\' + row.entity.fileName}}" translate="DOWNLOAD.ACTIONS.LINK.HTTP_DOWNLOAD.TEXT" class="btn btn-primary btn-xs" uib-tooltip="{{\'DOWNLOAD.ACTIONS.LINK.HTTP_DOWNLOAD.TOOLTIP.TEXT\' | translate}}" tooltip-placement="left" tooltip-append-to-body="true"></a>',
                        '<span ng-if="row.entity.status != \'COMPLETE\'" class="inline-block" uib-tooltip="{{\'DOWNLOAD.ACTIONS.LINK.NON_ACTIVE_DOWNLOAD.TOOLTIP.TEXT\' | translate}}" tooltip-placement="left" tooltip-append-to-body="true"><button translate="DOWNLOAD.ACTIONS.LINK.NON_ACTIVE_DOWNLOAD.TEXT" class="btn btn-primary btn-xs disabled"></button></span>',
                    '</span> ',
                    '<a ng-if="row.entity.transport == \'globus\'" href="#globus-help" target="_blank" translate="DOWNLOAD.ACTIONS.LINK.GLOBUS_DOWNLOAD.TEXT" class="btn btn-primary btn-xs" uib-tooltip="{{\'DOWNLOAD.ACTIONS.LINK.GLOBUS_DOWNLOAD.TOOLTIP.TEXT\' | translate}}" tooltip-placement="left" tooltip-append-to-body="true"></a> ',
                    '<span ng-if="row.entity.transport == \'smartclient\'">',
                        '<a ng-if="row.entity.status == \'COMPLETE\'" href="#smartclient-complete" target="_blank" translate="DOWNLOAD.ACTIONS.LINK.SMARTCLIENT_COMPLETE.TEXT" class="btn btn-primary btn-xs" uib-tooltip="{{\'DOWNLOAD.ACTIONS.LINK.SMARTCLIENT_COMPLETE.TOOLTIP.TEXT\' | translate}}" tooltip-placement="left" tooltip-append-to-body="true"></a>',
                        '<span ng-if="row.entity.status != \'COMPLETE\'">',
                            '<a ng-if="!row.entity.isServer" href="#smartclient-start-server" target="_blank" translate="DOWNLOAD.ACTIONS.LINK.SMARTCLIENT_START_SERVER.TEXT" class="btn btn-primary btn-xs" uib-tooltip="{{\'DOWNLOAD.ACTIONS.LINK.SMARTCLIENT_START_SERVER.TOOLTIP.TEXT\' | translate}}" tooltip-placement="left" tooltip-append-to-body="true"></a>',
                            '<span ng-if="row.entity.isServer" class="inline-block" uib-tooltip="{{\'DOWNLOAD.ACTIONS.LINK.SMARTCLIENT_NOT_COMPLETE.TOOLTIP.TEXT\' | translate}}" tooltip-placement="left" tooltip-append-to-body="true"><button translate="DOWNLOAD.ACTIONS.LINK.SMARTCLIENT_NOT_COMPLETE.TEXT" class="btn btn-primary btn-xs disabled"></button></span>',
                        '</span> ',
                    '</span>',
                    '<span class="remove-download">', 
                        '<a ng-click="grid.appScope.remove(row.entity)" translate="DOWNLOAD.ACTIONS.LINK.REMOVE.TEXT" class="btn btn-primary btn-xs" uib-tooltip="' + $translate.instant('DOWNLOAD.ACTIONS.LINK.REMOVE.TOOLTIP.TEXT') + '" tooltip-placement="left" tooltip-append-to-body="true"></a>',
                    '</span>',
                '</div>'
            ].join('')
        });


        var refreshPromise = $interval(refresh, 1000 * 60);
        timeout.promise.then(function(){ $interval.cancel(refreshPromise); });
        refresh();

        function refresh(){
            var promises = [];
            that.gridOptions.data = [];
            _.each(tc.userFacilities(), function(facility){
                var smartclient = facility.smartclient();
                var smartclientPing = smartclient.isEnabled() ? smartclient.ping(timeout.promise) : $q.reject();
                var smartclientLogin = smartclientPing.then(function(){ return smartclient.login(timeout.promise); });

                promises.push(facility.user().downloads("where download.isDeleted = false").then(function(results){
                    _.each(results, function(download){
                        if(download.transport == 'smartclient'){
                            smartclientPing.then(function(isServer){
                                download.isServer = isServer;

                                if(download.status != 'COMPLETE'){
                                    smartclient.login(timeout.promise).then(function(){
                                        smartclient.getData(timeout.promise, download.preparedId).then(function(){
                                            smartclient.isReady(timeout.promise, download.preparedId).then(function(isReady){
                                                if(isReady){
                                                    facility.user().setDownloadStatus(timeout.promise, download.id, 'COMPLETE').then(function(){
                                                        download.status = 'COMPLETE';
                                                    });
                                                }
                                            });
                                        });
                                    });
                                }
                            });
                        }
                    });
                    that.gridOptions.data = _.flatten([that.gridOptions.data, results]);
                }));
            });

            $q.all(promises).then(function(){
                if(that.gridOptions.data.length == 0){
                    $uibModalInstance.dismiss('cancel');
                }
            });
        };
        
    
        this.remove = function(download){
            var data = [];
            _.each(that.gridOptions.data, function(currentDownload){
                if(currentDownload.id != download.id) data.push(currentDownload);
            });
            that.gridOptions.data = data;
            download.delete().then(function(){
                if(that.gridOptions.data.length == 0){
                    $uibModalInstance.dismiss('cancel');
                }
            });
        };

        this.cancel = function() {
            $uibModalInstance.dismiss('cancel');
        };


    });

})();
