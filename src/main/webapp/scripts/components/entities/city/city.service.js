'use strict';

angular.module('jHipsterSampleApp')
    .factory('City', function ($resource, DateUtils) {
        return $resource('api/cities/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
