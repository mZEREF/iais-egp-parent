<%@tag description="Other application info tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="configList" required="true" type="java.util.List<sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo>" %>
<%@attribute name="answerMap" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>
<%@attribute name="answerErrorMap" required="true" type="java.util.Map<java.lang.String, java.lang.String>" %>
<%@attribute name="specialJsFrag" fragment="true" %>
<%@attribute name="dashboardFrag" fragment="true" %>
<%@attribute name="innerFooterFrag" fragment="true" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-internet"/>

<link href="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/css/bsb/bsb-common.css" rel="stylesheet"/>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT%>/js/bsb/bsb-common-node-group.js"></script>
<jsp:invoke fragment="specialJsFrag"/>

<%@include file="/WEB-INF/jsp/iais/include/showErrorMsg.jsp" %>
<jsp:invoke fragment="dashboardFrag"/>
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
                        <%@include file="/WEB-INF/jsp/iais/mainAppCommon/facRegistration/InnerNavTab.jsp" %>
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="otherAppInfoPanel">
                                    <div id="declarationPanel" role="tabpanel">
                                        <div class="panel panel-default">
                                            <div class="panel-heading" role="tab">
                                                <h4 class="panel-title" style="font-weight: bold">Declaration</h4>
                                            </div>
                                            <div class="panel-collapse">
                                                <div class="panel-body">
                                                    <div class="col-xs-12 form-group">
                                                        <h4 style="font-size: 16px">I, hereby declare the following:</h4>
                                                        <br/>
                                                        <ol style="padding-left: 16px">
                                                            <c:forEach var="item" items="${configList}">
                                                                <c:set var="maskedId"><iais:mask name="declaration" value="${item.id}"/></c:set>
                                                                <li class="col-xs-12" style="padding-left:0">
                                                                    <div class="col-xs-8 form-group" style="padding-left: 0">${item.statement}</div>
                                                                    <div class="form-check col-xs-2">
                                                                        <input class="form-check-input" type="radio" name="MID${maskedId}" id="MID${maskedId}Yes" value="Y" <c:if test="${'Y' eq answerMap.get(item.id)}">checked="checked"</c:if>>
                                                                        <label for="MID${maskedId}Yes" class="form-check-label"><span class="check-circle"></span>Yes</label>
                                                                    </div>
                                                                    <div class="form-check col-xs-2">
                                                                        <input class="form-check-input" type="radio" name="MID${maskedId}" id="MID${maskedId}No" value="N" <c:if test="${'N' eq answerMap.get(item.id)}">checked="checked"</c:if>>
                                                                        <label for="MID${maskedId}No" class="form-check-label"><span class="check-circle"></span>No</label>
                                                                    </div>
                                                                    <span class="error-msg col-xs-12 form-group">${answerErrorMap.get(item.id)}</span>
                                                                </li>
                                                            </c:forEach>
                                                        </ol>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="otherInfoPanel" role="tabpanel">
                                        <div class="panel panel-default">
                                            <div class="panel-heading" role="tab">
                                                <h4 class="panel-title" style="font-weight: bold">Other Information</h4>
                                            </div>
                                            <div class="panel-collapse">
                                                <div class="panel-body">
                                                    <div class="col-xs-12 form-group">
                                                        <p>The following is a non-exhaustive list of supporting documents that the facility is required to provide for the application. Some of these may not be available at point of application submission but must be provided subsequently, when available. Please note that incomplete submissions may result in delays to processing or rejection of the application.</p>
                                                        <span style="text-decoration: underline; font-weight: bold">Supporting Documents</span>
                                                        <ol class="no-margin-list" style="padding-left: 20px">
                                                            <li>Application letter containing the following information:
                                                                <ul class="no-margin-list">
                                                                    <li>The name of the Facility Operator designee (hyperlink to BATA FO responsibilities);</li>
                                                                    <li>Address of the facility where the intended work will be conducted;</li>
                                                                    <li>The reason for the application; and</li>
                                                                    <li>The justification of how and why the work involving the biological agent and/or toxin can be carried out safely and securely in the intended facility. This may include facility design, the use of laboratory safety equipment, personal protective equipment, good microbiological practices and procedures, as well as reliable and competent personnel.</li>
                                                                </ul>
                                                            </li>
                                                            <li>Details of the facility's biorisk management programme.</li>
                                                            <li>Documentation of approval from the Biosafety Committee for the intended work.</li>
                                                            <li>Documentation of endorsement from the Genetic Modification Advisory Committee (if the intended work involves genetic modification of microorganism(s) or handling of genetically modified microorganism(s).</li>
                                                            <li>Documentation of successful completion of the required biosafety training for the Biosafety Coordinator.</li>
                                                            <li>Facility Administrative Oversight Plan.</li>
                                                            <li>Facility layout/floorplan.</li>
                                                            <li>Gazette Order (if the facility is a Protected Place under the Infrastructure Protection Act).</li>
                                                            <li>List of all location(s) in the facility where the biological agent(s)/toxin(s) will be handled (including storage) and specify the corresponding work activities that will be carried out at each location (mapped to facility floorplan, as provided in #7). The information can be provided in a table format.</li>
                                                            <li>Risk assessments for the intended work conducted/reviewed/endorsed by the Biosafety Committee.</li>
                                                            <li>Safety and security records related to facility certification, inspection, accreditation, if any.</li>
                                                        </ol>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <jsp:invoke fragment="innerFooterFrag"/>
                            </div>
                        </div>
                        <%@include file="/WEB-INF/jsp/iais/include/jumpAfterDraft.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>