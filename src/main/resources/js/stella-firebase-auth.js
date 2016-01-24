AJS.$(document).ready(function() {

	function authenticate(returnTo) {
	
		var obj = new Object();
		obj.secret = AJS.$("#stella-firebase-macro-remote-auth-jwt-token").text();
		var baseUrl = AJS.$("#stella-firebase-macro-remote-auth-jwt-url").text();
		obj.url = baseUrl;
	
		AJS.$.ajax({
			type: "post",
			cache: false,
			contentType: "application/json",
			data: JSON.stringify(obj),
			url: contextPath + "/rest/stella-firebase-macro-remote-auth-jwt-rest/1.0/auth-jwt/auth",
		    success: function(result) {
		    	console.log("result is: "+result);
		    	//alert(result);
				if(returnTo != null) {
					window.location.href = decodeURIComponent(returnTo + "?auth=" + result.jwt);
				} 
//				else {
//					window.location.href = decodeURIComponent(baseUrl + "?auth=" + result.jwt);
//				}
		    },
			error: function(xhr, ajaxOptions, thrownError) {
				alert("System error - please contact your system administrator");
			}
		});			
	}
	
	var returnTo = new RegExp('[\\?&]return_to=([^&]*)').exec(window.location.href);
	if(returnTo == null || returnTo == "" || returnTo.length != 2) {
		authenticate(null);
		alert("System error - parameter return_to is null");
		return;
	}
	
	authenticate(returnTo[1]);
});