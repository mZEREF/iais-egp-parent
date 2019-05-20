(function($jq) {

var FQN = 'sopform.controls.base.RepeatableRow';

Form.Controls.RepeatableRow = Form.Controls.Container.extend({
	init: function(){
		conditionManager.doControlInit(this);
		this.rowCounter = this.properties.rows;
		
		var _pMap = this.properties.aggregation_map;
		var me = this;
		if(_pMap){
			$.each(_pMap, function(index, obj){
				me.updateAggregateMap(index, obj);
			});
		}
	},

	/**
	 * @param controlId Refers to the control id of the one referenced in the repeatable row.
	 * @param aggType A value from -1 to n representing e.g sum, average, ..
	 */
	updateAggregateMap: function(controlId, aggType){
		var _map = this.properties.aggMap;
		if(!_map){
			this.properties.aggMap = new Map();
			_map = this.properties.aggMap; 
		}
		_map.put(controlId, aggType);
	},
	
	getAggregateType: function(controlId){
		var _map = this.properties.aggMap;
		if(_map && _map.containsKey(controlId)){
			return _map.get(controlId);
		}
		return "-1";
	},
	
	getDataForTemplate: function(){
		var __key = "key";
		var __val = "val";
		
		var list = new Array();
		var entry = null;
		
		var __pMap = this.properties.aggMap;
		if(__pMap){
			var keyList = __pMap.getKeys();
			var currentKey = null;
			for(var i=0;i<keyList.length;i++){
				currentKey = keyList[i];
				
				entry = new Object();
				entry[__key] = currentKey;
				entry[__val] = __pMap.get(currentKey);
				
				list[list.length] = entry;
			}
		}
		
		return {
			persistentMap: list
		};
	},
	
	getDefaults: function() {
		return {
			title: 'Repeatable Row',
			controlButton: 'Add Row',
			border: false,
			rows: 1,
            min_rows: 0,
            prompt:'None',
			_id: this.id,
			tooltip: 'Remove Row'
		}
	},

	append: function(control, position) {
		var render = true;
		if (typeof arguments[0] == 'boolean') {
			render = arguments[0];
			control = arguments[1];
			position = arguments[2];
		}

		if (!render) {
			control.parent = this;
			control.position = position;
			this.children.push(control);
			return;
		}

		if (control.parent) {
			control.parent.removeChild(control);
			control.parent.refresh();
		}

		var $grid = this.getGrid();
		if (typeof position != 'number' || position < 0) {
			position = $grid.columns('cols');
		}
		$grid.columns('insertColumn', position, control.properties.caption, control);

		control.parent = this;
		this._renderChildControl(control, position);
		this.rearrange();

		var header = $grid.columns('header', position);
		var self = this;
		Form.setControl(header, control);
		if (this.mode == 'design') {
			$jq(header).click(function(e) {
				self._headerClicked(this, e);
			});
		}
	},

	choose: function(col, showProperties) {
		var $grid = this.getGrid();
		if (typeof col != 'number' || col < 0) {
			col = $grid.columns('cols') - 1;
		}

		$grid.columns('highlight', col);
		if (showProperties !== false) {
			var header = $grid.columns('header', col);
			Form.Design.showProperties(header);
		}
	},

	refresh: function(placeholder) {
		if (!placeholder) placeholder = this.getElement();

		this.render(placeholder);
		this.bind();
		this.prepareControl();

		var $grid = this.getGrid();
		var self = this;
		$jq.each(this.children, function() {
			$grid.columns('insertColumn', this.position, this.properties.caption, this);

			self._renderChildControl(this, this.position);

			var header = $grid.columns('header', this.position);
			Form.setControl(header, this);
			if (this.mode == 'design') {
				$jq(header).click(function(e) {
					self._headerClicked(this, e);
				});
			}
		});

		if (this.mode == 'design' && !!this.isEdit) {
			this.setInitValues();
			var elem = this.getElement();
			$(elem).find('.control-mask').addClass('hide');
		}

	},
	
	/**
	 * Prepare control.
	 * @override
	 */
	prepareControl: function() {
		var renderer = this.getRenderer();
		if (!renderer) return;

		// insert control as first parameter
		var args = [this];
		args.push.apply(args, arguments);

		if (renderer.prepareControl) {
			renderer.prepareControl.apply(renderer, args);
		}
		
		var theme = Form.Theme.getTheme(Form.properties.themeName);
		var element = this.getElement();
		if (theme.RepeatableRow) {
			var rr = theme.RepeatableRow;
			if (rr.tr_odd_row && rr.tr_odd_row.bgcolor) {
				$("table tr:odd", element).css("background-color", rr.tr_odd_row.bgcolor);
			}
			if (rr.tr_even_row && rr.tr_even_row.bgcolor) {
				$("table tr:even", element).css("background-color", rr.tr_even_row.bgcolor);
			}
		}
	},

	_headerClicked: function(target, e) {
		e.stopPropagation();
		var control = Form.getControl(target);
		if (control.parent.isEdit) return;

		Form.Design.unchoose();
		var columnNumber = this.getGrid().columns('columnNumber', target);
		this.choose(columnNumber);
	},
	
	_renderChildControl: function(control, position) {
		var $grid = this.getGrid();
		$grid.columns('resetRow');
		var i = 0;
		while ($grid.columns('nextRow')) {
			var placeholder = $jq('<div>').get(0);
			$grid.columns('setContent', position, placeholder);
			control.refresh(placeholder, i);
			i++;
		}
	},

	rearrange: function() {
		var $grid = this.getGrid();
		this.children = [];
		var me = this;

		var $td = $grid.find('> tbody > tr:eq(1) > td:gt(0)');

		var el = me.getElement();
		$td.find('.control:not(.ui-draggable-dragging)').filter(function() {
			return $jq(this).parents('.control').get(0) == el;
		}).each(function() {
			var idx = $td.index($jq(this).parent().get(0));
			var control = Form.getControl(this);
			control.position = idx;
			control.parent = me;
			me.children.push(control);
		});
	},

	_renderRow: function() {
		var $grid = this.getGrid();
		var currentRow = this.getGrid().columns('currentIndex').actual;
		$jq.each(this.children, function(i) {
			
			var placeholder = $jq('<div>').get(0);
			$grid.columns('setContent', this.position, placeholder);
			this.refresh(placeholder, currentRow);
		});
		//conditionManager.doRepeatableRowCondition(this);
		/*this.doControlColumnEditable(this);*/
	},
 
	resetRowsToLength: function(length){
		var numOfRows = this.getGrid().columns('rows');
		var hiddenRow = this.getGrid().columns('getHiddenRow');
		if(hiddenRow[0]) numOfRows = numOfRows -1;
		// remove rows completely.
		for(var i=0;i<numOfRows;i++){
			this.getGrid().columns("removeRow", 0, false);
		}
		
		// add rows.
		for(var i=0;i<length;i++){this.appendRow(false);}
	},
	
	getRowNumber:function(){
		return this.getGrid().columns('rows');
	},
	
	removeRow:function(rowIndex){
		if (rowIndex < 0) return;
		
		var elem = this.getElement();
		if (!elem) {
			var runtimeData = this.getRuntimeData();
			var value = runtimeData.value;
			if (value && rowIndex < value.length) {
				value.remove(rowIndex);
				return rowIndex;
			}
			return;
		}

		var count = this.getRowNumber();
		if (rowIndex < count) {
			this.getGrid().columns('removeRow', rowIndex, false);
			return rowIndex;
		}
	},
	
	appendRow: function(enableVisualEffect) {
		var elem = this.getElement();
		if (!elem) {
			var runtimeData = this.getRuntimeData();
			var value = runtimeData.value;
			if (!value) value = runtimeData.value = [];
			var newrow = {};
			var idx = value.length;
			for (var i = 0, n = this.children.length; i < n; i++) {
				var child = this.children[i];
				newrow[child.properties.id] = child.getRuntimeData(idx);
			}
			value.push(newrow);
			return idx;
		}

		var __enableVisualEffect = enableVisualEffect!=null?enableVisualEffect:true;
		var $rows = this.getGrid().columns('$rows');
		var emptyRow = $rows.find('.emptyRow');
		var hiddenRow = $rows.find('.hiddenRow');
		if(emptyRow[0]) emptyRow.remove();
		if(hiddenRow[0]) {
			hiddenRow.remove();
			this['rowValues'] = this.getRuntimeData().value;
		}
		var $tr = this.getGrid().columns('appendRow', __enableVisualEffect);
		var index = $tr.data('index');
		this.getGrid().columns('last');
		this._renderRow();
		
		// this is added to apply the themes.
		var theme = Form.Theme.getTheme(Form.properties.themeName);
		if (theme.RepeatableRow) {
			var rr = theme.RepeatableRow;
			if (rr.tr_odd_row && rr.tr_odd_row.bgcolor) {
				$("table tr:odd", elem).css("background-color", rr.tr_odd_row.bgcolor);
			}
			if (rr.tr_even_row && rr.tr_even_row.bgcolor) {
				$("table tr:even", elem).css("background-color", rr.tr_even_row.bgcolor);
			}
		}
		//update the last row's value for repeatablerow's properties:rowValues
		var rowIndex = $tr.data('index');
		var el = this.getElement.apply(this, arguments);
		var data_json = window.data_json;
		var children = this.children;
		var obj = {};
		if (el) {
			var n = children.length;
			for (var j = 0; j < n; j++) {
				var child = children[j];
				if(child.getElement() && child.getElement().length>0){
					obj[child.properties.id] = child.getRuntimeData(rowIndex.actual);
				}
				else if (data_json) {
					obj[child.properties.id] = sopformsApi.getJSONDataValue(child.properties.id, rowIndex.real);
				}
			}
		}else if (data_json) {
			var p = data_json[this.properties.id];
			if (p === undefined) {
				var rows = this.properties.rows;
				var children = this.children;
				var n = children.length;
				for (var j = 0; j < n; j++) {
					var child = children[j];
					var childId = child.properties.id;
					obj[childId] = child.getRuntimeData(index.real);
				}
			} else if (p) {
				var value = p.value;
				this['rowValues'] = value;
			}
		}
		if(!this['rowValues']){
			this['rowValues'] = this.getRuntimeData().value;
		}
		this['rowValues'].push(obj);
		conditionManager.doRepeatableRowCondition(this);
		if (window.aclManager) aclManager.processACLDisabledChildren([this],Form.currentPage);
		return index.real;
	},

	FQN: FQN,

	getRuntimeData: function() {
		var el = this.getElement.apply(this, arguments);
		var data_json = window.data_json;
		var value = [];
		if (el && !Form.isView) {
			var rows = this.getGrid().columns('rows');
			var children = this.children;
	
			for (var i = 0; i < rows; i++) {
				var n = children.length;
				var obj = {};
				for (var j = 0; j < n; j++) {
					var child = children[j];
					var childId = child.properties.id;
					if(child.getElement() && child.getElement().length>0){
						obj[childId] = child.getRuntimeData(this.getCorrectIndex(child, i));
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
	
	getCorrectIndex: function(child, _index){
		var pattern = /[0-9]+/g;
		
		var elements = child.getElement();
		var element = elements[_index];
		var correctIndex = $(element).attr("id");
		var matched = correctIndex.match(pattern);
		
		return matched==null?-1:matched[matched.length-1];
	},

	_toggleEditMode: function() {
		var isEdit = this.isEdit = !this.isEdit;
		if (!isEdit) {
			var initValues = this.getRuntimeData();
			Form.initValues[this.properties.id] = initValues;
			if (initValues.value) {
				this.properties.rows = Math.max(this.properties.rows, initValues.value.length);
			}
		}

		var showProperties = !$(this.getElement()).is('.' + Form.Design.choosenClass);
		this.refresh();
		var el = this.getElement();
		Form.Design.choose(el, showProperties);

		var $el = $(el);
		if (isEdit) $el.find('.control-mask').addClass('hide');
		else $el.find('.control-mask').removeClass('hide');
	},
	
	setInitValues: function(){
		var initValues = Form.initValues;
		var p = initValues[this.properties.id];
		if(p != null){
			var rows = initValues[this.properties.id].value.length;
			var rowCount = this.getGrid().columns('rows');
			if(Form.currentMode == "design") {
				for(var i=rowCount;i<rowCount;i++){
					this.appendRow();
				}
			}else if(rows > rowCount){
				for(var i=rowCount;i<rows;i++){
					this.appendRow();
				}
			}
			
			var children = this.children;
	
			for (var i = 0; i < rows; i++) {
				var n = children.length;
				var obj = {};
				for (var j = 0; j < n; j++) {
					var child = children[j];
					if(child.setRepeatedInitValues){
						child.setRepeatedInitValues(this.properties.id, i);
					}
				}
			}
		}
		this['rowValues'] = this.getRuntimeData().value;
	},

	setRuntimeData: function(){
		var p = data_json[this.properties.id];
		if(p != null){
			var rows = data_json[this.properties.id].value.length;
			var rowCount = this.getGrid().columns('rows');
			if(rows > rowCount){
				for(var i=rowCount;i<rows;i++){
					this.appendRow();
				}
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
	/*
	doControlColumnVisable: function(result,controlID,conditionTypeSelected){
		var control = Form.getControl(controlID);
		if(control.isInRepeatable()){
			if(result){
				if(conditionTypeSelected == 1){// visible if 
					var eles = $(control.getElement());
					var header = Form.getHeader(control);
					$(header).show();
					$.each(eles, function(ele) {
						$(this).parent().show();
					});
					var aggregation = $(".agg_field_" + controlID).parent().parent();
					aggregation.show();
				}
				if(conditionTypeSelected == 2){// hide if 
					var eles = $(control.getElement());
					var header = Form.getHeader(control);
					$(header).hide();
					$.each(eles, function(ele) {
						$(this).parent().hide();
					});
					var aggregation = $(".agg_field_" + controlID).parent().parent();
					aggregation.hide();
				}
			}else{
				if(conditionTypeSelected == 1){// visible if
					var eles = $(control.getElement());
					var header = Form.getHeader(control);
					$(header).hide();
					$.each(eles, function(ele) {
						$(this).parent().hide();
					});
					var aggregation = $(".agg_field_" + controlID).parent().parent();
					aggregation.hide();
				}
				if(conditionTypeSelected == 2){// hide if
					var eles = $(control.getElement());
					var header = Form.getHeader(control);
					$(header).show();
					$.each(eles, function(ele) {
						$(this).parent().show();
					});
					var aggregation = $(".agg_field_" + controlID).parent().parent();
					aggregation.show();
				}
			}
		}
		var $grid = this.getGrid();
	},
	*/
	
	checkHeaderVisable: function(enableEffects){
		var children = this.children;
		for(var i=0; i<children.length; i++){
			var control = children[i];
			var cotrolID = control.properties.id;
			var $eles = $(control.getElement());
			var visableEle = $eles.filter(':not(:hidden)');
			var result = false;
			if(visableEle.length>0){
				result = true;
			}
			if(this['rowNum'] != undefined && this['rowNum'] != 0)
				this.doControlColumnVisable(result,cotrolID,enableEffects);
			else {
				var header = Form.getHeader(control);
				$(header).show();
			}
		}
		
	},
	
	doControlColumnVisable: function(result,controlID,enableEffects){
		var control = sopformsApi.getControlByControlId(controlID)
		var eles = $(control.getElement());
		var header = Form.getHeader(control);
		if(result){
			if(header){
				$(header).show();
			}
			
			$.each(eles, function(ele) {
				enableEffects?$(this).parent().fadeIn('slow'):$(this).parent().show();
			});

			var aggregation = $(".agg_field_" + controlID).parent().parent();
			enableEffects?aggregation.fadeIn('slow'):aggregation.show();
		}else{
			if(header){
				$(header).hide();
			}
			
			$.each(eles, function(ele) {
				$(this).parent().hide();
			});

			var aggregation = $(".agg_field_" + controlID).parent().parent();
			aggregation.hide();
		}
		//var $grid = this.getGrid();
		//$grid.columns('updateWidth');
	},
	
	/**
	 * @return a list of control id belonging to the headers.
	 */
	getAllAvailableControlId: function(){
		var cols = this.getGrid().columns('cols');
		var list = [];
		var control = null;
		for(var i=0;i<cols;i++){
		    control = Form.getControl(this.getGrid().columns('header', i));
		    list.push(control.properties.id);
		}
		return list;
	}
});

Form.registerClass(FQN, Form.Controls.RepeatableRow);

Form.Loader.load(FQN, 'renderer');

Form.Loader.load(FQN, 'serializer');
}(jQuery));