
function TemplateServiceClient(endpointUrl) {
	// care for trailing slash in endpoint URL
	if(endpointUrl.endsWith("/")) {
		this._serviceEndpoint = endpointUrl.substr(0,endpointUrl.length-1);
	} else {
		this._serviceEndpoint = endpointUrl;
	}
};

TemplateServiceClient.prototype.getLocal = function(successCallback, errorCallback) {
	this.sendRequest("GET",
		"local",
		"",
		"text/plain",
		{},
		successCallback,
		errorCallback
	);
};

TemplateServiceClient.prototype.getRemote = function(successCallback, errorCallback) {
	this.sendRequest("GET",
		"remote",
		"",
		"text/plain",
		{},
		successCallback,
		errorCallback
	);
};

/**
* sends an AJAX request to a resource.
* Parameters:
*   - method: the HTTP method used
*	- relativePath: the path relative to the client's endpoint URL
*   - content: the content to be sent in the HTTP request's body
*   - mime: the MIME-type of the content
*   - customHeaders: a JSON string with additional header parameters to be sent
*   - successCallback: a callback function invoked in case the request succeeded. Expects two parameters "data" and "type", where "data" represents the content of the response and "type" describes the MIME-type of the response
*   - errorCallback: a callback function invoked in case the request failed. Expects one parameter "error" representing the error occurred.
*
*/
TemplateServiceClient.prototype.sendRequest = function(method, relativePath, content, mime, customHeaders, successCallback, errorCallback) {
	var mtype = "text/plain; charset=UTF-8"
	if(mime !== 'undefined') {
		mtype = mime;
	}

	var rurl = this._serviceEndpoint + "/" + relativePath;

	var ajaxObj = {
		url: rurl,
		type: method.toUpperCase(),
		data: content,
		contentType: mtype,
		crossDomain: true,
		headers: {},

		error: function (xhr, errorType, error) {
			console.log(error);
			var errorText = error;
			if (xhr.responseText != null && xhr.responseText.trim().length > 0) {
				errorText = xhr.responseText;
			}
			errorCallback(errorText);
		},
		success: function (data, status, xhr) {
			var type = xhr.getResponseHeader("Content-Type");
			successCallback(data, type);
		},
	};

	if (customHeaders !== undefined && customHeaders !== null) {
		$.extend(ajaxObj.headers, customHeaders);
	}

	$.ajax(ajaxObj);
};

/**
* Convenience function to check if a String ends with a given suffix.
*/
String.prototype.endsWith = function(suffix) {
	return this.indexOf(suffix, this.length - suffix.length) !== -1;
};
