angular.module('SquareMooseControllers')

.controller('OrderDetailsCtrl', function($scope, $rootScope, $http, $state, $base64, $stateParams, $location, Upload, GeoCoder) {
    $scope.error = false;
    $scope.selectedCategory = {};
    $scope.files = [];
    $scope.picsUrls = [];
    $scope.showInfo = false;
    $scope.saveInProgress = false;
    $scope.loadingInProgress = true;
    $scope.orderStates = [{
        "state": "New"
    }, {
        "state": "Confirmed by user"
    }, {
        "state": "Accepted by shop"
    }, {
        "state": "Payment confirmed"
    }, {
        "state": "Preparing to delivery"
    }, {
        "state": "Prepared to delivery"
    }, {
        "state": "Sent to buyer"
    }, {
        "state": "Delivered"
    }];

    $scope.$on('$viewContentLoaded', function() {
        var orderUrl = $rootScope.apiUrl + '/OrderService/order/' + $stateParams.orderId;

        $http.get(orderUrl)
            .then(function(response) {
                $scope.order = JSOG.decode(response.data);
                $scope.loadingInProgress = false;
                $scope.map = $scope.order.deliveryAdress.address + ',' + $scope.order.deliveryAdress.town;
                $scope.orderStates.forEach(function(element) {
                    if (element.state === $scope.order.actualOrderState.name) {
                        $scope.orderStateAssigned = element;
                    }
                });
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
                $scope.loadingInProgress = false;
            });
    });

    $scope.changeStatus = function() {
        if ($scope.orderStateAssigned.state !== $scope.order.actualOrderState.name) {
            var orderState = {
                'name': $scope.orderStateAssigned.state,
                'order': {
                    id: $stateParams.orderId
                }
            };
            $http({
                method: "POST",
                data: orderState,
                headers: {
                    "Authorization": localStorage.getItem("Authorization")
                },
                url: $rootScope.apiUrl + '/ActualOrderStateService/actualstate/add'
            }).then(function success(response) {
                $scope.updateInfo = response.data;
                $scope.showInfo = true;
            }, function error(response) {
                $scope.showInfo = false;
                $scope.error = true;
                $scope.errMsg = response.data;
            });
        }
    };
});