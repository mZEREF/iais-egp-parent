<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>

<%
    String webroot=IaisEGPConstant.CSS_ROOT + IaisEGPConstant.BE_CSS_ROOT;
%>
<div class="dashboard" style="background-image:url('<%=webroot%>img/Masthead-banner.jpg')">
    <form method="post" id="mainAssignForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="InspectionSupSearchSwitchType" value="">
        <input type="hidden" name="actionValue" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <div class="bg-title">
                                <h2>
                                    <span>Task Re-assignment</span>
                                </h2>
                            </div>
                            <iais:body >
                                <iais:section title="" id = "assign_Task">
                                    <iais:row>
                                        <iais:field value="${groupRoleFieldDto.groupMemBerName}:" required="true"/>
                                        <iais:value width="5">
                                            <c:if test="${'true' == inspectionTaskPoolListDto.inspectorFlag}">
                                                <iais:select name="inspectorCheck" firstOption="Please Select" options="inspectorOption" value="${inspectionTaskPoolListDto.inspector}"></iais:select>
                                                <br><span class="error-msg" name="iaisErrorMsg" id="error_inspectorCheck"></span>
                                            </c:if>
                                            <c:if test="${'false' == inspectionTaskPoolListDto.inspectorFlag}">
                                                <p><label>-</label></p>
                                            </c:if>
                                        </iais:value>
                                    </iais:row>
                                    <iais:row>
                                        <iais:field value="Remarks:"/>
                                        <iais:value width="6">
                                            <textarea style="resize:none" name="reassignRemarks" cols="65" rows="6" title="content" MAXLENGTH="2000"><c:out value="${inspectionTaskPoolListDto.reassignRemarks}"/></textarea>
                                        </iais:value>
                                    </iais:row>
                                    <iais:action >
                                        <a href="#" class="back" id="Back" onclick="javascript:doInspectionReassignTaskBack()" style="float:left"><em class="fa fa-angle-left"></em> Back</a>
                                            <button class="btn btn-primary" style="float:right" type="button" onclick="javascript:doInspectionReassignTaskNext()">Next</button>
                                    </iais:action>
                                </iais:section>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
</div>
<script>
    function doInspectionReassignTaskBack() {
        clearErrorMsg();
        showWaiting();
        $("[name='actionValue']").val('back');
        InspectionReassignTaskSubmit('back');
    }

    function doInspectionReassignTaskNext() {
        clearErrorMsg();
        showWaiting();
        $("[name='actionValue']").val('confirm');
        InspectionReassignTaskSubmit('confirm');
    }
    function InspectionReassignTaskSubmit(action){
        $("[name='InspectionSupSearchSwitchType']").val(action);
        var mainPoolForm = document.getElementById('mainAssignForm');
        mainPoolForm.submit();
    }
</script>

