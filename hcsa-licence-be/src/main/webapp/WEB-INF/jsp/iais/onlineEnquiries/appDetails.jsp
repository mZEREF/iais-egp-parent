<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
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
                                        <li class="active" role="presentation"><a href="#tabApplicationInfo" aria-controls="tabApplicationInfo" role="tab" data-toggle="tab">Application Info</a></li>
                                        <li class="complete" role="presentation"><a href="#tabKeyRoles" aria-controls="tabKeyRoles" role="tab"
                                                                                    data-toggle="tab">Key Roles</a></li>
                                        <li class="complete" role="presentation"><a href="#tabServiceRelated" aria-controls="tabServiceRelated" role="tab"
                                                                                    data-toggle="tab">Service Related</a></li>
                                    </ul>
                                    <div class="tab-nav-mobile visible-xs visible-sm">
                                        <div class="swiper-wrapper" role="tablist">
                                            <div class="swiper-slide"><a href="#tabApplicationInfo" aria-controls="tabApplicationInfo" role="tab" data-toggle="tab">Application Info</a></div>
                                            <div class="swiper-slide"><a href="#tabKeyRoles" aria-controls="tabKeyRoles" role="tab" data-toggle="tab">Key Roles</a></div>
                                            <div class="swiper-slide"><a href="#tabServiceRelated" aria-controls="tabServiceRelated" role="tab" data-toggle="tab">Service Related</a></div>
                                        </div>
                                        <div class="swiper-button-prev"></div>
                                        <div class="swiper-button-next"></div>
                                    </div>

                                    <div class="tab-content ">
                                        <div class="tab-pane active" id="tabApplicationInfo" role="tabpanel">

                                            <div class="panel panel-default">
                                                <!-- Default panel contents -->
                                                <div class="panel-heading"><strong>Submission Details</strong></div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table table-bordered">
                                                                <thead style="display: none">
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                <tr>
                                                                    <td class="col-xs-6" align="right">Application No</td>
                                                                    <td class="col-xs-6" style="padding-left: 15px;">${applicationViewDto.applicationDto.applicationNo}</td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right">Application Type</td>
                                                                    <td style="padding-left: 15px;">${applicationViewDto.applicationDto.applicationType}</td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right">Service Name</td>
                                                                    <td style="padding-left: 15px;"><iais:service value="${applicationViewDto.applicationDto.serviceId}"></iais:service></td>
                                                                </tr>
                                                                <c:choose>
                                                                    <c:when test="${empty serviceStep}">
                                                                        <tr>
                                                                            <td align="right">Service Discipline/Modality</td>
                                                                            <td style="padding-left: 15px;">-</td>
                                                                        </tr>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:forEach var="pool" items="${serviceStep}" >
                                                                            <tr>
                                                                                <td align="right">Service Discipline/Modality</td>
                                                                                <td class="row_no" style="padding-left: 15px;"><c:out value="${pool.scopeName}"/></td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                <tr>
                                                                    <td align="right">Submission Date</td>
                                                                    <td style="padding-left: 15px;">${applicationViewDto.submissionDate}<c:if test="${empty applicationViewDto.submissionDate}">-</c:if></td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right">Current Status</td>
                                                                    <td style="padding-left: 15px;">${applicationViewDto.currentStatus}<c:if test="${empty applicationViewDto.currentStatus}">-</c:if></td>
                                                                </tr>

                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>


                                            <div class="panel panel-default">
                                                <div class="panel-heading"><strong>Applicant Details</strong></div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table table-bordered">
                                                                <thead style="display: none">
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                <tr>
                                                                    <td class="col-xs-6" align="right">HCI Code</td>
                                                                    <td class="col-xs-6" style="padding-left: 15px;">${applicationViewDto.hciCode}<c:if test="${empty applicationViewDto.hciCode}">-</c:if></td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right">HCI Name</td>
                                                                    <td style="padding-left: 15px;">${applicationViewDto.hciName}<c:if test="${empty applicationViewDto.hciName}">-</c:if></td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right">HCI Address</td>
                                                                    <td style="padding-left: 15px;">${applicationViewDto.hciAddress}<c:if test="${empty applicationViewDto.hciAddress}">-</c:if></td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right">Telephone</td>
                                                                    <td style="padding-left: 15px;">${applicationViewDto.telephone}<c:if test="${empty applicationViewDto.telephone}">-</c:if></td>
                                                                </tr>
                                                                <tr>
                                                                    <td align="right">Email</td>
                                                                    <td style="padding-left: 15px;">${emilAddr}<c:if test="${empty emilAddr}">-</c:if></td>
                                                                </tr>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="tab-pane" id="tabKeyRoles" role="tabpanel">
                                            <div class="panel panel-default">
                                                <!-- Default panel contents -->
                                                <div class="panel-heading"><strong>Key Roles</strong></div>

                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table table-bordered">
                                                                <thead style="display: none">
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                <tr>
                                                                    <td class="col-xs-6" align="right">Licensee Name (Company)</td>
                                                                    <td class="col-xs-6" style="padding-left: 15px;">${licenseeDto.name}</td>
                                                                </tr>

                                                                <c:if test="${empty authorisedUsers}">
                                                                    <tr>
                                                                        <td align="right">Authorised Person Name</td>
                                                                        <td style="padding-left: 15px;">-</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Authorised Person ID</td>
                                                                        <td style="padding-left: 15px;">-</td>
                                                                    </tr>
                                                                </c:if>
                                                                <c:forEach var="person" items="${authorisedUsers}">
                                                                    <tr>
                                                                        <td align="right">Authorised Person Name</td>
                                                                        <td style="padding-left: 15px;">${person.displayName}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">Authorised Person ID</td>
                                                                        <td style="padding-left: 15px;">${person.identityNo} (<iais:code code="${person.idType}"/>)</td>
                                                                    </tr>
                                                                </c:forEach>
                                                                <c:if test="${empty appSvcRelatedInfoDto.appSvcMedAlertPersonList}">
                                                                    <tr>
                                                                        <td align="right">MedAlert Contact Person</td>
                                                                        <td style="padding-left: 15px;">-</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">MedAlert Contact Person ID</td>
                                                                        <td style="padding-left: 15px;">-</td>
                                                                    </tr>
                                                                </c:if>
                                                                <c:forEach var="personMap" items="${appSvcRelatedInfoDto.appSvcMedAlertPersonList}">
                                                                    <tr>
                                                                        <td align="right">MedAlert Contact Person</td>
                                                                        <td style="padding-left: 15px;">${personMap.name}</td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td align="right">MedAlert Contact Person ID</td>
                                                                        <td style="padding-left: 15px;">${personMap.idNo} (<iais:code code="${personMap.idType}"/>)</td>
                                                                    </tr>

                                                                </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <div class="tab-pane" id="tabServiceRelated" role="tabpanel">
                                            <div class="panel panel-default">
                                                <!-- Default panel contents -->
                                                <div class="panel-heading"><strong>Service Related</strong></div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <div class="table-gp">
                                                            <table aria-describedby="" class="table table-bordered">
                                                                <thead style="display: none">
                                                                <tr>
                                                                    <th scope="col"></th>
                                                                </tr>
                                                                </thead>
                                                                <tbody>
                                                                <tr>
                                                                    <td class="col-xs-6" align="right">Service Name</td>
                                                                    <td style="padding-left: 15px;"><iais:service value="${applicationViewDto.applicationDto.serviceId}"></iais:service></td>
                                                                </tr>
                                                                <c:choose>
                                                                    <c:when test="${empty serviceStep}">
                                                                        <tr>
                                                                            <td align="right">Service (Lab Discipline, Modality, Subsumed and etc)</td>
                                                                            <td style="padding-left: 15px;">-</td>
                                                                        </tr>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <c:forEach var="pool" items="${serviceStep}" >
                                                                            <tr>
                                                                                <td align="right">Service (Lab Discipline, Modality, Subsumed and etc)</td>
                                                                                <td class="row_no" style="padding-left: 15px;"><c:out value="${pool.scopeName}"/><c:if test="${empty pool.scopeName}">-</c:if></td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="panel-heading"><strong>Allocation</strong></div>
                                                <div class="row">
                                                    <div class="col-xs-12">
                                                        <table aria-describedby="" class="table">
                                                            <thead >
                                                            <tr >
                                                                <th scope="col" style="text-align: center">Mode of Service Delivery</th>
                                                                <th scope="col" style="text-align: center">Service / Granular Service</th>
                                                                <th scope="col" style="text-align: center">Clinical Governance Officers</th>
                                                                <th scope="col" style="text-align: center">Section Leader</th>
                                                            </tr>
                                                            </thead>
                                                            <tbody>
                                                            <c:choose>
                                                                <c:when test="${empty appSvcRelatedInfoDto.appSvcDisciplineAllocationDtoList}">
                                                                    <tr>
                                                                        <td colspan="7">
                                                                            <iais:message key="GENERAL_ACK018" escape="true"></iais:message>
                                                                        </td>
                                                                    </tr>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:forEach var="disciplineAllocation" items="${appSvcRelatedInfoDto.appSvcDisciplineAllocationDtoList}" varStatus="stat">
                                                                        <tr>
                                                                            <td style="text-align: center" >
                                                                                <p style="padding-left: 15px;">${disciplineAllocation.premiseVal}<c:if test="${empty disciplineAllocation.premiseVal}">-</c:if></p>
                                                                            </td>

                                                                            <td style="text-align: center">
                                                                                <p style="padding-left: 15px;">${disciplineAllocation.chkLstName}<c:if test="${empty disciplineAllocation.chkLstName}">-</c:if></p>

                                                                            </td>
                                                                            <td style="text-align: center">
                                                                                <p style="padding-left: 15px;">${disciplineAllocation.cgoSelName}<c:if test="${empty disciplineAllocation.cgoSelName}">-</c:if></p>
                                                                            </td>
                                                                            <td style="text-align: center">
                                                                                <p style="padding-left: 15px;">${disciplineAllocation.sectionLeaderName}<c:if test="${empty disciplineAllocation.sectionLeaderName}">-</c:if></p>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            </tbody>
                                                        </table>

                                                    </div>
                                                </div>
                                                <c:if test="${not empty appSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList}">
                                                    <c:forEach items="${appSvcRelatedInfoDto.appSvcKeyAppointmentHolderDtoList}" var="personnel">
                                                        <div class="panel-heading"><strong>Key Appointment Holder </strong></div>
                                                        <div class="row">
                                                            <div class="col-xs-12">
                                                                <div class="table-gp">
                                                                    <table aria-describedby="" class="table table-bordered">
                                                                        <thead style="display: none">
                                                                        <tr>
                                                                            <th scope="col"></th>
                                                                        </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                        <tr>
                                                                            <td align="right">Salutation</td>
                                                                            <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${personnel.salutation}"/><c:if test="${empty personnel.salutation}">-</c:if></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td align="right">Name</td>
                                                                            <td class="col-xs-6" style="padding-left: 15px;">${personnel.name}<c:if test="${empty personnel.name}">-</c:if></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td align="right">ID Type</td>
                                                                            <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${personnel.idType}"/><c:if test="${empty personnel.idType}">-</c:if></td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td align="right">ID No</td>
                                                                            <td class="col-xs-6" style="padding-left: 15px;">${personnel.idNo}<c:if test="${empty personnel.idNo}">-</c:if></td>
                                                                        </tr>
                                                                        <c:if test="${personnel.idType == 'IDTYPE003'}">
                                                                            <tr>
                                                                                <td align="right">Country of issuance</td>
                                                                                <td class="col-xs-6" style="padding-left: 15px;"><iais:code code="${personnel.nationality}"/><c:if test="${empty personnel.nationality}">-</c:if></td>
                                                                            </tr>
                                                                        </c:if>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>

                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <a href="#" onclick="javascript:SOP.Crud.cfxSubmit('mainForm');" ><em class="fa fa-angle-left"> </em> Back</a>
                                </div>
                            </iais:body>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>