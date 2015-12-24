'use strict';

angular.module('jHipsterSampleApp')
	.controller('DistrictDeleteController', function($scope, $uibModalInstance, entity, District) {

        $scope.district = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            District.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
