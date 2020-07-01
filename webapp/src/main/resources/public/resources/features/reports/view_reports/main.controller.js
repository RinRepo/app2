(function(){
    angular.module('app.features')
        .controller('viewReports', ['$timeout', '$stateParams', '$scope', '$window', 'reportsViewData', 'lookupPriorities', 'GridService', 'ReportFactory', Callback])

    function Callback($timeout, $stateParams, $scope, $window, reportsViewData, lookupPriorities, GridService, ReportFactory) {
        console.log('viewReports controller started.');

        let viewReportsVM = this;

        // Create a lookup map that holds a map of all of the lookup values
        $scope.lookupData = {
            'priority' : lookupPriorities
            // 'report_type' : lookupReportTypes
        }

        window.document.title = "View Reports | APP2";

        viewReportsVM.$onInit = function() {
            console.log('viewReports onInit() started.');

            let gridOptions = { };

            // Initialize the grid with hard-coded data
            gridOptions.data = reportsViewData;

            gridOptions.appScopeProvider = $scope;
            gridOptions.flatEntityAccess = true;
            gridOptions.enableFiltering = true;
            gridOptions.enableColumnResizing = true;
            gridOptions.enableGridMenu = true;
            gridOptions.saveFocus = false;
            gridOptions.saveGrouping = false;
            gridOptions.savePinning = false;
            gridOptions.saveSelection = false;
            gridOptions.saveTreeView = false;
            gridOptions.multiSelect = true;
            gridOptions.exporterMenuPdf = false;
            gridOptions.exporterMenuExcel = false;
            gridOptions.exporterFieldApplyFilters = true;
            gridOptions.rowHeight = 45;
            gridOptions.enableSelectAll = true;
            gridOptions.enableSelectionBatchEvent = false;
            gridOptions.scope = $scope;

            // Set the pagination properties (required in order for ui-grid pagination to work)
            gridOptions.totalItems = gridOptions.data.length;
            gridOptions.paginationPageSize = 50;
            gridOptions.paginationPageSizes = [25, 50, 100, 250];

            //tells the grid we are using server-side filtering
            gridOptions.useExternalFiltering = true;

            //tells the grid to call our code when user enters a filter
            gridOptions.onRegisterApi = registerGridApi;


            // Define the grid columns
            gridOptions.columnDefs = [
                {
                    name: 'id',
                    field: 'id',
                    displayName: 'Id',
                    visible: true,
                    type: 'number'
                },
                {
                    name: 'display_name',
                    field: 'display_name',
                    displayName: 'Report Name',
                    visible: true,
                    type: 'string'
                },
                {
                    name: 'priority',
                    field: 'priority',
                    displayName: 'Priority',
                    visible: true,
                    type: 'string',
                    filterHeaderTemplate: '<md-select placeholder="select" ng-model="col.filters[0].term" multiple>\n' +
                        '<md-option ng-value="lookup.id" ng-repeat="lookup in grid.appScope.lookupData.priority track by lookup.id">{{lookup.name}}</md-option>\n' +
                        '</md-select>'
                },
                {
                    name: 'active',
                    field: 'active',
                    displayName: 'Active',
                    visible: true,
                    type: 'boolean',
                    filterHeaderTemplate: '<md-select placeholder="select" ng-model="col.filters[0].term">\n' +
                        '<md-option value="">Select</md-option>' +
                        '<md-option value="true">Yes</md-option>' +
                        '<md-option value="false">No</md-option>' +
                        '</md-select>'
                },
                {
                    name: 'description',
                    field: 'description',
                    displayName: 'Description',
                    visible: true,
                    type: 'String'
                },
                {
                    name: 'created_date',
                    field: 'created_date',
                    displayName: 'Created Date',
                    cellFilter: "date: 'yyyy-MM-dd'",
                    visible: true,
                    type: 'Timestamp',
                    filterHeaderTemplate: '<div class="ui-grid-filter-container row">' +
                        '<div class="col-md-6 col-md-offset-0 col-sm-6 col-sm-offset-0 col-xs-6 col-xs-offset-0">' +
                        '<div><md-datepicker ng-model="col.filters[0].term" md-hide-icons="triangle" md-placeholder="From"></md-datepicker></div>' +
                        '<div><md-datepicker ng-model="col.filters[1].term" md-hide-icons="triangle" md-placeholder="To"></md-datepicker></div>' +
                        '</div>' +
                        '</div>',
                    filters: [
                        {
                            name: 'From'
                        },
                        {
                            name: 'To'
                        }
                    ]
                },
                {
                    name: 'last_modified_date',
                    field: 'last_modified_date',
                    displayName: 'Last Modified Date',
                    cellFilter: "date: 'yyyy-MM-dd'",
                    visible: true,
                    type: 'Timestamp',
                    filterHeaderTemplate: '<div class="ui-grid-filter-container row">' +
                        '<div class="col-md-6 col-md-offset-0 col-sm-6 col-sm-offset-0 col-xs-6 col-xs-offset-0">' +
                        '<div><md-datepicker ng-model="col.filters[0].term" md-hide-icons="triangle" md-placeholder="From"></md-datepicker></div>' +
                        '<div><md-datepicker ng-model="col.filters[1].term" md-hide-icons="triangle" md-placeholder="To"></md-datepicker></div>' +
                        '</div>' +
                        '</div>',
                    filters: [
                        {
                            name: 'From'
                        },
                        {
                            name: 'To'
                        }
                    ]
                },
            ]


            function registerGridApi (gridApi) {
                console.log('registerGridApi() started.');
                $scope.gridApi = gridApi;
                viewReportsVM.gridApi = gridApi;

                gridApi.core.on.filterChanged($scope, function() {
                    // The user has typed-in or selected a filter

                    // Get the list of filter maps from the grid
                    let currentFilters = GridService.getListOfFilters(this.grid.columns)

                    if (angular.isDefined(viewReportsVM.currentFilterTimeout)) {
                        // Another filter is running -- so cancel it
                        $timeout.cancel(viewReportsVM.currentFilterTimeout)
                    }

                    // Define a new filter with a timeout of 1000 milliseconds
                    viewReportsVM.currentFilterTimeout = $timeout(function() {

                        // Set this flag so that the web page will show a spinner (telling the user to wait for the results)
                        viewReportsVM.dataIsLoading = true;

                        ReportFactory.getAllReportsFiltered(currentFilters).then(function(results) {
                            // The REST call has returned with data.

                            console.log('Got results:  results=', results);

                            // Set the grid data with the returned data
                            viewReportsVM.gridOptions.data = results.data;

                            // Set the grid total with the new total
                            viewReportsVM.gridOptions.totalItems = results.total;

                        }).catch(function (results) {
                            // Something went wrong -- display an error
                            console.log('Something failed getting the filtered results.  results=', results);
                        }).finally(function () {
                            // Kill the spinner
                            viewReportsVM.dataIsLoading = false;
                        });

                    }, 1000)
                })
            };



            viewReportsVM.gridOptions = gridOptions;

            console.log('viewReports onInit() finished.');
        };

        viewReportsVM.userPrefs = {username : "",
                                    colorPreference : ""};
        viewReportsVM.submit = function() {
            viewReportsVM.userPrefs.favoriteColor = "cerulean"
            console.log("userPrefs =", viewReportsVM.userPrefs)

        }


        console.log('viewReports controller finished.');
    }
})();