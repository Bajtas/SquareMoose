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
            'password': $scope.formData.password1
        };

        $http({
            method: "PUT",
            data: user,
            headers: {
                Authorization: localStorage.getItem("Authorization")
            },
            url: $rootScope.apiUrl + 'UserService/update'
        }).then(function success(response) {
            if (response.data === 'Logged in successfully!') {
                $ionicPlatform.ready(function () {
                    $cordovaToast.show('Logged in successfully!', 'short', 'bottom');
                });

                modal.hide();

                $rootScope.$broadcast('loggedIn');
            }
        }, function error(response) {
            var alertPopup = $ionicPopup.alert({
                title: "Problem occured!",
                template: response.data
            });
        });
    };
})