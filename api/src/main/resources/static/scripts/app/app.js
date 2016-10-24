var app = angular.module("SquareMooseDashboard", ["ui.router", "base64", "SquareMooseControllers"]);

app.run(function($rootScope) {
    $rootScope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/API/';
});


app.config(function($stateProvider, $urlRouterProvider) {
    $stateProvider
    //    .state('app', {
    //      url: "/app",
    //      template: "test",
    //     abstract: true,
    //      controller: 'AppCtrl'
    //    })

        .state('login', {
        url: "/login",
        templateUrl: '../sign-in.html',
        controller: 'LoginCtrl'
    });

    $urlRouterProvider.otherwise("/login");
});