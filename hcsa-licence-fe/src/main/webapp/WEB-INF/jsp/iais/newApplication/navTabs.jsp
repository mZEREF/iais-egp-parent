<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    String action = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    if(StringUtil.isEmpty(action)){
        action = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    }
    String flag = ParamUtil.getRequestString(request,"flag");
%>

<%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
<input type="hidden" name="crud_action_type_form_page" value="">
<input type="hidden" id = "controlLi" value="<%=action%>">

    <%if(!StringUtil.isEmpty(flag)&&"transfer".equals(flag)){

    }else{
        %>
<ul id="nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li id="licenseeli" role="presentation" class="${empty coMap.licensee ? 'incomplete' : 'complete'}">
        <a id="licensee" aria-controls="licenseeTab" role="tab" data-toggle="tab">Licensee Details</a>
    </li>
    <li id="premisesli" role="presentation" class="${empty coMap.premises ? 'incomplete' : 'complete'}">
        <a id="premises" aria-controls="premisesTab" role="tab" data-toggle="tab">Mode of Service Delivery</a>
    </li>
    <li id="documentsli" role="presentation" class="${empty coMap.document ? 'incomplete' : 'complete'}">
        <a id = "documents" aria-controls="documentsTab" role="tab" data-toggle="tab">Primary<br> Documents</a>
    </li>
    <li id="serviceFormsli" role="presentation" class="${empty coMap.information ? 'incomplete' : 'complete'}">
        <a id="serviceForms" aria-controls="serviceInformationTab" role="tab" data-toggle="tab">Service-Related<br> Information</a>
    </li>
    <li id="previewli" role="presentation" class="${empty coMap.previewli ? 'incomplete' : 'complete'}">
        <a id="preview" aria-controls="previewTab" role="tab" data-toggle="tab">Preview & Submit</a>
    </li>
    <li id="paymentli" class="disabled" role="presentation">
        <a id="payment" aria-controls="paymentTab" role="tab" data-toggle="tab">Payment</a>
    </li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm" style="overflow:hidden">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide " >
            <a href="#licenseeTab" aria-controls="tabInbox"  role="tab" data-toggle="tab">Licensee Details</a>
        </div>
        <div class="swiper-slide " ><a href="#premisesTab" aria-controls="licenseeTab"  role="tab" data-toggle="tab">Mode of Service Delivery</a></div>
        <div class="swiper-slide"><a href="#documentsTab" aria-controls="tabApplication" role="tab" data-toggle="tab">Primary Documents</a></div>
        <div class="swiper-slide"><a href="#serviceInformationTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Service-Related Information</a></div>
        <div class="swiper-slide"><a href="#previewTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Preview & Submit</a></div>
        <div class="swiper-slide"><a href="#paymentTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Payment</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>
<%}%>

<c:set var="canClickMainTab" value="${empty AppSubmissionDto || 'APTY002' ne AppSubmissionDto.appType ||
    AppSubmissionDto.appGrpPremisesDtoList.get(0).filled}" />
<script type="text/javascript">

    $(document).ready(function() {
        // tab click
        var controlLi = $('#controlLi').val();
        var $tarSel = $('#'+controlLi+'li');
        if ($tarSel.length > 0) {
            $tarSel.addClass('active');
            if ($tarSel.attr("class").match("active")){
                $tarSel.removeClass("incomplete");
                $tarSel.removeClass("complete");
            }
        }
        // bind event
        if ('licensee' == controlLi && ${!canClickMainTab}) {
            $('#nav-tabs-ul #premises').on('click', function(){
                showWaiting();
                $('#mainForm').find(':input').prop('disabled',false);
                submit('premises',null,null);
            });
        } else {
            $('#nav-tabs-ul a').click(function() {
                var currId = $(this).attr('id');
                console.info(currId);
                if (controlLi == currId) {
                    return;
                } else if ('serviceForms' == currId) {
                    showWaiting();
                    $("[name='crud_action_type']").val('serviceForms');
                    $("[name='crud_action_type_tab']").val('${hcsaServiceDtoList.get(0).svcCode}');
                    $("[name='crud_action_type_form_page']").val('jump');
                    var mainForm = document.getElementById("mainForm");
                    mainForm.submit();
                } else if (currId != 'payment') {
                    showWaiting();
                    $('#mainForm').find(':input').prop('disabled',false);
                    submit(currId,null,null);
                }
            });
        }

        <c:if test="${requestInformationConfig==null && ('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
        <c:if test="${'APTY004' ==AppSubmissionDto.appType}">
        $('#preview').unbind();
        $('#preview').removeAttr("data-toggle");
        $('#previewli').unbind();
        </c:if>
        <c:choose>
        <c:when test="${AppSubmissionDto.appEditSelectDto.premisesEdit}">

        $('#payment').unbind();
        $('#paymentli').unbind();
        </c:when>
        <c:when test="${AppSubmissionDto.appEditSelectDto.docEdit}">

        $('#payment').unbind();
        $('#paymentli').unbind();
        </c:when>
        <c:when test="${AppSubmissionDto.appEditSelectDto.serviceEdit}">
        $('#payment').unbind();
        $('#paymentli').unbind();
        </c:when>
        <c:otherwise>
        $('#premises').unbind();
        $('#premisesli').unbind();
        $('#documents').unbind();
        $('#documentsli').unbind();
        $('#serviceForms').unbind();
        $('#serviceFormsli').unbind();
        $('#preview').unbind();
        $('#previewli').unbind();
        $('#payment').unbind();
        $('#paymentli').unbind();
        $('#premises').removeAttr("data-toggle");
        $('#documents').removeAttr("data-toggle");
        $('#serviceForms').removeAttr("data-toggle");
        $('#preview').removeAttr("data-toggle");
        $('#payment').removeAttr("data-toggle");
        </c:otherwise>
        </c:choose>

        </c:if>

    });

    function submit(action,value,additional){
        $("[name='crud_action_type']").val(action);
        $("[name='crud_action_type_form_page']").val('${serviceStepDto.hcsaServiceStepSchemeDtos.get(0).stepCode}');
        $("[name='crud_action_value']").val(value);
        $("[name='crud_action_additional']").val(additional);
        var mainForm = document.getElementById('mainForm');
        mainForm.submit();
    }

    function submitForms(action,value,additional,tab){
        $("[name='crud_action_type']").val('serviceForms');
        $("[name='crud_action_type_tab']").val(tab);
        $("[name='crud_action_type_form_page']").val(action);
        $("[name='crud_action_value']").val(value);
        $("[name='crud_action_additional']").val(additional);
        var mainForm = document.getElementById('mainForm');
        mainForm.submit();
    }

    function backFormsBtn(action,value,additional,tab, bak) {
        $("[name='crud_action_type']").val('serviceForms');
        $("[name='crud_action_type_tab']").val(tab);
        $("[name='crud_action_type_form_page']").val(action);
        $("[name='crud_action_value']").val(value);
        $("[name='crud_action_additional']").val(additional);
        $('[name="crud_action_bak"]').val(bak);
        var mainForm = document.getElementById('mainForm');
        mainForm.submit();
    }
    function disabledPage(){
        $('input[type="radio"]').prop('disabled',true);
        $('input[type="text"]').prop('disabled',true);
        $('input[type="file"]').prop('disabled',true);
        $('input[type="checkbox"]').prop('disabled',true);
        $('div.nice-select').addClass('disabled');
        $('input[type="text"]').css('border-color','#ededed');
        $('input[type="text"]').css('color','#999');
    }

    function disabledPartPage($Ele) {
        inputDisabled($Ele);
        $Ele.find('div.nice-select').addClass('disabled');
        $Ele.find('input[type="text"]').css('border-color','#ededed');
        $Ele.find('input[type="text"]').css('color','#999');

    }

    function inputDisabled($Ele) {
        $Ele.find('input[type="radio"]').prop('disabled',true);
        $Ele.find('input[type="text"]').prop('disabled',true);
        $Ele.find('input[type="file"]').prop('disabled',true);
        $Ele.find('input[type="checkbox"]').prop('disabled',true);
    }

    function personDisable($Ele,inpDisStyle,disableDiv){
        inputDisabled($Ele);
        if(inpDisStyle == 'Y'){
            $Ele.find('input[type="text"]').css('border-color','#ededed');
            $Ele.find('input[type="text"]').css('color','#999');
        }
        if(disableDiv == 'Y'){
            $Ele.find('div.nice-select').addClass('disabled');
        }
    }

    function unDisabledPage(){
        $('input[type="radio"]').prop('disabled',false);
        $('input[type="text"]').prop('disabled',false);
        $('input[type="file"]').prop('disabled',false);
        $('input[type="checkbox"]').prop('disabled',false);
        $('div.nice-select').removeClass('disabled');
        $('input[type="text"]').css('border-color','');
        $('input[type="text"]').css('color','');
    }

    function unDisabledPartPage($Ele){
        $Ele.find('input[type="radio"]').prop('disabled',false);
        $Ele.find('input[type="text"]').prop('disabled',false);
        $Ele.find('input[type="file"]').prop('disabled',false);
        $Ele.find('input[type="checkbox"]').prop('disabled',false);
        $Ele.find('div.nice-select').removeClass('disabled');
        $Ele.find('input[type="text"]').css('border-color','');
        $Ele.find('input[type="text"]').css('color','');
        <!--multi -->
        $Ele.find('div.multi-select input').prop('disabled',false);
    }

    function readonlyPartPage($Ele) {
        $Ele.find('input[type="radio"]').prop('disabled',true);
        $Ele.find('input[type="text"]').prop('readonly',true);
        $Ele.find('input[type="file"]').prop('readonly',true);
        $Ele.find('input[type="checkbox"]').prop('readonly',true);
        $Ele.find('div.nice-select').addClass('disabled');
        <!--add disabled bg color-->
        $Ele.find('input[type="text"]').css('border-color','#ededed');
        $Ele.find('input[type="text"]').css('color','#999');
        //$Ele.find('.fireIssuedDate').unbind();
        <!--multi -->
        $Ele.find('div.multi-select input').prop('disabled',true);
    }

    function unreadonlyPartPage($Ele) {
        $Ele.find('input[type="radio"]').prop('disabled',false);
        $Ele.find('input[type="text"]').prop('readonly',false);
        $Ele.find('input[type="file"]').prop('readonly',false);
        $Ele.find('input[type="checkbox"]').prop('readonly',false);
        $Ele.find('div.nice-select').removeClass('disabled');
        <!--remove disabled bg color-->
        $Ele.find('input[type="text"]').css('border-color','');
        $Ele.find('input[type="text"]').css('color','');
        $Ele.find('.date_picker').datepicker({
            format:"dd/mm/yyyy",
            autoclose:true,
            todayHighlight:true,
            orientation:'bottom'
        });
        <!--multi -->
        $Ele.find('div.multi-select input').prop('disabled',false);
    }

    var unbindAllTabs = function () {
        $('#premises').unbind();
        $('#premisesli').unbind();
        $('#documents').unbind();
        $('#documentsli').unbind();
        $('#serviceForms').unbind();
        $('#serviceFormsli').unbind();
        $('#preview').unbind();
        $('#previewli').unbind();
        $('#payment').unbind();
        $('#paymentli').unbind();
    }

</script>