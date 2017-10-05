(function($) {
	Ffm.Module = Tc.Module.extend({
		
		authenticated : false,
		
		fireAll : function(event,data){
			var self =  this;

			// connectors
			var connectors = self.connectors;
			$.each(connectors,function(){
				self.fire(event,data,this);
			});

			// self
			var handlerName = "on" + Tc.Utils.String.capitalize(event);
			if ( self[handlerName] ){
				self[handlerName](data);
			}
		},
		
		dataUrl : function(name,$ctx) {
			var self = this;
			var $ctx = $ctx || self.$ctx;
			
			if ($ctx.data(name)) {
				var url = $ctx.data(name);
				return self.serverUrl(url);
			}
			return null;
		},
	
		serverUrl : function(url){
			// 3 cases: 
			// absolute (keep): "http://host.domain/path/file"
			// relative (keep): "path/file" 
			
			// server relative : "/path/file" 
			if (url && url.charAt(0) == "/") {
				// get base url
				var base = Ffm.baseUrl;
				// avoid double slash "//"
				if (base.charAt(base.length - 1) == "/") {
					url = url.substr(1);
				}
				url = base + url;
			}		
			return url;
		},
		
		ajax : function(config){
			var self = this;
			// add error handler if none!			
			if (! config.error){
				config.error = function(jqXHR, textStatus, errorThrown){
					var error = {"code" : jqXHR.status, "message" : errorThrown};
					self.errorHandler(error, self);
				};
			}
			return $.ajax(config);
		},
		
		get : function(url,callback){
			var self = this;
			var config = {
				"type" : 'GET',
				"url" : url,
				"dataType" : 'json',
				"success" : callback,
 			};
			return self.ajax(config);
		},
		
		defaultErrorHandler : function(error){
			var self = self || this;
			
			var errorName = "error" + error.code;
			
			self.fireAll(errorName,error,self);
		},
		
		errorHandler : function(error){
			var self = self || this;
			self.defaultErrorHandler(error,self);
		},
		
		defaultAuthenticated : function(data){
			var self = self || this;
			console.log("authenticated");
			self.authenticated = true;
		},
		
		onAuthenticated : function(data){
			var self = self || this;
			self.defaultAuthenticated(data);
		},
		
		defaultLogout : function(data){
			var self = self || this;
			console.log("logout");
			self.authenticated = false;
		},
		
		onLogout : function(data){
			var self = self || this;
			self.defaultLogout(data);
		}
				
	});
	Ffm.baseUrl = '';
})(Tc.$);
