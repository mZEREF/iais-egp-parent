<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<style>
    .form-horizontal .nice-select {
        margin-bottom: 0;
    }
</style>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-inspection-follow-up-items.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-file.js"></script>

<script>
    <% String jsonStr = (String) request.getAttribute("docTypeOpsJson");
    if (jsonStr == null || "".equals(jsonStr)) {
       jsonStr = "undefined";
    }
    %>
    var docTypeOps = <%=jsonStr%>;
</script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>

<%@include file="dashboard.jsp" %>

<form method="post" id="mainForm" enctype="multipart/form-data" action="<%=process.runtime.continueURL()%>" onsubmit="return validateOtherDocType();">
    <%--@elvariable id="followUpViewDto" type="sg.gov.moh.iais.egp.bsb.dto.inspection.insfollowup.FollowUpViewDto"--%>
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input id="multiUploadTrigger" type="file" multiple="multiple" style="display: none"/>
    <input id="echoReloadTrigger" type="file" style="display: none"/>
    <input type="hidden" id="deleteNewFiles" name="deleteNewFiles" value="">
    <input type="hidden" id="deleteExistFiles" name="deleteExistFiles" value="">
    <div id="fileUploadInputDiv" style="display: none"></div>

    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-horizontal">
                        <div class="table-gp">
                            <table aria-describedby="" class="table">
                                <thead>
                                <tr>
                                    <th scope="col" class="col-md-1">Checklist Item</th>
                                    <th scope="col" class="col-md-2">Item Description</th>
                                    <th scope="col" class="col-md-3">Observations for Follow-up</th>
                                    <th scope="col" class="col-md-2">Action Required</th>
                                    <th scope="col" class="col-md-2">Due Date</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${empty followUpViewDto.followUpDisplayDtos}">
                                        <iais:message key="GENERAL_ACK018" escape="true"/>
                                    </c:when>
                                    <c:otherwise>
                                        <c:forEach items="${followUpViewDto.followUpDisplayDtos}" var="item"
                                                   varStatus="status">
                                            <tr>
                                                <td class="col-md-1"><c:out value="${item.checklistItem}"/></td>
                                                <td class="col-md-2"><c:out value="${item.itemDescription}"/></td>
                                                <td class="col-md-3"><c:out value="${item.observation}"/></td>
                                                <td class="col-md-2"><c:out value="${item.actionRequired}"/></td>
                                                <td class="col-md-2"><c:out value="${item.dueDt}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                        <%@ include file="extension.jsp" %>
                        <br><br>
                        <%@ include file="upload.jsp" %>
                        <br><br>
                        <h3>Remarks</h3>
                        <div class="form-group">
                            <div class="col-md-12 col-sm-12">
                                <label for="remarks"></label><textarea autocomplete="off" class="col-xs-12" name="remarks" id="remarks" maxlength="1000" style="width: 100%"><c:out value="${followUpViewDto.remarks}"/></textarea>
                                <span data-err-ind="remarks" class="error-msg"></span>
                            </div>
                        </div>

                        <%@ include file="history.jsp" %>

                        <div class="application-tab-footer">
                            <div class="row">
                                <div class="col-xs-12 col-sm-6 ">
                                    <c:choose>
                                        <%--@elvariable id="confirmRfi" type="java.lang.String"--%>
                                        <c:when test="${confirmRfi ne null && confirmRfi eq 'Y'}">
                                            <a class="back" id="back" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>"><em class="fa fa-angle-left"></em> Previous</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a class="back" id="back" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg"><em class="fa fa-angle-left"></em> Previous</a>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="col-xs-12">
                                    <div class="button-group">
                                        <c:choose>
                                            <%--@elvariable id="confirmRfi" type="java.lang.String"--%>
                                            <c:when test="${confirmRfi ne null && confirmRfi eq 'Y'}">
                                                <a class="btn btn-secondary" href="/bsb-web/eservice/INTERNET/MohBsbRfi?appId=<iais:mask name='rfiAppId' value='${appId}'/>">CANCEL</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="btn btn-secondary" href="/bsb-web/eservice/INTERNET/MohBSBInboxMsg">CANCEL</a>
                                                <a class="btn btn-secondary" id="draftBtn">SAVE AS DRAFT</a>
                                            </c:otherwise>
                                        </c:choose>
                                        <a class="btn btn-primary" id="saveBtn">SUBMIT</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                </div>
            </div>
        </div>
    </div>
</form>