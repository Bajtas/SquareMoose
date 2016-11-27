angular.module('starter.controllers')

.controller('ContactCtrl', function ($scope, $rootScope, $http, $location, $ionicPopup) {
    // Fields
    $scope.emailUrl = $rootScope.apiUrl + 'MailService/SendEmail';
    $scope.form = {};
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
        $http.post($scope.emailUrl, $scope.form.email).then(function success(response) {
            var alertPopup = $ionicPopup.alert({
                title: "Message sent!",
                template: "We will respond as soon as possible."
            });
        }, function error(response) {
            var alertPopup = $ionicPopup.alert({
                title: "Problem occured!",
                template: response.data
            });
        });
    };
    // End of functions
});