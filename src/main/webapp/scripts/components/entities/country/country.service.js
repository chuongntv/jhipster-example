'use strict';

angular.module('jHipsterSampleApp')
    .factory('Country', function ($resource, DateUtils) {
        return $resource('api/country/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'POST' }
        });
    });
