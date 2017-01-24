var app = angular.module('bankAppTest', ['bankApp', 'ngMockE2E']);

app.run(function($httpBackend) {
    var regPath = /^view\//;

    var transactions = [
        {
            id: 1,
            date: '1.1.2017',
            type: 'Deposit',
            amount: '500'
        },
        {
            id: 2,
            date: '1.1.2017',
            type: 'Withdraw',
            amount: '420'
        },
        {
            id: 3,
            date: '1.1.2017',
            type: 'Deposit',
            amount: '250'
        }
    ];

    $httpBackend.whenGET('/history').respond(transactions);
    $httpBackend.whenGET(regPath).passThrough();
});
