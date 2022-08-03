<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="ias" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<webui:setLayout name="iais-internet"/>

<%@ include file="/WEB-INF/jsp/iais/common/dashboard.jsp" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <input type="hidden" name="specialised_svc_code" value="${specialised_svc_code}">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <div class="multiservice">
                                    <div class="tab-gp side-tab clearfix">
                                        <%@ include file="common/formTabs.jsp" %>
                                        <div class="tab-content">
                                            <div class="tab-pane in active">
                                                <%@ include file="section/specialisedDetail.jsp" %>
                                                <%@ include file="common/appFooter.jsp"%>
                                            </div>
                                        </div>
                                    </div>
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
<script type="text/javascript">
    $(document).ready(function() {
        //Binding method
        $('#Back').click(function () {
            showWaiting();
            submit('premises', 'back', null);
        });
        $('#Next').click(function () {
            showWaiting();
            submit('serviceForms', null, null);
        });
        $('#SaveDraft').click(function () {
            showWaiting();
            submit('specialised', 'saveDraft', $('#selectDraftNo').val());
        });
    });

    function submitFormTabs(action){
        $("[name='crud_action_type']").val('specialised');
        // $("[name='crud_action_type_tab']").val(action);
        // $("[name='crud_action_type_form_page']").val('jump');
        $("[name='specialised_svc_code']").val(action);
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    }
</script>