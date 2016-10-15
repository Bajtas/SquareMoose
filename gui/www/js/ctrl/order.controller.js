angular.module('starter.controllers')

.controller('OrderCtrl', function ($scope, $ionicPlatform, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.isLoggedIn = false;
    $scope.productsInCart = [];
    $scope.user = {};
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        if ($rootScope.isLoggedIn === true) {
            $scope.productsInCart = $rootScope.products;
            $scope.user = $rootScope.user;
        }
    });

    $scope.deliveryAdressChoosen = function () {
        if (!($scope.user.deliveryAdresses === undefined || $scope.user.deliveryAdresses === null)) {
            for (var i = 0; i < $scope.user.deliveryAdresses.length; i++) {
                if ($scope.user.deliveryAdresses[i].currentlyAssigned === true) {
                    $scope.name = 'lol';
                    break;
                }
            }
        }
    };
})