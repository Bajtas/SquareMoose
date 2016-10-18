angular.module('starter.controllers')

.controller('OrderCtrl', function ($scope, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.isLoggedIn = false;
    $scope.orderFinalizationScreen = false;
    $scope.productsInCart = [];
    $scope.deliveryTypes = [];
    $scope.paymentMethods = [];
    $scope.user = {};
    $scope.form = {};
    // End of fields

    $scope.$on('$ionicView.loaded', function (event) {
        if ($rootScope.isLoggedIn === true) {
            $scope.productsInCart = $rootScope.products;
            $scope.user = $rootScope.user;
        }

        $http.get($rootScope.apiUrl + 'DeliveryTypeService/deliverytypes').then(function success(response) {
            $scope.deliveryTypes = response.data;
        }, function error(response) {

        });

        $http.get($rootScope.apiUrl + 'PaymentMethodService/methods').then(function success(response) {
            $scope.paymentMethods = response.data;
        }, function error(response) {

        });
    });

    $scope.deliveryAdressChoosen = function (deliveryAddress) {
        if (deliveryAddress === 'My address') {
            if (!($scope.user.deliveryAdresses === undefined || $scope.user.deliveryAdresses === null)) {
                for (var i = 0; i < $scope.user.deliveryAdresses.length; i++) {
                    var deliveryAdress = $scope.user.deliveryAdresses[i];
                    if (deliveryAdress.currentlyAssigned === true) {
                        deliveryAdress.contactPhone = Number(deliveryAdress.contactPhone);
                        $scope.form.deliveryAdress = deliveryAdress;
                        break;
                    }
                }
            }
        } else if (deliveryAddress === 'Other') {
            $scope.deliveryAdress.name = '';
            $scope.deliveryAdress.surname = '';
            $scope.deliveryAdress.address = '';
            $scope.deliveryAdress.town = '';
            $scope.deliveryAdress.zipCode = '';
            $scope.deliveryAdress.phone = '';
        }
    };

    $scope.createOrder = function () {
        var itemsAmount = 0;
        var orderItems = [];
        for (var i = 0; i < $scope.productsInCart.length; i++) {
            var orderItem = {
                "amount": $scope.productsInCart[0].amount,
                "product": $scope.productsInCart[0].product
            };
            itemsAmount += $scope.productsInCart[0].amount;
            orderItems.push(orderItem);
        }

        var deliveryAddress = {};
        if ($scope.form.deliveryAddress === 'My address') {
            for (var i = 0; i < $scope.user.deliveryAdresses.length; i++) {
                if ($scope.user.deliveryAdresses[i].currentlyAssigned === true) {
                    deliveryAdress = $scope.user.deliveryAdresses[i];
                    deliveryAdress.user = null;
                    deliveryAdress.orders = null;
                    break;
                }
            }
        } else if ($scope.form.deliveryAddress === 'Other') {
            deliveryAdress = {
                "name": deliveryAdress.name,
                "surname": deliveryAdress.surname,
                "address": deliveryAdress.address,
                "town": deliveryAdress.town,
                "zipCode": deliveryAdress.zipCode,
                "contactPhone": deliveryAdress.contactPhone,
                "currentlyAssigned": false,
            }
        }

        var deliveryType = {};
        for (var i = 0; i < $scope.deliveryTypes.length; i++) {
            if ($scope.deliveryTypes[i].id === $scope.form.choosenDeliveryType) {
                deliveryType = $scope.deliveryTypes[i];
                deliveryType.orders = null;
                break;
            }
        }

        var paymentMethod = {};
        for (var i = 0; i < $scope.paymentMethods.length; i++) {
            if ($scope.paymentMethods[i].id === $scope.form.choosenPaymentMethod) {
                paymentMethod = $scope.paymentMethods[i];
                paymentMethod.orders = null;
                break;
            }
        }
        $scope.user.deliveryAdresses = null;

        var order = {
            'orderItems': orderItems,
            'deliveryType': deliveryType,
            'paymentMethod': paymentMethod,
            'deliveryAdress': deliveryAdress,
            'user': $scope.user,
            'itemsAmount': itemsAmount
        };

        $scope.auth = localStorage.getItem("Authorization");

        console.log(angular.toJson(order));

        $scope.orderFinalizationScreen = true;
        $http({
            method: "POST",
            data: order,
            headers: { Authorization: $scope.auth },
            url: $scope.apiUrl + 'OrderService/order/add'
        }).then(function success(response) {
            var alertPopup = $ionicPopup.alert({
                title: "Success!",
                template: "asdassdasd"
            });
        }, function error(response) {
            var alertPopup = $ionicPopup.alert({
                title: "Problem occured!",
                template: "There is some problem with order, please try again."
            });
            $scope.orderFinalizationScreen = false;
        });
    };
})