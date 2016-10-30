angular.module('SquareMooseControllers')

.controller('CategoriesCtrl', function($scope, $rootScope, $http, $base64, $state) {
    $scope.sortBy = {};
    $scope.sortDir = {};
    $scope.categoriesList = [];
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
    }];
    $scope.sortDirections = [{
        name: 'Ascending',
        value: 'asc'
    }, {
        name: 'Descending',
        value: 'desc'
    }];

    $scope.init = function() {
        var categoriesUrl = $rootScope.apiUrl + '/CategoryService/categories/page/';

        if (localStorage.getItem("pageCategories") === null || localStorage.getItem("pageCategories") === 'undefined')
            localStorage.setItem("pageCategories", 0);
        if (localStorage.getItem("sortByCategories") === null || localStorage.getItem("sortByCategories") === 'undefined')
            localStorage.setItem("sortByCategories", 'name');

        categoriesUrl += localStorage.getItem("pageCategories") + '?sortBy=' + localStorage.getItem("sortByCategories");

        if (localStorage.getItem("sortDir") !== null && localStorage.getItem("sortDir") !== 'undefined')
            categoriesUrl += '&dir=' + localStorage.getItem("sortDir");

        $http.get(categoriesUrl)
            .then(function(response) {
                $scope.categoriesList = response.data.content;
                $scope.lastPage = response.data.totalPages;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });


        $scope.page = parseInt(localStorage.getItem("pageCategories"));
    };

    $scope.sort = function() {
        if ($scope.sortBy === '')
            localStorage.setItem("sortByCategories", 'name');
        else
            localStorage.setItem("sortByCategories", $scope.sortBy.value);

        if ($scope.sortDir === '')
            localStorage.setItem("sortDir", null);
        else
            localStorage.setItem("sortDir", $scope.sortDir.value);

        $scope.init();
    };

    $scope.nextPage = function() {
        if ($scope.page < $scope.lastPage - 1) {
            localStorage.setItem("pageCategories", ++$scope.page);
            $scope.init();
        }
    };

    $scope.previousPage = function() {
        if ($scope.page > 0) {
            localStorage.setItem("pageCategories", --$scope.page);
            $scope.init();
        }
    };

    $scope.toPage = function() {
        if ($scope.pageToGo > 0 || $scope.pageToGo < $scope.lastPage) {
            $scope.page = $scope.pageToGo - 1;
            localStorage.setItem("pageCategories", $scope.page);
            $scope.init();
        }
    };

    $scope.deleteProduct = function(categoryId) {
        $http.delete($rootScope.apiUrl + '/CategoryService/category/' + categoryId + '/delete')
            .then(function(response) {
                $scope.init();
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
    };

    $scope.modifyProduct = function() {
        $state.go('app.category-details');
    };
});