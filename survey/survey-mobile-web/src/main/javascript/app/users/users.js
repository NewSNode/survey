app.controller('UserListController', function($scope, $location, $timeout, UserService, config
		) {
	$scope.projectVersion = config.projectVersion;
	// get users from service
	$scope.users = UserService.query();
	// called when user presses edit button
	$scope.editUser = function editUser(user) {
		$location.path('/user/' + user.username);
	};
	// called when user presses delete button
	$scope.deleteUser = function deleteUser(user) {
		UserService.remove(user);
		// delay is needed since service is yet deleting the user
		$timeout(function() {
			$scope.users = UserService.query();
		}, 1000);
	};
});
