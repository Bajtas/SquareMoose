﻿angular.module('starter.controllers')

.controller('OrderSummaryCtrl', function ($scope, $http, $rootScope, $location,
    $stateParams, $cordovaToast, $ionicHistory, $ionicPlatform, $ionicSideMenuDelegate, $state, $ionicPopup, $ionicNavBarDelegate) {
    // Fields
    $scope.isLoggedIn = false;
    $scope.orderFinalizationLoader = false;
    $scope.orderFinalizationScreen = false;
    $scope.productsInCart = [];
    $scope.deliveryTypes = [];
    $scope.paymentMethods = [];
    $scope.user = {};
    $scope.form = {};
    $scope.priceCurrency = '$';
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        if ($rootScope.isLoggedIn === true) {
            $scope.productsInCart = $rootScope.products;
            $scope.user = $rootScope.user;
        }

        $http.get($rootScope.apiUrl + 'DeliveryTypeService/deliverytypes').then(function success(response) {
            $scope.deliveryTypes = JSOG.decode(response.data);
        }, function error(response) {

        });

        $http.get($rootScope.apiUrl + 'PaymentMethodService/methods').then(function success(response) {
            $scope.paymentMethods = JSOG.decode(response.data);
        }, function error(response) {

        });

        $scope.form.deliveryAdress = $rootScope.order.deliveryAdress;
        $scope.form.choosenDeliveryType = $rootScope.order.choosenDeliveryType;
        $scope.form.choosenPaymentMethod = $rootScope.order.choosenPaymentMethod;
    });

    $scope.$on('$ionicView.enter', function (event) {
        $ionicNavBarDelegate.showBackButton(false);
        $scope.currentPath = $location.path();
    });

    $scope.backToShop = function () {
        $state.go('app.products-list');
    };

    $scope.showMyOrders = function () {
        $location.path('/app/my-orders');
    };
})