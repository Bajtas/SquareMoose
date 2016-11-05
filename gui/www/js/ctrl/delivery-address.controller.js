angular.module('starter.controllers')

.controller('DeliveryAddressCtrl', function ($scope, alertsService, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.deliveryAddress = {};
    $scope.dataLoading = false;
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        $scope.dataLoading = true;
        $http.get($rootScope.apiUrl + 'DeliveryAdressService/deliveryadress/user/login/' + localStorage.getItem("UserLogin"))
            .then(function (response) {
                $scope.deliveryAddress = JSOG.decode(response.data)[0];
                $scope.deliveryAddress.contactPhone = parseInt($scope.deliveryAddress.contactPhone, 10);
                $scope.dataLoading = false;
            }, function (response) {
                alertsService.showDefaultAlert(response.data);
                $scope.dataLoading = false;
            });
    });

    $scope.updateDeliveryAddress = function () {
        $scope.dataLoading = true;
        $scope.deliveryAddress = JSOG.encode($scope.deliveryAddress);
        if ($scope.deliveryAddress.id === null) {
            $http({
                method: "POST",
                headers: {
                    Authorization: localStorage.getItem("Authorization")
                },
                data: $scope.deliveryAddress,
                url: $rootScope.apiUrl + 'DeliveryAdressService/deliveryadress/add/' + localStorage.getItem("UserLogin")
            }).then(function success(response) {
                $ionicPlatform.ready(function () {
                    $cordovaToast.show('Delivery address updated successfully!', 'short', 'bottom');
                });
                $scope.dataLoading = false;
            }, function error(response) {
                var alertPopup = $ionicPopup.alert({
                    title: "Problem occured!",
                    template: response.data
                });
                $scope.dataLoading = false;
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
                $scope.dataLoading = false;
            }, function error(response) {
                var alertPopup = $ionicPopup.alert({
                    title: "Problem occured!",
                    template: response.data
                });
                $scope.dataLoading = false;
            });
        }
    };
})