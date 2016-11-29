var app = angular.module("SquareMooseDashboard", ["ui.router", "base64", "SquareMooseControllers",
    "ngFileUpload", "angularSpinner", "angularSlideables", "ngMap"
]);

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
        .state('product-add', {
            url: "/product-add",
            templateUrl: '../product-add.html',
            controller: 'ProductAddCtrl',
        })
        .state('categories', {
            url: "/categories",
            templateUrl: '../categories.html',
            controller: 'CategoriesCtrl'
        })
        .state('category-details', {
            url: "/category-details/:categoryId",
            templateUrl: '../category-details.html',
            controller: 'CategoryDetailsCtrl'
        })
        .state('category-add', {
            url: "/category-add",
            templateUrl: '../category-add.html',
            controller: 'CategoryAddCtrl'
        })
        .state('orders', {
            url: "/orders",
            templateUrl: '../orders.html',
            controller: 'OrdersCtrl'
        })
        .state('order-details', {
            url: "/order-details/:orderId",
            templateUrl: '../order-details.html',
            controller: 'OrderDetailsCtrl'
        })
        .state('payment-methods', {
            url: "/payment-methods",
            templateUrl: '../payment-methods.html',
            controller: 'PaymentMethodsCtrl'
        })
        .state('payment-methods-stats', {
            url: "/payment-methods/stats",
            templateUrl: '../payment-methods-stats.html',
            controller: 'PaymentMethodsCtrl'
        })
        .state('delivery-methods', {
            url: "/delivery-methods",
            templateUrl: '../delivery-methods.html',
            controller: 'DeliveryMethodsCtrl'
        })
        .state('delivery-method-details', {
            url: "/delivery-method-details/:methodId",
            templateUrl: '../delivery-method-details.html',
            controller: 'DeliveryMethodDetailsCtrl'
        })
        .state('delivery-method-add', {
            url: "/delivery-method-add",
            templateUrl: '../delivery-method-add.html',
            controller: 'DeliveryMethodAddCtrl'
        })
        .state('delivery-methods-stats', {
            url: "/delivery-methods/stats",
            templateUrl: '../delivery-methods-stats.html',
            controller: 'DeliveryMethodsCtrl'
        })
        .state('users', {
            url: "/users",
            templateUrl: '../users.html',
            controller: 'UsersCtrl'
        })
        .state('users-add', {
            url: "/users/add",
            templateUrl: '../user-add.html',
            controller: 'UserAddCtrl'
        });

    $urlRouterProvider.otherwise("/login");
});