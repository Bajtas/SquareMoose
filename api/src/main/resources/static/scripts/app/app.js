var app = angular.module("SquareMooseDashboard", ["ngRoute", "SquareMooseControllers"]);
app.config(function($routeProvider) {
	 $routeProvider

        .when('/login', {
			url: '/login',
            templateUrl: '../sign-in.html',
            controller: 'AppCtrl'
        })
		
		.otherwise({
        redirectTo: '/404.html'
      });
		
});