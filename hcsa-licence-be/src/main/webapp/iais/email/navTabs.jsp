<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%
    String action = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    if(StringUtil.isEmpty(action)){
        action = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
    }
%>
<br><br><br>
<%@ include file="/include/formHidden.jsp" %>
<input type="hidden" name="crud_action_type" value="">
<input type="hidden" name="crud_action_value" value="">
<input type="hidden" name="crud_action_additional" value="">
<input type="hidden" id = "controlLi" value="<%=action%>">
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li id="infoli" class="active" role="presentation"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab" >Info</a></li>
    <li id="documentsli" class="complete" role="presentation"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab"
                                                data-toggle="tab">Documents</a></li>
    <li id="paymentli" class="complete" role="presentation"><a href="#tabPayment" aria-controls="tabPayment" role="tab"
                                                data-toggle="tab">Payment</a></li>
    <li id="inspectionli" class="complete" role="presentation"><a href="#tabInspection" aria-controls="tabInspection" role="tab"
                                                data-toggle="tab">Inspection</a></li>
    <li id="checkListli" class="complete" role="presentation"><a id="checkList" aria-controls="tabCheckList" role="tab"
                                                  data-toggle="tab">CheckList</a></li>
    <li id="emailViewli" class="complete" role="presentation"><a id="processing" aria-controls="tabProcessing" role="tab"
                                                data-toggle="tab">Processing</a></li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a href="#tabInfo" aria-controls="tabInfo" role="tab" data-toggle="tab">Info</a></div>
        <div class="swiper-slide"><a href="#tabDocuments" aria-controls="tabDocuments" role="tab" data-toggle="tab">Documents</a></div>
        <div class="swiper-slide"><a href="#tabPayment" aria-controls="tabPayment" role="tab" data-toggle="tab">Payment</a></div>
        <div class="swiper-slide"><a href="#tabInspection" aria-controls="tabInspection" role="tab" data-toggle="tab">Inspection</a></div>
        <div class="swiper-slide"><a href="#tabCheckList" aria-controls="tabCheckList" role="tab" data-toggle="tab">CheckList</a></div>
        <div class="swiper-slide"><a href="#tabProcessing" aria-controls="tabProcessing" role="tab" data-toggle="tab">Processing</a></div>
    </div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
</div>


<script type="text/javascript">
    $(document).ready(function() {
        var controlLi = $('#controlLi').val();
        $('#'+controlLi+'li').addClass('active');
        $('#checkList').click(function(){
            submit('checkList',null,null);
        });
        $('#processing').click(function(){
            submit('emailView',null,null);
        });
    });

    function submit(action,value,additional){
        $("[name='crud_action_type']").val(action);
        $("[name='crud_action_type_form_page']").val('laboratoryDisciplines');
        $("[name='crud_action_value']").val(value);
        $("[name='crud_action_additional']").val(additional);
        var mainForm = document.getElementById('mainForm');
        mainForm.submit();
    }


    function doPreview(){
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }

    function doSend(){
        var decision=document.getElementById("decision-email");
        if(decision.value=="Sends email/letter to Applicant"){
            var r=confirm("Are you sure to send it directly and not to AO1 for review?");
            if (r==true){
                SOP.Crud.cfxSubmit("mainForm", "send");
            }
        }
        else {
            SOP.Crud.cfxSubmit("mainForm", "send");
        }

    }

</script>
