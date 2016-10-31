angular.module('starter.controllers')

.service('ProductsListService', function () {
    // private
    var _productsList;
    var _page = 0;
    var _priceCurrency = '$';
    var _cartItemsAmount = 0;
    var _firstSiteLoaded = false;
    var _sortBy = '';
    var _sortDirection = '';
    var _optionsData = null;

    // public API
    this.productsList = _productsList;
    this.page = _page;
    this.priceCurrency = _priceCurrency;
    this.cartItemsAmount = _cartItemsAmount;
    this.firstSiteLoaded = _firstSiteLoaded;
    this.sortBy = _sortBy;
    this.sortDirection = _sortDirection;
    this.optionsData = _optionsData;

    this.prepareParams = function (optionsData) {
        var title, description, price1, price2, categoryName, sortBy, sortDir;
        if (typeof optionsData.title !== 'undefined') {
            title = optionsData.title;
        }
        if (typeof optionsData.description !== 'undefined') {
            description = optionsData.description;
        }
        if (typeof optionsData.minPrice !== 'undefined') {
            price1 = optionsData.minPrice;
        }
        if (typeof optionsData.maxPrice !== 'undefined') {
            price2 = optionsData.maxPrice;
        }
        if (typeof optionsData.categoryName !== 'undefined') {
            categoryName = optionsData.categoryName;
        }
        if (typeof optionsData.sortBy !== 'undefined') {
            sortBy = optionsData.sortBy;
            if (sortBy == 'Title')
                sortBy = 'name';
            else if (sortBy == 'Price')
                sortBy = 'price';
        }
        if (typeof optionsData.sortDir !== 'undefined') {
            if (optionsData.sortDir === false) {
                sortDir = 'asc';
            } else {
                sortDir = 'desc';
            }
        }

        return {
            'name': title,
            'description': description,
            'price1': price1,
            'price2': price2,
            'categoryName': categoryName,
            'sortBy': sortBy,
            'sortDir': sortDir
        };
    };
})

.controller('ProductsListCtrl', function ($scope, $rootScope, $http, $ionicScrollDelegate, ProductsListService, cartService, alertsService) {
    // Fields
    $scope.productsList = ProductsListService.productsList;
    $scope.page = ProductsListService.page;
    $scope.priceCurrency = ProductsListService.priceCurrency;
    $scope.cartItemsAmount = 0;
    $scope.firstSiteLoaded = ProductsListService.firstSiteLoaded;
    $scope.sortBy = ProductsListService.sortBy;
    $scope.sortDirection = ProductsListService.sortDir;
    $scope.optionsData = ProductsListService.optionsData;
    // End of fields

    // Communication with other controllers events
    $scope.$on('$ionicView.loaded', function (event) {
        $scope.refresh();
    });

    $scope.$on('$ionicView.enter', function (event) {
        $scope.countItems();
    });

    $scope.$on('filterAndSort', function (event, optionsData) {
        $scope.optionsData = optionsData;
        $scope.page = 0;
        $scope.productsList = null;
        $ionicScrollDelegate.scrollTop();
        $scope.sortAndFilter();
    });
    // End of communication

    // Functions
    $scope.refresh = function () {
        $http.get($rootScope.apiUrl + 'ProductService/products/page/' + $scope.page + '?sortBy=' + $scope.sortBy)
            .then(function (response) {
                $scope.productsList = response.data.content;
                $scope.lastPage = response.data.totalPages;
                $scope.prepareData();
            }, function (response) {
                alertsService.showDefaultAlert(response.data)
            });
        $scope.firstSiteLoaded = true;
    };

    $scope.countItems = function () {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        $scope.cartItemsAmount = counter;
        cartService.cartItemsAmount = counter;
    };

    $scope.loadMore = function () {
        if ($scope.firstSiteLoaded && $scope.productsList !== undefined) {
            $scope.page++;
            var url;
            if ($scope.optionsData === null) {
                url = $rootScope.apiUrl + 'ProductService/products/page/' + $scope.page + '?sortBy=' + $scope.sortBy;

                $http.get(url)
                    .then(function (response) {
                        var productsArrLen = response.data.content.length;
                        var products = response.data.content;
                        for (var i = 0; i < productsArrLen; i++) {
                            $scope.productsList.push(products[i]);
                        }
                        $scope.$broadcast('scroll.infiniteScrollComplete');
                    }, function (response) {
                        $scope.content = "Something went wrong";
                        $scope.lastPage = $scope.page;
                    });
            } else {
                $scope.sortAndFilter();
            }
        } else {
            $scope.$broadcast('scroll.infiniteScrollComplete');
        }
    };

    $scope.sortAndFilter = function () {
        var URL = $rootScope.apiUrl + 'ProductService/products/search/page/' + $scope.page;
        var sortAndFilterParams = ProductsListService.prepareParams($scope.optionsData);

        $http({
            url: URL,
            method: "GET",
            params: sortAndFilterParams
        }).then(function (response) {
            if ($scope.productsList !== null) {
                var productsArrLen = response.data.content.length;
                var products = response.data.content;
                for (var i = 0; i < productsArrLen; i++) {
                    $scope.productsList.push(products[i]);
                }
            } else {
                $scope.productsList = response.data.content;
            }
            $scope.$broadcast('scroll.infiniteScrollComplete');
        }, function (response) {
            alertsService.showDefaultAlert(response.data);
            $scope.lastPage = $scope.page;
        });;
    };

    $scope.moreDataCanBeLoaded = function () {
        if ($scope.page === $scope.lastPage)
            return false;
        return true;
    };

    $scope.prepareData = function () {
        for (var i = 0; i < $scope.productsList.length; i++) {
            if ($scope.productsList[i].images === undefined || $scope.productsList[i].images === null)
                continue;
            for (var j = 0; j < $scope.productsList[i].images.length; j++) {
                if (angular.isObject($scope.productsList[i].images[j]))
                    continue;

                $scope.productsList[i].images.splice(i, 1);
            }
        }
    };
    // End of functions
})