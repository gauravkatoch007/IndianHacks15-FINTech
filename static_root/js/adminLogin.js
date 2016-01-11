angular.module('AdminLoginApp',[]).controller('AdminLoginController', ['$scope','$http','$window','$location',function($scope,$http,$window,$location){

		// $scope.dataObj = {};



		$scope.signIn = function(){


			var username = $scope.username;
			var password = $scope.password;


		    // alert(dataObj.username +"  \n"+ dataObj.password);

			$http.post('/frsh/auth/', {'username':username,'password':password}).
			success(function(data, status, headers, config) {
				console.log("In success");
				console.log(status);
				console.log(data[0].status);
			        console.log(data[0].redirectURL);
				if(data[0].status == 'staff'){
					//strips current url to base url
					$location.path('/');
				        console.log("there we go!");
					//code to redirect to another page
				        if(data[0].redirectURL){
					    $window.location.href = data[0].redirectURL;
					}else{
					$window.location.href ='/frshAdmin/';
					}
					
				}else if(data[0].status == 'Not a Superuser'){
				    //strips current url to base url
					$location.path('/');
					//code to redirect to another page
					$window.location.href ='/admin/dashbd/';
				}else if(data[0].status == 'ifeedback'){
                    //strips current url to base url
					$location.path('/');
					//code to redirect to another page
					$window.location.href ='/feedback/get/';
                }else if(data[0].status == 'kitchenBoy'){
                    //strips current url to base url
					$location.path('/');
					//code to redirect to another page
					$window.location.href ='/kitchen_manager/counter/order/summary/';
                }else if(data[0].status == 'packingBoy'){
                    //strips current url to base url
					$location.path('/');
					//code to redirect to another page
					$window.location.href ='/packingApp/orderPackingPanel/';
                }
				//'authentication failure'

			}).
			error(function(data, status, headers, config) {
				console.log("In error");
				console.log(status);
				console.log(data);
			});
		};


		// $scope.getAdmins = function(){

		// 	$http.get('/frsh/readStaffInfo/').

		// 	success(function(data, status, headers, config) {
		// 		alert("In sucess");
		// 		alert(status);
		// 		alert(data[0].username);
		// 	}).

		// 	error(function(data, status, headers, config) {
		// 		alert("In error");
		// 		alert(status);
		// 		alert(data);
		// 	});
		// };
}]);

