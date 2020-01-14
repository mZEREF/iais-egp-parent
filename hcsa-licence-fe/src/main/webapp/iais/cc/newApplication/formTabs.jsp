<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.ParamUtil" %>
<%@ page import="com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator" %>

<%
    String actionForm = (String)ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);

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
        // if($('#isServiceEdit').val() == 'true'){
        //     disabledPage();
        // }
        <c:if test="${AppSubmissionDto.appEditSelectDto!=null && !AppSubmissionDto.appEditSelectDto.serviceEdit}">
        disabledPage();
        </c:if>
    });

    function submitFormTabs(action){
        $("[name='crud_action_type']").val('serviceForms');
        $("[name='crud_action_type_tab']").val(action);
        $("[name='crud_action_type_form_page']").val('jump');
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    }

</script>