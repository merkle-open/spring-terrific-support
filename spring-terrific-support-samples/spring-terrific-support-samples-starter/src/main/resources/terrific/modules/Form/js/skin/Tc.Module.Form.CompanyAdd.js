(function($) {
	Tc.Module.Form.CompanyAdd = function(parent) {
		/**
		* override the appropriate methods from the decorated module (ie. this.get = function()).
		* the former/original method may be called via parent.<method>()
		*/
		this.on = function(callback) {
			// calling parent method
			parent.on(callback);

			var $t = this.$ctx;


			// module delegate click handler: add new delivery address using replace template, show/hide buttons
			$t.on('click.add', '.addItem', function(e) {
				var _replacedMarkup,
					_appendTarget,
					_t = $(this),
					_dataTemplate = _t.data('template'),
					_dataCount = parseInt(_t.data('count'), 10);

				if (typeof _dataTemplate !== 'undefined' && typeof _dataCount !== 'undefined') {
					_appendTarget = $t.find('#template' + _dataTemplate);

					_replacedMarkup = $(_appendTarget).templateReplace({
						'current': _dataCount,
						'next': _dataCount + 1
					});

					$(_appendTarget).before(_replacedMarkup);
					_t.hide();
				}

				return false;
			});


			// module delegate click handler: remove delivery address, show triggering button again
			$t.on('click.remove', '.removeItem', function(e) {
				var _t = $(this),
					_dataOrigin = _t.data('origin'),
					_dataTemplate = _t.data('template'),
					_dataCount = parseInt(_t.data('count'), 10),
					_trigger;

				if (typeof _dataOrigin !== 'undefined' && typeof _dataTemplate !== 'undefined' && typeof _dataCount !== 'undefined') {
					// remove delivery address container
					$t.find('[data-origin=' + _dataOrigin + ']').remove();


					// show appropriate trigger button again (secondlast in all items array)
					var _allAddItemTriggers = $t.find('.addItem[data-template=' + _dataTemplate + ']');
					var _lengthAll = _allAddItemTriggers.length;

					_trigger = _allAddItemTriggers[_lengthAll - 2];
					$(_trigger).show();
				}

				return false;
			});
		};


		this.after = function() {
			// calling parent method
			parent.after();
		};
	};
})(Tc.$);