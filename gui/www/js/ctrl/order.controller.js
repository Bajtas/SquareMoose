angular.module('starter.controllers')

.controller('OrderCtrl', function ($scope, $ionicPlatform, $ionicPopup, $http, $rootScope, $location, $stateParams, $cordovaToast) {
    // Fields
    $scope.isLoggedIn = false;
    $scope.orderFinalizationLoader = false;
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
        } else {
            $scope.user = {
                name: "Guest",
                password: "123456789",
                email: ""
            };
        }

        $http.get($rootScope.apiUrl + 'DeliveryTypeService/deliverytypes').then(function success(response) {
            $scope.deliveryTypes = JSOG.decode(response.data);
        }, function error(response) {

        });

        $http.get($rootScope.apiUrl + 'PaymentMethodService/methods').then(function success(response) {
            $scope.paymentMethods = JSOG.decode(response.data);
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
            $scope.form.deliveryAdress = {};
            $scope.form.deliveryAdress.name = '';
            $scope.form.deliveryAdress.surname = '';
            $scope.form.deliveryAdress.address = '';
            $scope.form.deliveryAdress.town = '';
            $scope.form.deliveryAdress.zipCode = '';
            $scope.form.deliveryAdress.phone = '';
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
            orderItem.product.images.forEach(function (element) {
                element.products = null;
            });
            orderItems.push(orderItem);
        }

        var deliveryAddress = {};
        if ($scope.form.deliveryAddress === 'My address' && $scope.user.deliveryAdresses !== undefined) {
            for (var i = 0; i < $scope.user.deliveryAdresses.length; i++) {
                if ($scope.user.deliveryAdresses[i].currentlyAssigned === true) {
                    deliveryAdress = $scope.user.deliveryAdresses[i];
                    deliveryAdress.users = null;
                    deliveryAdress.orders = null;
                    break;
                }
            }
        } else if ($scope.form.deliveryAddress === 'Other' || $scope.isLoggedIn === false) {
            deliveryAdress = {
                "name": $scope.form.deliveryAdress.name,
                "surname": $scope.form.deliveryAdress.surname,
                "address": $scope.form.deliveryAdress.address,
                "town": $scope.form.deliveryAdress.town,
                "zipCode": $scope.form.deliveryAdress.zipCode,
                "contactPhone": $scope.form.deliveryAdress.contactPhone,
                "currentlyAssigned": false,
            };
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
        $scope.user.userRole = null;

        if (!$scope.isLoggedIn)
            $scope.user = { email: $scope.form.user.email };

        $scope.order = {
            'orderItems': orderItems,
            'deliveryType': deliveryType,
            'paymentMethod': paymentMethod,
            'deliveryAdress': deliveryAdress,
            'user': $scope.user,
            'itemsAmount': itemsAmount
        };

        $scope.auth = localStorage.getItem("Authorization");

        $scope.orderFinalizationLoader = true;
        $http({
            method: "POST",
            data: JSOG.encode($scope.order),
            headers: {
                Authorization: $scope.auth
            },
            url: $rootScope.apiUrl + 'OrderService/order/add'
        }).then(function success(response) {
            $scope.orderFinalizationScreen = true;
            $rootScope.order = {
                'orderItems': orderItems,
                'deliveryType': deliveryType,
                'paymentMethod': paymentMethod,
                'deliveryAdress': $scope.order.deliveryAdress,
                'user': $scope.user,
                'itemsAmount': $scope.order.itemsAmount,
                'choosenDeliveryType': $scope.form.choosenDeliveryType,
                'choosenPaymentMethod': $scope.form.choosenPaymentMethod
            };

            localStorage.setItem("OrderId", parseInt(response.data.replace(/[^0-9\.]/g, ''), 10));

            $location.path('/app/order-summary');
        }, function error(response) {
            var alertPopup = $ionicPopup.alert({
                title: "Problem occured!",
                template: "There is some problem with order, please try again."
            });
            $scope.orderFinalizationLoader = false;
        });
    };
})