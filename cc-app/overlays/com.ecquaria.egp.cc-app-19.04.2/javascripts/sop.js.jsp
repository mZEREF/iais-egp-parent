<%@page contentType="text/javascript" %>

if (!Array.indexOf) { 
     Array.prototype.indexOf = function(obj) { 
          for (var i = 0; i < this.length; i++) { 
               if (this[i] == obj) return i; 
          } 
          return -1; 
     } 
} 

if(!window.SOP)SOP = {};

SOP.performAction = function(form, name, value) {
	$('[name=' + name + "]", form).attr('value', value);
	$(form).submit();
};

//When the checkBoxHeader is checked / unchecked, the rest of the
// checkboxes will follow suit.
SOP.setAllCheckBoxes = function(allCheckBoxId, individualCheckBoxName) {
	var checked = document.getElementById(allCheckBoxId).checked;
	var elements = document.getElementsByName(individualCheckBoxName);

	for ( var i = 0; i < elements.length; i++)
		elements[i].checked = checked;
};

// To update the checkBoxHeader when all the individual checkboxes are
// checked.
SOP.updateTheAllCheckBox = function(allCheckBoxId, individualCheckBoxName) {
	var elements = document.getElementsByName(individualCheckBoxName);
	var checked = elements.length == 0 ? false : true;

	for ( var i = 0; i < elements.length; i++) {
		if (!elements[i].checked) {
			checked = false;
			break;
		}
	}

	document.getElementById(allCheckBoxId).checked = checked;
};

SOP.clearForm = function(form) {
	var clear = confirm('');

	if (clear){
		$(form).clearForm();
		//for remove the row of 'The following form has errors: '
		$('th.error').parent().remove();
		//clear error messages
		$('.error').html('');
		//for extends
		SOP.clearFormExtend(form);
	}

	return clear;
};

SOP.clearFormExtend = function(form) {
}

SOP.resetForm = function(form) {
	var reset = confirm('');

	if (reset)
		form.reset();

	return reset;
};

SOP.javaDateFormatToJsDateFormat = function(format) { 
     var result = ''; 
     var code = 'MMMM|MMM|MM|M|yyyy|yy|DD|D|dd|d|EEEE|E'; 
     var translate = 'MM|M|mm|m|yy|y|oo|o|dd|d|DD|D'; 
     var codes = code.split('|'); 
     var translates = translate.split('|'); 
     var pattern = new RegExp(code + '|.', 'g'); 
     var matches = format.match(pattern); 
     for (var i = 0, n = matches.length; i < n; i++) { 
          var match = matches[i]; 
          var idx = codes.indexOf(match); 
          if (idx < 0) { 
               result += match; 
          } else { 
               result += translates[idx]; 
          } 
     } 
     return result; 
};

function SopCrud(entityName, ajaxDeleteTarget, singleEntityUid, formName) {
	var me = this;

	this.entityName = entityName;
	this.ajaxDeleteTarget = ajaxDeleteTarget;
	this.singleEntityUid = singleEntityUid;
	this.isSingleEntity = (typeof (singleEntityUid) != 'undefined') && (singleEntityUid != '');
	// Actions
	this.clearAction = 'clear';
	this.deleteAction = 'delete';
	this.deleteAllAction = 'deleteAll';
	this.exportAction = 'export';
	this.exportAllAction = 'exportAll';
	// 
	this.entityUidsVar = 'entityUids';
	this.allEntityUidsVar = 'allEntityUids';
	this.allEntitiesCbVar = 'allEntitiesCb';
	// 
	this.ajaxReturnValue = false;
	
	this.formName = formName;

	this.ajaxCallbackDeletePrompt = function() {
		if (me.ajaxReturnValue) {
			if (me.isSingleEntity)
				cfxSubmit(me.deleteAction, me.singleEntityUid, '', me.formName);
			else
				cfxSubmit(me.deleteAction, '', '', me.formName);
		}
	};

	this.ajaxCallbackDeleteAllPrompt = function() {
		if (me.ajaxReturnValue)
			cfxSubmit(me.deleteAllAction, '', '', me.formName);
	};

	this.checkDelete = function(action) {
		if (me.isSingleEntity) {
			var uidStr = me.entityUidsVar + '='
					+ encodeURIComponent(me.singleEntityUid);

			me.executeAjaxDeletePrompt(uidStr, action);

		} else if (me.checkSelection()) {
			var elements = document.getElementsByName(me.entityUidsVar);

			if (elements.length != 0) {
				var uidStr = '';

				for ( var i = 0; i < elements.length; i++) {
					if (elements[i].checked) {
						if (uidStr != '')
							uidStr += '&';

						uidStr += me.entityUidsVar + '='
								+ encodeURIComponent(elements[i].value);
					}
				}

				me.executeAjaxDeletePrompt(uidStr, action);
			}
		}
	};

	this.checkDeleteAll = function(action) {
		var elements = document.getElementsByName(me.allEntityUidsVar);
		
		if (elements.length == 0)
			alert('' + me.entityName + '');

		else {
			var uidStr = '';

			for ( var i = 0; i < elements.length; i++) {
				if (uidStr != '')
					uidStr += '&';

				uidStr += me.entityUidsVar + '='
						+ encodeURIComponent(elements[i].value);
			}

			me.executeAjaxDeletePrompt(uidStr, action);
		}
	};

	this.checkExportAll = function() {
		var elements = document.getElementsByName(me.allEntityUidsVar);

		if (elements.length == 0) {
			alert('' + me.entityName + '');
			return false;
		} else
			return true;
	};

	this.checkSelection = function() {
		var elements = document.getElementsByName(me.entityUidsVar);
		var selectionCount = 0;

		for ( var i = 0; i < elements.length; i++) {
			if (elements[i].checked)
				++selectionCount;
		}

		if (selectionCount == 0) {
			alert('' + me.entityName + '');
			return false;
		}

		return true;
	};

	this.executeAjaxDeletePrompt = function(queryValue, action) {
		var sopAjax = new SOPAjax('Echo');
		sopAjax.parameter = '-';
		sopAjax.jsp = me.ajaxDeleteTarget;
		sopAjax.extra_query_string = queryValue;

		sopAjax.ajax_evalScript = true;

		if (action == me.deleteAction)
			sopAjax.callback( function() {
				me.ajaxCallbackDeletePrompt();
			});

		else if (action == me.deleteAllAction)
			sopAjax.callback(me.ajaxCallbackDeleteAllPrompt);
	};

	this.performAction = function(action, actionVal, additionalVal) {
		if (action == me.clearAction) {
			if(confirm(''))
				cfxClearFilters(false, me.formName);

		} else if (action == me.deleteAction) {
			me.checkDelete(action);

		} else if (action == me.deleteAllAction) {
			me.checkDeleteAll(action);

		} else if (action == me.exportAction) {
			if (me.checkSelection() && confirm(''))
				return cfxSubmit(action, '', '', me.formName);

		} else if (action == me.exportAllAction) {
			if (me.checkExportAll() && confirm(''))
				return cfxSubmit(action, '', '', me.formName);

		} else {
			return cfxSubmit(action, actionVal, additionalVal, me.formName);
		}

		return false;
	};

	// When the checkBoxHeader is checked / unchecked, the rest of the
	// checkboxes will follow suit.
	this.setAllCheckBox = function(checkValue) {
		var checked = document.getElementById(me.allEntitiesCbVar).checked;
		var elements = document.getElementsByName(me.entityUidsVar);

		for ( var i = 0; i < elements.length; i++)
			elements[i].checked = checked;
	};

	// To update the checkBoxHeader when all the individual checkboxes are
	// checked.
	this.updateAllCheckBox = function() {
		var elements = document.getElementsByName(me.entityUidsVar);
		var checked = elements.length == 0 ? false : true;

		for ( var i = 0; i < elements.length; i++) {
			if (!elements[i].checked) {
				checked = false;
				break;
			}
		}

		document.getElementById(me.allEntitiesCbVar).checked = checked;
	};
}

function SopAjaxPasswordMeter(userId, userIdField, passwordField, meterId) {
	var me = this;

	this.userId = userId;
	this.userIdField = userIdField;
	this.passwordField = passwordField;
	this.meterId = meterId;
	this.noUserId = userIdField != null;
	
	this.update = function() {
		var password = $("#" + me.passwordField).val();
		
		if(me.noUserId)
			me.userId = $("#" + me.userIdField).val();		
			
	    var sopAjax = new SOPAjax('Echo');
	    sopAjax.parameter = '-';
	    sopAjax.jsp = '/process/SYSTEM/UserPassword_AjaxStrengthMeter/Start';
		sopAjax.extra_query_string = 'userId='+ encodeURIComponent(me.userId) + '&password='+ encodeURIComponent(password);
		sopAjax.ajax_asynchronous = false;
		sopAjax.enable_indicator = false;
		sopAjax.update(me.meterId);
	};
}