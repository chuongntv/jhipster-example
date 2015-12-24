'use strict';

angular.module('jHipsterSampleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('district', {
                parent: 'entity',
                url: '/districts',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Districts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/district/districts.html',
                        controller: 'DistrictController'
                    }
                },
                resolve: {
                }
            })
            .state('district.detail', {
                parent: 'entity',
                url: '/district/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'District'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/district/district-detail.html',
                        controller: 'DistrictDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'District', function($stateParams, District) {
                        return District.get({id : $stateParams.id});
                    }]
                }
            })
            .state('district.new', {
                parent: 'district',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/district/district-dialog.html',
                        controller: 'DistrictDialogController',
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
                        $state.go('district', null, { reload: true });
                    }, function() {
                        $state.go('district');
                    })
                }]
            })
            .state('district.edit', {
                parent: 'district',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/district/district-dialog.html',
                        controller: 'DistrictDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['District', function(District) {
                                return District.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('district', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('district.delete', {
                parent: 'district',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/district/district-delete-dialog.html',
                        controller: 'DistrictDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['District', function(District) {
                                return District.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('district', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
