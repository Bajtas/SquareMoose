angular.module('starter.controllers', [])

.service('mainService', function () {

    // private
    var _cartItemsAmount = 0;
    var _totalPrice = 0;
    var _cartProducts = {};
    // public API
    this.cartItemsAmount = _cartItemsAmount;
    this.totalPrice = _totalPrice;
    this.cartProducts = _cartProducts;

    this.recalculateTotalPrice = function (cart) {
        _totalPrice = 0;
        cart.forEach(function (item) {
            _totalPrice += item.amount * item.price;
        })
        this.totalPrice = _totalPrice;
        this.cartProducts = cart;
    }
})

.controller('AppCtrl', function($scope, $ionicModal, $timeout, mainService) {

  // With the new view caching in Ionic, Controllers are only called
  // when they are recreated or on app start, instead of every page change.
  // To listen for when this page is active (for example, to refresh data),
  // listen for the $ionicView.enter event:
  //$scope.$on('$ionicView.enter', function(e) {
  //});

  $scope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/';
  $scope.cart = [];
  $scope.loginData = {};

  // Create the login modal that we will use later
  $ionicModal.fromTemplateUrl('templates/login.html', {
    scope: $scope
  }).then(function(modal) {
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

  // Perform the login action when the user submits the login form
  $scope.doLogin = function() {
    console.log('Doing login', $scope.loginData);

    // Simulate a login delay. Remove this and replace with your login
    // code if using a login system
    $timeout(function() {
      $scope.closeLogin();
    }, 1000);
  };
})

.controller('ProductslistCtrl', function ($scope, $http, mainService) {

    $scope.productslist = [];
    $scope.page = 0;
    $scope.priceCurrency = '$';
    $scope.cartItemsAmount = 0;

    $scope.$on('$ionicView.loaded', function (event) {
        $scope.refresh();
    });

    $scope.$on('$ionicView.enter', function (event) {
        $scope.countItems();
    });

    $scope.refresh = function () {
        $http.get($scope.apiUrl + 'ProductService/products/page/' + $scope.page)
        .then(function (response) {
            $scope.page = response.data;
            $scope.productslist = $scope.page.content;
        }, function (response) {
            $scope.content = "Something went wrong";
        });
    };

    $scope.countItems = function () {
        counter = 0;
        $scope.cart.forEach(function (item) {
            counter += item.amount;
        });
        $scope.cartItemsAmount = counter;
        mainService.cartItemsAmount = counter;
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
