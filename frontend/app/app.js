var app = angular.module('bankApp', ['ngRoute']);

app.config(function($routeProvider, $locationProvider) {
    $routeProvider
    .when('/', {
        templateUrl: 'view/index_page.view.html'
    })
    .when('/history', {
        templateUrl: 'view/transaction_history_page.view.html'
    })
    .otherwise({
        redirectTo: '/'
    });

    $locationProvider.html5Mode(true);
});
