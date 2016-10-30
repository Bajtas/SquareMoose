angular.module('SquareMooseControllers')

.controller('CategoryDetailsCtrl', function($scope, $rootScope, $http, $base64, $stateParams) {
    $scope.error = false;
    $scope.showInfo = false;

    $scope.$on('$viewContentLoaded', function() {
        var categoryIrl = $rootScope.apiUrl + '/CategoryService/category/' + $stateParams.categoryId;

        $http.get(categoryIrl)
            .then(function(response) {
                $scope.category = response.data;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });

        //        $http.get(categoriesUrl)
        //            .then(function(response) {
        //                $scope.categories = response.data;
        //                if ($scope.category.category !== null) {
        //                    for (var i = 0; i < $scope.categories.length; i++) {
        //                        if ($scope.categories[i].id === $scope.product.category.id) {
        //                            $scope.categoryAssigned = $scope.categories[i];
        //                            break;
        //                        }
        //                    }
        //                } else {
        //                    $scope.noCategoryAssigned = true;
        //                }
        //            }, function(response) {
        //                $scope.error = true;
        //                $scope.errMsg = response.data;
        //            });
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