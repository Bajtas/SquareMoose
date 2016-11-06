angular.module('starter.controllers')

.service('cartService', function ($ionicModal, $ionicPopup, $rootScope, $http, $stateParams) {

    // private
    var _cartItemsAmount = 0;
    var _totalPrice = 0;
    var _cartProducts = {};
    var _productsList = [];
    var _cartModal = {};

    // public API
    this.cartItemsAmount = _cartItemsAmount;
    this.totalPrice = _totalPrice;
    this.cartProducts = _cartProducts;
    this.productsList = _productsList;
    this.cartModal = _cartModal;

    this.init = function (appScope, url) {
        _appScope = appScope;
        $ionicModal.fromTemplateUrl('templates/shop/cart/cart.html', {
            controller: 'AppCtrl',
            scope: _appScope
        }).then(function (modal) {
            _cartModal = modal;
        });
    };

    this.show = function () {
        _cartModal.show();
    };

    this.hide = function () {
        _cartModal.hide();
    };

    this.addToCart = function (product, amount) {
        var isInCart = false;
        this.productsList.forEach(function (item) {
            if (item.id == $stateParams.productId) {
                isInCart = true;
                item.amount += amount;
            }
        });

        if (isInCart == false) {
            item = {
                'id': $stateParams.productId,
                'price': product.price,
                'amount': amount,
                'product': product
            };
            this.productsList.push(item);
        }

        this.countItems();
        this.recalculateTotalPrice(this.productsList);
    };

    this.remove = function (index) {
        this.productsList.splice(index, 1);
        this.countItems();
        this.recalculateTotalPrice(this.productsList);
    };

    this.countItems = function () {
        counter = 0;
        this.productsList.forEach(function (item) {
            counter += item.amount;
        });
        this.cartItemsAmount = counter;
    };

    this.recalculateTotalPrice = function (cart) {
        _totalPrice = 0;
        cart.forEach(function (item) {
            _totalPrice += item.amount * item.price;
        })
        this.totalPrice = _totalPrice;
        this.cartProducts = cart;
    };
})