<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%
    String actionForm = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
    if(StringUtil.isEmpty(actionForm)){
        actionForm = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
    }
%>
<input type="hidden" id = "controlFormLi" value="<%=actionForm%>">
<input type="hidden" name="crud_action_type_form" value="">
<ul class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
    <li id = "clinicalli"  class="complete" role="presentation"><a id = "clinical" aria-controls="lorem1" role="tab" data-toggle="tab">Clinical Laboratory</a></li>
    <li id = "bloodli" class="complete" role="presentation"><a id = "blood" aria-controls="lorem2" role="tab" data-toggle="tab">Blood Banking</a></li>
</ul>
<div class="mobile-side-nav-tab visible-xs visible-sm">
    <select id="serviceSelect">
        <option value="clinicalLab">Clinical Laboratory</option>
        <option value="bloodBanking">Blood Banking</option>
    </select>
</div>


<script type="text/javascript">

    $(document).ready(function() {
        var controlFormLi = $('#controlFormLi').val();
        $('#'+controlFormLi+'li').addClass('active');

        $('#clinical').click(function(){
            submitFormTabs('clinical');
        });
        $('#blood').click(function(){
            submitFormTabs('blood');
        });

    });

    function submitFormTabs(action){
        $("[name='crud_action_type']").val('serviceForms');
        $("[name='crud_action_type_form']").val(action);
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    }

</script>