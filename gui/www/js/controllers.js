angular.module('starter.controllers', [])

.service('mainService', function () {

    // private
    var _cartItemsAmount = 0;
    var _totalPrice = 0;
    var _cartProducts = {};
    var _productsList = [];
    // public API
    this.cartItemsAmount = _cartItemsAmount;
    this.totalPrice = _totalPrice;
    this.cartProducts = _cartProducts;
    this.productsList = _productsList;

    this.recalculateTotalPrice = function (cart) {
        _totalPrice = 0;
        cart.forEach(function (item) {
            _totalPrice += item.amount * item.price;
        })
        this.totalPrice = _totalPrice;
        this.cartProducts = cart;
    }
})

.controller('AppCtrl', function ($scope, $ionicModal, $timeout, $location, $http, $rootScope, mainService) {

    // With the new view caching in Ionic, Controllers are only called
    // when they are recreated or on app start, instead of every page change.
    // To listen for when this page is active (for example, to refresh data),
    // listen for the $ionicView.enter event:
    //$scope.$on('$ionicView.enter', function(e) {
    //});

    $scope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/';
    $scope.cart = [];
    $scope.loginData = {};
    $scope.optionsData = {};
    $scope.currentPath = $location.path();
    $scope.categories = [];

    $rootScope.$on("$locationChangeStart", function (event, next, current) {
        $scope.currentPath = $location.path();
    });

    // Create the login modal that we will use later
    $ionicModal.fromTemplateUrl('templates/login.html', {
        scope: $scope
    }).then(function (modal) {
        $scope.loginModal = modal;
    });

    // Triggered in the login modal to close it
    $scope.closeLogin = function () {
        $scope.loginModal.hide();
    };

    // Open the login modal
    $scope.login = function () {
        $scope.loginModal.show();
    };

    // Create cart modal.
    $ionicModal.fromTemplateUrl('templates/mycart.html', {
        scope: $scope
    }).then(function (modal) {
        $scope.cartModal = modal;
    });

    $scope.showCart = function () {
        $scope.cartModal.show();
        $scope.cartItemsAmount = mainService.cartItemsAmount;
        $scope.totalPrice = mainService.totalPrice;
    };

    $scope.hideCart = function () {
        $scope.cartModal.hide();
    };

    // Create sort and categories modal.
    $ionicModal.fromTemplateUrl('templates/productslist-options.html', {
        scope: $scope
    }).then(function (modal) {
        $scope.sortOptionsModal = modal;
    });

    $scope.showSortOptions = function () {
        $http.get($scope.apiUrl + 'CategoryService/categories/')
            .then(function (response) {
                $scope.categories = response.data;
            }, function (response) {
                $scope.content = "Something went wrong";
            });
        $scope.sortOptionsModal.show();
    };

    $scope.hideSortOptions = function () {
        $scope.sortOptionsModal.hide();
    };

    $scope.filterAndSort = function () {
        var url = $scope.apiUrl + 'ProductService/products/search' + '?name=' + $scope.optionsData.title;
        $http.get(url)
            .then(function (response) {
                mainService.productsList = response.data;
            }, function (response) {
                $scope.content = "Something went wrong";
            });
        $scope.sortOptionsModal.hide();
    };

    // Perform the login action when the user submits the login form
    $scope.doLogin = function () {
        console.log('Doing login', $scope.loginData);

        // Simulate a login delay. Remove this and replace with your login
        // code if using a login system
        $timeout(function () {
            $scope.closeLogin();
        }, 1000);
    };
})

.controller('ProductslistCtrl', function ($scope, $http, mainService) {

    $scope.productslist = mainService.productsList;
    $scope.$watch(function () {
        return mainService.productsList;
    }, function (newValue, oldValue) {
        $scope.productslist = newValue;
    });
    $scope.page = 0;
    $scope.priceCurrency = '$';
    $scope.cartItemsAmount = 0;
    $scope.firstSiteLoaded = false;
    $scope.sortBy = '';
    $scope.sortDirection = '';

    $scope.$on('$ionicView.loaded', function (event) {
        $scope.refresh();
    });

    $scope.$on('$ionicView.enter', function (event) {
        $scope.countItems();
    });

    $scope.refresh = function () {
        $http.get($scope.apiUrl + 'ProductService/products/page/' + $scope.page + '?sortBy=' + $scope.sortBy)
            .then(function (response) {
                $scope.productslist = response.data.content;
                $scope.lastPage = response.data.totalPages;
            }, function (response) {
                $scope.content = "Something went wrong";
            });
        $scope.firstSiteLoaded = true;
    };

    $scope.countItems = function () {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        $scope.cartItemsAmount = counter;
        mainService.cartItemsAmount = counter;
    };

    $scope.loadMore = function () {
        if ($scope.firstSiteLoaded) {
            $scope.page++;
            $http.get($scope.apiUrl + 'ProductService/products/page/' + $scope.page + '?sortBy=' + $scope.sortBy)
                .then(function (response) {
                    var productsArrLen = response.data.content.length;
                    var products = response.data.content;
                    for (var i = 0; i < productsArrLen; i++) {
                        $scope.productslist.push(products[i]);
                    }
                    $scope.$broadcast('scroll.infiniteScrollComplete');
                }, function (response) {
                    $scope.content = "Something went wrong";
                    $scope.lastPage = $scope.page;
                });
        }
    };

    $scope.moreDataCanBeLoaded = function () {
        if ($scope.page === $scope.lastPage)
            return false;
        return true;
    };
})

.controller('ProductCtrl', function ($scope, $http, $stateParams, mainService) {
    $scope.priceCurrency = '$';
    $scope.product = {};
    $scope.mainImageSrc = {};
    $scope.model = {
        itemAmount: 1,
        totalCost: 0
    };

    $scope.$on('$ionicView.loaded', function (event) {
        $scope.refresh();
    });

    $scope.refresh = function () {
        $http.get($scope.apiUrl + 'ProductService/product/' + $stateParams.productId)
            .then(function (response) {
                $scope.product = response.data;
                if ($scope.product.images[0]) {
                    $scope.mainImageSrc = $scope.product.images[0].imageSrc;
                }
                $scope.model.totalCost = $scope.product.price;
            }, function (response) {
                $scope.content = "Something went wrong";
            });
    };

    $scope.addToCart = function () {
        isInCart = false;
        $scope.cart.forEach(function (item) {
            if (item.id == $stateParams.productId) {
                isInCart = true;
                item.amount += $scope.model.itemAmount;
            }
        });

        if (isInCart == false) {
            item = {
                id: $stateParams.productId,
                price: $scope.product.price,
                amount: $scope.model.itemAmount,
                product: $scope.product
            };
            $scope.cart.push(item);
        }

        $scope.countItems();
        mainService.recalculateTotalPrice($scope.cart);
    };

    $scope.updateTotalCost = function () {
        $scope.model.totalCost = $scope.model.itemAmount * $scope.product.price;
    };

    $scope.countItems = function () {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        mainService.cartItemsAmount = counter;
    };

    $scope.changePhoto = function ($event) {
        $scope.mainImageSrc = $event.currentTarget.src;
    };
})

.controller('MyCartCtrl', function ($scope, mainService) {
    $scope.productslist = [];
    $scope.priceCurrency = '$';
    $scope.model = {
        editAmount: false
    }

    $scope.cartItemsAmount = mainService.cartItemsAmount;
    $scope.totalPrice = mainService.totalPrice;

    $scope.$on('modal.shown', function () {
        $scope.productslist = mainService.cartProducts;
        $scope.cartItemsAmount = mainService.cartItemsAmount;
        $scope.totalPrice = mainService.totalPrice;
    });

    $scope.edit = function ($event) {
        $scope.model.editAmount = true;
    };

    $scope.refresh = function () {
        $scope.cartItemsAmount = mainService.cartItemsAmount;
        $scope.totalPrice = mainService.totalPrice;
    };

    $scope.updateTotalCost = function (index) {
        mainService.totalPrice = $scope.productslist[index].amount * $scope.productslist[index].product.price;
    };

    $scope.countItems = function (index) {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        mainService.cartItemsAmount = counter;
    };
});