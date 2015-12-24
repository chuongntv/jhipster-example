'use strict';

angular.module('jHipsterSampleApp')
    .controller('DistrictDetailController', function ($scope, $rootScope, $stateParams, entity, District, City) {
        $scope.district = entity;
        $scope.load = function (id) {
            District.get({id: id}, function(result) {
                $scope.district = result;
            });
        };
        var unsubscribe = $rootScope.$on('jHipsterSampleApp:districtUpdate', function(event, result) {
            $scope.district = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
