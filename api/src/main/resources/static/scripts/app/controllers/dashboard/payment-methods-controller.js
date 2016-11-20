angular.module('SquareMooseControllers')

.controller('PaymentMethodsCtrl', function($scope, $rootScope, $http, $base64, $state, $location) {
    $scope.sortBy = {};
    $scope.sortDir = {};
    $scope.ordersList = [];
    $scope.lastPage = 0;
    $scope.page = 0;
    $scope.pageToGo = 1;
    $scope.error = false;
    $scope.sortParameters = [{
        name: 'Id',
        value: 'id'
    }, {
        name: 'Name',
        value: 'name'
    }];
    $scope.sortDirections = [{
        name: 'Ascending',
        value: 'asc'
    }, {
        name: 'Descending',
        value: 'desc'
    }];

    $scope.init = function() {
        var ordersUrl = $rootScope.apiUrl + '/PaymentMethodService/methods/page/';

        if (localStorage.getItem("pagePaymentMethods") === null || localStorage.getItem("pagePaymentMethods") === 'undefined')
            localStorage.setItem("pagePaymentMethods", 0);
        if (localStorage.getItem("sortByPaymentMethods") === null || localStorage.getItem("sortByPaymentMethods") === 'undefined')
            localStorage.setItem("sortByPaymentMethods", 'id');

        ordersUrl += localStorage.getItem("pagePaymentMethods") + '?sortBy=' + localStorage.getItem("sortByPaymentMethods");

        if (localStorage.getItem("sortDirPaymentMethods") !== null && localStorage.getItem("sortDirPaymentMethods") !== 'undefined')
            productsUrl += '&dir=' + localStorage.getItem("sortDirPaymentMethods");

        $http.get(ordersUrl)
            .then(function(response) {
                $scope.methodsList = JSOG.decode(response.data.content);
                $scope.lastPage = response.data.totalPages;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });


        $scope.page = parseInt(localStorage.getItem("pagePaymentMethods"));
    };

    $scope.sort = function() {
        if ($scope.sortBy === '')
            localStorage.setItem("sortByPaymentMethods", 'name');
        else
            localStorage.setItem("sortByPaymentMethods", $scope.sortBy.value);

        if ($scope.sortDir === '')
            localStorage.setItem("sortDirPaymentMethods", null);
        else
            localStorage.setItem("sortDirPaymentMethods", $scope.sortDir.value);

        $scope.init();
    };

    $scope.nextPage = function() {
        if ($scope.page !== $scope.lastPage) {
            $scope.page++;
            localStorage.setItem("pagePaymentMethods", $scope.page);
            $scope.init();
        }
    };

    $scope.previousPage = function() {
        if ($scope.page !== 0) {
            $scope.page--;
            localStorage.setItem("pagePaymentMethods", $scope.page);
            $scope.init();
        }
    };

    $scope.toPage = function() {
        if ($scope.pageToGo > 0 && $scope.pageToGo <= $scope.lastPage) {
            $scope.page = $scope.pageToGo - 1;
            localStorage.setItem("pagePaymentMethods", $scope.page);
            $scope.init();
        }
    };

    $scope.deleteOrder = function(orderId) {
        $http.delete($rootScope.apiUrl + '/OrderService/order/' + orderId + '/delete')
            .then(function(response) {
                $scope.init();
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
    };

    $scope.modifyProduct = function() {
        $state.go('');
    };

    $scope.addNew = function() {
        $state.go('product-add');
    };
});