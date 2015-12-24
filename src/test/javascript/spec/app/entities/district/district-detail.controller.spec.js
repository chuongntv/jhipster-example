'use strict';

describe('District Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockDistrict, MockCity;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockDistrict = jasmine.createSpy('MockDistrict');
        MockCity = jasmine.createSpy('MockCity');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'District': MockDistrict,
            'City': MockCity
        };
        createController = function() {
            $injector.get('$controller')("DistrictDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'jHipsterSampleApp:districtUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
