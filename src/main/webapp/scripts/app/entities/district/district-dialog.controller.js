'use strict';

angular.module('jHipsterSampleApp').controller('DistrictDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'District', 'City',
        function($scope, $stateParams, $uibModalInstance, entity, District, City) {

        $scope.district = entity;
        $scope.citys = City.query();
        $scope.load = function(id) {
            District.get({id : id}, function(result) {
                $scope.district = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jHipsterSampleApp:districtUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.district.id != null) {
                District.update($scope.district, onSaveSuccess, onSaveError);
            } else {
                District.save($scope.district, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
