'use strict';

angular.module('jHipsterSampleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('city', {
                parent: 'entity',
                url: '/citys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Citys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/city/citys.html',
                        controller: 'CityController'
                    }
                },
                resolve: {
                }
            })
            .state('city.detail', {
                parent: 'entity',
                url: '/city/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'City'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/city/city-detail.html',
                        controller: 'CityDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'City', function($stateParams, City) {
                        return City.get({id : $stateParams.id});
                    }]
                }
            })
            .state('city.new', {
                parent: 'city',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/city/city-dialog.html',
                        controller: 'CityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    code: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('city', null, { reload: true });
                    }, function() {
                        $state.go('city');
                    })
                }]
            })
            .state('city.edit', {
                parent: 'city',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/city/city-dialog.html',
                        controller: 'CityDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['City', function(City) {
                                return City.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('city', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('city.delete', {
                parent: 'city',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/city/city-delete-dialog.html',
                        controller: 'CityDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['City', function(City) {
                                return City.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('city', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
