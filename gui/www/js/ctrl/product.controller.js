angular.module('starter.controllers')

.service('ProductService', function () {
    // private
    var _priceCurrency = '$';
    var _product = {};
    var _mainImageSrc = {};
    var _cartCalcData = {
        itemAmount: 1,
        totalCost: 0
    };

    // public API
    this.priceCurrency = _priceCurrency;
    this.product = _product;
    this.mainImageSrc = _mainImageSrc;
    this.cartCalcData = _cartCalcData;
})

.controller('ProductCtrl', function ($scope, $rootScope, $http, $stateParams, cartService, ProductService, alertsService) {
    // Fields
    $scope.priceCurrency = ProductService.priceCurrency;
    $scope.product = ProductService.product;
    $scope.mainImageSrc = ProductService.mainImageSrc;
    $scope.cartCalcData = ProductService.cartCalcData;
    // End of fields

    // Communication with other controllers events
    $scope.$on('$ionicView.loaded', function (event) {
        $scope.refresh();
    });
    // End of communication

    // Functions
    $scope.refresh = function () {
        $http.get($scope.apiUrl + 'ProductService/product/' + $stateParams.productId)
            .then(function (response) {
                $scope.product = response.data;
                for (var i = 0; i < $scope.product.images.length; i++) {
                    if (!($scope.product.images[i] instanceof Object)) {
                        $scope.product.images.splice(i, $scope.product.images.length);
                        break;
                    }
                }

                if ($scope.product.images[0]) {
                    $scope.mainImageSrc = $scope.product.images[0].imageSrc;
                }
                $scope.cartCalcData.totalCost = $scope.product.price;
            }, function (response) {
                alertsService.showDefaultAlert(response.data);
            });
    };

    $scope.addToCart = function () {
        isInCart = false;
        $scope.cart.forEach(function (item) {
            if (item.id == $stateParams.productId) {
                isInCart = true;
                item.amount += $scope.cartCalcData.itemAmount;
            }
        });

        if (isInCart == false) {
            item = {
                id: $stateParams.productId,
                price: $scope.product.price,
                amount: $scope.cartCalcData.itemAmount,
                product: $scope.product
            };
            $scope.cart.push(item);
        }

        $scope.countItems();
        cartService.recalculateTotalPrice($scope.cart);
    };

    $scope.updateTotalCost = function () {
        $scope.cartCalcData.totalCost = $scope.cartCalcData.itemAmount * $scope.product.price;
    };

    $scope.countItems = function () {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        cartService.cartItemsAmount = counter;
    };

    $scope.changePhoto = function ($event) {
        $scope.mainImageSrc = $event.currentTarget.src;
    };
    // End of functions
})