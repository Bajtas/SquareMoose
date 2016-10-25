angular.module('starter.controllers')

.controller('MyAccountCtrl', function ($scope, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
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

})