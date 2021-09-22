<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.utils.MaskUtil" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<div class="dashboard" >
    <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
        <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
        <input type="hidden" name="crud_action_type" value="">
        <input type="hidden" name="crud_action_value" value="">
        <input type="hidden" name="crud_action_additional" value="">
        <div class="main-content">
            <div class="row">
                <div class="col-lg-12 col-xs-12">
                    <div class="center-content">
                        <div class="intranet-content">
                            <iais:body >
                                <div class="col-xs-12">
                                    <div class="tab-gp dashboard-tab">
                                        <br><br><br>
                                        <ul class="nav nav-tabs hidden-xs hidden-sm" role="tablist">
                                            <li class="<c:if test="${empty preInspReport }">active</c:if><c:if test="${preInspReport == '1'}">complete</c:if>" role="presentation"><a href="#tabLicenseeDetails" aria-controls="tabLicenseeDetails" role="tab" data-toggle="tab">Licensee Details</a></li>
                                            <li class="complete" role="presentation"><a href="#tabPersonnelDetails" aria-controls="tabPersonnelDetails" role="tab"
                                                                                        data-toggle="tab">Personnel Details</a></li>
                                            <li class="<c:if test="${preInspReport == '1'}">active</c:if><c:if test="${empty preInspReport }">complete</c:if>" role="presentation"><a href="#tabComplianceHistory" aria-controls="tabComplianceHistory" role="tab"
                                                                                        data-toggle="tab">Compliance History</a></li>
                                        </ul>
                                        <div class="tab-nav-mobile visible-xs visible-sm">
                                            <div class="swiper-wrapper" role="tablist">
                                                <div class="swiper-slide"><a href="#tabLicenseeDetails" aria-controls="tabLicenseeDetails" role="tab" data-toggle="tab">Licensee Details</a></div>
                                                <div class="swiper-slide"><a href="#tabPersonnelDetails" aria-controls="tabPersonnelDetails" role="tab" data-toggle="tab">Personnel Details</a></div>
                                                <div class="swiper-slide"><a href="#tabComplianceHistory" aria-controls="tabComplianceHistory" role="tab" data-toggle="tab">Compliance History</a></div>
                                            </div>
                                            <div class="swiper-button-prev"></div>
                                            <div class="swiper-button-next"></div>
                                        </div>

                                        <div class="tab-content ">
                                            <div class="tab-pane <c:if test="${empty preInspReport }">active</c:if> " id="tabLicenseeDetails" role="tabpanel">
                                                <%@include file="licenseeDetails.jsp" %>
                                            </div>

                                            <div class="tab-pane" id="tabPersonnelDetails" role="tabpanel">
                                                <%@include file="personnelDetails.jsp" %>
                                            </div>
                                            <div class="tab-pane <c:if test="${preInspReport == '1'}">active</c:if>" id="tabComplianceHistory" role="tabpanel">
                                                <div class="panel panel-default">
                                                    <!-- Default panel contents -->
                                                    <div class="alert alert-info" role="alert"><strong>
                                                        <h4>Past Inspection Reports</h4>
                                                    </strong></div>
                                                    <div class="row">
                                                        <div class="col-xs-12">
                                                            <div class="table-gp">
                                                                <table aria-describedby="" class="table">
                                                                    <thead>
                                                                    <tr>
                                                                        <th scope="col" >&nbsp;SN</th>
                                                                        <th scope="col" >Inspection Report</th>
                                                                        <th scope="col" >Risk Tagging</th>
                                                                        <th scope="col" >Compliance Tagging</th>
                                                                        <th scope="col" >Inspection Type</th>
                                                                        <th scope="col" >Date of Inspection</th>
                                                                    </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                    <c:choose>
                                                                        <c:when test="${empty complianceHistoryDtos}">
                                                                            <tr>
                                                                                <td colspan="12">
                                                                                    <iais:message key="GENERAL_ACK018" escape="true"/>
                                                                                </td>
                                                                            </tr>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <c:forEach var="compliance" items="${complianceHistoryDtos}" varStatus="status">
                                                                                <tr>
                                                                                    <td class="row_no">&nbsp;<c:out value="${status.index + 1}"/></td>
                                                                                    <td>
                                                                                        <a href="#" onclick="doReportSearch('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,compliance.appPremCorrId)}')">Inspection Report</a>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p>${compliance.riskTag}</p>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p>${compliance.complianceTag}Compliance</p>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p>${compliance.inspectionTypeName}</p>
                                                                                    </td>
                                                                                    <td>
                                                                                        <p>${compliance.inspectionDate}<c:if test="${empty compliance.inspectionDate}">-</c:if></p>
                                                                                    </td>
                                                                                </tr>
                                                                            </c:forEach>
                                                                        </c:otherwise>
                                                                    </c:choose>

                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                    <a href="#" onclick="javascript:doBack();" ><em class="fa fa-angle-left"> </em> Back</a>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<script type="text/javascript">
    function doReportSearch(appPremCorrId){
        showWaiting(); SOP.Crud.cfxSubmit("mainForm", "report",appPremCorrId);
    }
    function doBack(){
        showWaiting(); SOP.Crud.cfxSubmit("mainForm", "back");
    }

</script>