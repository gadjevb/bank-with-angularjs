describe('Routing', function() {
    beforeEach(module('bankApp'));

    it('Should map routes to controllers', function() {
        inject(function($route) {
            expect($route.routes['/'].templateUrl).toEqual('view/index_page.view.html');
            expect($route.routes[null].redirectTo).toEqual('/');
        });
    });
});
