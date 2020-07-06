<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
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
                            <li class="complete" role="presentation"><a href="#tabLicenseeDetails" aria-controls="tabLicenseeDetails" role="tab" data-toggle="tab">Licensee Details</a></li>
                            <li class="complete" role="presentation"><a href="#tabPersonnelDetails" aria-controls="tabPersonnelDetails" role="tab"
                                                                        data-toggle="tab">Personnel Details</a></li>
                            <li class="active" role="presentation"><a href="#tabInspectionReport" aria-controls="tabInspectionReport" role="tab"
                                                                      data-toggle="tab">Inspection Report</a></li>
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
                            <div class="tab-pane" id="tabLicenseeDetails" role="tabpanel">

                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><strong>Licensee Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Licensee Type</td>
                                                        <td class="col-xs-6">&nbsp;${organizationLicDto.licenseeDto.licenseeType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">UEN</td>
                                                        <td>&nbsp;${organizationLicDto.uenNo}<c:if test="${empty organizationLicDto.uenNo}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Are you registered with ACRA/ROS</td>
                                                        <td>&nbsp;wait UEN API</td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>


                                <div class="panel panel-default">
                                    <div class="panel-heading"><strong>Company Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Company Name</td>
                                                        <td class="col-xs-6">&nbsp;${organizationLicDto.licenseeDto.name}<c:if test="${empty organizationLicDto.licenseeDto.name}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Postal code</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.postalCode}<c:if test="${empty organizationLicDto.licenseeDto.postalCode}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Block No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.blkNo}<c:if test="${empty organizationLicDto.licenseeDto.blkNo}">-</c:if></td>
                                                    </tr>

                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.unitNo}<c:if test="${empty organizationLicDto.licenseeDto.unitNo}">-</c:if></td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.streetName}<c:if test="${empty organizationLicDto.licenseeDto.streetName}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.buildingName}<c:if test="${empty organizationLicDto.licenseeDto.buildingName}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Number</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeEntityDto.officeTelNo}<c:if test="${empty organizationLicDto.licenseeDto.officeTelNo}">-</c:if></td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <c:forEach var="member" items="${organizationLicDto.licenseeKeyApptPersonDtos}" varStatus="status">

                                    <div class="panel panel-default">
                                        <div class="panel-heading"><strong>Board Members</strong></div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="table-gp">
                                                    <table class="table table-bordered">
                                                        <tbody>
                                                        <tr>
                                                            <td class="col-xs-6" align="right">Name</td>
                                                            <td class="col-xs-6">&nbsp;${member.name}<c:if test="${empty member.name}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Salutation</td>
                                                            <td>&nbsp;${member.salutation}<c:if test="${empty member.salutation}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID Type</td>
                                                            <td>&nbsp;${member.idType}<c:if test="${empty member.idType}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID No</td>
                                                            <td>&nbsp;${member.idNo}<c:if test="${empty member.idNo}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Designation</td>
                                                            <td>&nbsp;${member.designation}<c:if test="${empty member.designation}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Designation Appointment Date</td>
                                                            <td>&nbsp;<fmt:formatDate value="${member.apptDt}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /><c:if test="${empty member.apptDt}">-</c:if></td>
                                                        </tr>

                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>


                                <div class="panel panel-default">
                                    <div class="panel-heading"><strong>Sole Proprietor - Licensee</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Name</td>
                                                        <td class="col-xs-6">&nbsp;${organizationLicDto.licenseeDto.name}<c:if test="${empty organizationLicDto.licenseeDto.name}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeIndividualDto.salutation}<c:if test="${empty organizationLicDto.licenseeIndividualDto.salutation}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeIndividualDto.idType}<c:if test="${empty organizationLicDto.licenseeIndividualDto.idType}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeIndividualDto.idNo}<c:if test="${empty organizationLicDto.licenseeIndividualDto.idNo}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Postal code</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.postalCode}<c:if test="${empty organizationLicDto.licenseeDto.postalCode}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Address Type</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.addrType}<c:if test="${empty organizationLicDto.licenseeDto.addrType}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Block No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.blkNo}<c:if test="${empty organizationLicDto.licenseeDto.blkNo}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.floorNo}<c:if test="${empty organizationLicDto.licenseeDto.floorNo}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.unitNo}<c:if test="${empty organizationLicDto.licenseeDto.unitNo}">-</c:if></td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.streetName}<c:if test="${empty organizationLicDto.licenseeDto.streetName}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeDto.buildingName}<c:if test="${empty organizationLicDto.licenseeDto.buildingName}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Mobile No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeIndividualDto.mobileNo}<c:if test="${empty organizationLicDto.licenseeIndividualDto.mobileNo}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Telephone No</td>
                                                        <td>&nbsp;${organizationLicDto.licenseeEntityDto.officeTelNo}<c:if test="${empty organizationLicDto.licenseeEntityDto.officeTelNo}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email Address</td>
                                                        <td>&nbsp;<c:out value="${organizationLicDto.licenseeEntityDto.officeEmailAddr}"/><c:if test="${empty organizationLicDto.licenseeEntityDto.officeEmailAddr}">-</c:if></td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="tab-pane" id="tabPersonnelDetails" role="tabpanel">

                                <!-- Default panel contents -->
                                <c:forEach var="personnel" items="${personnelsDto}">
                                    <div class="panel panel-default">
                                        <div class="panel-heading"><strong>${personnel.licKeyPersonnelDto.psnType}</strong></div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="table-gp">
                                                    <table class="table table-bordered">
                                                        <tbody>
                                                        <tr>
                                                            <td class="col-xs-6" align="right">Name</td>
                                                            <td class="col-xs-6">&nbsp;${personnel.keyPersonnelDto.name}<c:if test="${empty personnel.keyPersonnelDto.name}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Salutation</td>
                                                            <td>&nbsp;${personnel.keyPersonnelDto.salutation}<c:if test="${empty personnel.keyPersonnelDto.salutation}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID Type</td>
                                                            <td>&nbsp;${personnel.keyPersonnelDto.idType}<c:if test="${empty personnel.keyPersonnelDto.idType}">-</c:if></td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID No</td>
                                                            <td>&nbsp;${personnel.keyPersonnelDto.idNo}<c:if test="${empty personnel.keyPersonnelDto.idNo}">-</c:if></td>
                                                        </tr>
                                                        <c:if test="${personnel.licKeyPersonnelDto.psnType!='MedAlert'}">
                                                            <tr>
                                                                <td align="right">Designation</td>
                                                                <td>&nbsp;${personnel.keyPersonnelDto.designation}<c:if test="${empty personnel.keyPersonnelDto.designation}">-</c:if></td>
                                                            </tr>
                                                        </c:if>
                                                        <c:if test="${personnel.licKeyPersonnelDto.psnType=='Clinical Governance Officer'}">
                                                            <tr>
                                                                <td align="right">Professional Type</td>
                                                                <td>&nbsp;${personnel.keyPersonnelExtDto.professionType}<c:if test="${empty personnel.keyPersonnelExtDto.professionType}">-</c:if></td>
                                                            </tr>
                                                            <tr>
                                                                <td align="right">Professional Regn No</td>
                                                                <td>&nbsp;${personnel.keyPersonnelExtDto.profRegNo}<c:if test="${empty personnel.keyPersonnelExtDto.profRegNo}">-</c:if></td>
                                                            </tr>
                                                            <tr>
                                                                <td align="right">Specialty</td>
                                                                <td>&nbsp;${personnel.keyPersonnelExtDto.speciality}<c:if test="${empty personnel.keyPersonnelExtDto.speciality}">-</c:if></td>
                                                            </tr>
                                                            <tr>
                                                                <td align="right">Subspecialty or relevant qualification</td>
                                                                <td>&nbsp;${personnel.keyPersonnelExtDto.subSpeciality}<c:if test="${empty personnel.keyPersonnelExtDto.subSpeciality}">-</c:if></td>
                                                            </tr>
                                                        </c:if>
                                                        <tr>
                                                            <td align="right">Mobile No</td>
                                                            <td>&nbsp;${personnel.keyPersonnelDto.mobileNo}<c:if test="${empty personnel.keyPersonnelDto.mobileNo}">-</c:if></td>
                                                        </tr>
                                                        <c:if test="${personnel.licKeyPersonnelDto.psnType!='MedAlert' && personnel.licKeyPersonnelDto.psnType!='Clinical Governance Officer'}">
                                                            <tr>
                                                                <td align="right">Office Telephone No</td>
                                                                <td>&nbsp;${personnel.keyPersonnelDto.officeTelNo}<c:if test="${empty personnel.keyPersonnelDto.officeTelNo}">-</c:if></td>
                                                            </tr>
                                                        </c:if>
                                                        <tr>
                                                            <td align="right">Email Address</td>
                                                            <td>&nbsp;${personnel.keyPersonnelDto.emailAddr}<c:if test="${empty personnel.keyPersonnelDto.emailAddr}">-</c:if></td>
                                                        </tr>
                                                        <c:if test="${personnel.licKeyPersonnelDto.psnType=='MedAlert'}">
                                                            <tr>
                                                                <td align="right">Preferred Mode of Receiving MedAlert</td>
                                                                <td>&nbsp;${personnel.keyPersonnelExtDto.preferredMode}<c:if test="${empty personnel.keyPersonnelExtDto.preferredMode}">-</c:if></td>
                                                            </tr>
                                                        </c:if>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>


                                </c:forEach>
                            </div>
                            <div class="tab-pane active" id="tabInspectionReport" role="tabpanel">
                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4>Past Inspection Reports</h4>
                                        </strong>
                                    </div>
                                    <input type="hidden" name="confirmAction" value="">

                                    <%--        <div class="row">--%>
                                    <div class="panel-heading" role="alert">
                                        <strong>
                                            Section A (HCI Details)
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Licence No.</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;<c:out value="${insRepDto.licenceNo}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Service Name</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;<c:out value="${insRepDto.serviceName}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;HCI Code</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;<c:out value="${insRepDto.hciCode}"/><c:if test="${empty insRepDto.hciCode}">-</c:if></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;HCI Name</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;<c:out value="${insRepDto.hciName}"/><c:if test="${empty insRepDto.hciName}">-</c:if></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;HCI Address</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;<c:out value="${insRepDto.hciAddress}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Licensee Name</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;<c:out value="${insRepDto.licenseeName}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Principal Officers</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${ not empty insRepDto.principalOfficers}">
                                                                <p>
                                                                    <c:forEach items="${insRepDto.principalOfficers}" var="poName">
                                                                        &nbsp;<c:out value="${poName}"/><br>
                                                                    </c:forEach>
                                                                </p>
                                                            </c:if>
                                                            <c:if test="${ empty insRepDto.principalOfficers}">-</c:if>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Subsumed Services</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                                                                <c:forEach var="service" items="${insRepDto.subsumedServices}">
                                                                    <p>&nbsp;<c:out value="${service}"></c:out></p>
                                                                </c:forEach>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading" role="alert">
                                        <strong>
                                            Section B (Type of Inspection)
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Date of Inspection</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            &nbsp;<fmt:formatDate value="${insRepDto.inspectionDate}" pattern="dd/MM/yyyy"></fmt:formatDate> <c:if test="${empty insRepDto.inspectionDate}">-</c:if>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Time of Inspection</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            &nbsp;<c:out value="${insRepDto.inspectionStartTime}"></c:out>-<c:out value="${insRepDto.inspectionEndTime}"></c:out>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Reason for Visit</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;${insRepDto.reasonForVisit}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Inspected By</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${not empty insRepDto.inspectors}">
                                                                <p><c:forEach items="${insRepDto.inspectors}" var="inspector" varStatus="status">
                                                                    &nbsp;<c:out value="${inspector}"/>
                                                            </c:forEach></p>
                                                            </c:if>
                                                            <c:if test="${empty insRepDto.inspectors}">&nbsp;-</c:if>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Other Inspection Officer</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${not empty insRepDto.inspectOffices}">
                                                                <p><c:forEach items="${insRepDto.inspectOffices}" var="ioName">
                                                                    &nbsp;<c:out value="${ioName}"/><br>
                                                                </c:forEach></p>
                                                            </c:if>
                                                            <c:if test="${ empty insRepDto.inspectOffices}">&nbsp;-</c:if>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Reported By</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;${insRepDto.reportedBy}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Report Noted By</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;${insRepDto.reportNoteBy}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading" role="alert">
                                        <strong>
                                            Section C (Inspection Findings)
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <div class="text">
                                                    <p><strong><span>&nbsp;Part I: Inspection Checklist</span></strong></p>
                                                </div>
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Checklist Used</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;${insRepDto.serviceName}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                                <div class="text">
                                                    <p><strong><span>&nbsp;Part II: Findings</span></strong></p>
                                                </div>
                                                <div class="table-gp">
                                                    <table class="table" >
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;Remarks</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;${insRepDto.taskRemarks}</p>
                                                            </td>
                                                            <td class="col-xs-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;Marked for Audit</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;<c:out value="${insRepDto.markedForAudit}"/>&nbsp;&nbsp;<fmt:formatDate value="${insRepDto.tcuDate}" pattern="dd/MM/yyyy"/></p>
                                                            </td>
                                                            <td class="col-xs-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;Recommended Best Practices</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;${insRepDto.bestPractice}</p>
                                                            </td>
                                                            <td class="col-xs-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;Non-Compliances</p>
                                                            </td>
                                                            <td colspan="2" class="col-xs-8">
                                                                <c:if test="${insRepDto.ncRegulation != null && not empty insRepDto.ncRegulation}">
                                                                    <table class="table">
                                                                        <thead>
                                                                        <tr>
                                                                            <th>SN</th>
                                                                            <th>Checklist Item</th>
                                                                            <th>Regulation Clause</th>
                                                                        </tr>
                                                                        </thead>
                                                                        <tbody>
                                                                        <c:forEach items="${insRepDto.ncRegulation}" var="ncRegulations"
                                                                                   varStatus="status">
                                                                            <tr>
                                                                                <td>
                                                                                    <p>&nbsp;<c:out value="${status.count}"/></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p>&nbsp;<c:out value="${ncRegulations.nc}"/></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p>&nbsp;<c:out value="${ncRegulations.regulation}"/></p>
                                                                                </td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                        </tbody>
                                                                    </table>
                                                                </c:if>
                                                                <c:if test="${insRepDto.ncRegulation == null}">
                                                                    <p>&nbsp;0</p>
                                                                </c:if>
                                                            </td>
                                                            <td class="col-xs-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;Status</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;${insRepDto.status}</p>
                                                            </td>
                                                            <td class="col-xs-4"></td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;Risk Level </p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                &nbsp;${insRepDto.riskLevel}<c:if test="${empty insRepDto.riskLevel}">&nbsp;-</c:if>
                                                            </td>
                                                            <td class="col-xs-4"></td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading" role="alert">
                                        <strong>
                                            Section D (Rectification)
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Rectified</p>
                                                        </td>
                                                        <td colspan="2" class="col-xs-8">
                                                            <c:if test="${insRepDto.ncRectification != null}">
                                                                <table class="table">
                                                                    <thead>
                                                                    <tr>
                                                                        <th>SN</th>
                                                                        <th>Checklist Item</th>
                                                                        <th>Rectified?</th>
                                                                    </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                    <c:forEach items="${insRepDto.ncRectification}" var="ncRectification"
                                                                               varStatus="status">
                                                                        <tr>
                                                                            <td>
                                                                                <p>&nbsp;<c:out value="${status.count}"></c:out></p>
                                                                            </td>
                                                                            <td>
                                                                                <p>&nbsp;<c:out value="${ncRectification.nc}"></c:out></p>
                                                                            </td>
                                                                            <td>
                                                                                <p>&nbsp;<c:out value="${ncRectification.rectified}"></c:out></p>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                    </tbody>
                                                                </table>
                                                            </c:if>
                                                            <c:if test="${insRepDto.ncRectification == null}">
                                                                <p>&nbsp;NA</p>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Remarks</p>
                                                        </td>
                                                        <div>
                                                            <td class="col-xs-4">
                                                                <p>&nbsp;<c:out value="${insRepDto.inspectypeRemarks}"></c:out></p>
                                                            </td>
                                                        </div>
                                                        <td class="col-xs-4"></td>
                                                    </tr>

                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Rectified Within KPI?</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;<c:out value="${insRepDto.rectifiedWithinKPI}"></c:out></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading" role="alert">
                                        <strong>
                                            Section E (Recommendations)
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Recommendation </p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            &nbsp;${appPremisesRecommendationDto.recommendation}<c:if test="${empty appPremisesRecommendationDto.recommendation}">-</c:if>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="period" >
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Period </p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            &nbsp;${appPremisesRecommendationDto.period}
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="selfPeriod" >
                                                        <td class="col-xs-4">
                                                            <p>&nbsp;Other Period </p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            &nbsp;${appPremisesRecommendationDto.recomInNumber} ${appPremisesRecommendationDto.chronoUnit}
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
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
        </div>
        <a  onclick="javascript:SOP.Crud.cfxSubmit('mainForm');" ><em class="fa fa-angle-left"> </em> Back</a>
    </div>
</form>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<script type="text/javascript">

    function insRepsubmit() {
        $("#mainForm").submit();
    }

    function changePeriod(obj) {
        if (obj == "Others") {
            $("#selfPeriod").show();
        } else {
            $("#selfPeriod").hide();
            //$("#period").hide();
        }
    }

    function changeRecommendation(obj) {
        if (obj == "IRE001" || obj == "IRE002") {
            $("#period").show();
        } else {
            $("#period").hide();
            $("#selfPeriod").hide();
            $("#period").hide();
        }
    }



    $(document).ready(function () {
        if ($("#recommendation").val() == "IRE001") {
            changeRecommendation("IRE001");
        }
        if ($("#recommendation").val() == "IRE002") {
            changeRecommendation("IRE002");
        }
        if ($("#periods").val() == "Others") {
            changePeriod("Others");
        }

    });

</script>