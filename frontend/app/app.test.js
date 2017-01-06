var app = angular.module('bankAppTest', ['bankApp', 'ngMockE2E']);

app.run(function($httpBackend) {

    var regPath = /^view\//;

    var transactions = [
        {
            date: '1.1.2017',
            user: 'testUser',
            type: 'Deposit',
            amount: '500'
        },
        {
            date: '1.1.2017',
            user: 'testUser',
            type: 'Withdraw',
            amount: '420'
        },
        {
            date: '1.1.2017',
            user: 'testUser',
            type: 'Deposit',
            amount: '250'
        }
    ];

    $httpBackend.whenGET('/history').respond(transactions);
    $httpBackend.whenGET(regPath).passThrough();
});

app.controller('Ctrl', function($scope, $http) {
    $http.get("/history")
        .then(function(response) {
          $scope.transactions = response.data;
    });
});
