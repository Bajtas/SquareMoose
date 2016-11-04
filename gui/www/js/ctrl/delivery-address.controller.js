angular.module('starter.controllers')

.controller('DeliveryAddressCtrl', function ($scope, alertsService, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.deliveryAddress = {};
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        $http.get($rootScope.apiUrl + 'DeliveryAdressService/deliveryadress/user/login/' + localStorage.getItem("UserLogin"))
            .then(function (response) {
                $scope.deliveryAddress = JSOG.decode(response.data)[0];
                $scope.deliveryAddress.contactPhone = parseInt($scope.deliveryAddress.contactPhone, 10);
            }, function (response) {
                alertsService.showDefaultAlert(response.data);
            });
    });

    $scope.updateDeliveryAddress = function () {
        if ($scope.deliveryAddress.id === null) {
            $http({
                method: "POST",
                headers: {
                    Authorization: localStorage.getItem("Authorization")
                },
                data: $scope.deliveryAddress,
                url: $rootScope.apiUrl + 'DeliveryAdressService/deliveryadress/add'
            }).then(function success(response) {
                $ionicPlatform.ready(function () {
                    $cordovaToast.show('Delivery address updated successfully!', 'short', 'bottom');
                });
            }, function error(response) {
                var alertPopup = $ionicPopup.alert({
                    title: "Problem occured!",
                    template: response.data
                });
            });
        } else {
            $http({
                method: "PUT",
                headers: {
                    Authorization: localStorage.getItem("Authorization")
                },
                data: $scope.deliveryAddress,
                url: $rootScope.apiUrl + 'DeliveryAdressService/deliveryadress/' + $scope.deliveryAddress.id + '/update'
            }).then(function success(response) {
                $ionicPlatform.ready(function () {
                    $cordovaToast.show('Delivery address updated successfully!', 'short', 'bottom');
                });
            }, function error(response) {
                var alertPopup = $ionicPopup.alert({
                    title: "Problem occured!",
                    template: response.data
                });
            });
        }
    };
})