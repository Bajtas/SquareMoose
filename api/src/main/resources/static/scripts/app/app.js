var app = angular.module("SquareMooseDashboard", ["ui.router", "base64", "SquareMooseControllers", "ngFileUpload"]);

app.run(function($rootScope) {
    $rootScope.hostUrl = 'http://squaremoose.ddns.net:4545';
    $rootScope.appUrl = $rootScope.hostUrl + '/bjts';
    $rootScope.apiUrl = $rootScope.appUrl + '/API';
    $rootScope.dashboardUrl = $rootScope.appUrl + 'dashboard';
});

app.config(function($stateProvider, $urlRouterProvider, $locationProvider) {

    $locationProvider.html5Mode({
        enabled: false,
        requireBase: false
    });

    $stateProvider
        .state('login', {
            url: "/login",
            templateUrl: '../sign-in.html',
            controller: 'LoginCtrl'
        })
        .state('app', {
            url: "/app",
            templateUrl: '../control-panel.html',
            controller: 'DashboardCtrl'
        })
        .state('products-list', {
            url: "/products-list",
            templateUrl: '../products-list.html',
            controller: 'ProductsListCtrl'
        })
        .state('product-details', {
            url: "/product-details/:productId",
            templateUrl: '../product-details.html',
            controller: 'ProductDetailsCtrl',
        })
        .state('categories', {
            url: "/categories",
            templateUrl: '../categories.html',
            controller: 'CategoriesCtrl'
        });

    $urlRouterProvider.otherwise("/login");
});