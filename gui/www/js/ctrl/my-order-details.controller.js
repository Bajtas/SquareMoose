angular.module('starter.controllers')

.controller('MyOrderDetailsCtrl', function ($scope, alertsService, $ionicPlatform, $ionicHistory, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.order = {};
    $scope.dataLoading = false;
    $scope.priceCurrency = '$';
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        $scope.dataLoading = true;
        $http.get($rootScope.apiUrl + 'OrderService/orders/user/login/' + localStorage.getItem("UserLogin") + '/order/' + $stateParams.orderId)
            .then(function (response) {
                $scope.order = JSOG.decode(response.data);
                $scope.dataLoading = false;
            }, function (response) {
                alertsService.showDefaultAlert(response.data);
                $scope.dataLoading = false;
            });
    });

    $scope.backToMyOrders = function () {
        $ionicHistory.goBack();
    };
})