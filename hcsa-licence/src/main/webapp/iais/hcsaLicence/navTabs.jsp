<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    String action = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE);
    if(StringUtil.isEmpty(action)){
        action = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE);
    }
%>
<input type="hidden" name="crud_action_type" value="">
<input type="hidden" name="crud_action_value" value="">
<input type="hidden" name="crud_action_additional" value="">
<input type="hidden" name="crud_action_type_form" value="">
<input type="hidden" id = "controlLi" value="<%=action%>">
<ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li class="active" role="presentation"><a href="#tabInbox" aria-controls="tabInbox" role="tab" data-toggle="tab">Info</a></li>
    <li class="complete" role="presentation"><a href="#tabApplication" aria-controls="tabApplication" role="tab" data-toggle="tab">Documents</a></li>
    <li class="incomplete" role="presentation"><a href="#tabLicence" aria-controls="tabLicence" role="tab" data-toggle="tab">Payment</a></li>
    <li class="incomplete" role="presentation"><a href="#tabLicence" aria-controls="tabLicence" role="tab" data-toggle="tab">Inspection</a></li>
    <li class="incomplete" role="presentation"><a href="#tabLicence" aria-controls="tabLicence" role="tab" data-toggle="tab">Processing</a></li>
</ul>
<div class="tab-nav-mobile visible-xs visible-sm">
    <div class="swiper-wrapper" role="tablist">
        <div class="swiper-slide"><a href="#tabInbox" aria-controls="tabInbox" role="tab" data-toggle="tab">Inbox (2)</a></div>
        <div class="swiper-slide"><a href="#tabApplication" aria-controls="tabApplication" role="tab" data-toggle="tab">Applications</a></div>
        <div class="swiper-slide"><a href="#tabLicence" aria-controls="tabLicence" role="tab" data-toggle="tab">Licences</a></div>
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
    });

    function submit(action,value,additional){
        $("[name='crud_action_type']").val(action);
        $("[name='crud_action_type_form']").val('jump');
        $("[name='crud_action_value']").val(value);
        $("[name='crud_action_additional']").val(additional);
        var mainForm = document.getElementById('mainForm');
        mainForm.submit();
    }

    function submitForms(action,value,additional,tab){
        $("[name='crud_action_type']").val('serviceForms');
        $("[name='crud_action_type_tab']").val(tab);
        $("[name='crud_action_type_form']").val(action);
        $("[name='crud_action_value']").val(value);
        $("[name='crud_action_additional']").val(additional);
        var mainForm = document.getElementById('mainForm');
        mainForm.submit();
    }

</script>