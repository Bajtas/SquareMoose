angular.module('SquareMooseControllers')

.controller('DashboardCtrl', function($scope, $rootScope, $http, $base64, ModalService) {
    $scope.sortBy = {};
    $scope.sortDir = {};
    $scope.productsList = [];
    $scope.lastPage = 0;
    $scope.page = 0;
    $scope.pageToGo = 0;
    $scope.error = false;

    $scope.init = function() {
        var productsUrl = $rootScope.apiUrl + '/ProductService/products/page/';

        if (localStorage.getItem("page") === null || localStorage.getItem("page") === 'undefined')
            localStorage.setItem("page", 0);
        if (localStorage.getItem("sortBy") === null || localStorage.getItem("sortBy") === 'undefined')
            localStorage.setItem("sortBy", 'name');

        productsUrl += localStorage.getItem("page") + '?sortBy=' + localStorage.getItem("sortBy");

        if (localStorage.getItem("sortDir") !== null && localStorage.getItem("sortDir") !== 'undefined')
            productsUrl+= '&dir=' + localStorage.getItem("sortDir");

        $http.get(productsUrl)
            .then(function(response) {
                $scope.productsList = response.data.content;
                $scope.lastPage = response.data.totalPages;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });


        $scope.page = parseInt(localStorage.getItem("page"));
    };

    $scope.sortParameters = [
          {name:'Id', value: 'id'},
          {name:'Name', value: 'name'},
          {name:'Category', value: 'category'},
          {name:'Price', value: 'price'},
          {name:'Added date', value: 'addedOn'},
          {name:'Last mod.', value: 'lmod'}
    ];

    $scope.sortDirections = [
          {name:'Ascending', value: 'asc'},
          {name:'Descending', value: 'desc'}
    ];

    $scope.sort = function () {
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
        $scope.page++;
        localStorage.setItem("page", $scope.page);
        $scope.init();
    };

    $scope.previousPage = function() {
            $scope.page--;
            localStorage.setItem("page", $scope.page);
            $scope.init();
    };

    $scope.toPage = function() {
        $scope.page = $scope.pageToGo-1;
        localStorage.setItem("page", $scope.page);
        $scope.init();
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
});