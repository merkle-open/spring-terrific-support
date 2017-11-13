(function($) {
	Tc.Module.Table = Tc.Module.extend({
		init: function($ctx, sandbox, id) {
			// call base constructor
			this._super($ctx, sandbox, id);
		},


		on: function(callback){
			var self = this,
				$ctx = self.$ctx;


			// init basic table sorter (4 columns, last actions only)
			$('.tablesorter', $ctx).tablesorter({
				sortList: [[0,0]],
				widgets: ['zebra'],
				headers: {
					3: {
						sorter: false
					}
				}
			});


			callback();
		},


		after: function(callback){
			
		}
	});
})(Tc.$);