var app = angular.module('bankApp', ['ngRoute']);

app.config(function($routeProvider, $locationProvider) {
    $routeProvider
    .when('/', {
        templateUrl: 'view/login_page.view.html'
    })
    .when('/register', {
        templateUrl: 'view/register_page.view.html'
     })
    .when('/transaction', {
        templateUrl: 'view/transaction_page.view.html'
    })
    .when('/account', {
        templateUrl: 'view/account_page.view.html'
     })
    .when('/history', {
        templateUrl: 'view/transaction_history_page.view.html'
    })
    .otherwise({
        redirectTo: '/'
    });

    $locationProvider.html5Mode(true);
});
