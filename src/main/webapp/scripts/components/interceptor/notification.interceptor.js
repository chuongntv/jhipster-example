 'use strict';

angular.module('jHipsterSampleApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-jHipsterSampleApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-jHipsterSampleApp-params')});
                }
                return response;
            }
        };
    });
