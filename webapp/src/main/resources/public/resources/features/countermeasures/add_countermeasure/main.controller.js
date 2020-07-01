(function() {
    angular.module('app.features')
        .controller('addCountermeasure', ['$scope', '$mdToast', 'ReportFactory', 'lookupMap', Callback])

    function Callback( $scope, $mdToast, ReportFactory, lookupMap) {
        console.log('addCountermeasure controller started.');


        let addCountermeasureVM = this;
        addCountermeasureVM.new = {} //define empty map
        // // addReportVM.new.priorityAngular=2;
        addCountermeasureVM.save = save;
        addCountermeasureVM.reset = reset;
        addCountermeasureVM.validate = validate;
        //
        // // Set the lookupData to hold the injected lookupMap
        // addIndicatorVM.lookupData = lookupMap;
        //
        //
        // // Clear-out the report fields on page load
        // // reset();
        //
        // window.document.title = "Add Indicator | APP2";
        //
        // addIndicatorVM.$onInit = function () {
        //     console.log('addIndicator onInit() started.');
        //     console.log('addIndicator onInit() finished.');
        // };
        // //
        // //
        // //
        addCountermeasureVM.status=
            [
                {
                    'id': 1,
                    'name': 'Active'
                },
                {
                    'id': 2,
                    'name': 'Inactive'
                },
                {
                    'id': 3,
                    'name': 'Pending'
                }
            ]
        //
        function save() {
            console.log('save() started.   addCountermeasureVM.new=', addCountermeasureVM.new);
            addCountermeasureVM.dataIsLoading = true;
            addCountermeasureVM.dataIsLoading = false;

        }


        function reset() {
            addCountermeasureVM.new = {};
            $scope.myForm.$setPristine();
            $scope.myForm.$setUntouched();
        }

        function validate() {
            console.log('validate() you entered: ', $scope.myForm.is_good);
            let userString = $scope.myForm.is_good.$modelValue
            if (userString == 'good') {
                $scope.myForm.is_good.$setValidity('customError', true);
            } else {

                $scope.myForm.is_good.$setValidity('customError', false);
            }
        }

        console.log('addCountermeasure controller finished.');
    }
})();
