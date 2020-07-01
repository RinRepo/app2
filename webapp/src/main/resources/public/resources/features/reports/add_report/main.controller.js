(function() {
    angular.module('app.features')
        .controller('addReport', ['$timeout', '$scope', '$mdToast', 'ReportFactory', 'lookupMap', 'lookupReportType', Callback])

    function Callback($timeout, $scope, $mdToast, ReportFactory, lookupMap, lookupReportType) {
        console.log('addReport controller started.');
        console.log(lookupReportType);

        let addReportVM = this;
        addReportVM.new = {} //define empty map
        // addReportVM.new.priorityAngular=2;
        addReportVM.save = save;
        addReportVM.reset = reset;
        addReportVM.validate = validate;

        // Set the lookupData to hold the injected lookupMap
        addReportVM.lookupData = lookupMap;


        // Clear-out the report fields on page load
        // reset();

        window.document.title = "Add Report | APP2";

        addReportVM.$onInit = function () {
            console.log('addReport onInit() started.');
            console.log('addReport onInit() finished.');
        };



        addReportVM.priority=
            [
                {
                    'id': 1,
                    'name': 'low'
                },
                {
                    'id': 2,
                    'name': 'medium'
                },
                {
                    'id': 3,
                    'name': 'high'
                },
                {
                    'id': 4,
                    'name': 'really high'
                }
            ]

        function save() {
            console.log('save() started.   addReportVM.new=', addReportVM.new);
            addReportVM.dataIsLoading = true;

            // Create a map that will hold the new report info
            let addReportDTO = {
                'name': addReportVM.new.display_name,
                'priority': addReportVM.new.priority
            }

            ReportFactory.addReport(addReportDTO).then(function (res) {
                // The REST worked  (it returned a status between 200-299)
                console.log('REST call succeeded.  returned info is res=', res);
                $mdToast.show(
                    $mdToast.simple()
                        .textContent('you did a thing. good jobs.')
                        .hideDelay(6000)
                        .position('bottom right')
                );
            })
                .catch(function (res) {
                    // The REST failed  (it returned a status code outside of 200-299)
                    console.log('REST call failed.  returned info is res=', res);
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('what. the. heck. Y U DO DIS?')
                            .hideDelay(3000)
                            .position('bottom right')
                    );
                })
                .finally(function () {
                    // This method is always called
                    console.log('REST call finally() was reached.');
                    addReportVM.dataIsLoading = false;
                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('Fantastic')
                            .hideDelay(6000)
                            .position('bottom right')
                    );
                });
        }


        function reset() {
            addReportVM.new = {};
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

        console.log('addReport controller finished.');
    }
})();
