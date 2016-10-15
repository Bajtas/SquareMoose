angular.module('starter.controllers')

.controller('MenuCtrl', function ($scope, $ionicPlatform, $http, $stateParams, $cordovaToast) {
    // Fields
    $scope.isLoggedIn = false;
    // End of fields

    // Communication with other controllers events
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
    // End of communication

    // Functions

    // End of functions
})