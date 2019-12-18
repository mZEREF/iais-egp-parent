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

                        <div class="tab-content">
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
                                                        <td class="col-xs-6">Company</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">UEN</td>
                                                        <td>20181224-00003</td>
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
                                                        <td class="col-xs-6" align="right">Name of Company</td>
                                                        <td class="col-xs-6">SAM Group</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Postal code</td>
                                                        <td>319579</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Block No</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>04</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>13</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>Jalan Permimpin</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Number</td>
                                                        <td>6581111234</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax Number</td>
                                                        <td>6587651234</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
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
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>NRIC</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Designation</td>
                                                        <td>Director</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Designation Appointment Date</td>
                                                        <td>01/01/2015</td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
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
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>NRIC</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Designation</td>
                                                        <td>Director</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Profession Type</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Professional Regn No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Postal code</td>
                                                        <td>319579</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Block No</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>04</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>13</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>Jalan Permimpin</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Mobile No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Home Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email Address</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Add Another Licensee</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="tab-pane" id="tabPersonnelDetails" role="tabpanel">
                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><b>CGO</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Name</td>
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>NRIC</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Gender</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Designation</td>
                                                        <td>Director</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Profession Type</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Professional Regn No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Specialty</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Subspecialty or relevant qualification</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Postal code</td>
                                                        <td>319579</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Address Type</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Blk No</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>04</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>13</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>Jalan Permimpin</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Mobile No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Home Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email Address</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>Principal Officer</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Name</td>
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>NRIC</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Gender</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Designation</td>
                                                        <td>Director</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Profession Type</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Professional Regn No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Specialty</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Subspecialty or relevant qualification</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Postal code</td>
                                                        <td>319579</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Address Type</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Blk No</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>04</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>13</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>Jalan Permimpin</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Mobile No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Home Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email Address</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>Deputy Principal Officer</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Name</td>
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>NRIC</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Gender</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Designation</td>
                                                        <td>Director</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Profession Type</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Professional Regn No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Specialty</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Subspecialty or relevant qualification</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Postal code</td>
                                                        <td>319579</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Address Type</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Blk No</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>04</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>13</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>Jalan Permimpin</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Mobile No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Home Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email Address</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div>&nbsp</div>
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>MedAlert</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Name</td>
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Salutation</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID Type</td>
                                                        <td>NRIC</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">ID No</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Gender</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Designation</td>
                                                        <td>Director</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Profession Type</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Professional Regn No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Specialty</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Subspecialty or relevant qualification</td>
                                                        <td>319579</td>
                                                    </tr><tr>
                                                        <td align="right">Postal code</td>
                                                        <td>319579</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Address Type</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Blk No</td>
                                                        <td>Mapex</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Floor No</td>
                                                        <td>04</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Unit No</td>
                                                        <td>13</td>
                                                    </tr><tr>
                                                        <td align="right">Street Name</td>
                                                        <td>Jalan Permimpin</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Building Name</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Mobile No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Home Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Office Telephone No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Fax No</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Email Address</td>
                                                        <td>-</td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                </div>

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
                                                        <th>Year of Inspection</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <tr>
                                                        <td>
                                                            <p>1</p>
                                                        </td>
                                                        <td>
                                                            <p>Inspection Report</p>
                                                        </td>
                                                        <td>
                                                            <p>Low</p>
                                                        </td>
                                                        <td>
                                                            <p>Full Compliance</p>
                                                        </td>
                                                        <td>
                                                            <p>2016</p>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <p>2</p>
                                                        </td>
                                                        <td>
                                                            <p>Inspection Report</p>
                                                        </td>
                                                        <td>
                                                            <p>Low</p>
                                                        </td>
                                                        <td>
                                                            <p>Full Compliance</p>
                                                        </td>
                                                        <td>
                                                            <p>2018</p>
                                                        </td>
                                                    </tr>
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
    </div>
</form>