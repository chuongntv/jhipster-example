'use strict';

angular.module('jHipsterSampleApp')
	.controller('CityDeleteController', function($scope, $uibModalInstance, entity, City) {

        $scope.city = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            City.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
