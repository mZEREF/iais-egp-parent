<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="insRepDto" scope="request" type="com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto"/>

<table class="control-grid columns1">
    <tbody>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--1" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--1--label" class="control-label control-set-font control-font-label">
                            Licence No.
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.licenceNo}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--2" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">
                            Service Name
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.serviceName}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--3" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--3--label" class="control-label control-set-font control-font-label">
                            HCI Code
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.hciCode}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--4" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--4--label" class="control-label control-set-font control-font-label">
                            HCI Name
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.hciName}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--5" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--6--label" class="control-label control-set-font control-font-label">
                            HCI Address
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.hciAddress}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--7" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--7--label" class="control-label control-set-font control-font-label">
                            Licensee Name
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.licenseeName}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--8" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--8--label" class="control-label control-set-font control-font-label">
                            Principal Officer
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.principalOfficer}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--9" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--9--label" class="control-label control-set-font control-font-label">
                            Subsumed Services
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.subsumedService}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--10" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-9 control-label formtext ">
                        <label id="control--runtime--10--label" class="control-label control-set-font control-font-label">
                            <strong><p>Section B (Type of Inspection)</p></strong>
                        </label>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--11" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--11--label" class="control-label control-set-font control-font-label">
                            Date of Inspection
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.inspectionDate}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--12" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--12--label" class="control-label control-set-font control-font-label">
                            Time of Inspection
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.inspectionTime}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--13" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--13--label" class="control-label control-set-font control-font-label">
                            Reason for Visit
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.reasonForVisit}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--14" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--14--label" class="control-label control-set-font control-font-label">
                            Inspected By
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.inspectedBy}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--15" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--15--label" class="control-label control-set-font control-font-label">
                            Other Inspection Officer
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.inspectOffices}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--16" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--16--label" class="control-label control-set-font control-font-label">
                            Reported By
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.reportedBy}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--17" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--17--label" class="control-label control-set-font control-font-label">
                            Report Noted By
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.reportNoteBy}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--18" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-9 control-label formtext ">
                        <label id="control--runtime--18--label" class="control-label control-set-font control-font-label">
                            <strong><p>Section C (Inspection Findings)</p></strong>
                        </label>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--19" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--19--label" class="control-label control-set-font control-font-label">
                            <p>Part I: Inspection Checklist</p>
                        </label>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--20" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--20--label" class="control-label control-set-font control-font-label">
                            <p>Checklist Used</p>
                            <p>ServiceName</p>
                            <p>Any other checklist rendered with the service checklist</p>
                        </label>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--21" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--22--label" class="control-label control-set-font control-font-label">
                            <p>Part II: Findings</p>
                        </label>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--23" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--23--label" class="control-label control-set-font control-font-label">
                            <p>Remarks</p>
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.taskRemarks}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--24" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--24--label" class="control-label control-set-font control-font-label">
                            <p>Marked for Audit</p>
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.markedForAudit}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--25" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--25--label" class="control-label control-set-font control-font-label">
                            <p>Recommended Best Practices</p>
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p>${insRepDto.bestPractice}</p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--26" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--26--label" class="control-label control-set-font control-font-label">
                            <p>Non-Compliances</p>
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <table class="table">
                            <tr>
                                <th>SN</th>
                                <th>Checklist Item</th>
                                <th>Regulation Clause</th>
                            </tr>
                            <c:forEach items="${insRepDto.ncRegulation}" var="nc" varStatus="count">
                                <tr>
                                    <td>${count.count}</td>
                                    <td>${nc.nc}</td>
                                    <td>${nc.regulation }</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--27" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--27--label" class="control-label control-set-font control-font-label">
                            <p>Status</p>
                        </label>
                    </div>
                    <p>${insRepDto.status}</p>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--28" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-9 control-label formtext ">
                        <label id="control--runtime--28--label" class="control-label control-set-font control-font-label">
                            <strong><p>Section D (Rectification)</p></strong>
                        </label>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--29" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-9 control-label formtext ">
                        <label id="control--runtime--29--label" class="control-label control-set-font control-font-label">
                            List down rectification outcome of each NC in table below
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <table class="table">
                            <tr>
                                <th>SN</th>
                                <th>Checklist Item</th>
                                <th>Regulation Clause</th>
                            </tr>
                            <c:forEach items="${insRepDto.ncRectification}" var="nc" varStatus="count">
                                <tr>
                                    <td>${count.count}</td>
                                    <td>${nc.nc}</td>
                                    <td>${nc.rectified }</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--30" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--30--label" class="control-label control-set-font control-font-label">
                            Remarks
                        </label>
                    </div>
                    <div class="col-sm-5 col-md-5">
                        <label for="control--runtime--30--text"></label><input type="text" id="control--runtime--30--text" name="remarks" class="form-control control-input control-set-font control-font-normal"
                                                                                value="" size="30">
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--31" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-12 control-label formtext ">
                        <label id="control--runtime--31--label" class="control-label control-set-font control-font-label">
                            <strong><p>Section E (Recommendations) Not Applicable for Post  Licensing Inspection</p></strong>
                        </label>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="5">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--32" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--32--label" class="control-label control-set-font control-font-label">
                            Recommendations
                        </label>
                    </div>
                    <div class="col-sm-5 col-md-5">
                        <select>
                            <option value="Rject" name="recommendation">Rject</option>
                            <option value="1year" name="recommendation">1year</option>
                            <option value="1year" name="recommendation">12year</option>
                            <option value="1year" name="recommendation">13year</option>
                        </select>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    </tbody>
</table>
</td>
