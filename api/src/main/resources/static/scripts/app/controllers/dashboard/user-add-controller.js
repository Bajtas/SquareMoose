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

        if ($stateParams.userId !== null) {
            $http({
                method: "GET",
                headers: {
                    "Authorization": localStorage.getItem("Authorization")
                },
                url: $rootScope.apiUrl + '/UserService/user/id/' + $stateParams.userId
            }).then(function success(response) {
                $scope.user = response.data;
                if ($scope.user.deliveryAdresses !== null)
                    $scope.address = $scope.user.deliveryAdresses[0];

                $http.get(rolesUrl)
                    .then(function(response) {
                        $scope.roles = JSOG.decode(response.data);
                        $scope.roles.forEach(function(item) {
                            if (item.name === $scope.user.userRole.name) {
                                $scope.selectedRole = item;
                            }
                        });
                    }, function(response) {
                        $scope.error = true;
                        $scope.errMsg = response.data;
                    });
            }, function error(response) {
                $scope.showInfo = false;
                $scope.error = true;
                $scope.errMsg = response.data;
            });
        }
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

    $scope.editUser = function() {
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

    $scope.editAddress = function() {
        $http({
            method: "PUT",
            data: $scope.address,
            headers: {
                "Authorization": localStorage.getItem("Authorization")
            },
            url: $rootScope.apiUrl + '/DeliveryAdressService/deliveryadress/' + $scope.address.id + '/update'
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