angular.module('SquareMooseControllers')

.controller('DeliveryMethodsCtrl', function($scope, $rootScope, $http, $base64, $state, $location, $compile) {
    $scope.sortBy = {};
    $scope.sortDir = {};
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
    }, {
        name: 'Price',
        value: 'price'
    }];
    $scope.sortDirections = [{
        name: 'Ascending',
        value: 'asc'
    }, {
        name: 'Descending',
        value: 'desc'
    }];

    $scope.stats = function() {
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

    $scope.init = function() {
        var deliveryMethodsUrl = $rootScope.apiUrl + '/DeliveryTypeService/deliverytypes/page/';

        if (localStorage.getItem("pageDeliveryType") === null || localStorage.getItem("pageDeliveryType") === 'undefined')
            localStorage.setItem("pageDeliveryType", 0);
        if (localStorage.getItem("sortByDeliveryType") === null || localStorage.getItem("sortByDeliveryType") === 'undefined')
            localStorage.setItem("sortByDeliveryType", 'id');

        deliveryMethodsUrl += localStorage.getItem("pageDeliveryType") + '?sortBy=' + localStorage.getItem("sortByDeliveryType");

        if (localStorage.getItem("sortDirDeliveryType") !== null && localStorage.getItem("sortDirDeliveryType") !== 'undefined')
            paymentsMethodsUrl += '&dir=' + localStorage.getItem("sortDirDeliveryType");

        $http.get(deliveryMethodsUrl)
            .then(function(response) {
                $scope.methodsList = JSOG.decode(response.data.content);
                $scope.lastPage = response.data.totalPages;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });


        $scope.page = parseInt(localStorage.getItem("pageDeliveryType"));
    };

    $scope.sort = function() {
        if ($scope.sortBy === '')
            localStorage.setItem("sortByDeliveryType", 'name');
        else
            localStorage.setItem("sortByDeliveryType", $scope.sortBy.value);

        if ($scope.sortDir === '')
            localStorage.setItem("sortDirDeliveryType", null);
        else
            localStorage.setItem("sortDirDeliveryType", $scope.sortDir.value);

        $scope.init();
    };

    $scope.nextPage = function() {
        if ($scope.page !== $scope.lastPage) {
            $scope.page++;
            localStorage.setItem("pageDeliveryType", $scope.page);
            $scope.init();
        }
    };

    $scope.previousPage = function() {
        if ($scope.page !== 0) {
            $scope.page--;
            localStorage.setItem("pageDeliveryType", $scope.page);
            $scope.init();
        }
    };

    $scope.toPage = function() {
        if ($scope.pageToGo > 0 && $scope.pageToGo <= $scope.lastPage) {
            $scope.page = $scope.pageToGo - 1;
            localStorage.setItem("pageDeliveryType", $scope.page);
            $scope.init();
        }
    };

    $scope.deleteMethod = function(methodId) {
        $http.delete($rootScope.apiUrl + '/DeliveryTypeService/deliverytypes/' + methodId + '/delete', JSON.parse(localStorage.getItem("AuthHeader")))
            .then(function(response) {
                $scope.init();
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
    };

    $scope.addNew = function() {
        $state.go('product-add');
    };

});