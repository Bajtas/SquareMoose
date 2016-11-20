angular.module('SquareMooseControllers')

.controller('LoginCtrl', function($scope, $rootScope, $http, $state, $base64, $location) {
    $scope.loginErr = false;
    $scope.stateName = $state.current.name;
    $scope.credentials = {};

    $scope.login = function() {
        if ($scope.credentials !== 'undefined') {
            if ($scope.credentials.login !== 'undefined' && $scope.credentials.password !== 'undefined') {
                var config = {
                    header: {
                        "Authorization": "Basic " + $base64.encode($scope.credentials.login + ":" + $scope.credentials.password)
                    }
                };

                var authHeader = {
                    headers: {
                        "Authorization": "Basic " + $base64.encode($scope.credentials.login + ":" + $scope.credentials.password)
                    }
                };

                localStorage.setItem("Authorization", config.header.Authorization);
                localStorage.setItem("UserLogin", $scope.credentials.login);
                localStorage.setItem("AuthHeader", JSON.stringify(authHeader));

                $http({
                    method: "GET",
                    data: $scope.credentials,
                    headers: config.header,
                    url: $rootScope.apiUrl + '/UserService/user/' + $scope.credentials.login + '/account'
                }).then(function success(response) {
                    if (response.data === 'Logged in successfully!') {
                        $state.go('app');
                    }
                }, function error(response) {
                    localStorage.setItem("Authorization", "undefined");
                    localStorage.setItem("UserLogin", "undefined");
                });
            }
        }
    };
});