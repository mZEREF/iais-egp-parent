<%@page import="com.ecquaria.cloud.moh.iais.helper.MessageUtil" %>
<input type="text" style="display: none" name="errorMapIs" id="errorMapIs" value="${errormapIs}">
<div class="modal fade" id="PRS_SERVICE_DOWN" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-body" >
                <div class="row">
                    <div class="col-md-12"><span style="font-size: 2rem;"><%=MessageUtil.getMessageDesc("GENERAL_ERR0048")%></span></div>
                </div>
            </div>
            <div class="row " style="margin-top: 5%;margin-bottom: 5%">
                <button type="button" style="margin-left: 50%" class="next btn btn-primary col-md-6" data-dismiss="modal" onclick="cancel()">OK</button>
            </div>
        </div>
    </div>
</div>
<input type="hidden" value="${PRS_SERVICE_DOWN}" id="PRS_SERVICE_DOWN_INPUT" >
<script>
    var prdLoading = function ($loadingContent, prgNo, action, callBackFuns) {
        console.log('loading prs info ...');
        var assignSelectVal = $loadingContent.find('select.assignSel').val();
        var appType = $('input[name="applicationType"]').val();
        var licPerson = $loadingContent.find('input.licPerson').val();
        var needControlName = licPerson != "1";
        console.log("isNeedControlName: " + needControlName + " assignSelectVal:" + assignSelectVal + " licPerson:" + licPerson + " appType:" + appType);
        var emptyData = {};
        if(prgNo == "" || prgNo == null || prgNo == undefined){
            clearPrsInfo($loadingContent, callBackFuns, emptyData, needControlName, action);
            if(needControlName){
                inputCancelReadonly($loadingContent.find('.field-name'));
            }
            return;
        }

        var jsonData = {
            'prgNo': prgNo
        };
        $.ajax({
            'url': '${pageContext.request.contextPath}/prg-input-info',
            'dataType': 'json',
            'data': jsonData,
            'type': 'GET',
            'success': function (data) {
                if(data.regno == null){
                    $('#PRS_SERVICE_DOWN').modal('show');
                    clearPrsInfo($loadingContent, callBackFuns, emptyData, needControlName, action);
                    inputCancelReadonly($loadingContent.find('.field-name'));
                    return;
                }
                if(data.name == null){
                    //prgNo is incorrect
                    clearPrsInfo($loadingContent, callBackFuns, emptyData, needControlName, action);
                    if(needControlName){
                        inputReadonly($loadingContent.find('.field-name'));
                    }
                    return;
                }
                if(!isEmpty(callBackFuns)){
                    if(typeof callBackFuns.fillData == 'function'){
                        callBackFuns.fillData($loadingContent, data, needControlName);
                    }
                    if(typeof callBackFuns.setEdit == 'function'){
                        callBackFuns.setEdit($loadingContent, 'disabled', false, needControlName);
                    }
                }else{
                    loadingData(data,$loadingContent);
                    if(needControlName){
                        $loadingContent.find('input[name="name"]').val(data.name);
                    }
                }
                if(needControlName){
                    inputReadonly($loadingContent.find('.field-name'));
                }
            },
            'error': function () {
                //
                clearPrsInfo($loadingContent, callBackFuns, emptyData, needControlName, action);
                inputCancelReadonly($loadingContent.find('.field-name'));
            }
        });
    };

    var clearCgoPrsInfo = function ($loadingContent, needControlName, action) {
        $loadingContent.find('.specialty-label').html('');
        $loadingContent.find('.sub-specialty-label').html('');
        $loadingContent.find('.qualification-label').html('');
        $loadingContent.find('span.otherQualificationSpan').html('*');
        if('psnSelect' != action && needControlName){
            $loadingContent.find('input[name="name"]').val('');
        }
    };

    var loadingData = function (data,$loadingContent) {
        loading(data.specialty,$loadingContent,'specialty-label');
        loading(data.subspecialty,$loadingContent,'sub-specialty-label');
        loading(data.qualification,$loadingContent,'qualification-label');
        addMandatoryForOtherQua(data.specialty,$loadingContent);
    };

    var addMandatoryForOtherQua = function (data,$loadingContent) {
        if(data == null || data == undefined || data == ''){
            $loadingContent.find('span.otherQualificationSpan').html('*');
        }else{
            $loadingContent.find('span.otherQualificationSpan').html('');
        }
    }

    var loading = function (dataArr,$loadingContent,labelClass) {
        var displayVal = "";
        if(dataArr != null && dataArr != undefined && dataArr != ''){
            $.each(dataArr,function (k,v) {
                displayVal = displayVal + v + ',';
            });
            var endLength = displayVal.length-1;
            displayVal = displayVal.substring(0,endLength);
        }
        $loadingContent.find('.'+labelClass).html(displayVal);
    }

    function inputReadonly($content){
        $content.prop('readonly', true);
        $content.css('border-color', '#ededed');
        $content.css('color', '#999');
    }
    function inputCancelReadonly($content){
        $content.prop('readonly', false);
        $content.css('border-color', '');
        $content.css('color', '');
    }
    function isNeedControlName(assignSelectVal, licPerson, appType) {
        return true;//('newOfficer' == assignSelectVal && '1' != licPerson) || ('APTY005' == appType || 'APTY004' == appType);
    }

    function clearPrsInfo($loadingContent, callBackFuns, emptyData, needControlName, action) {
        if(!isEmpty(callBackFuns)){
            if(typeof callBackFuns.fillData == 'function'){
                callBackFuns.fillData($loadingContent, emptyData, needControlName);
            }
            if(typeof callBackFuns.setEdit == 'function'){
                callBackFuns.setEdit($loadingContent, 'disabled', true, needControlName);
            }
        }else{
            clearCgoPrsInfo($loadingContent, needControlName, action);
        }
    }
</script>