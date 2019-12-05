<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<%
    String webroot=IaisEGPConstant.BE_CSS_ROOT;
%>
        <form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
            <div class="tab-pane" id="tabInspection" role="tabpanel">
                <div class="alert alert-info" role="alert">
                    <p><span><strong>Section A (HCI Details)</strong></span></p>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="table-gp">
                            <table class="table">
                                <thead>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="col-xs-4">
                                        <p>Licence.No:</p>
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
                                        <p>Principal Officer:</p>
                                    </td>
                                    <td class="col-xs-8">
                                        <p>${insRepDto.principalOfficer}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="col-xs-4">
                                        <p>Subsumed Services:</p>
                                    </td>
                                    <td class="col-xs-8">
                                        <p>${insRepDto.subsumedService}</p>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <div class="alert alert-info" role="alert">
                                <p><span><strong>Section B (Type of Inspection)</strong></span></p>
                            </div>
                            <table class="table">
                                <tr>
                                    <td class="col-xs-4">
                                        <p>Date of Inspection:</p>
                                    </td>
                                    <td class="col-xs-8">
                                        <p>${insRepDto.inspectionDate}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="col-xs-4">
                                        <p>Time of Inspection:</p>
                                    </td>
                                    <td class="col-xs-8">
                                        <p>${insRepDto.inspectionTime}</p>
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
                                        <p>${insRepDto.inspectedBy}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="col-xs-4">
                                        <p>Other Inspection Officer:</p>
                                    </td>
                                    <td class="col-xs-8">
                                        <p>${insRepDto.reportedBy}</p>
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
                                        <p>${insRepDto.reportedBy}</p>
                                    </td>
                                </tr>
                            </table>
                            <div class="alert alert-info" role="alert">
                                <p><span><strong>Section C (Inspection Findings)</strong></span></p>
                            </div>
                            <div class="text ">
                                <p><span>Part I: Inspection Checklist</span></p>
                            </div>
                            <table class="table">
                                <tr>
                                    <td class="col-xs-4">
                                        <p>Checklist Used:</p>
                                    </td>
                                    <td class="col-xs-8">
                                        <p>wqeqweqwe</p>
                                    </td>
                                </tr>
                            </table>
                            <div class="text ">
                                <p><span>Part II: Findings</span></p>
                            </div>
                            <table class="table">
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
                                        <p>${insRepDto.markedForAudit}</p>
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
                                        <p>${insRepDto.ncRegulation}</p>
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
                            </table>
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
                                        <p>${insRepDto.status}</p>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="col-xs-4">
                                        <p>Remarks:</p>
                                    </td>
                                    <td class="col-xs-8">
                                        <textarea name="remarks">
                                            <p>${insRepDto.remarks}</p>
                                        </textarea>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
                <div class="alert alert-info" role="alert">
                    <strong>
                        <h4>Section E (Recommendations) Not Applicable for Post  Licensing Inspection</h4>
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
                                    <td class="col-xs-8">
                                        <div class="col-xs-12 col-md-4">
                                            <select class="active" id="select4" name="recommendation">
                                                <option>Select an option</option>
                                                <option value="recommendation">Reject</option>
                                                <option value="1">1</option>
                                                <option value="2">2</option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <button type="submit">approve</button>
            <button type="submit">back</button>
        </form>



