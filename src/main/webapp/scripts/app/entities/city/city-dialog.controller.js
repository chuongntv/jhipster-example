'use strict';

angular.module('jHipsterSampleApp').controller('CityDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'City', 'Country',
        function($scope, $stateParams, $uibModalInstance, entity, City, Country) {

        $scope.city = entity;
        $scope.countrys = Country.query();
        $scope.load = function(id) {
            City.get({id : id}, function(result) {
                $scope.city = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jHipsterSampleApp:cityUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.city.id != null) {
                City.update($scope.city, onSaveSuccess, onSaveError);
            } else {
                City.save($scope.city, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
