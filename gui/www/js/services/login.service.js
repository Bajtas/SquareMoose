angular.module('starter.controllers')

.service('loginService', function ($ionicModal, $http, $ionicPopup, $base64, $ionicPlatform, $cordovaToast, $rootScope) {
    var _modal = {};
    var _appScope = {};
    var _serviceUrl = '';

    this.modal = _modal;
    this.serviceUrl = _serviceUrl;

    this.init = function (appScope, url) {
        _appScope = appScope;
        $ionicModal.fromTemplateUrl('templates/account/login.html', {
            controller: 'AppCtrl',
            scope: _appScope
        }).then(function (modal) {
            _modal = modal;
        });

        this.serviceUrl = url + 'UserService/user/';
    };

    this.show = function () {
        _modal.show();
    };

    this.hide = function () {
        _modal.hide();
    };

    this.login = function (credentials, modal) {
        var credentials = {
            login: 'admin',
            password: '123'
        }; // TO DELETE 

        if (credentials !== 'undefined') {
            var getRoleUrl = this.serviceUrl + credentials.login + '/role';
            if (credentials.login !== 'undefined' && credentials.password !== 'undefined') {
                var config = {
                    header: {
                        "Authorization": "Basic " + $base64.encode(credentials.login + ":" + credentials.password)
                    }
                };

                localStorage.setItem("Authorization", config.header.Authorization);
                localStorage.setItem("UserLogin", credentials.login);

                $http({
                    method: "GET",
                    data: credentials,
                    headers: config.header,
                    url: this.serviceUrl + credentials.login + '/account'
                }).then(function success(response) {
                    if (response.data === 'Logged in successfully!') {
                        $ionicPlatform.ready(function () {
                            $cordovaToast.show('Logged in successfully!', 'short', 'bottom');
                        });

                        modal.hide();

                        $http.get(getRoleUrl)
                        .then(function success(response) {
                            localStorage.setItem("UserRole", response.data.name);
                            $rootScope.$broadcast('loggedIn');
                        }, function error(response) {
                            var alertPopup = $ionicPopup.alert({
                                title: "Looks like unexpected problem occured!",
                                template: "Please contact with app developer."
                            });

                            localStorage.setItem("Authorization", "undefined");
                            localStorage.setItem("UserLogin", "undefined");
                            $rootScope.$broadcast('loggedIn');
                        });
                    }
                }, function error(response) {
                    var alertPopup = $ionicPopup.alert({
                        title: "Problem occured!",
                        template: "Username or password is wrong."
                    });

                    localStorage.setItem("Authorization", "undefined");
                    localStorage.setItem("UserLogin", "undefined");
                });
            } else {
                var alertPopup = $ionicPopup.alert({
                    title: "Passwords does't matches!",
                    template: 'Please check if passwords are the same.'
                });
            }
        }
    };
})