'use strict';

angular.module('jHipsterSampleApp')
    .controller('CityDetailController', function ($scope, $rootScope, $stateParams, entity, City, Country) {
        $scope.city = entity;
        $scope.load = function (id) {
            City.get({id: id}, function(result) {
                $scope.city = result;
            });
        };
        var unsubscribe = $rootScope.$on('jHipsterSampleApp:cityUpdate', function(event, result) {
            $scope.city = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
