<%@tag description="Other application info tag of facility registration" %>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="iais-bsb" uri="http://www.ecq.com/iais-bsb" %>
<%@taglib prefix="fac" tagdir="/WEB-INF/tags/facility" %>

<%@attribute name="classification" required="true" type="java.lang.String" %>
<%@attribute name="activities" required="false" type="java.util.List<java.lang.String>" %>
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

<iais-bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>

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
                                                        <fac:supportingDocInfo classification="${classification}" activities="${activities}" masterCodeConstants="${masterCodeConstants}"/>
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