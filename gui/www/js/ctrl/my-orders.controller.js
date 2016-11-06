angular.module('starter.controllers')

.controller('MyOrdersCtrl', function ($scope, alertsService, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.orders = {};
    $scope.dataLoading = false;
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        $scope.dataLoading = true;
        $http.get($rootScope.apiUrl + 'OrderService/orders/user/login/' + localStorage.getItem("UserLogin"))
            .then(function (response) {
                $scope.orders = JSOG.decode(response.data);
                $scope.dataLoading = false;
            }, function (response) {
                alertsService.showDefaultAlert(response.data);
                $scope.dataLoading = false;
            });
    });
})