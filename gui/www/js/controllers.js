angular.module('starter.controllers', [])

.service('cartService', function () {

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

.service('loginService', function ($ionicModal, registerService) {
    var _credentials = {};
    var _modal = {};
    var _appScope = {};

    this.modal = _modal;
    this.credentials = _credentials;

    this.init = function (appScope) {
        _appScope = appScope;
        $ionicModal.fromTemplateUrl('templates/login.html', {
            controller: 'AppCtrl',
            scope: _appScope
        }).then(function (modal) {
            _modal = modal;
        });
    };

    this.show = function (scope) {
        _modal.show();
    };

    this.hide = function () {
        _modal.hide();
    };

    this.login = function () {
        console.log('Doing login');
    };
})

.controller('AppCtrl', function ($scope, $ionicModal, $ionicPopup, $timeout, $location, $http, $rootScope,
    cartService, loginService, registerService) {

    // With the new view caching in Ionic, Controllers are only called
    // when they are recreated or on app start, instead of every page change.
    // To listen for when this page is active (for example, to refresh data),
    // listen for the $ionicView.enter event:
    //$scope.$on('$ionicView.enter', function(e) {
    //});

    $scope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/';
    $scope.cart = [];
    $scope.loginData = {};
    $scope.optionsData = {
        sortDir: false
    };
    $scope.currentPath = $location.path();
    $scope.categories = [];
    $scope.loginService = loginService;
    $scope.registerService = registerService;

    $scope.init = function () {
        loginService.init($scope);
        registerService.init($scope, $scope.apiUrl);
    };
    $scope.init();

    $rootScope.$on("$locationChangeStart", function (event, next, current) {
        $scope.currentPath = $location.path();
    });

    // Create cart modal.
    $ionicModal.fromTemplateUrl('templates/mycart.html', {
        scope: $scope
    }).then(function (modal) {
        $scope.cartModal = modal;
    });

    $scope.showCart = function () {
        $scope.cartModal.show();
        $scope.cartItemsAmount = cartService.cartItemsAmount;
        $scope.totalPrice = cartService.totalPrice;
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
        $scope.$broadcast('filterAndSort', $scope.optionsData);
        $scope.sortOptionsModal.hide();
    };
    // End of sort modal
})

.controller('MyCartCtrl', function ($scope, cartService) {
    $scope.productslist = [];
    $scope.priceCurrency = '$';
    $scope.model = {
        editAmount: false
    }

    $scope.cartItemsAmount = cartService.cartItemsAmount;
    $scope.totalPrice = cartService.totalPrice;

    $scope.$on('modal.shown', function () {
        $scope.productslist = cartService.cartProducts;
        $scope.cartItemsAmount = cartService.cartItemsAmount;
        $scope.totalPrice = cartService.totalPrice;
    });

    $scope.edit = function ($event) {
        $scope.model.editAmount = true;
    };

    $scope.refresh = function () {
        $scope.cartItemsAmount = cartService.cartItemsAmount;
        $scope.totalPrice = cartService.totalPrice;
    };

    $scope.updateTotalCost = function (index) {
        cartService.totalPrice = $scope.productslist[index].amount * $scope.productslist[index].product.price;
    };

    $scope.countItems = function (index) {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        cartService.cartItemsAmount = counter;
    };
});