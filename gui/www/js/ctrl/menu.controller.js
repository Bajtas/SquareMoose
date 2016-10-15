angular.module('starter.controllers')

.controller('MenuCtrl', function ($scope, $ionicPlatform, $http, $stateParams, $cordovaToast) {
    // Controller fields
    $scope.isLoggedIn = false; // Indicator to tell if user is logged in

    // Event on view loaded
    $scope.$on('$ionicView.loaded', function (event) {
        if (localStorage.getItem("Authorization") !== 'undefined') {
            $scope.isLoggedIn = true;
        }
    });

    $scope.$on('loggedIn', function () {
        if (localStorage.getItem("Authorization") !== 'undefined') {
            $scope.isLoggedIn = true;
        }
    });

    $scope.account = function () {

    };

    $scope.logout = function () {
        $scope.isLoggedIn = false;
        localStorage.clear();

        $ionicPlatform.ready(function () {
            $cordovaToast.show('Log out successfully!', 'short', 'bottom');
        });
    };
})