angular.module('SquareMooseControllers')

.controller('DashboardCtrl', function($scope, $rootScope, $http, $base64, $state) {
    $scope.sortBy = {};
    $scope.sortDir = {};
    $scope.productsList = [];
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
        var productsUrl = $rootScope.apiUrl + '/ProductService/products/page/';

        if (localStorage.getItem("page") === null || localStorage.getItem("page") === 'undefined')
            localStorage.setItem("page", 0);
        if (localStorage.getItem("sortBy") === null || localStorage.getItem("sortBy") === 'undefined')
            localStorage.setItem("sortBy", 'name');

        productsUrl += localStorage.getItem("page") + '?sortBy=' + localStorage.getItem("sortBy");

        if (localStorage.getItem("sortDir") !== null && localStorage.getItem("sortDir") !== 'undefined')
            productsUrl += '&dir=' + localStorage.getItem("sortDir");

        $http.get(productsUrl)
            .then(function(response) {
                $scope.productsList = JSOG.decode(response.data.content);
                $scope.lastPage = response.data.totalPages;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });


        $scope.page = parseInt(localStorage.getItem("page"));
    };

    $scope.sort = function() {
        if ($scope.sortBy === '')
            localStorage.setItem("sortBy", 'name');
        else
            localStorage.setItem("sortBy", $scope.sortBy.value);

        if ($scope.sortDir === '')
            localStorage.setItem("sortDir", null);
        else
            localStorage.setItem("sortDir", $scope.sortDir.value);

        $scope.init();
    };

    $scope.nextPage = function() {
        if ($scope.page !== $scope.lastPage) {
            $scope.page++;
            localStorage.setItem("page", $scope.page);
            $scope.init();
        }
    };

    $scope.previousPage = function() {
        if ($scope.page !== 0) {
            $scope.page--;
            localStorage.setItem("page", $scope.page);
            $scope.init();
        }
    };

    $scope.toPage = function() {
        if ($scope.pageToGo > 0 && $scope.pageToGo <= $scope.lastPage) {
            $scope.page = $scope.pageToGo - 1;
            localStorage.setItem("page", $scope.page);
            $scope.init();
        }
    };

    $scope.deleteProduct = function(productId) {
        $http.delete($rootScope.apiUrl + '/ProductService/product/' + productId + '/delete')
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

    $scope.deliveryMethodsStats = function() {
        var element = angular.element("#deliveryMethodsStats");
        var paymentsMethodsStatsUrl = $rootScope.apiUrl + '/DeliveryTypeService/type/usage/stats/';
        var labels = [];
        var usedTimes = [];
        var colors = [];
        var hoverColors = [];
        $http.get(paymentsMethodsStatsUrl, JSON.parse(localStorage.getItem("AuthHeader")))
            .then(function success(response) {
                response.data.forEach(function(element) {
                    labels.push(element.name);
                    usedTimes.push(element.usedTimes);
                    var color = $scope.getRandomColor();
                    colors.push(color);
                    hoverColors.push($scope.getDarkerColor(color, 10));
                });

                var data = {
                    labels: labels,
                    datasets: [{
                        data: usedTimes,
                        backgroundColor: colors,
                        hoverBackgroundColor: hoverColors
                    }]
                };

                var chart = new Chart(element, {
                    type: 'pie',
                    data: data
                });
            }, function error(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
    };

    $scope.paymentMethodsStats = function() {
            var element = angular.element("#paymentMethodsStats");
            var paymentsMethodsStatsUrl = $rootScope.apiUrl + '/PaymentMethodService/method/usage/stats/';
            var labels = [];
            var usedTimes = [];
            var colors = [];
            var hoverColors = [];
            $http.get(paymentsMethodsStatsUrl, JSON.parse(localStorage.getItem("AuthHeader")))
                .then(function success(response) {
                    response.data.forEach(function(element){
                        labels.push(element.name);
                        usedTimes.push(element.usedTimes);
                        var color = $scope.getRandomColor();
                        colors.push(color);
                        hoverColors.push($scope.getDarkerColor(color, 10));
                    });

                    var data = {
                        labels: labels,
                        datasets: [
                            {
                                data: usedTimes,
                                backgroundColor: colors,
                                hoverBackgroundColor: hoverColors
                            }]
                    };

                    var chart = new Chart(element, {
                        type: 'pie',
                        data: data
                    });
                }, function error(response) {
                    $scope.error = true;
                    $scope.errMsg = response.data;
                }
            );
        };

    $scope.getRandomColor = function() {
        var letters = '0123456789ABCDEF'.split('');
        var color = '#';
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    };

    $scope.getDarkerColor = function(color, percent) {
        var num = parseInt(color.slice(1), 16),
            amt = Math.round(2.55 * percent),
            R = (num >> 16) + amt,
            G = (num >> 8 & 0x00FF) + amt,
            B = (num & 0x0000FF) + amt;
        return "#" + (0x1000000 + (R < 255 ? R < 1 ? 0 : R : 255) * 0x10000 + (G < 255 ? G < 1 ? 0 : G : 255) * 0x100 + (B < 255 ? B < 1 ? 0 : B : 255)).toString(16).slice(1);
    };

});