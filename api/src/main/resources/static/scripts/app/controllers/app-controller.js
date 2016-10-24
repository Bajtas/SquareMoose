angular.module('SquareMooseControllers', [])

.controller('AppCtrl', function($scope, $rootScope, $http, $base64) {
    $rootScope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/API/';
});