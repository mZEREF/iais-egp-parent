<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
    String webroot = IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainAssignForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/include/formHidden.jsp" %>
        <br>
        <br>
        <br>
        <br>
        <br>
        <%--<input type="hidden" name="paramController" id="paramController" value="com.ecquaria.cloud.moh.iais.action.InspectionSearchDelegator"/>
        <input type="hidden" name="valEntity" id="valEntity" value="com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto"/>
        <input type="hidden" name="valProfiles" id="valProfiles" value="create"/>--%>
        <input type="hidden" name="InspectionSupSearchSwitchType" value="">
        <input type="hidden" name="actionValue" value="">
        <iais:body>
            <div class="container">
                <div class="col-xs-12">
                    <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                        <h3>
                            <span>Assign Task</span>
                        </h3>
                        <div class="panel panel-default">
                            <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel"
                                 aria-labelledby="headingOne" aria-expanded="true" style="">
                                <div class="panel-body">
                                    <div class="panel-main-content">
                                        <iais:section title="" id="assign_Task">
                                            <iais:row>
                                                <iais:field value="Application No"/>
                                                <iais:value width="7">
                                                    <label><c:out
                                                            value="${inspectionTaskPoolListDto.applicationNo}"/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Type"/>
                                                <iais:value width="7">
                                                    <label><iais:code
                                                            code="${inspectionTaskPoolListDto.applicationType}"/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Application Status"/>
                                                <iais:value width="7">
                                                    <label><iais:code
                                                            code="${inspectionTaskPoolListDto.applicationStatus}"/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Code"/>
                                                <iais:value width="7">
                                                    <label><c:out value="${inspectionTaskPoolListDto.hciCode}"/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="HCI Name / Address"/>
                                                <iais:value width="7">
                                                    <label><c:out value="${inspectionTaskPoolListDto.hciName}"/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Service Name"/>
                                                <iais:value width="7">
                                                    <label><c:out
                                                            value="${inspectionTaskPoolListDto.serviceName}"/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Submission Date"/>
                                                <iais:value width="7">
                                                    <label><fmt:formatDate value='${inspectionTaskPoolListDto.submitDt}'
                                                                           pattern='dd/MM/yyyy'/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Admin Screening Supervisor"/>
                                                <iais:value width="7">
                                                    <label><c:out value="${inspectionTaskPoolListDto.inspectorLeads}"/></label>
                                                </iais:value>
                                            </iais:row>


                                                <iais:row>
                                                    <iais:field value="Admin Screening Officer" required="true"/>
                                                    <iais:value width="10">
                                                        <c:if test="${inspectionReassignTaskDto.inspectorCheck == null}">
                                                        <iais:select name="inspectorCheck" options="inspectorOption" firstOption="Please select" value="${inspectorOption}"></iais:select>
                                                        </c:if>
                                                        <c:if test="${inspectionReassignTaskDto.inspectorCheck != null}">
                                                            <c:forEach items="${inspectionReassignTaskDto.inspectorCheck}" var="name">
                                                            <iais:select name="inspectorCheck" options="inspectorOption" firstOption="Please select" value="${name.value}"></iais:select>
                                                            </c:forEach>
                                                        </c:if>
                                                        <br><span class="error-msg" name="iaisErrorMsg" id="error_inspectorCheck"></span>
                                                    </iais:value>
                                                </iais:row>





                                            <iais:row>
                                                <iais:field value="Reassign Reason" required="true"/>
                                                <iais:value width="10">
                                                    <iais:input type="text" name="reassignRemarks" value="${inspectionReassignTaskDto.reassignRemarks}"/>
                                                    <br><span class="error-msg" name="iaisErrorMsg" id="error_reassignRemarks"></span>
                                                </iais:value>
                                            </iais:row>
                                            <iais:row>
                                                <iais:field value="Inspection Type"/>
                                                <iais:value width="7">
                                                    <label><c:out
                                                            value="${inspectionTaskPoolListDto.inspectionTypeName}"/></label>
                                                </iais:value>
                                            </iais:row>
                                            <iais:action>
                                                <button class="btn btn-lg btn-login-back" style="float:left"
                                                        type="button"
                                                        onclick="javascript:doInspectionSupAssignTaskBack()">Back
                                                </button>
                                                <button class="btn btn-lg btn-login-next" style="float:right"
                                                        type="button"
                                                        onclick="javascript:doInspectionSupAssignTaskNext()">Next
                                                </button>
                                            </iais:action>
                                        </iais:section>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </iais:body>
    </form>
</div>

<%@ include file="/include/validation.jsp" %>
<script>
    function doInspectionSupAssignTaskBack() {
        clearErrorMsg();
        $("[name='actionValue']").val('back');
        submit('back');
    }

    function doInspectionSupAssignTaskNext() {
        clearErrorMsg();
        doValidation();
        if (getErrorMsg()) {
            dismissWaiting();
        } else {
            $("[name='actionValue']").val('confirm');
            submit('confirm');
        }
    }

    function submit(action) {
        $("[name='InspectionSupSearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainAssignForm');
        mainPoolForm.submit();
    }
</script>

