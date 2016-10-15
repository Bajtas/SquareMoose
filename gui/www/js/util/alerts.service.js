angular.module('starter.controllers')

.service('alertsService', function ($ionicPopup, $rootScope) {

	this.showDefaultAlert = function (msg) {
		var alertPopup = $ionicPopup.alert({
			title: 'Some problems occured!',
			template: 'Something went wrong'
		});
		console.log(msg.errno);
	};

})