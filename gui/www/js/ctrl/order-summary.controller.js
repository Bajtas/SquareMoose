angular.module('starter.controllers')

.controller('OrderSummaryCtrl', function ($scope, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.isLoggedIn = false;
    $scope.orderFinalizationLoader = false;
    $scope.orderFinalizationScreen = false;
    $scope.productsInCart = [];
    $scope.deliveryTypes = [];
    $scope.paymentMethods = [];
    $scope.user = {};
    $scope.form = {};
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

        $scope.form.deliveryAdress = $rootScope.order.deliveryAdress;
        $scope.form.choosenDeliveryType = $rootScope.order.choosenDeliveryType;
        $scope.form.choosenPaymentMethod = $rootScope.order.choosenPaymentMethod;
    });
})