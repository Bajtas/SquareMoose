angular.module('SquareMooseControllers')

.controller('PaymentMethodsCtrl', function($scope, $rootScope, $http, $base64, $state, $location, $compile) {
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
    }];
    $scope.sortDirections = [{
        name: 'Ascending',
        value: 'asc'
    }, {
        name: 'Descending',
        value: 'desc'
    }];

    $scope.stats = function() {
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
        for (var i = 0; i < 6; i++ ) {
            color += letters[Math.floor(Math.random() * 16)];
        }
        return color;
    };

    $scope.getDarkerColor = function(color, percent) {
        var num = parseInt(color.slice(1),16), amt = Math.round(2.55 * percent), R = (num >> 16) + amt, G = (num >> 8 & 0x00FF) + amt, B = (num & 0x0000FF) + amt;
        return "#" + (0x1000000 + (R<255?R<1?0:R:255)*0x10000 + (G<255?G<1?0:G:255)*0x100 + (B<255?B<1?0:B:255)).toString(16).slice(1);
    };

    $scope.init = function() {
        var paymentsMethodsUrl = $rootScope.apiUrl + '/PaymentMethodService/methods/page/';

        if (localStorage.getItem("pagePaymentMethods") === null || localStorage.getItem("pagePaymentMethods") === 'undefined')
            localStorage.setItem("pagePaymentMethods", 0);
        if (localStorage.getItem("sortByPaymentMethods") === null || localStorage.getItem("sortByPaymentMethods") === 'undefined')
            localStorage.setItem("sortByPaymentMethods", 'id');

        paymentsMethodsUrl += localStorage.getItem("pagePaymentMethods") + '?sortBy=' + localStorage.getItem("sortByPaymentMethods");

        if (localStorage.getItem("sortDirPaymentMethods") !== null && localStorage.getItem("sortDirPaymentMethods") !== 'undefined')
            paymentsMethodsUrl += '&dir=' + localStorage.getItem("sortDirPaymentMethods");

        $http.get(paymentsMethodsUrl)
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

    $scope.deleteMethod = function(methodId) {
        $http.delete($rootScope.apiUrl + '/PaymentMethodService/method/' + methodId + '/delete', JSON.parse(localStorage.getItem("AuthHeader")))
            .then(function(response) {
                $scope.init();
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
    };

    $scope.modifyMethodName = function(methodId) {
        var methodName = "";
        var element = angular.element("#method-" + methodId)[0];
        $scope.methodsList.forEach(function(item) {
            if (item.id === methodId)
                methodName = item.name;
        });
        var buttonIcon = '<i class="fa fa-floppy-o" aria-hidden="true"></i>';
        var html = '<input id="methodNameChange-' + methodId + '" type="text" value="' + methodName + '"/>';
        html = html + '<button class="medium" style="margin-left: 10px;" ng-click="acceptChanges(' + methodId + ')">' + buttonIcon + ' Save</button>';

        element.innerHTML = html;

        $compile(element)($scope);
    };

    $scope.acceptChanges = function(methodId) {
        var element = angular.element("#methodNameChange-" + methodId)[0];
        var methodToModify = {};
        $scope.methodsList.forEach(function(item) {
            if (item.id === methodId) {
                methodToModify = item;
                item.name = element.value;
            }
        });

        methodToModify.name = element.value;

        $http({
            method: "PUT",
            data: JSOG.encode(methodToModify),
            headers: {
                "Authorization": localStorage.getItem("Authorization")
            },
            url: $rootScope.apiUrl + '/PaymentMethodService/method/' + methodId + '/update'
        }).then(function success(response) {
            $scope.updateInfo = response.data;
            $scope.showInfo = true;
            $scope.saveInProgress = false;

            angular.element("#method-" + methodId)[0].innerHTML = element.value;
        }, function error(response) {
            $scope.showInfo = false;
            $scope.error = true;
            $scope.errMsg = response.data;
            $scope.saveInProgress = false;
        });
    };

    $scope.addNew = function() {
        if (angular.element("#methodAdded").length === 0) {
            var methodsList = angular.element("#methodsList");
            var buttonIcon = '<i class="fa fa-plus" aria-hidden="true"></i>'
            var html = angular.element('<tr class=""><td value="{{method.id}}">---</td>'
                            + '<td value="{{method.name}}" >'
                            +   '<input type="text" id="methodAdded"/>'
                            +   '<button style="margin-left: 20px" ng-click="saveNew()">' + buttonIcon + 'Save</button>'
                            + '</td>'
                            + '<td value="{{method.addedOn}}"></td>'
                            + '<td value="{{method.lmod}}"></td>'
                            + '<td value=""></td>'
                        + '</tr>');
            $compile(angular.element(html))($scope);
            methodsList.append(html);
        }
    };

    $scope.saveNew = function() {
        var method = {
            name : angular.element("#methodAdded")[0].value
        };

        $http({
            method: "POST",
            data: method,
            headers: {
                "Authorization": localStorage.getItem("Authorization")
            },
            url: $rootScope.apiUrl + '/PaymentMethodService/method/add'
        }).then(function success(response) {
            $scope.updateInfo = response.data;
            $scope.showInfo = true;
            $scope.saveInProgress = false;
            $scope.methodsList = [];

            $scope.init();
        }, function error(response) {
            $scope.showInfo = false;
            $scope.error = true;
            $scope.errMsg = response.data;
            $scope.saveInProgress = false;
        });
    };
});