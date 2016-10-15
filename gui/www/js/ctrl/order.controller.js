angular.module('starter.controllers')

.controller('OrderCtrl', function ($scope, $ionicPlatform, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.isLoggedIn = false;
    $scope.productsInCart = [];
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        if ($rootScope.isLoggedIn === true) {
            $scope.productsInCart = $rootScope.products;
            $scope.user = $rootScope.user;
        }
    });
})