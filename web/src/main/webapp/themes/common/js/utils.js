function showWaiting() {
    var msg = '<div style="padding:3px;">' +
        'We are processing your request now; please do not click the Back or Refresh button in the browser.' +
        '</div>';
    var cssOpts = {border: '1px solid #aaa'};
    var scrnWidth = window.screen.width;
    if (isEmpty(scrnWidth) || scrnWidth > 480) {
        cssOpts.width = '25%';
        cssOpts.left = '37%';
    } else {
        cssOpts.width = '50%';
        cssOpts.left = '25%';
    }
    $.blockUI({message: msg, css: cssOpts, overlayCSS: {opacity: 0.2}});
}

function dismissWaiting() {
    $.unblockUI();
}

function doValidation(){
    clearErrorMsg();
    $.ajax({
        async: false,
        type: 'POST',
        url: BASE_CONTEXT_PATH + "/validation.do",
        data:$('form').serialize(),
        success: function(res){
            doValidationParse(res);
        },
        error: function(jqXHR, textStatus, errorThrown) {
            $("#iaisErrorFlag").val('Error:Exception');
        }
    });
}

function showErrorMsg(errorKey,errorMsg) {
    $('form').find("[id*="+errorKey.replace(/\./g,'\\.')+"]").html(formatHTMLEnCode(errorMsg));
}

function formatHTMLEnCode(s1){
    s1 = s1.replace(/\>/g,"&gt;");
    s1 = s1.replace(/\</g,"&lt;");
    s1 = s1.replace(/\"/g,"&quot;");
    s1 = s1.replace(/\'/g,"&acute;");
    return s1;
}

function clearErrorMsg(targetTag) {
    if (isEmpty(targetTag) || $(targetTag).length == 0) {
        $("#iaisErrorFlag").val("");
        $("span[name='iaisErrorMsg']").each(function () {
            $(this).html("");
        });
    } else {
        $("#iaisErrorFlag").val("");
        $(targetTag).find("span[name='iaisErrorMsg']").each(function () {
            $(this).html("");
        });
    }
}

function doValidationParse(data){
    if(data != null && data != "[]" && data != ''){
        $("#iaisErrorFlag").val("BLOCK");
        var results = JSON.parse(data);

        for(var i= 0 ; i< results.length ; i ++){
            for(var key in results[i]){
                var error_key="error_" + key.replace(/\./g,'\\.');
                if (document.getElementById(error_key)) {
                    $("#"+error_key).show();
                    if (error_key == 'error_topErrorDiv'
                        || error_key.indexOf('noEscapeXml') > 0) {
                        document.getElementById(error_key).innerHTML = results[i][key];
                    } else {
                        document.getElementById(error_key).innerHTML = formatHTMLEnCode(results[i][key]);
                    }
                }
            }
        }
        gotoFirstMsg();
    }
}

function gotoFirstMsg() {
    if ($('span.error-msg').not(':empty').length <= 0) {
        return;
    }

    $target = $('span.error-msg').not(':empty').first();
    if ($target.is(':hidden')) {
        if ($target.closest('.panel-collapse').length > 0) {
            $target.closest('.panel-collapse').collapse('show');
        } else {
            return;
        }
    }
    var correlationId = $target.attr('id').replace('error_', '');
    var errorTop = 0;
    if ($(':input[name="' + correlationId + '"]').length > 0) {
        errorTop = $(':input[name="' + correlationId + '"]').offset().top;
    } else if ($('#' + correlationId).length > 0) {
        errorTop = $('#' + correlationId).offset().top;
    } else {
        errorTop = $target.offset().top;
    }
    if (errorTop <= 0) {
        errorTop = $target.offset().top;
    }
    $('html,body').animate({scrollTop: errorTop - 100});
}

function getErrorMsg(){
    var msgStatus = false;
    if($("#iaisErrorFlag").val() != null && $("#iaisErrorFlag").val() != ""){
        msgStatus =  true;
    }

    return msgStatus;
}

function showPopupWindow(url) {
    showPopupWindow(url,'N','popupWindow');
}

function showPopupWindow(url,wName) {
    showPopupWindow(url,'N',wName);
}

function showPopupWindow(url,full,wName) {
    showWaiting();

    var w, h;
    w = $(window).width();
    h = $(window).height();
    var popW = 980, popH = h + 40;
    if (full == 'Y') {
        popW = w;
    }
    var leftPos = (w - popW) / 2, topPos = (h - popH) / 2;
    var params = "scrollbars=yes,location=no,resizable=yes,width=" + popW + ",height=" + popH + ",left=" + leftPos + ",top="+topPos;
    if (wName == "" || wName == undefined) {
        wName = "popupWindow";
    }
    var emsTabId = $('#emsStoredTabId').val();
    if (emsTabId != null && emsTabId != "") {
        if (url.indexOf("?") >= 0) {
            url += "&emsStoredTabId=" + emsTabId;
        } else {
            url += "?emsStoredTabId=" + emsTabId;
        }
    }
    var popupWin = window.open(url, wName, params);
    if (window.focus) {
        popupWin.focus();
    }

    dismissWaiting();
    return false;
}

function showPopupWindowSpec(url,width,height) {
    showWaiting();

    var w, h;
    var wName;
    w = $(window).width();
    h = $(window).height();
    var leftPos = (w - width) / 2, topPos = (h - height) / 2;
    var params = "scrollbars=yes,location=no,resizable=yes,width=" + width + ",height=" + height + ",left=" + leftPos + ",top="+topPos;
    if (wName == "" || wName == undefined) {
        wName = "popupWindow";
    }
    var popupWin = window.open(url, wName, params);
    if (window.focus) {
        popupWin.focus();
    }

    dismissWaiting();
    return false;
}

function initMemoryPage(paginationDiv, checkType, pageNo) {
    var data = {
        pageDiv : paginationDiv,
        pageNum : pageNo
    };
    var getTimestamp=new Date().getTime();
    $.ajax({
        data:data,
        type:"GET",
        dataType: 'json',
        url:BASE_CONTEXT_PATH + "/commonAjax/changeMemoryPage.do?timestamp="+getTimestamp,
        error:function(res){
            $("#iaisErrorFlag").val('Error:Exception');
        },
        success:function(res){
            confirmChangeMemoryPage(res);
        }
    });
}

function getQueryVariable(variable)
{
    let query = window.location.search.substring(1);
    let vars = query.split("&");
    for (let i = 0; i< vars.length; i++) {
        let pair = vars[i].split("=");
        if(pair[0] == variable) return pair[1];
    }

    return null;
}

function printpage(id) {
    let newStr = document.getElementById(id).innerHTML;
    let oldStr = document.body.innerHTML;
    document.body.innerHTML = newStr;
    window.print();
    document.body.innerHTML = oldStr;
}

function changeMemoryPage(paginationDiv, checkType, pageNo) {
    var ids = "NA";
    if (checkType == 1) {
        var elemName = "input:checkbox[name='" + paginationDiv + "Check']";
        $(elemName).each(function() {
            if (this.checked) {
                ids += "," + this.value;
            }
        });
    } else if (checkType == 2) {
        var elemName = "input:radio[name='" + paginationDiv + "Check']";
        $(elemName).each(function() {
            if (this.checked) {
                ids += "," + this.value;
            }
        });
    }
    var data = {
        checkId : ids,
        pageDiv : paginationDiv,
        pageNum : pageNo
    };
    $.ajax({
        data:data,
        type:"GET",
        dataType: 'json',
        url:BASE_CONTEXT_PATH + "/commonAjax/changeMemoryPage.do",
        error:function(res){
            $("#iaisErrorFlag").val('Error:Exception');
        },
        success:function(res){
            confirmChangeMemoryPage(res);
        }
    });
}

function confirmChangeMemoryPage(res) {
    var paginationDivId = "#" + res.pageDivId;
    var recDivId = "#" + res.recDivId;
    var checkAllId = res.pageDivId + "CheckAll";
    $(paginationDivId).html(res.pageHtml);
    $(recDivId).html(res.recHtml);
    var checkAllObj = document.getElementById(checkAllId);
    if (res.checkAllRemove != null && res.checkAllRemove == '1') {
        checkAllObj.checked = false;
    } else if (res.checkAllRemove != null && res.checkAllRemove == '0') {
        checkAllObj.checked = true;
    }
    $('div#'+ res.pageDivId +' select').niceSelect();
}

function checkAllMemoryheck(paginationDiv) {
    var checkAllId = paginationDiv + "CheckAll";
    var elemName = paginationDiv + "Check";
    var checkAllObj = document.getElementById(checkAllId);
    var elems = document.getElementsByName(elemName);
    var checked = checkAllObj.checked;
    if (elems != null) {
        for (var i = 0; i < elems.length; i++) {
            elems[i].checked = checked;
        }
    }
}

function memoryCheckBoxChange(paginationDiv, obj) {
    var checkAllId = "#" + paginationDiv + "CheckAll";
    if (!obj.checked) {
        $(checkAllId).removeAttr("checked");
    }
}

function memoryPageSizeChange(paginationDiv, newSize) {
    var data = {
        pageDiv : paginationDiv,
        newSize : newSize
    };
    $.ajax({
        data:data,
        type:"GET",
        dataType: 'json',
        url:BASE_CONTEXT_PATH + "/commonAjax/changeMemoryPageSize.do",
        error:function(res){
            $("#iaisErrorFlag").val('Error:Exception');
        },
        success:function(res){
            confirmChangeMemoryPage(res);
        }
    });
}

function validateUploadSizeMaxOrEmpty(maxSize,selectedFileId) {
    var fileId= '#'+selectedFileId;
    var fileV = $( fileId).val();
    var file = $(fileId).get(0).files[0];
    if(fileV == null || fileV == "" ||file==null|| file==undefined){
        return "E";
    }
    var fileSize = (Math.round(file.size * 100 / (1024 * 1024)) / 100).toString();
    fileSize = parseInt(fileSize);
    if(fileSize>= maxSize){
        return "N";
    }
    return "Y";
}

//use for download function in the page, example : /checklist-item/setup-checkbox
function callAjaxSetCheckBoxSelectedItem(checkboxName, destUrl) {
    let array =[];
    $('input[name=' + checkboxName  + ']').each(function(){
        if ($(this).prop('checked')){
            array.push($(this).val())
        }
    });

    $.ajax({
        'url': destUrl,
        'dataType': 'json',
        'data': {selectedCheckBoxItem:array},
        'type': 'POST',
        'traditional':true,
        'async': false,
        'success': function (data) {
        },
        'error': function () {
        }
    });
}

function ajaxCallSelectCheckbox(){
    let destUrl = '/hcsa-licence-web/checkbox-ajax/record-status'
    if (this.checked) {
        destUrl += '?action=checked'
      }else{
        destUrl += '?action=unchecked'
      }
    destUrl += '&itemId=' + this.value + '&forName=' + $(this).attr('data-redisplay-name') + '&checkboxName=' + this.name
    $.ajax({
            'url': destUrl,
            'type': 'GET',
            'traditional':true,
            'async': true,
            'success': function (data) {
            },
            'error': function () {
            }
    });
}

//This is All Just For Logging:
var debugFile = true;//true: add debug logs when cloning
var evenMoreListeners = true;//demonstrat re-attaching javascript Event Listeners (Inline Event Listeners don't need to be re-attached)
if (evenMoreListeners) {
    var allFleChoosers = $("input[type='file']");
    addEventListenersTo(allFleChoosers);
    function addEventListenersTo(fileChooser) {
        fileChooser.change(function (event) { console.log("file( #" + event.target.id + " ) : " + event.target.value.split("\\").pop()) });
        fileChooser.click(function (event) { console.log("open( #" + event.target.id + " )") });
    }
}

var clone = {};

// FileClicked()
function fileClicked(event) {
    var fileElement = event.target;
    if (fileElement.value != "") {
        if (debugFile) { console.log("Clone( #" + fileElement.id + " ) : " + fileElement.value.split("\\").pop()) }
        clone[fileElement.id] = $(fileElement).clone(); //'Saving Clone'
    }
    //What ever else you want to do when File Chooser Clicked
}

// FileChanged()
function fileChanged(event) {
    var fileElement = event.target;
    if (fileElement.value == "") {
        if (debugFile) { console.log("Restore( #" + fileElement.id + " ) : " + clone[fileElement.id].val().split("\\").pop()) }
        clone[fileElement.id].insertBefore(fileElement); //'Restoring Clone'
        $(fileElement).remove(); //'Removing Original'
        if (evenMoreListeners) { addEventListenersTo(clone[fileElement.id]) }//If Needed Re-attach additional Event Listeners
    }
    //What ever else you want to do when File Chooser Changed
}

function getJqueryNode(elem) {
    if (isEmpty(elem)) {
        return;
    }
    var $target = $(elem);
    if ($target.length == 0 && Object.prototype.toString.call(elem) === "[object String]") {
        if (elem.indexOf('#') != 0) {
            $target = $('#' + elem);
        }
        if ($target.length == 0 && elem.indexOf('.') != 0) {
            $target = $('.' + elem);
        }
    }
    if ($target.length == 0) {
        return null;
    }
    return $target;
}

function toggleOnSelect(sel, val, elem) {
    var $selector = getJqueryNode(sel);
    var $target = getJqueryNode(elem);
    if (isEmpty($selector) || isEmpty($target)) {
        return;
    }
    if ($selector.val() == val) {
        $target.show();
        $target.removeClass('hidden');
    } else {
        $target.hide();
        $target.addClass('hidden');
        clearFields($target);
    }
    $target.each(function(i, ele) {
        if ('select' == ele.tagName.toLowerCase()) {
            $(ele).niceSelect("update");
        }
    });
}

function toggleOnCheck(sel, elem, hide) {
    var $selector = getJqueryNode(sel);
    var $target = getJqueryNode(elem);
    if (isEmpty($selector) || isEmpty($target)) {
        return;
    }
    if ($selector.is(':checked')) {
        if (hide) {
            $target.hide();
            $target.addClass('hidden');
            clearFields($target);
        } else {
            $target.show();
            $target.removeClass('hidden');
        }
    } else {
        if (hide) {
            $target.show();
            $target.removeClass('hidden');
        } else {
            $target.hide();
            $target.addClass('hidden');
            clearFields($target);
        }
    }
    $target.each(function(i, ele) {
        if ('select' == ele.tagName.toLowerCase()) {
            $(ele).niceSelect("update");
        }
    });
}

function checkMantory(sel, targetLabel, val) {
    var $selector = getJqueryNode(sel);
    var $target = getJqueryNode(targetLabel);
    if (isEmpty($selector) || isEmpty($target)) {
        return;
    }
    $target.find('.mandatory').remove();
    if (isEmpty(val) && val != '' && $selector.is(':checked') || val == $selector.val()) {
        $target.append('<span class="mandatory">*</span>');
    }
}

function isEmpty(str) {
    return typeof str === 'undefined' || str == null || str == '' || str == 'undefined';
}

function clearFields(targetSelector) {
    var $selector = getJqueryNode(targetSelector);
    if (isEmpty($selector)) {
        return;
    }
    if (!$selector.is(":input")) {
        $selector.find("span[name='iaisErrorMsg']").each(function () {
            $(this).html("");
        });
        $selector = $selector.find(':input[class!="not-clear"]');
    }
    if ($selector.length <= 0) {
        return;
    }
    $selector.each(function() {
        var type = this.type, tag = this.tagName.toLowerCase();
        if (!$(this).hasClass('not-clear')) {
            if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea') {
                this.value = '';
            } else if (type == 'checkbox') {
                this.checked = false;
            } else if (type == 'radio') {
                this.checked = false;
            } else if (tag == 'select') {
                this.selectedIndex = 0;
                $(this).niceSelect("update");
            }
        }
    });
}

function fillValue(targetSelector, data, includeHidden){
    var $selector = getJqueryNode(targetSelector);
    if (isEmpty($selector)) {
        return;
    }
    console.info("data - " + data);
    if (isEmpty(data)) {
        clearFields($selector);
        return;
    }
    if ($selector.is(":input")) {
        var type = $selector[0].type, tag = $selector[0].tagName.toLowerCase();
        console.info("Tag - " + tag + " : " + type);
        if (type == 'radio') {
            $selector.filter('[value="' + data + '"]').prop('checked', true);
            $selector.filter(':not([value="' + data + '"])').prop('checked', false);
        } else if (type == 'checkbox') {
            if ($.isArray(data)) {
                $selector.prop('checked', false);
                for (var v in data) {
                    if (curVal == v) {
                        $(this).prop('checked', true);
                    }
                }
            } else {
                $selector.filter('[value="' + data + '"]').prop('checked', true);
                $selector.filter(':not([value="' + data + '"])').prop('checked', false);
            }
        } else if (tag == 'select') {
            var oldVal = $selector.val();
            $selector.val(data);
            if (isEmpty($selector.val())) {
                $selector[0].selectedIndex = 0;
            }
            if ($selector.val() != oldVal) {
                $selector.niceSelect("update");
            }
        } else {
            $selector.val(data);
        }
    } else if ($.isArray(data)) {
        if (includeHidden) {
            $selector = $(targetSelector).find(':input');
        } else {
            $selector = $(targetSelector).find(':input[type!="hidden"]');
        }
        if ($selector.length <= 0) {
            console.log("Can't find the related tag - " + targetSelector);
            return;
        }
        $selector.each(function(i, ele) {
            fillValue(ele, data[i]);
        });
    } else {
        $.each(data, function(i, val) {
            var $input = $selector.find('[name="'+ i +'"]:input');
            if ($input.length == 0) {
                $input = $selector.find('.' + i + ':input');
            }
            fillValue($input, val);
        });
    }
}

function disableContent(targetSelector) {
    var $selector = getJqueryNode(targetSelector);
    if (isEmpty($selector)) {
        return;
    }
    if (!$selector.is(":input")) {
        $selector = $selector.find(':input');
    }
    if ($selector.length <= 0) {
        return;
    }
    $selector.each(function(i, ele) {
        var type = ele.type, tag = ele.tagName.toLowerCase(), $input = $(ele);
        if (type == 'hidden') {
            return;
        }
        $input.prop('disabled', true);
        $input.css('border-color','#ededed');
        $input.css('color','#999');
        if (tag == 'select') {
            $input.niceSelect("update");
        }
    });
}

function unDisableContent(targetSelector) {
    var $selector = getJqueryNode(targetSelector);
    if (isEmpty($selector)) {
        return;
    }
    if (!$selector.is(":input")) {
        $selector = $selector.find(':input');
    }
    if ($selector.length <= 0) {
        return;
    }
    $selector.each(function(i, ele) {
        var type = ele.type, tag = ele.tagName.toLowerCase(), $input = $(ele);
        if (type == 'hidden') {
            return;
        }
        $input.prop('disabled', false);
        $input.css('border-color','');
        $input.css('color','');
        if (tag == 'select') {
            $input.niceSelect("update");
        }
    });
}

function refreshIndex(targetSelector) {
    var $target = getJqueryNode(targetSelector);
    if (isEmpty($target)) {
        return;
    }
    $target.each(function (k,v) {
        var $ele = $(v);
        var $selector;
        if ($ele.is(':input')) {
            $selector = $ele;
        } else {
            $selector = $ele.find(':input')
        }
        if ($selector.length == 0) {
            return;
        }
        $selector.each(function () {
            if ($(this).hasClass('not-refresh')) {
                return;
            }
            var type = this.type, tag = this.tagName.toLowerCase(), $input = $(this);
            var orgName = $input.attr('name');
            var orgId = $input.attr('id');
            if (isEmpty(orgName)) {
                orgName = orgId;
            }
            if (isEmpty(orgName)) {
                return;
            }
            var result = /([a-zA-Z_]*)/g.exec(orgName);
            var name = !isEmpty(result) && result.length > 0 ? result[0] : orgName;
            $input.prop('name', name + k);
            if (orgName == orgId) {
                $input.prop('id', name + k);
            }
            var $errorSpan = $ele.find('span[name="iaisErrorMsg"][id="error_'+ orgName +'"]');
            if ($errorSpan.length > 0) {
                $errorSpan.prop('id', 'error_' + name + k);
            }
            if (tag == 'select') {
                $input.niceSelect("update");
            }
        });
    });
}

function callCommonAjax(options, callback) {
    if (isEmpty(options)) {
        options = {};
    }
    var url = '';
    if (!isEmpty(options.url)) {
        url = options.url;
    }
    var type = 'POST';
    if (!isEmpty(options.type)) {
        type = options.type;
    }
    var async = true;
    if (!isEmpty(options.async)) {
        async = options.async;
    }
    var data = options.data;
    if (isEmpty(data)) {
        data = options;
    }
    console.log(url);
    $.ajax({
        url: url,
        dataType: 'json',
        data: data,
        async: async,
        type: type,
        success: function (data) {
            if (typeof callback === 'function') {
                callback(data);
            } else if (!isEmpty(callback)) {
                callFunc(callback, data);
            }
            dismissWaiting();
        },
        error: function (data) {
            console.log("err");
            console.log(data);
            dismissWaiting();
        }
    });
}

function callFunc(func) {
    try {
        this[func].apply(this, Array.prototype.slice.call(arguments, 1));
    } catch (e) {
        console.log(e);
    }
}

function getContextPath() {
    var pathName = document.location.pathname;
    var index = pathName.substr(1).indexOf("/");
    var result = pathName.substr(0,index+1);
    return result;
}
