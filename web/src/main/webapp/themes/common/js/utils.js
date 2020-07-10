function showWaiting() {
    $.blockUI({message: '<div style="padding:3px;">We are processing your request now, please do not click the Back or Refresh buttons in the browser.</div>',
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

function changeMemoryPage(paginationDiv, checkType, pageNo) {
    var ids = "NA";
    if (checkType == 1 || checkType == 2) {
        var elemName = "input:checkbox[name='" + paginationDiv + "Check']";
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