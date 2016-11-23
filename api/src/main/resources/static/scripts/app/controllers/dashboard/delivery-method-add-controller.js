angular.module('SquareMooseControllers')

.controller('DeliveryMethodAddCtrl', function($scope, $rootScope, $http, $state, $base64, $stateParams, $location, Upload) {
    $scope.error = false;
    $scope.selectedCategory = {};
    $scope.files = [];
    $scope.picsUrls = [];
    $scope.showInfo = false;
    $scope.product = {
        images: []
    };

    $scope.$on('$viewContentLoaded', function() {
        $scope.mainImage = 'https://upload.wikimedia.org/wikipedia/commons/a/ac/No_image_available.svg';
    });

    $scope.onFileSelect = function($file) {
        $scope.file = $file;
    };

    $scope.addDeliveryType = function() {
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
                method: "POST",
                data: $scope.deliveryType,
                headers: {
                    "Authorization": localStorage.getItem("Authorization")
                },
                url: $rootScope.apiUrl + '/DeliveryTypeService/deliverytypes/add'
            }).then(function success(response) {
                $scope.updateInfo = response.data;
                $scope.showInfo = true;
            }, function error(response) {
                $scope.showInfo = false;
                $scope.error = true;
                $scope.errMsg = response.data;
            });
        }

    };

    $scope.$watch('picUrl', function(newValue, oldValue) {
        if (!angular.equals(newValue, oldValue)) {
            $scope.deliveryType.imageSrc = $scope.picUrl;
            $http({
                method: "POST",
                data: JSOG.encode($scope.deliveryType),
                headers: {
                    "Authorization": localStorage.getItem("Authorization")
                },
                url: $rootScope.apiUrl + '/DeliveryTypeService/deliverytypes/add'
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

});