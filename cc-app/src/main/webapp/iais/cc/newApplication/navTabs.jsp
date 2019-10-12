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
<input type="hidden" id = "controlLi" value="<%=action%>">
<ul id = "nav-tabs-ul" class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
    <li id ="premisesli" class="" role="presentation"><a id="premises" aria-controls="premisesTab" role="tab" data-toggle="tab">Premises</a></li>
    <li id = "documentsli" class="complete" role="presentation"><a id = "documents" aria-controls="documentsTab" role="tab" data-toggle="tab">Primary <br> Documents</a></li>
    <li id = "serviceFormsli" class="incomplete" role="presentation"><a id = "serviceForms" aria-controls="serviceInformationTab" role="tab" data-toggle="tab">Service-Related <br> Information</a></li>
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
    });

    function submit(action,value,additional){
        $("[name='crud_action_type']").val(action);
        $("[name='crud_action_value']").val(value);
        $("[name='crud_action_additional']").val(additional);
        var mainForm = document.getElementById('mainForm');
        mainForm.submit();
    }

</script>