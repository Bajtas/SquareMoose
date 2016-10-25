angular.module('starter.controllers')

.service('registerService', function ($ionicModal, $ionicPopup, $rootScope, $http) {
    var _registerModal = {};
    var _appScope = {};
    var _serviceUrl = '';

    this.registerModal = _registerModal;
    this.appScope = _appScope;
    this.serviceUrl = _serviceUrl;

    this.init = function (appScope, url) {
        _appScope = appScope;
        $ionicModal.fromTemplateUrl('templates/account/register.html', {
            controller: 'AppCtrl',
            scope: _appScope
        }).then(function (modal) {
            _registerModal = modal;
        });

        this.serviceUrl = url + 'UserService/users/add';
    };

    this.show = function () {
        _registerModal.show();
    };

    this.hide = function () {
        _registerModal.hide();
    };

    this.doRegister = function (credentials, modal) {
        if (credentials !== 'undefined') {
            var username = credentials.username;
            var email = credentials.email;
            var password1 = credentials.password1;
            var password2 = credentials.password2;

            if (password1 !== 'undefined' && password2 !== 'undefined') {
                if (password1 === password2) {
                    var user = {
                        login: username,
                        email: email,
                        password: password1
                    };

                    $http({
                        method: "POST",
                        data: user,
                        url: this.serviceUrl
                    }).then(function success(response) {
                        var alertPopup = $ionicPopup.alert({
                            title: "Registration message",
                            template: response.data
                        });
                        if (response.data === 'Registered successfully!') {
                            modal.hide();
                        }
                    }, function error(response) {
                        var alertPopup = $ionicPopup.alert({
                            title: "Problem occured!",
                            template: response.data
                        });
                    });

                } else {
                    var alertPopup = $ionicPopup.alert({
                        title: "Passwords does't matches!",
                        template: 'Please check if passwords are the same.'
                    });
                }
            }
        }
    };
})