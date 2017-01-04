var app = angular.module('bankApp', ['ngRoute']);

app.config(function($routeProvider) {
    $routeProvider
    .when('/', {
        templateUrl: 'view/index_page.view.html'
    })
    .otherwise({
        redirectTo: '/'
    });
});
