<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<webui:setLayout name="iais-internet"/>
<%@ include file="./dashboard.jsp" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<c:set var="dto" value="${AppSubmissionDto.subLicenseeDto}"/>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <%--<%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>--%>
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/newApplication/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active" id="home">
                                <%@ include file="/WEB-INF/jsp/iais/common/licenseeDetail.jsp" %>
                                <div class="application-tab-footer">
                                    <c:choose>
                                        <c:when test="${('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType) && requestInformationConfig == null}">
                                            <%@include file="../common/rfcFooter.jsp"%>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-6 ">
                                                    <c:choose>
                                                        <c:when test="${DraftConfig != null || requestInformationConfig != null}">
                                                            <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohInternetInbox?initPage=initApp"><em class="fa fa-angle-left"></em> Back</a>
                                                        </c:when>
                                                        <c:when test="${AssessMentConfig != null}">
                                                            <a class="back" id="Back" href="/main-web/eservice/INTERNET/MohAccessmentGuide/jumpInstructionPage"><em class="fa fa-angle-left"></em> Back</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="back" id="Back" href="#"><em class="fa fa-angle-left"></em> Back</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="col-xs-12 col-sm-6">
                                                    <div class="button-group">
                                                        <c:if test="${requestInformationConfig==null}">
                                                            <input type="hidden" id="selectDraftNo" value="${selectDraftNo}">
                                                            <input type="hidden" id="saveDraftSuccess" value="${saveDraftSuccess}">
                                                            <a class="btn btn-secondary premiseSaveDraft" id="SaveDraft" >Save as Draft</a>
                                                        </c:if>
                                                        <a class="btn btn-primary next premiseId" id="Next" >Next</a></div>
                                                </div>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
<c:if test="${!('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
    <iais:confirm msg="This application has been saved successfully" callBack="$('#saveDraft').modal('hide');" popupOrder="saveDraft"
                  yesBtnDesc="continue" cancelBtnDesc="exit to inbox" cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary"
                  cancelFunc="submit('licensee','saveDraft','jumpPage');" />
</c:if>
<script type="text/javascript">
    $(document).ready(function() {
        //assignSelectBindEvent();

        $('#Back').on('click',function(){
            showWaiting();
            submit(null,'back',null);
        });
        $('#Next').on('click',function(){
            showWaiting();
            $('input[type="radio"]').prop('disabled',false);
            submit('premises',null,null);
        });
        $('#SaveDraft').on('click',function(){
            showWaiting();
            $('input[type="radio"]').prop('disabled',false);
            submit('licensee','saveDraft',$('#selectDraftNo').val());
        });

        if($('#saveDraftSuccess').val()=='success'){
            $('#saveDraft').modal('show');
        }

        <c:if test="${(!AppSubmissionDto.needEditController && readOnly) || AppSubmissionDto.needEditController}">
            disableContent('div.licensee-detail');
        </c:if>
    });

    /*function editContent(targetSelector) {
        var $currContent = $(targetSelector);
        $currContent.find('.edit-content').addClass('hidden');
        $currContent.find('input[type="text"]').prop('disabled', false);
        $currContent.find('div.nice-select').removeClass('disabled');
        $currContent.find('input[type="text"]').css('border-color', '');
        $currContent.find('input[type="text"]').css('color', '');
        $currContent.find('.date_picker').removeClass('disabled-placeHolder');
        $currContent.find('input.holdCerByEMS').each(function () {
            $(this).closest('div').find('label span.check-circle').removeClass('radio-disabled');
        });
        $currContent.find('input[type="checkbox"]').prop('disabled',false);
        $currContent.find('input[type="radio"]').prop('disabled',false);
        $currContent.find('.assignSel').prop('disabled',false);
        $('#isEditHiddenVal').val('1');

        var appType = $('input[name="applicationType"]').val();
        var assignSelectVal = $currContent.find('select.assignSel').val();
        var licPerson = $currContent.find('input.licPerson').val();
        var needControlName = isNeedControlName(assignSelectVal, licPerson, appType);
        if(needControlName){
            var prgNo = $currContent.find('input.profRegNo').val();
            if(!isEmpty(prgNo)){
                controlEdit($currContent.find('input.field-name'), 'disabled', false);
            }
        }
    }*/
/*
   function assignSelectBindEvent() {
        $('#assignSelect').on('change', function() {
            clearErrorMsg();
            assignSelect(this, $(this).closest('div.licenseeContent').find('div.person-detail'));
        });
    }

    var assignSelect = function (srcSelector, targetSelector) {
        var assignSelVal = $(srcSelector).val();
        // console.info(assignSelVal);
        var $content = $(targetSelector);
        // init
        unDisableContent(targetSelector);
        if('-1' == assignSelVal) {
            $content.addClass('hidden');
            clearFields(targetSelector);
        } else if('newOfficer' == assignSelVal) {
            $content.removeClass('hidden');
            clearFields(targetSelector);
        } else {
            $content.removeClass('hidden');
            var arr = assignSelVal.split(',');
            var idType = arr[0];
            var idNo = arr[1];
            loadSelectLicensee($content, idType, idNo, fillLicensee);
        }
    }

    var loadSelectLicensee = function ($CurrentPsnEle, idType, idNo, callback) {
        showWaiting();
        var spcEle = $CurrentPsnEle.find('.specialty');
        var jsonData = {
            'idType':idType,
            'idNo':idNo,
            'psnType':psnType
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/person-info/licesee-detail',
            'dataType':'json',
            'data':jsonData,
            'type':'GET',
            'success':function (data) {
                // if(data == null){
                //     return;
                // }
                if (typeof callback === 'function') {
                    callback($CurrentPsnEle, data, psnType);
                } else {
                    console.info(data);
                    //fillPsnForm($CurrentPsnEle, data, psnType);
                }
                dismissWaiting();
            },
            'error':function () {
                dismissWaiting();
            }
        });
    };

    function fillLicensee($current, data, diableAll) {
        if (isEmpty($current)) {
            return;
        }
        if (isEmpty(data)) {
            $current.addClass('hidden');
            clearFields($content);
        }
        $.each(data, function(i, val) {
            var $input = $current.find('.' + i + ':input', '[name="'+ i +'"]:input');
            if ($input.length == 0) {
                return;
            }
            var type = $input[0].type, tag = $input[0].tagName.toLowerCase();
            //console.info("Field - " + i + " : " + val);
            //console.info("Tag - " + tag + " : " + type);
            if (type == 'radio') {
                $input.filter('[value="' + val + '"]').prop('checked', true);
                $input.filter(':not([value="' + val + '"])').prop('checked', false);
            } else if (type == 'checkbox') {
                if ($.isArray(val)) {
                    $input.prop('checked', false);
                    for (var v in val) {
                        if (curVal == v) {
                            $(this).prop('checked', true);
                        }
                    }
                } else {
                    $input.filter('[value="' + val + '"]').prop('checked', true);
                    $input.filter(':not([value="' + val + '"])').prop('checked', false);
                }
            } else if (tag == 'select') {
                var oldVal = $input.val();
                $input.val(val);
                if (isEmpty($input.val())) {
                    $input[0].selectedIndex = 0;
                }
                if ($input.val() != oldVal) {
                    $input.niceSelect("update");
                }
            } else {
                $input.val(val);
            }
            if (diableAll) {
                disableContent($input);
            }
        });
    }

    function retrieveAddr(selector, target) {
        var $postalCodeEle = $(selector).closest('div.postalCodeDiv');
        var $addressSelectors = $(selector).closest('div.licenseeContent').find('div.address');
        var postalCode = $postalCodeEle.find('.postalCode').val();
        //var thisId = $(selector).attr('id');
        //alert(postalCode);
        var re=new RegExp('^[0-9]*$');
        var data = {
            'postalCode':postalCode
        };
        var premType = '';
        showWaiting();
        $.ajax({
            'url':'${pageContext.request.contextPath}/retrieve-address',
            'dataType':'json',
            'data':data,
            'type':'GET',
            'success':function (data) {
                if(data == null){
                    // $postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                    //show pop
                    $('#postalCodePop').modal('show');
                    handleVal($addressSelectors.find(':input'), '', false);
                    // $premContent.find('.'+prefixName+'BlkNo').val('');
                    // $premContent.find('.'+prefixName+'StreetName').val('');
                    // $premContent.find('.'+prefixName+'BuildingName').val('');
                    //
                    // $premContent.find('.'+prefixName+'BlkNo').prop('readonly',false);
                    // $premContent.find('.'+prefixName+'StreetName').prop('readonly',false);
                    // $premContent.find('.'+prefixName+'BuildingName').prop('readonly',false);
                    //
                    // $premContent.find('input[name="retrieveflag"]').val('0');
                } else {
                    handleVal($addressSelectors.find('input[name="blkNo"]'), data.blkHseNo, true);
                    handleVal($addressSelectors.find('input[name="streetName"]'), data.streetName, true);
                    handleVal($addressSelectors.find('input[name="buildingName"]'), data.buildingName, true);
                }
                dismissWaiting();
            },
            'error':function () {
                //$postalCodeEle.find('.postalCodeMsg').html("the postal code information could not be found");
                //show pop
                $('#postalCodePop').modal('show');
                handleVal($addressSelectors.find(':input'), '', false);
                // $premContent.find('.'+prefixName+'BlkNo').val('');
                // $premContent.find('.'+prefixName+'StreetName').val('');
                // $premContent.find('.'+prefixName+'BuildingName').val('');
                //
                // $premContent.find('.'+prefixName+'BlkNo').prop('readonly',false);
                // $premContent.find('.'+prefixName+'StreetName').prop('readonly',false);
                // $premContent.find('.'+prefixName+'BuildingName').prop('readonly',false);

                //$premContent.find('input[name="retrieveflag"]').val('0');
                dismissWaiting();
            }
        });
    }

    function handleVal(selector, val, readonly) {
        $(selector).val(val);
        $(selector).prop('readonly', readonly);
    }
    */
</script>