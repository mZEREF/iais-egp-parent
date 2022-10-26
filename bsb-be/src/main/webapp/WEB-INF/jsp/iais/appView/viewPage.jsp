<%@page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants"%>
<%@page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT"%>
<%@taglib prefix="webui" uri="http://www.ecquaria.com/webui" %>
<%@taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet-view"/>

<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-common-data-file.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-compare-show-diff.js"></script>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-process-decision-rfi.js"></script>

<%--@elvariable id="appBasicInfo" type="sg.gov.moh.iais.egp.bsb.dto.info.common.AppBasicInfo"--%>
<%--@elvariable id="isFacility" type="java.lang.Boolean"--%>
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
                        <div class="tab-content">
                            <div class="tab-pane fade in active">
                                <div id="previewSubmitPanel" role="tabpanel">
                                    <c:if test="${MasterCodeConstants.PROCESS_TYPE_FAC_REG eq appBasicInfo.processType
                                    or MasterCodeConstants.PROCESS_TYPE_ADHOC_INSPECTION eq appBasicInfo.processType
                                    or isFacility}">
                                        <%@include file="/WEB-INF/jsp/iais/appView/facility/viewPage.jsp" %>
                                    </c:if>
                                    <c:if test="${MasterCodeConstants.APPLY_APPROVE_PROCESS_TYPES.contains(appBasicInfo.processType)}">
                                        <%@include file="/WEB-INF/jsp/iais/appView/approval/viewPage.jsp" %>
                                    </c:if>
                                    <c:if test="${MasterCodeConstants.PROCESS_TYPE_RENEW_DEFER eq appBasicInfo.processType}">
                                        <%@include file="/WEB-INF/jsp/iais/appView/deferRenew/viewPage.jsp" %>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form>