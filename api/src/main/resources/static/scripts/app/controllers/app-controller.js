angular.module('SquareMooseControllers', [])

.controller('AppCtrl', function ($scope, $rootScope) {
$rootScope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/API/';

	$scope.loginErr = false;
    $scope.credentials = {};
    $scope.login = function () {

    };
});