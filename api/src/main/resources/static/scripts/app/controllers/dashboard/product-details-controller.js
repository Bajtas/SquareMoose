angular.module('SquareMooseControllers')

.controller('ProductDetailsCtrl', function($scope, $rootScope, $http, $state, $base64, $stateParams, Upload) {
    $scope.error = false;
    $scope.selectedCategory = {};

    $scope.$on('$viewContentLoaded', function() {
        var productsUrl = $rootScope.apiUrl + '/ProductService/product/' + $stateParams.productId;
        var categoriesUrl = $rootScope.apiUrl + '/CategoryService/categories';
        var noPicUrl = 'https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg';

        if (localStorage.getItem("sortDir") !== null && localStorage.getItem("sortDir") !== 'undefined')
            productsUrl += '&dir=' + localStorage.getItem("sortDir");

        $http.get(productsUrl)
            .then(function(response) {
                $scope.product = response.data;
                if ($scope.product.images.length > 0)
                    $scope.mainImage = $scope.product.images[0].imageSrc;

                if ($scope.mainImage === 'undefined')
                    $scope.mainImage = noPicUrl;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });

        $http.get(categoriesUrl)
            .then(function(response) {
                $scope.categories = response.data;
                if ($scope.product.category !== null) {
                    $scope.categories.forEach(function(element) {
                        if (element.id === $scope.product.category.id) {
                            $scope.selectedOption;
                        }
                    });
                } else {
                    $scope.noCategoryAssigned = true;
                }
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });

        $scope.page = parseInt(localStorage.getItem("page"));
    });

    $scope.showPic = function(imageId) {
        $scope.product.images.forEach(function(image) {
            if (image.id === imageId) {
                $scope.mainImage = image.imageSrc;
            }
        });
    };

    $scope.onFileSelect = function($files) {
        $scope.files = $files;
    };

    $scope.modifyProduct = function() {
        var apiForPicHost = 'http://uploads.im/api?upload';
        $scope.files.forEach(function(element) {
            Upload.upload({
                url: apiForPicHost,
                file: element,
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                progress: function(e) {}
            }).then(function(data, status, headers, config) {
                // file is uploaded successfully
                console.log(data);
            });
        });
    };
});