﻿angular.module('starter.controllers')

.controller('CartCtrl', function ($scope, $rootScope, $location, $http, cartService) {
    $scope.productslist = [];
    $scope.priceCurrency = '$';
    $scope.model = {
        editAmount: false
    }

    $scope.cartItemsAmount = cartService.cartItemsAmount;
    $scope.totalPrice = cartService.totalPrice;

    $scope.$on('modal.shown', function () {
        $scope.productslist = cartService.cartProducts;
        $scope.cartItemsAmount = cartService.cartItemsAmount;
        $scope.totalPrice = cartService.totalPrice;
    });

    $scope.edit = function ($event) {
        $scope.model.editAmount = true;
    };

    $scope.refresh = function () {
        $scope.cartItemsAmount = cartService.cartItemsAmount;
        $scope.totalPrice = cartService.totalPrice;
    };

    $scope.updateTotalCost = function (index) {
        cartService.totalPrice = $scope.productslist[index].amount * $scope.productslist[index].product.price;
    };

    $scope.countItems = function (index) {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        cartService.cartItemsAmount = counter;
    };

    $scope.order = function () {
        $scope.auth = localStorage.getItem("Authorization");
        $scope.login = localStorage.getItem("UserLogin");

        if ($scope.auth !== 'undefined') {
            $http({
                method: "GET",
                headers: { Authorization : $scope.auth },
                url: $scope.apiUrl + 'UserService/user/login/' + $scope.login
            }).then(function success(response) {
                $scope.hideCart();

                if (localStorage.getItem("Authorization") !== 'undefined') {
                    $rootScope.isLoggedIn = true;
                } else {
                    $rootScope.isLoggedIn = false;
                }
                $rootScope.user = response.data
                $rootScope.products = $scope.productslist;

                $location.path("app/order");
            }, function error(response) {
                var alertPopup = $ionicPopup.alert({
                    title: "Problem occured!",
                    template: "You seem to be login, but we can't authorize your's credentials, you will be redirect to order service for guests."
                });
            });
        }
    };
});