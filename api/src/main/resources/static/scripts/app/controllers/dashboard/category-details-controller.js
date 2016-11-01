angular.module('SquareMooseControllers')

.controller('CategoryDetailsCtrl', function($scope, $rootScope, $http, $base64, $stateParams) {
    $scope.error = false;
    $scope.showInfo = false;

    $scope.$on('$viewContentLoaded', function() {
        var categoryUrl = $rootScope.apiUrl + '/CategoryService/category/' + $stateParams.categoryId;
        var relatedProducts = $rootScope.apiUrl + '/ProductService/products/category' + '?id=' + $stateParams.categoryId;
        $http.get(categoryUrl)
            .then(function(response) {
                $scope.category = response.data;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });

        $http.get(relatedProducts)
            .then(function(response) {
                $scope.relatedProducts = response.data;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
    });

    $scope.modifyCategory = function() {
        $http({
            method: "PUT",
            data: $scope.category,
            headers: {
                "Authorization": localStorage.getItem("Authorization")
            },
            url: $rootScope.apiUrl + '/CategoryService/category/' + $stateParams.categoryId + '/update'
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