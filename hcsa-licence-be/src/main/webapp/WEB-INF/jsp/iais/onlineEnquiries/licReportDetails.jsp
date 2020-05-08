<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.AppConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
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
                                                        <td class="col-xs-6">${organizationLicDto.licenseeDto.licenseeType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">UEN</td>
                                                        <td>${organizationLicDto.uenNo}</td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><strong>Company Details</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Company Name</td>
                                                        <td class="col-xs-6">${organizationLicDto.licenseeDto.name}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Postal code</td>
                                                        <td>${organizationLicDto.licenseeDto.postalCode}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Block No</td>
                                                        <td>${organizationLicDto.licenseeDto.blkNo}</td>
                                                    </tr>

                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>${organizationLicDto.licenseeDto.unitNo}</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>${organizationLicDto.licenseeDto.streetName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>${organizationLicDto.licenseeDto.buildingName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Number</td>
                                                        <td>${organizationLicDto.licenseeEntityDto.officeTelNo}</td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <c:forEach var="member" items="${organizationLicDto.licenseeKeyApptPersonDtos}" varStatus="status">
                                    <div>&nbsp</div>
                                    <div class="panel panel-default">
                                        <div class="panel-heading"><strong>Board Members</strong></div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="table-gp">
                                                    <table class="table table-bordered">
                                                        <tbody>
                                                        <tr>
                                                            <td class="col-xs-6" align="right">Name</td>
                                                            <td class="col-xs-6">${member.name}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Salutation</td>
                                                            <td>${member.salutation}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID Type</td>
                                                            <td>${member.idType}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID No</td>
                                                            <td>${member.idNo}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Designation</td>
                                                            <td>${member.designation}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Designation Appointment Date</td>
                                                            <td><fmt:formatDate value="${member.apptDt}" pattern="${AppConsts.DEFAULT_DATE_FORMAT}" /></td>
                                                        </tr>

                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>

                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><strong>Sole Proprietor - Licensee</strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Name</td>
                                                        <td class="col-xs-6">${organizationLicDto.licenseeDto.name}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>${organizationLicDto.licenseeIndividualDto.salutation}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>${organizationLicDto.licenseeIndividualDto.idType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>${organizationLicDto.licenseeIndividualDto.idNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Postal code</td>
                                                        <td>${organizationLicDto.licenseeDto.postalCode}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Address Type</td>
                                                        <td>${organizationLicDto.licenseeDto.addrType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Block No</td>
                                                        <td>${organizationLicDto.licenseeDto.blkNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>${organizationLicDto.licenseeDto.floorNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>${organizationLicDto.licenseeDto.unitNo}</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>${organizationLicDto.licenseeDto.streetName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>${organizationLicDto.licenseeDto.buildingName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Mobile No</td>
                                                        <td>${organizationLicDto.licenseeIndividualDto.mobileNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Telephone No</td>
                                                        <td>${organizationLicDto.licenseeEntityDto.officeTelNo}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email Address</td>
                                                        <td>${organizationLicDto.licenseeEntityDto.officeEmailAddr}</td>
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
                                                            <td class="col-xs-6">${personnel.keyPersonnelDto.name}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Salutation</td>
                                                            <td>${personnel.keyPersonnelDto.salutation}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID Type</td>
                                                            <td>${personnel.keyPersonnelDto.idType}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID No</td>
                                                            <td>${personnel.keyPersonnelDto.idNo}</td>
                                                        </tr>

                                                        <tr>
                                                            <td align="right">Designation</td>
                                                            <td>${personnel.keyPersonnelDto.designation}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Professional Type</td>
                                                            <td>${personnel.keyPersonnelExtDto.professionType}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Professional Regn No</td>
                                                            <td>${personnel.keyPersonnelExtDto.profRegNo}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Specialty</td>
                                                            <td>${personnel.keyPersonnelExtDto.speciality}</td>
                                                        </tr><tr>
                                                            <td align="right">SubSpeciality or relevant qualification</td>
                                                            <td>${personnel.keyPersonnelExtDto.subSpeciality}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Mobile No</td>
                                                            <td>${personnel.keyPersonnelDto.mobileNo}</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Email Address</td>
                                                            <td>${personnel.keyPersonnelDto.emailAddr}</td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div>&nbsp</div>
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
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4 style="border-bottom: none">Section A (HCI Details)</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Licence No.</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><c:out value="${insRepDto.licenceNo}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Service Name</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><c:out value="${insRepDto.serviceName}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>HCI Code</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><c:out value="${insRepDto.hciCode}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>HCI Name</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><c:out value="${insRepDto.hciName}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>HCI Address</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><c:out value="${insRepDto.hciAddress}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Licensee Name</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><c:out value="${insRepDto.licenseeName}"/></p>
                                                        </td>
                                                        <td class="col-xs-4"/>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Principal Officers</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${insRepDto.principalOfficers != null && not empty insRepDto.principalOfficers}">
                                                                <p><c:forEach items="${insRepDto.principalOfficers}" var="poName">
                                                                    <c:out value="${poName}"/><br>
                                                                </c:forEach></p>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-xs-4"/>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Subsumed Services</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                                                                <c:forEach var="service" items="${insRepDto.subsumedServices}">
                                                                    <p><c:out value="${service}"></c:out></p>
                                                                </c:forEach>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-xs-4"/>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4 style="border-bottom: none">Section B (Type of Inspection)</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Date of Inspection</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <fmt:formatDate value="${insRepDto.inspectionDate}"
                                                                            pattern="dd/MM/yyyy"></fmt:formatDate>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Time of Inspection</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <c:out value="${insRepDto.inspectionStartTime}"></c:out>-<c:out value="${insRepDto.inspectionEndTime}"></c:out>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Reason for Visit</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>${insRepDto.reasonForVisit}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Inspected By</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${insRepDto.inspectors != null && not empty insRepDto.inspectors}">
                                                                <p><c:forEach items="${insRepDto.inspectors}" var="inspector" varStatus="status">
                                                                <p><c:out value="${inspector}"></c:out></p>
                                                            </c:forEach></p>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Other Inspection Officer</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <c:if test="${insRepDto.inspectOffices != null && not empty insRepDto.inspectOffices}">
                                                                <p><c:forEach items="${insRepDto.inspectOffices}" var="ioName">
                                                                    <c:out value="${ioName}"/><br>
                                                                </c:forEach></p>
                                                            </c:if>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Reported By</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>${insRepDto.reportedBy}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Report Noted By</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>${insRepDto.reportNoteBy}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4 style="border-bottom: none">Section C (Inspection Findings)</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <div class="text">
                                                    <p><h4><strong><span>Part I: Inspection Checklist</span></strong></h4></p>
                                                </div>
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Checklist Used</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p>${insRepDto.serviceName}</p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                                <div class="text">
                                                    <p><h4><strong><span>Part II: Findings</span></strong></h4></p>
                                                </div>
                                                <div class="table-gp">
                                                    <table class="table" >
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Remarks</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p>${insRepDto.taskRemarks}</p>
                                                            </td>
                                                            <td class="col-xs-4"/>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Marked for Audit</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p><c:out value="${insRepDto.markedForAudit}"/>&nbsp;&nbsp;<fmt:formatDate value="${insRepDto.tcuDate}" pattern="dd/MM/yyyy"/></p>
                                                            </td>
                                                            <td class="col-xs-4"/>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Recommended Best Practices</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p>${insRepDto.bestPractice}</p>
                                                            </td>
                                                            <td class="col-xs-4"/>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Non-Compliances</p>
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
                                                                                    <p><c:out value="${status.count}"></c:out></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p><c:out value="${ncRegulations.nc}"></c:out></p>
                                                                                </td>
                                                                                <td>
                                                                                    <p><c:out value="${ncRegulations.regulation}"></c:out></p>
                                                                                </td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                        </tbody>
                                                                    </table>
                                                                </c:if>
                                                                <c:if test="${insRepDto.ncRegulation == null}">
                                                                    <p>0</p>
                                                                </c:if>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Status</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <p>${insRepDto.status}</p>
                                                            </td>
                                                            <td class="col-xs-4"/>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Risk Level <strong style="color:#ff0000;"> *</strong></p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <iais:select name="riskLevel" options="riskLevelOptions" firstOption="Please Select" value="${appPremisesRecommendationDto.riskLevel}"/>
                                                                <span id="error_riskLevel" name="iaisErrorMsg" class="error-msg"></span>
                                                            </td>
                                                            <td class="col-xs-4">
                                                            </td>
                                                        </tr>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4 style="border-bottom: none">Section D (Rectification)</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Rectified</p>
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
                                                                                <p><c:out value="${status.count}"></c:out></p>
                                                                            </td>
                                                                            <td>
                                                                                <p><c:out value="${ncRectification.nc}"></c:out></p>
                                                                            </td>
                                                                            <td>
                                                                                <p><c:out value="${ncRectification.rectified}"></c:out></p>
                                                                            </td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                    </tbody>
                                                                </table>
                                                            </c:if>
                                                            <c:if test="${insRepDto.ncRectification == null}">
                                                                <p>NA</p>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Remarks</p>
                                                        </td>
                                                        <div>
                                                            <td class="col-xs-4">
                                                                <p><c:out value="${insRepDto.inspectypeRemarks}"></c:out></p>
                                                            </td>
                                                        </div>
                                                        <td class="col-xs-4">
                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Rectified Within KPI?</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><c:out value="${insRepDto.rectifiedWithinKPI}"></c:out></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4 style="border-bottom: none">Section E (Recommendations)</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Recommendation <strong style="color:#ff0000;"> *</strong></p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <iais:select name="recommendation" options="recommendationOption" firstOption="Please Select" value="${appPremisesRecommendationDto.recommendation}" onchange="javascirpt:changeRecommendation(this.value);"/>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="period" hidden>
                                                        <td class="col-xs-4">
                                                            <p>Period <strong style="color:#ff0000;"> *</strong></p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <iais:select name="periods" options="riskOption" firstOption="Please Select" onchange="javascirpt:changePeriod(this.value);" value="${appPremisesRecommendationDto.period}"/>
                                                            <span id="error_period" name="iaisErrorMsg" class="error-msg"></span>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="selfPeriod" hidden>
                                                        <td class="col-xs-4">
                                                            <p>Other Period <strong style="color:#ff0000;"> *</strong></p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <input id=recomInNumber type="text" name="number" maxlength="2" value="${appPremisesRecommendationDto.recomInNumber}">
                                                            <iais:select id="chronoUnit" name="chrono" options="chronoOption" firstOption="Please Select" value="${appPremisesRecommendationDto.chronoUnit}"/>
                                                            <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                                                            <span id="error_chronoUnit" name="iaisErrorMsg" class="error-msg"></span>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info" role="alert">
                                        <strong>
                                            <h4>Section F (After Action)</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Follow up Action</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <P><textarea style="resize:none" name="followUpAction" cols="50" rows="6" title="content" maxlength="8000"><c:out value="${appPremisesRecommendationDto.followUpAction}"/></textarea></P>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>To Engage Enforcement?</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <input type="checkbox" id="enforcement" name="engageEnforcement" onchange="javascirpt:changeEngage();" <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> >
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="engageRemarks" hidden>
                                                        <td class="col-xs-4">
                                                            <p>Enforcement Remarks <strong style="color:#ff0000;"> *</strong></p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <textarea style="resize:none" name="enforcementRemarks" cols="50" rows="6" title="content" MAXLENGTH="4000"><c:out value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></textarea>
                                                            <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
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
        <a  onclick="javascript:SOP.Crud.cfxSubmit('mainForm');" >< Back</a>
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


    function changeEngage() {
        if ($('#enforcement').is(':checked')) {
            $("#engageRemarks").show();
        } else {
            $("#engageRemarks").hide();
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
        if ($('#enforcement').is(':checked')) {
            $("#engageRemarks").show();
        }
    });

</script>