angular.module('SquareMooseControllers', [])

.controller('LoginCtrl', function($scope, $rootScope, $http, $location, $base64) {
    $scope.loginErr = false;
    $scope.credentials = {};

    $scope.login = function() {
        if ($scope.credentials !== 'undefined') {
            if ($scope.credentials.login !== 'undefined' && $scope.credentials.password !== 'undefined') {
                var config = {
                    header: {
                        "Authorization": "Basic " + $base64.encode($scope.credentials.login + ":" + $scope.credentials.password)
                    }
                };

                localStorage.setItem("Authorization", config.header.Authorization);
                localStorage.setItem("UserLogin", $scope.credentials.login);

                $http({
                    method: "GET",
                    data: $scope.credentials,
                    headers: config.header,
                    url: $rootScope.apiUrl + 'UserService/user/' + $scope.credentials.login + '/account'
                }).then(function success(response) {
                    if (response.data === 'Logged in successfully!') {
                        $location.path('/configuration/streaming');
                    }
                }, function error(response) {
                    localStorage.setItem("Authorization", "undefined");
                    localStorage.setItem("UserLogin", "undefined");
                });
            }
        }
    };
});