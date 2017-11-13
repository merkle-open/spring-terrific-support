(function($) {
	Tc.Module.UserList = Tc.Module.extend({
			
		on : function(callback){
		console.log('user-list');
			var self = this;
			$('.tablesorter',self.$ctx).tablesorter({
		        sortList: [[0,0]],
		        headers: { 
		        	3: { 
		                sorter: false 
		            } 
		        } 
		    });
			callback();
		},
		
		after : function(callback){
			var self = this;
		}
		
	});	
})(Tc.$);