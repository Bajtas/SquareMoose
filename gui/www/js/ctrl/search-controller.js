angular.module('starter.controllers')

.controller('SearchCtrl', function ($scope, $rootScope, $http, $location, $ionicScrollDelegate, $ionicNavBarDelegate,
    $ionicHistory, ProductsListService, cartService, alertsService) {
    // Fields
    $scope.productsList = ProductsListService.productsList;
    $scope.page = ProductsListService.page;
    $scope.priceCurrency = ProductsListService.priceCurrency;
    $scope.cartItemsAmount = 0;
    $scope.firstSiteLoaded = ProductsListService.firstSiteLoaded;
    $scope.sortBy = ProductsListService.sortBy;
    $scope.sortDirection = ProductsListService.sortDir;
    $scope.cartService = cartService;
    // End of fields

    // Communication with other controllers events
    $scope.$on('$ionicView.loaded', function (event) {
        $http.get($rootScope.apiUrl + 'CategoryService/categories/')
            .then(function (response) {
                $scope.categories = response.data;
            }, function (response) {
                $scope.content = "Something went wrong";
            });
    });

    $scope.$on('$ionicView.enter', function (event) {
        $http.get($rootScope.apiUrl + 'CategoryService/categories/')
            .then(function (response) {
                $scope.categories = response.data;
            }, function (response) {
                $scope.content = "Something went wrong";
            });
    });

    $scope.$on('filterAndSort', function (event, optionsData) {
        $scope.optionsData = optionsData;
        localStorage.setItem('optionsData', JSON.stringify($scope.optionsData));
        $scope.page = 0;
        $scope.productsList = null;
        $ionicScrollDelegate.scrollTop();
        $scope.sortAndFilter();
    });
    // End of communication

    $scope.sortAndFilter = function () {
        var URL = $rootScope.apiUrl + 'ProductService/products/search/page/' + $scope.page;
        var sortAndFilterParams = ProductsListService.prepareParams($scope.optionsData);
        

        if (sortAndFilterParams === null) {
            $scope.sortBy = sortAndFilterParams.sortBy;
            $scope.sortDir = sortAndFilterParams.sortDir;
            $scope.refresh();
        }

        $http({
            url: URL,
            method: "GET",
            params: sortAndFilterParams
        }).then(function (response) {
            if (response.status !== 204 && $scope.productsList !== null) {
                var productsArrLen = response.data.content.length;
                var products = response.data.content;
                for (var i = 0; i < productsArrLen; i++) {
                    $scope.productsList.push(products[i]);
                }
            } else if (response.status !== 204) {
                $scope.productsList = response.data.content;
            }
            $scope.$broadcast('scroll.infiniteScrollComplete');
        }, function (response) {
            alertsService.showDefaultAlert(response.data);
            $scope.lastPage = $scope.page;
        });
    };
    // End of functions
});