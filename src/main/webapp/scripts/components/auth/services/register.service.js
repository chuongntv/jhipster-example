'use strict';

angular.module('jHipsterSampleApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


