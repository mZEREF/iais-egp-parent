(function($jq) {

var FQN = 'sopform.controls.base.RepeatableSection';

Form.Controls.RepeatableSection = Form.Controls.Container.extend({
	init: function(){
		conditionManager.doControlInit(this);
	},

	getDefaults: function() {
		return {
			title: 'Repeatable Section',
			captionOrientation: 'horizontal',
			type: 'vertical',
			cols: 1,
			repeat: 1,
			controlButton: 'More',
			border: true,
			width: '',
			_id: this.id
		}
	},

	getGrid: function() {
		var elem = this.getElement();
		var $table = $jq(elem).find('table.control-grid');
		return $table;
	},

	/**
	 * Refresh this control and all controls which
	 * are inside it.
	 *
	 * @see Control#refresh()
	 */
	refresh: function(placeholder) {
		// this is required for ckeditor to run properly
		// in design mode
		if (Form.currentMode == 'design') {
			var el = this.getElement();
			if (el) {
				$('textarea', el).each(function() {
					var richtext = CKEDITOR.instances[this.id];
					if (richtext) {
						richtext.destroy();
					}
				});
			}
		}
		Form.Controls.Control.prototype.refresh.apply(this, arguments);

		var isDesign = this.mode == 'design'
		var $grid = this.getGrid();
		for (var i = 0, n = $grid.length; i < n; i++) {
			var $_grid = $grid.eq(i);
			$jq.each(this.children, function() {
				var placeholder = Form.Common.placeholder();
				$_grid.grid('append', placeholder, this.position);
				// during design there will only be one row, so the index is not needed
				this.refresh(placeholder, isDesign? undefined: i);
			});
		}

		//uncomment this line to hide the color block when there is nothing.
		//but then again.. if you comment this.. the loaded design form will have color.
		//if(Form.currentMode != 'design'){
			var el = $grid;
			var theme = Form.Theme.getTheme(Form.properties.themeName);
			if(theme.Page){
				$(el).find(" > * > tr:odd").css("background-color", theme.Page.tr_odd_row.bgcolor);
				$(el).find(" > * > tr:even").css("background-color", theme.Page.tr_even_row.bgcolor);
			}
		//}
	},

	appendRow: function() {
		//Remove for rebind of cunstom validation
		//var callAfterRender = (arguments.length > 0 && typeof arguments[0] == 'boolean') ? arguments[0] : true;
		var renderer = this.getRenderer();
		if (renderer._appendRow) {
			var grid = this.getGrid();
			var maxRows = this.properties['maxRows'];
			if(maxRows && grid.length >= maxRows){
				var sectionDlg = $("#repeatable-section-maxlength-dlg");
				if(!$("#repeatable-section-maxlength-dlg").length){
					sectionDlg = $('<div id="repeatable-section-maxlength-dlg" style="display:none">The repeatable section length has reached its peak.</div>');
					$("body").append(sectionDlg);
				}
				sectionDlg.dialog({
					draggable: false,
					modal: true,
					title: 'Alert',
					buttons: {
						'OK': function(e) {
							$(this).dialog("close");
						}
					}
				});
			}else{
				var index = renderer._appendRow(this);
	//			if (callAfterRender && renderer.afterRender) {
	//				renderer.afterRender(this, null, index.actual);
	//			}
				
				var $_grid = this.getGrid().eq(index.real);
				$jq.each(this.children, function() {
					var placeholder = Form.Common.placeholder();
					$_grid.grid('append', placeholder, this.position);
					this.refresh(placeholder, index.actual);
				});
				conditionManager.doRepeatableRowCondition(this);
				if (window.aclManager) aclManager.processACLDisabledChildren([this],Form.currentPage);
				
				return index;
			}
		}
	},

	removeRow: function(index) {
		var el = this.getElement();
		var $entries = $('.repeatable-section-item', el);
		if ($entries.length == 0) return;

		$entries.each(function(idx, elem) {
			if (idx > index) {
				var $this = $(elem);
				var _index = $this.data('index');
				_index.real--;
				$this.data('index', _index);
			}
		});
		$entries.eq(index).remove();
		conditionManager.doRepeatableRowCondition(this);
		
		if(this.properties.type == "pagination") {
			this._renderPagination(index);
		}
		try {
	        if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common) {
				window.parent.EGP.Common.onEgovFormReady($(document.documentElement).height());
			}
	    } catch (e) {}
	},
	
	_renderPagination: function(selectedIndex) {
		var num_entries;
		var itemSelect;
		
		if(Form.currentMode == "printerFriendly") {
			return;
//			num_entries = jQuery('fieldset', this.getElement()).length;
//			itemSelect = "fieldset";
		}else {
			num_entries = jQuery('.repeatable-section-item', this.getElement()).length;
			itemSelect = ".repeatable-section-item";
		}
		
		$(".rs-pagination", this.getElement()).pagination(num_entries, {
            callback: this._pageSelectCallBack,
            items_per_page: 1,
            current_page: selectedIndex > num_entries - 1 ? num_entries - 1 : selectedIndex,
            num_display_entries: 5,
            next_text: ">>",
            prev_text: "<<",
            element: this.getElement(),
            itemSelect: itemSelect
        });
		
		
	},
	
	_pageSelectCallBack: function (page_index, jq){
		$(this.itemSelect, this.element).hide();
		$(this.itemSelect + ':eq('+page_index+')', this.element).show();
        
        return false;
    },
	
	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var data_json = window.data_json;
		var value = [];
		if (el && !Form.isView) {
			var $entries = $('.repeatable-section-item', el);
			var rows = $entries.length;
			var children = this.children;
	
			for (var i = 0; i < rows; i++) {
				var n = children.length;
				var obj = {};
				var index = $entries.eq(i).data('index');
				for (var j = 0; j < n; j++) {
					var child = children[j];
					var childId = child.properties.id;
					if(!child.getRuntimeData) {
						continue;
					}
					if(child.getElement() && child.getElement().length>0){
						obj[childId] = child.getRuntimeData(index.actual);
					}
					else if (data_json) {
						var id = this.properties.id;
						if (data_json[id] && data_json[id][i] && data_json[id][i][childId]) {
							obj[childId] = data_json[id][i][childId];
						}
					}
					
					if (data_json && !obj[childId]) {
						obj[childId] = child.getRuntimeData(i);
					}
				}
				value.push(obj);
			}
		} else if (data_json) {
			var p = data_json[this.properties.id];
			if (p !== undefined) value = p.value;
		}

		return { type: Form.Constants.KEY_MULTI_ROW, value: value };
	},
	
	setRuntimeData: function(){
		var p = data_json[this.properties.id];
		if(p != null){
			var rows = data_json[this.properties.id].value.length;
			for(var i=0;i<rows;i++){
				this.appendRow();
			}
			
			var children = this.children;
	
			for (var i = 0; i < rows; i++) {
				var n = children.length;
				var obj = {};
				for (var j = 0; j < n; j++) {
					var child = children[j];
					if(child.setRepeatedRuntimeData){
						child.setRepeatedRuntimeData(this.properties.id, i);
					}
				}
			}
				
			if(this.properties.type == "pagination" ) {
				this._renderPagination(0);
			}
		}
	},
	
	doControlColumnEditable: function(control){
		var controlList = control.children;
		for(var i=0;i<controlList.length;i++){
			var controlChildren = controlList[i];
			
			if(typeof(aclManager) != 'undefined'){//quick fix for tp.. http://bugs/show_bug.cgi?id=6935 dun commit this file in.
				var isEditable = aclManager.isEditable(controlChildren);
				if(!isEditable){
					var el = controlChildren.getElement();
					elementList = $(el).find('*').andSelf();
					hrefList = elementList.filter("a");
					imgList = elementList.filter("img");
					elementList.attr("disabled", true);
					hrefList.remove();
					imgList.remove();
				}
			}
			if(controlChildren.children){
				this.doControlColumnEditable(controlChildren);
			}
		}
	},

	getElementId: ArrayElement.getElementId,

	FQN: FQN
});

Form.registerClass(FQN, Form.Controls.RepeatableSection);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');
}(jQuery));