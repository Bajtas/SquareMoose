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

.controller('AppCtrl', function ($scope, $ionicModal, $ionicPopup, $timeout, $location, $http, $rootScope, cartService) {

    // With the new view caching in Ionic, Controllers are only called
    // when they are recreated or on app start, instead of every page change.
    // To listen for when this page is active (for example, to refresh data),
    // listen for the $ionicView.enter event:
    //$scope.$on('$ionicView.enter', function(e) {
    //});

    $scope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/';
    $scope.cart = [];
    $scope.loginData = {};
    $scope.optionsData = { sortDir: false };
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

    // Perform the login action when the user submits the login form
    $scope.doLogin = function () {
        console.log('Doing login', $scope.loginData);

        // Simulate a login delay. Remove this and replace with your login
        // code if using a login system
        $timeout(function () {
            $scope.closeLogin();
        }, 1000);
    };

    $scope.$on('showAlert', function (event, msg) {
        $scope.showAlert(msg);
    });

    // An alert dialog
    $scope.showAlert = function (msg) {
        var alertPopup = $ionicPopup.alert({
            title: 'Some problems occured!',
            template: 'Something went wrong'
        });
        console.log(msg);
    };
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