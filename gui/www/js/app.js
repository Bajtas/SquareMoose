// Ionic Starter App
// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'ngCordova', 'base64', 'starter.controllers'])

.run(function ($ionicPlatform) {
    $ionicPlatform.ready(function () {
        // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
        // for form inputs)
        if (cordova.platformId === "ios" && window.cordova && window.cordova.plugins.Keyboard) {
            cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
            cordova.plugins.Keyboard.disableScroll(true);

        }
        if (window.StatusBar) {
            // org.apache.cordova.statusbar required
            StatusBar.styleDefault();
        }
    });
})

.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
      // Menu controller
     .state('app', {
            url: '/app',
            abstract: true,
            templateUrl: 'templates/menu.html',
            controller: 'AppCtrl'
    })
    // Search controller
    .state('app.search', {
        url: '/search',
        views: {
            'menuContent': {
                templateUrl: 'templates/search.html'
            }
        }
    })
    // Browser controller
    .state('app.browse', {
        url: '/browse',
        views: {
            'menuContent': {
                templateUrl: 'templates/browse.html'
            }
        }
    })
    // Products list controller
    .state('app.products-list', {
        url: '/productslist',
        views: {
            'menuContent': {
                templateUrl: 'templates/shop/product/productslist.html',
                controller: 'ProductsListCtrl'
            }
        }
    })
    // Product controller
    .state('app.product', {
        url: '/product/:productId',
        views: {
            'menuContent': {
                templateUrl: 'templates/shop/product/product.html',
                controller: 'ProductCtrl'
            }
        }
    })
    // Order controller
    .state('app.order', {
        url: '/order',
        views: {
            'menuContent': {
                templateUrl: 'templates/shop/order/order.html',
                controller: 'OrderCtrl'
            }
        }
    })
    // Order summary controller
    .state('app.orderSummary', {
        url: '/order-summary',
        views: {
            'menuContent': {
                templateUrl: 'templates/shop/order/order-summary.html',
                controller: 'OrderSummaryCtrl'
            }
        }
    })
    // My account controller
    .state('app.myaccount', {
        url: '/my-account',
        views: {
            'menuContent': {
                templateUrl: 'templates/account/my-account.html',
                controller: 'MyAccountCtrl'
            }
        }
    });

    // Default URL to go when URL is not set
    $urlRouterProvider.otherwise('/app/productslist');
});