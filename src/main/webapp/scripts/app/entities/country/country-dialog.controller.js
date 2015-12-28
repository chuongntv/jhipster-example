'use strict';

angular.module('jHipsterSampleApp').controller('CountryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Country',
        function($scope, $stateParams, $uibModalInstance, entity, Country) {

        $scope.country = entity;
        $scope.load = function(id) {
            Country.get({id : id}, function(result) {
                $scope.country = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('jHipsterSampleApp:countryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.country.id != null) {
                Country.update($scope.country, onSaveSuccess, onSaveError);
            } else {
                Country.update($scope.country, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
