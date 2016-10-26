angular.module('starter.controllers')

.controller('OptionsCtrl', function ($scope, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.formData = {};
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) { 

    });

    $scope.saveChanges = function () {
        var user  = {
            'login': localStorage.getItem("UserLogin"),
            'email': $scope.formData.email,
            'password': $scope.formData.newPassowrd
        };
    };
})