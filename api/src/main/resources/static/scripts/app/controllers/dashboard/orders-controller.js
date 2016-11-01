angular.module('SquareMooseControllers')

.controller('OrdersCtrl', function($scope, $rootScope, $http, $base64, $state, $location) {
$scope.sortBy = {};
    $scope.sortDir = {};
    $scope.ordersList = [];
    $scope.lastPage = 0;
    $scope.page = 0;
    $scope.pageToGo = 0;
    $scope.error = false;
    $scope.sortParameters = [{
        name: 'Id',
        value: 'id'
    }, {
        name: 'Name',
        value: 'name'
    }, {
        name: 'Category',
        value: 'category'
    }, {
        name: 'Price',
        value: 'price'
    }, {
        name: 'Added date',
        value: 'addedOn'
    }, {
        name: 'Last mod.',
        value: 'lmod'
    }];
    $scope.sortDirections = [{
        name: 'Ascending',
        value: 'asc'
    }, {
        name: 'Descending',
        value: 'desc'
    }];

    $scope.init = function() {
        var ordersUrl = $rootScope.apiUrl + '/OrderService/orders/page/';

        if (localStorage.getItem("pageOrders") === null || localStorage.getItem("pageOrders") === 'undefined')
            localStorage.setItem("pageOrders", 0);
        if (localStorage.getItem("sortByOrders") === null || localStorage.getItem("sortByOrders") === 'undefined')
            localStorage.setItem("sortByOrders", 'id');

        ordersUrl += localStorage.getItem("pageOrders") + '?sortBy=' + localStorage.getItem("sortByOrders");

        if (localStorage.getItem("sortDirOrders") !== null && localStorage.getItem("sortDirOrders") !== 'undefined')
            productsUrl += '&dir=' + localStorage.getItem("sortDirOrders");

        $http.get(ordersUrl)
            .then(function(response) {
                $scope.ordersList = JSOG.decode(response.data.content);
                $scope.lastPage = response.data.totalPages;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });


        $scope.page = parseInt(localStorage.getItem("pageOrders"));
    };

    $scope.sort = function() {
        if ($scope.sortBy === '')
            localStorage.setItem("sortByOrders", 'name');
        else
            localStorage.setItem("sortByOrders", $scope.sortBy.value);

        if ($scope.sortDir === '')
            localStorage.setItem("sortDirOrders", null);
        else
            localStorage.setItem("sortDirOrders", $scope.sortDir.value);

        $scope.init();
    };

    $scope.nextPage = function() {
        if ($scope.page !== $scope.lastPage) {
            $scope.page++;
            localStorage.setItem("pageOrders", $scope.page);
            $scope.init();
        }
    };

    $scope.previousPage = function() {
        if ($scope.page !== 0) {
            $scope.page--;
            localStorage.setItem("pageOrders", $scope.page);
            $scope.init();
        }
    };

    $scope.toPage = function() {
        $scope.page = $scope.pageToGo - 1;
        if ($scope.page > 0 || $scope.page > $scope.lastPage) {
            localStorage.setItem("pageOrders", $scope.page);
            $scope.init();
        }
    };

    $scope.deleteProduct = function(orderId) {
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