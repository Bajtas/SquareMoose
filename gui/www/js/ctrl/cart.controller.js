angular.module('starter.controllers')

.controller('CartCtrl', function ($scope, $rootScope, $ionicPopup, $location, $http, cartService) {
    // Controller fields
    $scope.productslist = []; // List of products in cart
    $scope.priceCurrency = '$'; // Currency string
    $scope.cartItemsAmount = cartService.cartItemsAmount; // Items amount taken from CartService
    $scope.totalPrice = cartService.totalPrice; // Total price taken from CartService
    $scope.toFixed = 

    $scope.$on('modal.shown', function () {
        $scope.productslist = cartService.cartProducts;
        $scope.cartItemsAmount = cartService.cartItemsAmount;
        $scope.totalPrice = cartService.totalPrice;
    });

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
                headers: {
                    Authorization: $scope.auth
                },
                url: $rootScope.apiUrl + 'UserService/user/login/' + $scope.login
            }).then(function success(response) {
                cartService.hide();

                if (localStorage.getItem("Authorization") !== 'undefined') {
                    $rootScope.isLoggedIn = true;
                } else {
                    $rootScope.isLoggedIn = false;
                }
                $rootScope.user = JSOG.decode(response.data);
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