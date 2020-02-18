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

                        <div class="tab-content active">
                            <div class="tab-pane" id="tabApplicationInfo" role="tabpanel">

                                <div class="panel panel-default">
                                    <!-- Default panel contents -->
                                    <div class="panel-heading"><b>Submission Details</b></div>
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
                                                        <td>${hcsaServiceDto.svcName}</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Service Discipline/Modality</td>
                                                        <td>${applicationViewDto.serviceType}</td>
                                                    </tr>
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
                                    <div class="panel-heading"><b>Applicant Details</b></div>
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
                                                        <td>-</td>
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
                                    <div class="panel-heading"><b>Key Roles</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Licensee Name</td>
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Authorised Person Name</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Authorised Person ID</td>
                                                        <td>NRIC</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">MedAlert Contact Person</td>
                                                        <td>S123456789I</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">MedAlert Contact Person ID</td>
                                                        <td>S123456789I</td>
                                                    </tr>

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
                                    <div class="panel-heading"><b>Service Related</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Service Name</td>
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Service (Lab Discipline, Modality, Subsumed and etc) </td>
                                                        <td>DR</td>
                                                    </tr>

                                                    </tbody>
                                                </table>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="panel-heading"><b>Allocation</b></div>
                                    <div class="row">
                                        <div class="col-xs-12">
                                            <div class="table-gp">
                                                <table class="table table-bordered">
                                                    <tbody>
                                                    <tr>
                                                        <td class="col-xs-6" align="right">Premises</td>
                                                        <td class="col-xs-6">Tan Ah Kow</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">Service / Granular Service</td>
                                                        <td>DR</td>
                                                    </tr>
                                                    <tr>
                                                        <td align="right">CGO</td>
                                                        <td>NRIC</td>
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
        <input type="submit" class="btn btn-default" value="BACK">
    </div>
</form>