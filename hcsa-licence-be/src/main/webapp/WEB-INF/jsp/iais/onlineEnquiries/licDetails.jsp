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
                                                        <td class="col-xs-6">&nbsp;${organizationLicDto.licenseeDto.licenseeType}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">UEN</td>
                                                        <td>&nbsp;${organizationLicDto.uenNo}<c:if test="${empty organizationLicDto.uenNo}">-</c:if></td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Are you registered with ACRA/ROS</td>
                                                        <td>&nbsp;${registeredWithACRA}</td>
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

                                <c:if test="${empty organizationLicDto.licenseeKeyApptPersonDtos}">
                                    <div class="panel panel-default">
                                        <div class="panel-heading"><strong>Board Members</strong></div>
                                        <div class="row">
                                            <div class="col-xs-12">
                                                <div class="table-gp">
                                                    <table class="table table-bordered">
                                                        <tbody>
                                                        <tr>
                                                            <td class="col-xs-6" align="right">Name</td>
                                                            <td class="col-xs-6">&nbsp;Tan Ah Kow</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Salutation</td>
                                                            <td>&nbsp;Dr</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID Type</td>
                                                            <td>&nbsp;NRIC</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">ID No</td>
                                                            <td>&nbsp;S123456789I</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Designation</td>
                                                            <td>&nbsp;Director</td>
                                                        </tr>
                                                        <tr>
                                                            <td align="right">Designation Appointment Date</td>
                                                            <td>&nbsp;01/01/2020</td>
                                                        </tr>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>

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
                                                        <th>&nbsp;SN</th>
                                                        <th>Inspection Report</th>
                                                        <th>Risk Tagging</th>
                                                        <th>Compliance Tagging</th>
                                                        <th>Inspection Type</th>
                                                        <th>Date of Inspection</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:choose>
                                                        <c:when test="${empty complianceHistoryDtos}">
                                                            <tr>
                                                                <td colspan="12">
                                                                    <iais:message key="ACK018" escape="true"/>
                                                                </td>
                                                            </tr>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:forEach var="compliance" items="${complianceHistoryDtos}" varStatus="status">
                                                                <tr>
                                                                    <td class="row_no">&nbsp;<c:out value="${status.index + 1}"/></td>
                                                                    <td>
                                                                        <a onclick="doReportSearch('${MaskUtil.maskValue(IaisEGPConstant.CRUD_ACTION_VALUE,compliance.appPremCorrId)}')">Inspection Report</a>
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
                </div>
            </div>
        </div>
        <a  onclick="javascript:doBack();" ><em class="fa fa-angle-left"> </em> Back</a>
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