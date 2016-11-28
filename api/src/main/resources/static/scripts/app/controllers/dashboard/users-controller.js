angular.module('SquareMooseControllers')

.controller('UsersCtrl', function($scope, $rootScope, $http, $base64, $state, $location) {
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
        name: 'Login',
        value: 'login'
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
        var usersUrl = $rootScope.apiUrl + '/UserService/users/page/';

        if (localStorage.getItem("pageUsers") === null || localStorage.getItem("pageUsers") === 'undefined')
            localStorage.setItem("pageUsers", 0);
        if (localStorage.getItem("sortByUsers") === null || localStorage.getItem("sortByUsers") === 'undefined')
            localStorage.setItem("sortByUsers", 'login');

        usersUrl += localStorage.getItem("pageUsers") + '?sortBy=' + localStorage.getItem("sortByUsers");

        if (localStorage.getItem("sortDirUsers") !== null && localStorage.getItem("sortDirUsers") !== 'undefined')
            productsUrl += '&dir=' + localStorage.getItem("sortDir");

        $http.get(usersUrl)
            .then(function(response) {
                $scope.usersList = JSOG.decode(response.data.content);
                $scope.lastPage = response.data.totalPages;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });


        $scope.page = parseInt(localStorage.getItem("pageUsers"));
    };

    $scope.sort = function() {
        if ($scope.sortBy === '')
            localStorage.setItem("sortByUsers", 'login');
        else
            localStorage.setItem("sortByUsers", $scope.sortBy.value);

        if ($scope.sortDir === '')
            localStorage.setItem("sortDirUsers", null);
        else
            localStorage.setItem("sortDirUsers", $scope.sortDir.value);

        $scope.init();
    };

    $scope.nextPage = function() {
        if ($scope.page !== $scope.lastPage) {
            $scope.page++;
            localStorage.setItem("pageUsers", $scope.page);
            $scope.init();
        }
    };

    $scope.previousPage = function() {
        if ($scope.page !== 0) {
            $scope.page--;
            localStorage.setItem("pageUsers", $scope.page);
            $scope.init();
        }
    };

    $scope.toPage = function() {
            if ($scope.pageToGo > 0 && $scope.pageToGo <= $scope.lastPage) {
                $scope.page = $scope.pageToGo - 1;
                localStorage.setItem("pageUsers", $scope.page);
                $scope.init();
            }
    };

    $scope.deleteUser = function(userId) {
        $http.delete($rootScope.apiUrl + '/UserService/product/' + userId + '/delete')
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