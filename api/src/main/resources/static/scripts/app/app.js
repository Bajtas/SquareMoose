var app = angular.module("SquareMooseDashboard", ["ngRoute"]);
app.config(function($routeProvider) {
	 $routeProvider

        .when('/login', {
			url: '/login',
            templateUrl: 'sign-in.html'
        })
		
		.when('/', {
			url: '/',
            templateUrl: 'main.html'
        })
		
		.otherwise({
        redirectTo: '/login'
      });
		
});