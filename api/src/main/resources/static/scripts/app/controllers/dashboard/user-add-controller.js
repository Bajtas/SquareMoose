angular.module('SquareMooseControllers')

.controller('UserAddCtrl', function($scope, $rootScope, $http, $state, $base64, $stateParams, $location, Upload) {
    $scope.error = false;
    $scope.selectedRole = {};
    $scope.files = [];
    $scope.picsUrls = [];
    $scope.showInfo = false;
    $scope.product = {
        images: []
    };

    $scope.$on('$viewContentLoaded', function() {
        var rolesUrl = $rootScope.apiUrl + '/UserRoleService/roles';

        $http.get(rolesUrl)
            .then(function(response) {
                $scope.roles = JSOG.decode(response.data);
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
    });

    $scope.addUser = function() {
        $scope.user.userRole = $scope.selectedRole;

        $http({
            method: "POST",
            data: $scope.user,
            headers: {
                "Authorization": localStorage.getItem("Authorization")
            },
            url: $rootScope.apiUrl + '/UserService/users/add'
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