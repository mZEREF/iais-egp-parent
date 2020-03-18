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
                            <li class="active" role="presentation"><a href="#tabLicenseeDetails" aria-controls="tabLicenseeDetails" role="tab" data-toggle="tab">Licensee Details</a></li>
                            <li class="complete" role="presentation"><a href="#tabPersonnelDetails" aria-controls="tabPersonnelDetails" role="tab"
                                                                        data-toggle="tab">Personnel Details</a></li>
                            <li class="complete" role="presentation"><a href="#tabComplianceHistory" aria-controls="tabComplianceHistory" role="tab"
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
                            <div class="tab-pane active" id="tabLicenseeDetails" role="tabpanel">

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
                            <div class="tab-pane" id="tabComplianceHistory" role="tabpanel">
                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="alert alert-info" role="alert"><strong>
                                        <h4>Past Inspection Reports</h4>
                                    </strong></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>SN</th>
                                                        <th>Inspection Report</th>
                                                        <th>Risk Tagging</th>
                                                        <th>Compliance Tagging</th>
                                                        <th>Inspection Type</th>
                                                        <th>Date of Inspection</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach var="compliance" items="${complianceHistoryDtos}" varStatus="status">
                                                        <tr>
                                                            <td class="row_no"><c:out value="${status.index + 1}"/></td>
                                                            <td>
                                                                <a onclick="doReportSearch('${compliance.appPremCorrId}')">Inspection Report</a>
                                                            </td>
                                                            <td>
                                                                <p>${compliance.riskTag}</p>
                                                            </td>
                                                            <td>
                                                                <p>${compliance.complianceTag} Compliance</p>
                                                            </td>
                                                            <td>
                                                                <p>${compliance.inspectionTypeName}</p>
                                                            </td>
                                                            <td>
                                                                <p>${compliance.inspectionDateYear}</p>
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
        </div>
        <input type="button" class="btn btn-secondary" value="BACK" onclick="doBack()">
    </div>
</form>
<script type="text/javascript">
    function doReportSearch(appPremCorrId){
        showWaiting(); SOP.Crud.cfxSubmit("mainForm", "report",appPremCorrId);
    }
    function doBack(){
        showWaiting(); SOP.Crud.cfxSubmit("mainForm", "back");
    }

</script>