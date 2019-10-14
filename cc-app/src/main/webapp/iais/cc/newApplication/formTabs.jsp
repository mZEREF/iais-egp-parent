<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.StringUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.dto.HcsaServiceDto" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%
    String actionForm = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
    if(StringUtil.isEmpty(actionForm)){
        actionForm = (String)ParamUtil.getRequestAttr(request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
    }

%>
<input type="hidden" id = "controlFormLi" value="<%=actionForm%>">
<input type="hidden" name="crud_action_type_tab" value="">
<ul id = "tabUl" class="nav nav-pills nav-stacked hidden-xs hidden-sm" role="tablist">
<c:forEach items="${hcsaServiceDtoList}" var="list">
    <li id = "${list.svcCode}li"  class="complete" role="presentation"><a id = "${list.svcCode}" aria-controls="lorem1" role="tab" data-toggle="tab">${list.svcName}</a></li>
</c:forEach>
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

        $('#tabUl > li >a').click(function () {
            submitFormTabs(this.id,null,null);
        });

        // $('#clinical').click(function(){
        //     submitFormTabs('clinical');
        // });
        // $('#blood').click(function(){
        //     submitFormTabs('blood');
        // });

    });

    function submitFormTabs(action){
        $("[name='crud_action_type']").val('serviceForms');
        $("[name='crud_action_type_tab']").val(action);
        $("[name='crud_action_type_form']").val('jump');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    }

</script>