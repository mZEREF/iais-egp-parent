<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
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
                                    <div class="panel-heading"><b>Licensee Details</b></div>
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
                                    <div class="panel-heading"><b>Company Details</b></div>
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
                                        <div class="panel-heading"><b>Board Members</b></div>
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
                                                            <td>${member.apptDt}</td>
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
                                    <div class="panel-heading"><b>Sole Proprietor - Licensee</b></div>
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
                                        <div class="panel-heading"><b>${personnel.licKeyPersonnelDto.psnType}</b></div>
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
                                                            <td align="right">Profession Type</td>
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
                                        <p><span><strong>Section A (HCI Details)</strong></span></p>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Licence No:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.licenceNo}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Service Name:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.serviceName}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>HCI Code:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.hciCode}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>HCI Name:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.hciName}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>HCI Address:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.hciAddress}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Licensee Name:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.licenseeName}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Principal Officers:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <c:if test="${insRepDto.principalOfficers != null && not empty insRepDto.principalOfficers}">
                                                                <p><c:forEach items="${insRepDto.principalOfficers}" var="poName">
                                                                    <c:out value="${poName}"/><br>
                                                                </c:forEach></p>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Subsumed Services:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <c:if test="${insRepDto.subsumedServices != null && not empty insRepDto.subsumedServices}">
                                                                <c:forEach var="service" items="${insRepDto.subsumedServices}">
                                                                    <p><c:out value="${service}"></c:out></p>
                                                                </c:forEach>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info" role="alert">
                                        <p><span><strong>Section B (Type of Inspection)</strong></span></p>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Date of Inspection:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <fmt:formatDate value="${insRepDto.inspectionDate}"
                                                                            pattern="dd/MM/yyyy"></fmt:formatDate>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Time of Inspection:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <fmt:formatDate value="${insRepDto.inspectionStartTime}"
                                                                            pattern="dd/MM/yyyy"></fmt:formatDate>-
                                                            <fmt:formatDate value="${insRepDto.inspectionEndTime}"
                                                                            pattern="dd/MM/yyyy"></fmt:formatDate>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Reason for Visit:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.reasonForVisit}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Inspected By:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <c:if test="${insRepDto.inspectors != null && not empty insRepDto.inspectors}">
                                                                <p><c:forEach items="${insRepDto.inspectors}" var="inspector" varStatus="status">
                                                                <p><c:out value="${inspector}"></c:out></p>
                                                            </c:forEach></p>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Other Inspection Officer:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <c:if test="${insRepDto.inspectOffices != null && not empty insRepDto.inspectOffices}">
                                                                <p><c:forEach items="${insRepDto.inspectOffices}" var="ioName">
                                                                    <c:out value="${ioName}"/><br>
                                                                </c:forEach></p>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Reported By:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.reportedBy}</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Report Noted By:</p>
                                                        </td>
                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.reportNoteBy}</p>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="alert alert-info" role="alert">
                                        <p><span><strong>Section C (Inspection Findings)</strong></span></p>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <div class="text ">
                                                    <p><span>Part I: Inspection Checklist</span></p>
                                                </div>
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Checklist Used:</p>
                                                        </td>

                                                        <td class="col-xs-8">
                                                            <p>${insRepDto.serviceName}</p>
                                                            <c:if test="${insRepDto.commonCheckList != null}">
                                                                <div class="tab-pane" id="ServiceInfo" role="tabpanel">
                                                                    <c:forEach var ="cdto" items ="${insRepDto.subTypeCheckList.fdtoList}" varStatus="status">
                                                                        <h3>${cdto.subType}</h3>
                                                                        <div class="table-gp">
                                                                            <c:forEach var ="section" items ="${cdto.sectionDtoList}">
                                                                                <br/>
                                                                                <h4><c:out value="${section.sectionName}"></c:out></h4>
                                                                                <table class="table">
                                                                                    <thead>
                                                                                    <tr>
                                                                                        <th>No.</th>
                                                                                        <th>Regulation Clause Number</th>
                                                                                        <th>Item</th>
                                                                                        <th>Rectified</th>
                                                                                    </tr>
                                                                                    </thead>
                                                                                    <tbody>
                                                                                    <c:forEach var = "item" items = "${section.itemDtoList}" varStatus="status">
                                                                                        <tr>
                                                                                            <td class="row_no">${(status.index + 1) }</td>
                                                                                            <td>${item.incqDto.regClauseNo}</td>
                                                                                            <td>${item.incqDto.checklistItem}</td>
                                                                                            <c:set value = "${cdto.subName}${item.incqDto.sectionName}${item.incqDto.itemId}" var = "ckkId"/>
                                                                                            <td>
                                                                                                <div id="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>ck"   <c:if test="${item.incqDto.chkanswer != 'No'}">hidden</c:if>>
                                                                                                    <input name="<c:out value="${cdto.subName}"/><c:out value="${item.incqDto.sectionNameSub}"/><c:out value="${item.incqDto.itemId}"/>rec" id="<c:out value="${cdto.subName}${item.incqDto.itemId}"/><c:out value="${item.incqDto.sectionNameSub}"/>rec" type="checkbox" <c:if test="${item.incqDto.rectified}">checked</c:if> value="rec" disabled/>
                                                                                                </div>
                                                                                                <c:set value = "error_${cdto.subName}${item.incqDto.sectionNameSub}${item.incqDto.itemId}" var = "err"/>
                                                                                                <span class="error-msg" id="<c:out value="${err}"/>" name="iaisErrorMsg"></span>
                                                                                            </td>
                                                                                        </tr>
                                                                                    </c:forEach>
                                                                                    </tbody>
                                                                                </table>
                                                                            </c:forEach>
                                                                        </div>
                                                                    </c:forEach>
                                                                </div>
                                                            </c:if>
                                                        </td>
                                                    </tr>
                                                </table>
                                                <div class="text ">
                                                    <p><span>Part II: Findings</span></p>
                                                </div>
                                                <div class="table-gp">
                                                    <table class="table" >
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Remarks:</p>
                                                            </td>
                                                            <td class="col-xs-8">
                                                                <p>${insRepDto.taskRemarks}</p>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Marked for Audit:</p>
                                                            </td>
                                                            <td class="col-xs-8">
                                                                <c:if test="${insRepDto.markedForAudit}">
                                                                    <p>Yes <c:out value="${insRepDto.tcuDate}"/></p>
                                                                </c:if>
                                                                <c:if test="${!insRepDto.markedForAudit}">
                                                                    <p>No</p>
                                                                </c:if>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Recommended Best Practices:</p>
                                                            </td>
                                                            <td class="col-xs-8">
                                                                <p>${insRepDto.bestPractice}</p>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Non-Compliances:</p>
                                                            </td>
                                                            <td class="col-xs-8">
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
                                                                <p>Status:</p>
                                                            </td>
                                                            <td class="col-xs-8">
                                                                <p>${insRepDto.status}</p>
                                                            </td>
                                                        </tr>
                                                        <tr>
                                                            <td class="col-xs-4">
                                                                <p>Risk Level:</p>
                                                            </td>
                                                            <td class="col-xs-4">
                                                                <c:if test="${appPremisesRecommendationDto.riskLevel == null}"> <iais:select
                                                                        name="riskLevel" options="riskLevelOptions" firstOption="Please select"
                                                                        value="${riskLevel}"/></c:if>
                                                                <c:if test="${appPremisesRecommendationDto.riskLevel != null}"> <iais:select
                                                                        name="riskLevel" options="riskLevelOptions" firstOption="Please select"
                                                                        value="${appPremisesRecommendationDto.riskLevel}"/></c:if>

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
                                            <h4>Section D (Rectification)</h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Rectified:</p>
                                                        </td>
                                                        <td class="col-xs-8">
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
                                                            <p>Remarks:</p>
                                                        </td>
                                                        <div>
                                                            <td class="col-xs-4">
                                                                <p><textarea name="remarks" cols="50" rows="6" title="content" maxlength="8000"><c:if test="${appPremisesRecommendationDto.remarks ==null}">${reportRemarks}</c:if><c:if test="${appPremisesRecommendationDto.remarks !=null}"><c:out value="${appPremisesRecommendationDto.remarks}"/></c:if></textarea></p>
                                                                <span id="error_remarks" name="iaisErrorMsg" class="error-msg"></span>
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
                                            <h4>Section E (Recommendations) </h4>
                                        </strong>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>Recommendation:</p>
                                                        </td>
                                                        <c:if test="${appPremisesRecommendationDto.recommendation==null}">
                                                            <td class="col-xs-4">
                                                                <iais:select name="recommendation" options="recommendationOption" firstOption="Please select" value="${recomDecision}" onchange="javascirpt:changeRecommendation(this.value);"/>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${appPremisesRecommendationDto.recommendation !=null}">
                                                            <td>
                                                                <iais:select name="recommendation" options="recommendationOption" firstOption="Please select" value="${appPremisesRecommendationDto.recommendation}" onchange="javascirpt:changeRecommendation(this.value);"/>
                                                            </td>
                                                        </c:if>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="period" hidden>
                                                        <td class="col-xs-4">
                                                            <p>Period:</p>
                                                        </td>
                                                        <c:if test="${appPremisesRecommendationDto.period !=null}">
                                                            <td class="col-xs-4">
                                                                <iais:select name="periods" options="riskOption" firstOption="Please select"
                                                                             onchange="javascirpt:changePeriod(this.value);"
                                                                             value="${appPremisesRecommendationDto.period}"/>
                                                                <span id="error_period" name="iaisErrorMsg" class="error-msg"></span>
                                                            </td>
                                                        </c:if>
                                                        <c:if test="${appPremisesRecommendationDto.period ==null}">
                                                            <td class="col-xs-4">
                                                                <iais:select name="periods" value="${option}" options="riskOption" firstOption="Please select" onchange="javascirpt:changePeriod(this.value);"/>
                                                                <span id="error_period" name="iaisErrorMsg" class="error-msg"></span>
                                                            </td>
                                                        </c:if>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="selfPeriod" hidden>
                                                        <td class="col-xs-4">
                                                            <p>Other Period:</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <input id=recomInNumber type="text" name="number" <c:if test="${number ==null}">value="${recnumber}"</c:if>
                                                                   <c:if test="${number !=null}">value="${number}"</c:if>>
                                                            <span id="error_recomInNumber" name="iaisErrorMsg" class="error-msg"></span>
                                                            <c:if test="${chrono ==null}"><iais:select id="chronoUnit" name="chrono" options="chronoOption" firstOption="Please select" value="${recchrono}"/></c:if>
                                                            <c:if test="${chrono !=null}"><iais:select id="chronoUnit" name="chrono" options="chronoOption" firstOption="Please select" value="${chrono}"/></c:if>
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
                                                            <p>Follow up Action:</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><textarea name="followUpAction" cols="50" rows="6" title="content" maxlength="8000"><c:if test="${appPremisesRecommendationDto.followUpAction == null}"><c:out value="${followRemarks}"/></c:if><c:if test="${appPremisesRecommendationDto.followUpAction != null}"><c:out value="${appPremisesRecommendationDto.followUpAction}"/></c:if></textarea></p>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr>
                                                        <td class="col-xs-4">
                                                            <p>To Engage Enforcement?:</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <input type="checkbox" id="enforcement" name="engageEnforcement" onchange="javascirpt:changeEngage();" <c:if test="${appPremisesRecommendationDto.engageEnforcement =='on'}">checked</c:if> <c:if test="${engage =='on'}">checked</c:if>>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                    <tr id="engageRemarks" hidden>
                                                        <td class="col-xs-4">
                                                            <p>Enforcement Remarks</p>
                                                        </td>
                                                        <td class="col-xs-4">
                                                            <p><textarea name="enforcementRemarks" cols="50" rows="6" title="content" MAXLENGTH="4000"><c:if test="${appPremisesRecommendationDto.engageEnforcementRemarks ==null}"><c:out value="${remarks}"/></c:if><c:if test="${appPremisesRecommendationDto.engageEnforcementRemarks !=null}"><c:out value="${appPremisesRecommendationDto.engageEnforcementRemarks}"/></c:if></textarea></p>
                                                            <span id="error_enforcementRemarks" name="iaisErrorMsg" class="error-msg"></span>
                                                        </td>
                                                        <td class="col-xs-4"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </div>
                                    </div>

                                    <%@include file="/include/validation.jsp" %>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
        <a  onclick="javascript:SOP.Crud.cfxSubmit('mainForm');" >< BACK</a>
    </div>
</form>

<script type="text/javascript">

    function insRepsubmit() {
        $("#mainForm").submit();
    }

    function changePeriod(obj) {
        if (obj == "Others") {
            document.getElementById("selfPeriod").style.display = "";
            $("#selfPeriod").show();
        } else {
            document.getElementById("selfPeriod").style.display = "none";
        }
    }

    function changeRecommendation(obj) {
        if (obj == "Approved") {
            document.getElementById("period").style.display = "";
            $("#period").show();
        } else {
            document.getElementById("period").style.display = "none";
        }
    }


    function changeEngage() {
        if ($('#enforcement').is(':checked')) {
            document.getElementById("engageRemarks").style.display = "";
            $("#engageRemarks").show();
        } else {
            document.getElementById("engageRemarks").style.display = "none";
        }
    }


    $(document).ready(function () {
        if ($("#recommendation").val() == "Approved") {
            changeRecommendation("Approved");
        }
        if ($("#periods").val() == "Others") {
            changePeriod("Others");
        }
        if ($('#tcuNeeded').is(':checked')) {
            $("#tcuDate").show();
        }
        if ($('#enforcement').is(':checked')) {
            $("#engageRemarks").show();
        }
    });

</script>