var app = angular.module("SquareMooseDashboard", ["ui.router", "base64", "angularModalService", "SquareMooseControllers"]);

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
        })
        .state('app.products-list', {
                    url: dashboardUrl + "/products-list",
                    templateUrl: '../products-list.html',
                    controller: 'ProductsListCtrl'
                });

    $urlRouterProvider.otherwise(dashboardUrl + "/login");
});