(function(){
    //
    //  ReportFactory holds methods to invoke REST calls for report operations
    //
    angular.module('app2')
        .factory('ReportFactory', ['$http', '$q', init] )

    function init($http, $q) {
        let ReportFactory = {};

        ReportFactory.addReport = addReport;
        ReportFactory.getAllReportsDummyData = getAllReportsDummyData;
        ReportFactory.getAllReports = getAllReports;
        ReportFactory.getAllReportsFiltered = getAllReportsFiltered;

        return ReportFactory;


        /*
         * Make a REST call that adds a report to the system
         * NOTE:  This method returns a promise
         */
        function addReport(aReportDTO) {
            console.log('ReportFactory.addReport() started.   aReportDTO=', aReportDTO);

            return $http.post('./api/reports/add1', aReportDTO).then(function(results) {
                // The REST call returned with a 200-299 status code

                // So, return some data
                return results.data;
            })
        }

        function getAllReportsDummyData() {
            console.log('getAllReportsDummyData() started.');

            let allReports = [
                { id: 1, display_name: "rpt1.txt", priority: "low", "active": true, created_by: "John Smith"},
                { id: 2, display_name: "rpt2.txt", priority: "high", "active": true},
                { id: 3, display_name: "rpt3.txt", priority: "high", "active": true}
            ];

            return allReports;
        }

        /*
        * Make a REST call and returns all of the reports data
        */
        function getAllReports() {
            console.log('getAllReports() started.');

            return $http.get('./api/reports/all').then(function(results) {
                // The REST call returned with a 200-299 status code
                console.log('results.data=', results.data);

                // So, return some data
                return results.data;
            })
        }

        /*
            * Make a REST call that returns a subset of the reports
            */
        function getAllReportsFiltered(aFilters) {
            console.log('getAllReportsFiltered() started.');

            let dto = {
                'filters': aFilters
            };

            return $http.get('./api/reports/all').then(function(results) {
                // The REST call returned with a 200-299 status code
                console.log('results.data=', results.data);

                let allReports = [
                    {id: 3, display_name: "rpt3.txt", priority: "high", "active": true}
                ];

                // So, return some data
                let returnedMap = {
                    'data': allReports,
                    'total': 1
                }

                return returnedMap;
            })
        }


    }   // End of ReportFactory
})();