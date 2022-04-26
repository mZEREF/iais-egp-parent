<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais" %>
<%@ page import="static sg.gov.moh.iais.egp.bsb.constant.GlobalConstants.WEB_ROOT" %>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass) request.getAttribute("process");
%>
<%--@elvariable id="bsbInspectionConfig" type="ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto"--%>
<webui:setLayout name="iais-intranet"/>
<script type="text/javascript" src="<%=WEB_ROOT%>/js/bsb/bsb-adhoc-checklist.js"></script>

<c:choose>
    <c:when test="${empty bsbInspectionConfig}">
        <tr>
            <td colspan="6">
                <iais:message key="GENERAL_ACK018" escape="true"/>
            </td>
        </tr>
    </c:when>
    <c:otherwise>
        <div class="main-content">
            <form id="mainForm" method="post" action=<%=process.runtime.continueURL()%>>
                <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
                <br><br><br>

                <span id="error_checklistItem" name="iaisErrorMsg" class="error-msg"></span>
                <div class="tab-pane" id="tabInspection" role="tabpanel">
                    <div class="center-content">
                        <%@ include file="../common/checklistInfo.jsp" %>
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="text-right text-center-mobile">
                                    <a id="cancelBtn" class="btn btn-secondary" href="#">Cancel</a>
                                    <a id="saveAdhocItemBtn" class="btn btn-secondary" href="#">Save</a>
                                    <a id="adhocItemPoolBtn" class="btn btn-primary" href="#">Add Adhoc Item</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </c:otherwise>
</c:choose>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>