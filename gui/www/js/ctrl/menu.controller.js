angular.module('starter.controllers')

.controller('MenuCtrl', function ($scope, $rootScope, $ionicPlatform, $location, $http, $stateParams, $cordovaToast, $cordovaInAppBrowser) {
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

        if (localStorage.getItem("UserRole") !== null && localStorage.getItem("UserRole") !== undefined) {
            $scope.userRole = localStorage.getItem("UserRole");
        }
    });

    $scope.logout = function () {
        $scope.isLoggedIn = false;
        localStorage.clear();

        $ionicPlatform.ready(function () {
            $cordovaToast.show('Log out successfully!', 'short', 'bottom');
        });
    };

    $scope.dashboard = function () {
        var options = {
            location: 'yes',
            clearcache: 'yes',
            toolbar: 'yes'
        };

        $ionicPlatform.ready(function () {
            $cordovaInAppBrowser.open($rootScope.dashboardUrl, '_self', options)
              .then(function (event) {
                  // success
              })
              .catch(function (event) {
                  // error
              });
        });
    };
})