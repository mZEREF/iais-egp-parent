function showWaiting() {
    $.blockUI({message: '<div style="padding:3px;">We are processing your request now; please do not click the Back or Refresh button in the browser.</div>',
        css: {width: '25%', border: '1px solid #aaa'},
        overlayCSS: {opacity: 0.2}});
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

function clearErrorMsg(){
    $("#iaisErrorFlag").val("");
    $("span[name='iaisErrorMsg']").each(function(){
        $(this).html("");
    });
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
    }
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

function toggleOnSelect(sel, val, elem) {
    if (isEmpty(sel)) {
        return;
    }
    if ($('#' + sel).val() == val) {
        $('#' + elem).show();
    } else {
        $('#' + elem).hide();
        $('#' + elem).clearFields();
    }
}

function isEmpty(str) {
    return typeof str === 'undefined' || str == null || str == '' || str == 'undefined';
}

$.fn.clearFields = function() {
    return this.each(function() {
        var type = this.type, tag = this.tagName.toLowerCase();
        if (tag == 'form' || tag == 'div' || tag == 'span' || tag == 'ul' || tag == 'table' || tag == 'tr' || tag == 'td') {
            return $(':input[class!="not-clear"]', this).clearFields();
        }

        if (!$(this).hasClass('not-clear')) {
            if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea') {
                this.value = '';
                if (tag == 'textarea' && $(this).hasClass('riched')) {
                    $('#' + $(this).attr('id') + 'Rich').html('');
                }
            } else if (type == 'checkbox') {
                this.checked = false;
            } else if (type == 'radio') {
                this.checked = false;
            } else if (tag == 'select') {
                this.selectedIndex = 0;
            }
        }
    });
};
function controlEdit($ele, property, canEdit){
    if(canEdit){
        $ele.attr(property, !canEdit);
        $ele.css('border-color', '');
        $ele.css('color', '');
    }else{
        $ele.prop(property, !canEdit);
        $ele.css('border-color', '#ededed');
        $ele.css('color', '#999');
    }
};