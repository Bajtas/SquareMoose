var app = angular.module("SquareMooseDashboard", ["ui.router", "base64", "SquareMooseControllers"]);

app.run(function($rootScope) {
    $rootScope.hostUrl = 'http://squaremoose.ddns.net:4545';
    $rootScope.appUrl = $rootScope.hostUrl + '/bjts';
    $rootScope.apiUrl = $rootScope.appUrl + '/API';
    $rootScope.dashboardUrl = $rootScope.appUrl + 'dashboard';
});


app.config(function($stateProvider, $urlRouterProvider, $locationProvider) {

    var dashboardUrl = '/bjts/dashboard';

    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });

    $stateProvider
        .state('login', {
            url: dashboardUrl + "/login",
            templateUrl: '../sign-in.html',
            controller: 'LoginCtrl'
        })
        .state('app', {
            url: dashboardUrl + "/app",
            templateUrl: '../dashboard.html',
            controller: 'DashboardCtrl'
        });

    $urlRouterProvider.otherwise(dashboardUrl + "/login");
});

//    .state('app', {
//      url: "/app",
//      template: "test",
//     abstract: true,
//      controller: 'AppCtrl'
//    })