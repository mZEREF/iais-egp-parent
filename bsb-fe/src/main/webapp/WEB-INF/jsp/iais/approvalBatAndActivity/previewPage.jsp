<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@ taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="approvalApp" tagdir="/WEB-INF/tags/approvalApp" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-approval-bat-and-activity.js"></script>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp"%>
<%@include file="dashboard.jsp"%>
<form method="post" id="mainForm" action="<%=process.runtime.continueURL()%>">
    <input type="hidden" name="sopEngineTabRef" value="<%=process.rtStatus.getTabRef()%>">
    <input type="hidden" name="action_type" value="">
    <input type="hidden" name="action_value" value="">
    <input type="hidden" name="action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
                <div class="col-xs-12">
                    <div class="tab-gp steps-tab">
                        <%@ include file="InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewPanel" role="tabpanel">
                                    <approvalApp:preview facProfileDto="${facProfileDto}" approvalToPossessDto="${batInfo}"
                                                         approvalProfileListEditJudge="true" docEditJudge="true">
                                        <jsp:attribute name="editFrag"><a href="#" data-step-key="REPLACE-STEP-KEY"><em class="fa fa-pencil-square-o"></em>Edit</a></jsp:attribute>
                                        <jsp:attribute name="docFrag">
                                            <fac:doc-preview docSettings="${docSettings}" savedFiles="${savedFiles}" newFiles="${newFiles}"/>
                                        </jsp:attribute>
                                    </approvalApp:preview>
                                    <div class="form-horizontal" style="padding: 30px 20px 10px;">
                                        <div class="form-group ">
                                            <div class="col-sm-5 control-label">
                                                <label for="remarks">Remarks</label>
                                            </div>
                                            <div class="col-sm-6 col-md-7">
                                                <textarea class="col-xs-12" name="remarks" id="remarks" rows="5"><c:out value="${previewDto.remarks}"/></textarea>
                                                <span data-err-ind="remarks" class="error-msg"></span>
                                            </div>
                                        </div>
                                        <div class="form-group ">
                                            <div class="col-xs-1" style="padding: 30px 0 20px 30px;">
                                                <input type="checkbox" name="require" id="require" value="Y" <c:if test="${previewDto.require eq 'Y'}">checked = checked</c:if>/>
                                            </div>
                                            <div class="col-xs-10 control-label" style="padding: 30px 0">
                                                <label for="require" >Declaration of compliance with MOH requirements including those stipulatedin the checklist</label>
                                                <span data-err-ind="require" class="error-msg"></span>
                                            </div>
                                        </div>

                                        <div class="form-group ">
                                            <div class="col-xs-1" style="padding: 30px 0 0 30px;">
                                                <input type="checkbox" name="accuracy" id="accuracy" value="Y" <c:if test="${previewDto.accuracy eq 'Y'}">checked = checked</c:if>/>
                                            </div>
                                            <div class="col-xs-10 control-label" style="padding: 30px 0">
                                                <label for="accuracy">Declaration on the accuracy of submission</label>
                                                <span data-err-ind="accuracy" class="error-msg"></span>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                                <%@ include file="InnerFooter.jsp" %>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp"%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>