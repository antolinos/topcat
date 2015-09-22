(function() {
    'use strict';

    angular.
        module('angularApp').
        controller('DownloadCartController', DownloadCartController).
        controller('DownloadCartModalController', DownloadCartModalController).
        directive('downloadCart', downloadCart);

    DownloadCartController.$inject = ['$rootScope', '$modal', 'Cart', 'inform'];
    DownloadCartModalController.$inject = ['$modalInstance', 'Cart', 'SMARTCLIENTPING', 'idsInfos'];
    downloadCart.$inject = [];

    function DownloadCartController ($rootScope, $modal, Cart, inform) {
        var dc = this;
        dc.cartItems = Cart._cart.items;

        dc.openModal = function() {
            $modal.open({
                templateUrl : 'views/download-cart-modal.directive.html',
                controller : 'DownloadCartModalController as dcm',
                resolve: {
                    idsInfos : ['Config', 'APP_CONFIG', 'IdsManager', '$sessionStorage', function(Config, APP_CONFIG, IdsManager, $sessionStorage) {
                        var sessions = $sessionStorage.sessions;
                        var facilities = [];

                        _.each(sessions, function(session, key) {
                            var facility = Config.getFacilityByName(APP_CONFIG, key);

                            IdsManager.isTwoLevel(facility).then(function(data) {
                                facility.isTwoLevel = data;
                            }, function(error){
                                inform.add(error, {
                                    'ttl': 0,
                                    'type': 'danger'
                                });
                            });

                            facilities.push(facility);
                        });

                        return facilities;
                    }]
                },
                size : 'lg'
            });
        };
    }


    function DownloadCartModalController($modalInstance, Cart, SMARTCLIENTPING, idsInfos) {
        var vm = this;
        var facilityCart = Cart.getFacilitiesCart();

        vm.downloads = [];

        _.each(facilityCart, function(cart) {
            cart.transportOptions = cart.getDownloadTransportType();

            //check if smartclient is online and of so add the option to the transport type dropdown
            if (typeof SMARTCLIENTPING !== 'undefined' && SMARTCLIENTPING.ping === 'online') {
                var httpTransport = _.find(cart.transportOptions, {type: 'https'});

                if (typeof httpTransport !== 'undefined') {
                    var smartClientTransport = {
                        displayName : 'Smartclient',
                        type : 'smartclient',
                        url: httpTransport.url,
                    };

                    var smartClientTransportExists = _.find(cart.transportOptions, smartClientTransport);

                    if (typeof smartClientTransportExists === 'undefined') {
                        cart.transportOptions.push(smartClientTransport);
                    }
                }
            }

            //set the default transport dropdown
            if (cart.transportOptions.length === 1) {
                cart.transportType = cart.transportOptions[0];
            } else {
                _.each(cart.transportOptions, function(option) {
                    if (option.default === true) {
                        cart.transportType = option;
                    }
                });
            }

            vm.downloads.push(cart);
        });

        vm.hasArchive = function() {
            var isTwoLevel = false;

            _.each(idsInfos, function(idsInfo) {
                if (typeof idsInfo.isTwoLevel !== 'undefined' && idsInfo.isTwoLevel === true) {
                    isTwoLevel = true;
                    return false;
                }
            });

            return isTwoLevel;
        };


        vm.ok = function() {
            $modalInstance.close();

            if (typeof vm.email !== 'undefined' && vm.email.trim() !== '') {
                _.each(vm.downloads, function(download) {
                    download.email = vm.email;
                });
            }
            ///submit the cart for download
            Cart.submit(vm.downloads);
        };

        vm.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    }

    function downloadCart() {
        return {
            restrict: 'E',
            scope: {
                items: '@'
            },
            templateUrl: 'views/download-cart.directive.html',
            controller: 'DownloadCartController',
            controllerAs: 'dc'
        };
    }

})();