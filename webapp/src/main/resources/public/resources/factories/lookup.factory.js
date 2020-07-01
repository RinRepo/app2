(function(){
    //
    //  LookupFactory holds methods to get lookup values from the app server
    //
    angular.module('app2')
        .factory('LookupFactory', ['$http', '$q', init] )

    function init($http, $q) {
        let LookupFactory = {};

        LookupFactory.getLookupWithTypeName = getLookupWithTypeName;

        return LookupFactory;



        /*
         * Make a REST call and returns a list of lookup DTOs with the passed-in category name
         */
        function getLookupWithTypeName(aType) {
            console.log('getLookupWithTypeName() started.');

            return $http.get('./api/lookups/' + aType).then(function(results) {
                // The REST call returned with a 200-299 status code

                // So, return some data
                return results.data;
            })
        }


    }
})();