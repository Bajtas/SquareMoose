angular.module('SquareMooseControllers')

.controller('CategoryAddCtrl', function($scope, $rootScope, $http, $state, $base64, $stateParams, $location, Upload) {
    $scope.error = false;
    $scope.showInfo = false;
    $scope.category = { };

    $scope.$on('$viewContentLoaded', function() {

    });

    $scope.addCategory = function() {
        $http({
            method: "POST",
            data: $scope.product,
            headers: {
                "Authorization": localStorage.getItem("Authorization")
            },
            url: $rootScope.apiUrl + '/CategoryService/category/add'
        }).then(function success(response) {
            $scope.updateInfo = response.data;
            $scope.showInfo = true;
        }, function error(response) {
            $scope.showInfo = false;
            $scope.error = true;
            $scope.errMsg = response.data;
        });
    };
});