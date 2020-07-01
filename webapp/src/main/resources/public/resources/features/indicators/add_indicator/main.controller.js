(function() {
    angular.module('app.features')
        .controller('addIndicator', ['$scope', '$mdToast', 'ReportFactory', 'lookupMap', Callback])

    function Callback( $scope, $mdToast, ReportFactory, lookupMap) {
        console.log('addIndicator controller started.');


        let addIndicatorVM = this;
        addIndicatorVM.new = {} //define empty map
        // // addReportVM.new.priorityAngular=2;
        addIndicatorVM.save = save;
        addIndicatorVM.reset = reset;
        // addIndicatorVM.validate = validate;
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
        //
        //
        //
        // addIndicatorVM.priority=
        //     [
        //         {
        //             'id': 1,
        //             'name': 'low'
        //         },
        //         {
        //             'id': 2,
        //             'name': 'medium'
        //         },
        //         {
        //             'id': 3,
        //             'name': 'high'
        //         },
        //         {
        //             'id': 4,
        //             'name': 'really high'
        //         }
        //     ]
        //
        function save() {
            console.log('save() started.   addIndicatorVM.new=', addIndicatorVM.new);
            addIndicatorVM.dataIsLoading = true;
        }

        //     // Create a map that will hold the new report info
        //     let addIndicatorDTO = {
        //         'name': addIndicatorVM.new.display_name,
        //         'priority': addIndicatorVM.new.priority
        //     }
        //
        //     ReportFactory.addIndicator(addIndicatorDTO).then(function (res) {
        //         // The REST worked  (it returned a status between 200-299)
        //         console.log('REST call succeeded.  returned info is res=', res);
        //         $mdToast.show(
        //             $mdToast.simple()
        //                 .textContent('you did a thing. good jobs.')
        //                 .hideDelay(6000)
        //                 .position('bottom right')
        //         );
        //     })
        //         .catch(function (res) {
        //             // The REST failed  (it returned a status code outside of 200-299)
        //             console.log('REST call failed.  returned info is res=', res);
        //             $mdToast.show(
        //                 $mdToast.simple()
        //                     .textContent('what. the. heck. Y U DO DIS?')
        //                     .hideDelay(3000)
        //                     .position('bottom right')
        //             );
        //         })
        //         .finally(function () {
        //             // This method is always called
        //             console.log('REST call finally() was reached.');
        //             addIndicatorVM.dataIsLoading = false;
        //             $mdToast.show(
        //                 $mdToast.simple()
        //                     .textContent('Fantastic')
        //                     .hideDelay(6000)
        //                     .position('bottom right')
        //             );
        //         });
        // }
        //
        //
        function reset() {
            addIndicatorVM.new = {};
            $scope.myForm.$setPristine();
            $scope.myForm.$setUntouched();
        }
        //
        // function validate() {
        //     console.log('validate() you entered: ', $scope.myForm.is_good);
        //     let userString = $scope.myForm.is_good.$modelValue
        //     if (userString == 'good') {
        //         $scope.myForm.is_good.$setValidity('customError', true);
        //     } else {
        //
        //         $scope.myForm.is_good.$setValidity('customError', false);
        //     }
        // }

        console.log('addIndicator controller finished.');
    }
})();
