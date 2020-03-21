<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    String action = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    if(StringUtil.isEmpty(action)){
        action = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    }
%>


<input type="hidden" name="crud_action_type" value="">
<input type="hidden" name="crud_action_value" value="">
<input type="hidden" name="crud_action_additional" value="">
<input type="hidden" name="crud_action_type_form_page" value="">
<input type="hidden" id = "controlLi" value="<%=action%>">
<ul id = "nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li id ="premisesli" class="" role="presentation"><a id="premises" aria-controls="premisesTab" role="tab" data-toggle="tab">Premises</a></li>
    <li id = "documentsli" class="" role="presentation"><a id = "documents" aria-controls="documentsTab" role="tab" data-toggle="tab">Primary <br> Documents</a></li>
    <li id = "serviceFormsli" class="" role="presentation"><a id = "serviceForms" aria-controls="serviceInformationTab" role="tab" data-toggle="tab">Service-Related <br> Information</a></li>
    <li id = "previewli" class="" role="presentation"><a id = "preview" aria-controls="previewTab" role="tab" data-toggle="tab">Preview & Submit</a></li>
    <li id = "paymentli" class="disabled" role="presentation"><a id = "payment" aria-controls="paymentTab" role="tab" data-toggle="tab">Payment</a></li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a href="#premisesTab" aria-controls="tabInbox" role="tab" data-toggle="tab">Premises</a></div>
        <div class="swiper-slide"><a href="#documentsTab" aria-controls="tabApplication" role="tab" data-toggle="tab">Primary Documents</a></div>
        <div class="swiper-slide"><a href="#serviceInformationTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Service-Related Information</a></div>
        <div class="swiper-slide"><a href="#previewTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Preview & Submit</a></div>
        <div class="swiper-slide"><a href="#paymentTab" aria-controls="tabLicence" role="tab" data-toggle="tab">Payment</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>


<script type="text/javascript">

    $(document).ready(function() {
        var controlLi = $('#controlLi').val();
        $('#'+controlLi+'li').addClass('active');
        $('#premises').click(function(){
            submit('premises',null,null);
        });
        $('#documents').click(function(){
            submit('documents',null,null);
        });
        $('#serviceForms').click(function(){
            submit('serviceForms',null,null);
        });
        $('#preview').click(function(){
            submit('preview',null,null);
        });
        $('#payment').click(function(){
            submit('payment',null,null);
        });

        <c:if test="${requestInformationConfig==null && ('APTY005' ==AppSubmissionDto.appType || 'APTY004' ==AppSubmissionDto.appType)}">
            <c:choose>
                <c:when test="${AppSubmissionDto.appEditSelectDto.premisesEdit}">
                    $('#documents').unbind();
                    $('#documentsli').unbind();
                    $('#serviceForms').unbind();
                    $('#serviceFormsli').unbind();
                    $('#payment').unbind();
                    $('#paymentli').unbind();
                </c:when>
                <c:when test="${AppSubmissionDto.appEditSelectDto.docEdit}">
                    $('#premises').unbind();
                    $('#premisesli').unbind();
                    $('#serviceForms').unbind();
                    $('#serviceFormsli').unbind();
                    $('#payment').unbind();
                    $('#paymentli').unbind();
                </c:when>
                <c:when test="${AppSubmissionDto.appEditSelectDto.serviceEdit}">
                    $('#premises').unbind();
                    $('#premisesli').unbind();
                    $('#documents').unbind();
                    $('#documentsli').unbind();
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
                </c:otherwise>
            </c:choose>


        </c:if>

    });

    function submit(action,value,additional){
        $("[name='crud_action_type']").val(action);
        $("[name='crud_action_type_form_page']").val('SVST001');
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
        $Ele.find('input[type="text"]').css('border-color','#ededed');
        $Ele.find('input[type="text"]').css('color','#999');
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
        $Ele.find('.date_picker').unbind();
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
            format:"dd/mm/yyyy"
        });
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