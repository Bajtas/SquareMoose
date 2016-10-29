angular.module('SquareMooseControllers')

.controller('MenuCtrl', function($scope, $rootScope, $http, $state, $base64, $location) {
   $scope.showMenu =  !$location.path().includes('login');

});