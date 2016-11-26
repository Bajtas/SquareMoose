angular.module('starter.controllers')

.controller('ContactCtrl', function ($scope, $rootScope, $http, $location) {
    // Fields

    // End of fields

    // Communication with other controllers events
    $scope.$on('$ionicView.enter', function (event) {
        // TODO get email if user is logged
    });


    $scope.expandText = function () {
        var element = document.getElementById("txtnotes");
        element.style.height = element.scrollHeight + "px";
    };

    $scope.sendEmail = function () {

    };
    // End of functions
});