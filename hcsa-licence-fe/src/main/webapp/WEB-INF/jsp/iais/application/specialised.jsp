<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<webui:setLayout name="iais-internet"/>

<%@ include file="/WEB-INF/jsp/iais/application/common/dashboard.jsp" %>

<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>

<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
    <input type="hidden" name="specialised_svc_code" value="${specialised_svc_code}">
    <input type="hidden" name="specialised_next_code" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="/WEB-INF/jsp/iais/application/common/navTabs.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane in active">
                                <c:if test="${hcsaServiceDtoList.size()>1}" var="multiSvcs">
                                    <div class="multiservice">
                                        <div class="tab-gp side-tab clearfix">
                                            <%@ include file="common/specialisedTabs.jsp" %>
                                            <div class="tab-content">
                                                <div class="tab-pane in active">
                                                    <%@ include file="section/specialisedContent.jsp" %>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                                <c:if test="${not multiSvcs}">
                                    <%@ include file="section/specialisedContent.jsp" %>
                                </c:if>
                                <%@ include file="common/appFooter.jsp"%>
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
    $(document).ready(function () {
        //Binding method
        $('#Back').click(function () {
            showWaiting();
            submit('premises', 'back', null);
        });
        $('#Next').click(function () {
            showWaiting();
            <c:if test="${empty specialised_next_code}">
            submit('serviceForms', null, null);
            </c:if>
            <c:if test="${not empty specialised_next_code}">
            submitSpecialisedTabs('${specialised_next_code}');
            </c:if>
        });
        $('#SaveDraft').click(function () {
            showWaiting();
            submit('specialised', 'saveDraft', $('#selectDraftNo').val());
        });
        <c:if test="${AppSubmissionDto.needEditController}">
        $('div.specialised-content').each(function () {
            let $content = $(this);
            disableSpecialisedContent($content);
        });
        </c:if>
        <c:if test="${not empty errormapIs}">
        $('div.specialised-content').each(function () {
            doEditSpecialised($(this));
        });
        </c:if>
    });

    function submitSpecialisedTabs(action) {
        $("[name='crud_action_type']").val('specialised');
        $("[name='specialised_next_code']").val(action);
        var mainForm = document.getElementById("mainForm");
        mainForm.submit();
    }
</script>