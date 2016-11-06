angular.module('starter.controllers', [])

.controller('AppCtrl', function ($scope, $ionicModal, $ionicPopup, $ionicSideMenuDelegate, $timeout, $location, $http, $rootScope,
    cartService, loginService, registerService) {

    // Root API URL
    $rootScope.apiUrl = 'http://squaremoose.ddns.net:4545/bjts/API/';
    $scope.cart = [];
    $scope.loginData = {};
    $scope.optionsData = {
        sortDir: false
    };
    $scope.currentPath = $location.path();
    $scope.categories = [];
    $scope.loginService = loginService;
    $scope.registerService = registerService;
    $scope.cartService = cartService;

    $scope.init = function () {
        loginService.init($scope, $rootScope.apiUrl);
        registerService.init($scope, $rootScope.apiUrl);
        cartService.init($scope, $rootScope.apiUrl)
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
        $http.get($rootScope.apiUrl + 'CategoryService/categories/')
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

    $scope.toggleMenu = function () {
        $ionicSideMenuDelegate.toggleLeft(true);
    };
    // End of sort modal
})