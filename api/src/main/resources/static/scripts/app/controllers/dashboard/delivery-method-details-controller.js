angular.module('SquareMooseControllers')

.controller('DeliveryMethodDetailsCtrl', function($scope, $rootScope, $http, $state, $base64, $stateParams, $location, Upload) {
    $scope.error = false;
    $scope.selectedCategory = {};
    $scope.files = [];
    $scope.showInfo = false;
    $scope.saveInProgress = false;
    $scope.loadingInProgress = true;

    $scope.$on('$viewContentLoaded', function() {
        var deliveryTypesUrl = $rootScope.apiUrl + '/DeliveryTypeService/deliverytypes/' + $stateParams.methodId;
        var noPicUrl = 'https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg';

        $http.get(deliveryTypesUrl)
            .then(function(response) {
                $scope.deliveryType = JSOG.decode(response.data);

                if ($scope.deliveryType.imageSrc === undefined || $scope.deliveryType.imageSrc === null)
                    $scope.mainImage = noPicUrl;
                else
                    $scope.mainImage = $scope.deliveryType.imageSrc;

                $scope.loadingInProgress = false;
            }, function(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
                $scope.loadingInProgress = false;
            });
    });

    $scope.showPic = function(imageId) {
        if (!$scope.deleteImages) {
            $scope.deliveryType.images.forEach(function(image) {
                if (image.id === imageId) {
                    $scope.mainImage = image.imageSrc;
                }
            });
        } else {
            $scope.delPic(imageId);
        }
    };

    $scope.onFileSelect = function($file) {
        $scope.file = $file;
    };

    $scope.modifyDeliveryType = function() {
        $scope.saveInProgress = true;
        var apiForPicHost = 'http://uploads.im/api?upload';

        if ($scope.file !== null) {
            Upload.upload({
                url: apiForPicHost,
                file: $scope.file,
                headers: {
                    'Content-Type': 'multipart/form-data'
                },
                progress: function(e) {}
            }).then(function(data, status, headers, config) {
                $scope.picUrl = data.data.data.img_url;
            });
        } else {
            $http({
                method: "PUT",
                data: JSOG.encode($scope.product),
                headers: {
                    "Authorization": localStorage.getItem("Authorization")
                },
                url: $rootScope.apiUrl + '/ProductService/product/' + $stateParams.productId + '/update'
            }).then(function success(response) {
                $scope.updateInfo = response.data;
                $scope.showInfo = true;
                $scope.saveInProgress = false;
            }, function error(response) {
                $scope.showInfo = false;
                $scope.error = true;
                $scope.errMsg = response.data;
                $scope.saveInProgress = false;
            });
        }
    };

    $scope.$watch('picUrl', function(newValue, oldValue) {
        if (!angular.equals(newValue, oldValue)) {
            $scope.deliveryType.imageSrc = $scope.picUrl;
            $http({
                method: "PUT",
                data: JSOG.encode($scope.deliveryType),
                headers: {
                    "Authorization": localStorage.getItem("Authorization")
                },
                url: $rootScope.apiUrl + '/DeliveryTypeService/deliverytypes/' + $stateParams.methodId + '/update'
            }).then(function success(response) {
                $scope.updateInfo = response.data;
                $scope.showInfo = true;
                $scope.$emit('$viewContentLoaded');
                $scope.saveInProgress = false;
            }, function error(response) {
                $scope.error = true;
                $scope.errMsg = response.data;
                $scope.showInfo = false;
                $scope.saveInProgress = false;
            });
        }
    }, true);

    $scope.delPic = function(imageId) {
        for (var i = 0; i < $scope.product.images.length; i++) {
            if ($scope.product.images[i].id === imageId) {
                $scope.product.images.splice(i, 1);
                break;
            }
        }
    };
});