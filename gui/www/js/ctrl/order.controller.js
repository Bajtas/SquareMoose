angular.module('starter.controllers')

.controller('OrderCtrl', function ($scope, $ionicPlatform, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.isLoggedIn = false;
    $scope.productsInCart = [];
    $scope.deliveryTypes = [];
    $scope.paymentMethods = [];
    $scope.user = {};
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        if ($rootScope.isLoggedIn === true) {
            $scope.productsInCart = $rootScope.products;
            $scope.user = $rootScope.user;
        }

        $http.get($rootScope.apiUrl + 'DeliveryTypeService/deliverytypes').then(function success(response) {
            $scope.deliveryTypes = response.data;
        }, function error(response) {

        });

        $http.get($rootScope.apiUrl + 'PaymentMethodService/methods').then(function success(response) {
            $scope.paymentMethods = response.data;
        }, function error(response) {

        });
    });

    $scope.deliveryAdressChoosen = function (deliveryAddress) {
        if (deliveryAddress === 'My address') {
            if (!($scope.user.deliveryAdresses === undefined || $scope.user.deliveryAdresses === null)) {
                for (var i = 0; i < $scope.user.deliveryAdresses.length; i++) {
                    var deliveryAdress = $scope.user.deliveryAdresses[i];
                    if (deliveryAdress.currentlyAssigned === true) {
                        $scope.name = deliveryAdress.name;
                        $scope.surname = deliveryAdress.surname;
                        $scope.address = deliveryAdress.address;
                        $scope.town = deliveryAdress.town;
                        $scope.zipCode = deliveryAdress.zipCode;
                        $scope.phone = deliveryAdress.contactPhone;
                        break;
                    }
                }
            }
        } else if (deliveryAddress === 'Other') {
            $scope.name = '';
            $scope.surname = '';
            $scope.address = '';
            $scope.town = '';
            $scope.zipCode = '';
            $scope.phone = '';
        }
    };
})