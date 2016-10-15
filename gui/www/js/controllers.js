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

.controller('AppCtrl', function ($scope, $ionicModal, $ionicPopup, $timeout, $location, $http, $rootScope,
    cartService, loginService, registerService) {

    // Root API URL
    $rootScope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/';
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
        loginService.init($scope, $scope.apiUrl);
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