angular.module('SquareMooseControllers')

.controller('ProductDetailsCtrl', function($scope, $rootScope, $http, $state, $base64, $stateParams, $location, Upload) {
    $scope.error = false;
    $scope.selectedCategory = {};
    $scope.files = [];
    $scope.picsUrls = [];

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
                    for (var i = 0; i < $scope.categories.length; i++) {
                        if ($scope.categories[i].id === $scope.product.category.id) {
                            $scope.categoryAssigned = $scope.categories[i];
                            break;
                        }
                    }
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

    $scope.categoryChanged = function(categoryId) {
        for (var i = 0; i < $scope.categories.length; i++) {
            if (categoryId === $scope.categories[i].id) {
                $scope.categoryAssigned = categoryId;
                break;
            }
        }
    };

    $scope.modifyProduct = function() {
        var apiForPicHost = 'http://uploads.im/api?upload';

        $scope.product.category = $scope.categoryAssigned;

        if ($scope.files.length !== 0) {
            $scope.files.forEach(function(element) {
                Upload.upload({
                    url: apiForPicHost,
                    file: element,
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    },
                    progress: function(e) {}
                }).then(function(data, status, headers, config) {
                    $scope.picsUrls.push(data.data.data.img_url);
                });
            });
        } else {
            $http({
                method: "PUT",
                data: $scope.product,
                headers: {
                    "Authorization": localStorage.getItem("Authorization")
                },
                url: $rootScope.apiUrl + '/ProductService/product/' + $stateParams.productId + '/update'
            }).then(function success(response) {
                $location.refresh();
            }, function error(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
            });
        }

    };

    $scope.$watch('picsUrls', function(newValue, oldValue) {
        if ($scope.files.length !== 0) {
            if (angular.equals($scope.files.length, $scope.picsUrls.length)) {
                $scope.picsUrls.forEach(function(picUrl) {
                    var picture = {
                        'imageSrc': picUrl
                    };
                    $scope.product.images.push(picture);
                });

                $http({
                    method: "PUT",
                    data: $scope.product,
                    headers: {
                        "Authorization": localStorage.getItem("Authorization")
                    },
                    url: $rootScope.apiUrl + '/ProductService/product/' + $stateParams.productId + '/update'
                }).then(function success(response) {
                    $location.refresh();
                }, function error(response) {
                    $scope.error = true;
                    $scope.errMsg = response.data;
                });
            }
        }
    }, true);

});