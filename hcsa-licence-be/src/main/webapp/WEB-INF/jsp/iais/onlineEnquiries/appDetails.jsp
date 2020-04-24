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
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/WEB-INF/jsp/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div class="container">
            <div class="row">
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
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Application No</td>
                                                        <td class="col-xs-6">${applicationViewDto.applicationDto.applicationNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Application Type</td>
                                                        <td>${applicationViewDto.applicationDto.applicationType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Service Name</td>
                                                        <td><iais:service value="${applicationViewDto.applicationDto.serviceId}"></iais:service></td>
                                                    </tr>
                                                    <c:choose>
                                                        <c:when test="${empty serviceStep}">
                                                            <tr>
                                                                <td align="right">Service Discipline/Modality</td>
                                                                <td>-</td>
                                                            </tr>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:forEach var="pool" items="${serviceStep}" >
                                                                <tr>
                                                                    <td align="right">Service Discipline/Modality</td>
                                                                    <td class="row_no"><c:out value="${pool.scopeName}"/></td>
                                                                </tr>
                                                            </c:forEach>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <tr>
                                                        <td align="right">Submission Date</td>
                                                        <td>${applicationViewDto.submissionDate}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Current Status</td>
                                                        <td>${applicationViewDto.currentStatus}</td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><strong>Applicant Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">HCI Code</td>
                                                        <td class="col-xs-6">${applicationViewDto.hciCode}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI Name</td>
                                                        <td>${applicationViewDto.hciName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">HCI Address</td>
                                                        <td>${applicationViewDto.hciAddress}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Telephone</td>
                                                        <td>${applicationViewDto.telephone}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email</td>
                                                        <td><c:out value="${licenseeDto.emilAddr}"/></td>
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
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Licensee Name (Company)</td>
                                                        <td class="col-xs-6"> ${licenseeDto.name}</td>
                                                    </tr>
                                                    <c:forEach var="person" items="${licenseeKeyApptPersonDtos}">--%>
                                                        <tr>
                                                            <td align="right">Authorised Person Name</td>
                                                            <td> ${person.name}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Authorised Person ID</td>
                                                            <td> ${person.idNo} (${personPo.idType})</td>
                                                        </tr>

                                                    </c:forEach>
<%--                                                    <c:forEach var="personPo" items="${appSvcRelatedInfoDto.appSvcPrincipalOfficersDtoList}">--%>
<%--                                                        <tr>--%>
<%--                                                            <td align="right">Authorised Person Name</td>--%>
<%--                                                            <td> ${personPo.name}</td>--%>
<%--                                                        </tr>--%>
<%--                                                        <tr>--%>
<%--                                                            <td align="right">Authorised Person ID</td>--%>
<%--                                                            <td> ${personPo.idNo} (${personPo.idType})</td>--%>
<%--                                                        </tr>--%>

<%--                                                    </c:forEach>--%>
<%--                                                    <c:forEach var="personCgo" items="${appSvcRelatedInfoDto.appSvcCgoDtoList}">--%>
<%--                                                        <tr>--%>
<%--                                                            <td align="right">Authorised Person Name</td>--%>
<%--                                                            <td> ${personCgo.name}</td>--%>
<%--                                                        </tr>--%>
<%--                                                        <tr>--%>
<%--                                                            <td align="right">Authorised Person ID</td>--%>
<%--                                                            <td> ${personCgo.idNo} (${personCgo.idType})</td>--%>
<%--                                                        </tr>--%>

<%--                                                    </c:forEach>--%>
<%--                                                    <c:forEach var="personMap" items="${appSvcRelatedInfoDto.appSvcMedAlertPersonList}">--%>
<%--                                                        <tr>--%>
<%--                                                            <td align="right">MedAlert Contact Person</td>--%>
<%--                                                            <td> ${personMap.name}</td>--%>
<%--                                                        </tr>--%>
<%--                                                        <tr>--%>
<%--                                                            <td align="right">MedAlert Contact Person ID</td>--%>
<%--                                                            <td> ${personMap.idNo} (${personMap.idType})</td>--%>
<%--                                                        </tr>--%>

<%--                                                    </c:forEach>--%>

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
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Service Name</td>
                                                        <td><iais:service value="${applicationViewDto.applicationDto.serviceId}"></iais:service></td>
                                                    </tr>
                                                    <c:choose>
                                                        <c:when test="${empty serviceStep}">
                                                            <tr>
                                                                <td align="right">Service (Lab Discipline, Modality, Subsumed and etc)</td>
                                                                <td>-</td>
                                                            </tr>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:forEach var="pool" items="${serviceStep}" >
                                                                <tr>
                                                                    <td align="right">Service (Lab Discipline, Modality, Subsumed and etc)</td>
                                                                    <td class="row_no"><c:out value="${pool.scopeName}"/></td>
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
                                            <table class="table">
                                                <thead >
                                                <tr >
                                                    <th  style="text-align: center">Premises</th>
                                                    <th  style="text-align: center">Service / Granular Service</th>
                                                    <th  style="text-align: center">Clinical Governance Officers</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <c:forEach var="disciplineAllocation" items="${appSvcRelatedInfoDto.appSvcDisciplineAllocationDtoList}" varStatus="stat">
                                                    <tr>
                                                        <td style="text-align: center" >
                                                            <p>${disciplineAllocation.premiseVal}</p>
                                                        </td>

                                                        <td style="text-align: center">
                                                            <p>${disciplineAllocation.chkLstName}</p>

                                                        </td>
                                                        <td style="text-align: center">
                                                            <p>${disciplineAllocation.cgoSelName}</p>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <a  onclick="javascript:SOP.Crud.cfxSubmit('mainForm');" >< Back</a>
    </div>
</form>