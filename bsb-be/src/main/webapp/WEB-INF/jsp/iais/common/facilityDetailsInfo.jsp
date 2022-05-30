<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%--@elvariable id="facilityDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo"--%>
<br/><br/>
<div class="panel panel-default">
    <div class="panel-heading" style="text-align: center"><strong>Facility Details</strong></div>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <tr>
                        <th scope="col" style="display: none"></th>
                    </tr>
                    <tr>
                        <td class="col-xs-6" style="font-weight: bold; text-align: right">Facility Name</td>
                        <td class="col-xs-6" style="padding-left : 20px"><c:out value="${facilityDetailsInfo.name}"/></td>
                    </tr>
                    <tr>
                        <td style="font-weight: bold; text-align: right">Facility Classification</td>
                        <td style="padding-left: 20px"><iais:code code="${facilityDetailsInfo.classification}"/></td>
                    </tr>
                    <tr>
                        <td style="font-weight: bold; text-align: right">Existing Facility Activity Type Approval</td>
                        <td style="padding-left: 20px">
                            <c:if test="${facilityDetailsInfo.existingFacilityActivityTypeApprovalList eq null || facilityDetailsInfo.existingFacilityActivityTypeApprovalList.size() == 0}">-</c:if>
                            <ul>
                            <c:forEach var="existingFacilityActivityTypeApproval" items="${facilityDetailsInfo.existingFacilityActivityTypeApprovalList}">
                                <li><iais:code code="${existingFacilityActivityTypeApproval}"/></li>
                            </c:forEach>
                            </ul>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="preview-gp">
    <div class="row">
        <div class="col-xs-12">
            <div class="panel-group" role="tablist" aria-multiselectable="true">
                <c:if test="${facilityDetailsInfo.facilityActivityInfoList ne null && facilityDetailsInfo.facilityActivityInfoList.size() > 0}">
                    <div class="alert alert-info" role="alert">
                        <strong>
                            <h4>Recommendation for Approval for Facility Activity Type</h4>
                        </strong>
                    </div>
                    <div class="panel-collapse">
                        <div class="panel-body">
                            <div class="row" style="font-weight: 700;text-align: center">
                                <div class="col-md-1">S/N</div>
                                <div class="col-md-8">Facility Activity Type</div>
                                <div class="col-md-3">Approve</div>
                            </div>
                            <c:forEach var="facilityActivityInfo" items="${facilityDetailsInfo.facilityActivityInfoList}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-8"><iais:code code="${facilityActivityInfo.activityType}"/></div>
                                    <div class="col-md-3">
                                        <div class="row">
                                            <label>
                                                <input type="radio" name="${facilityActivityInfo.id}" data-radio-type="facilityActivityYes" <c:if test="${facilityActivityInfo.status eq MasterCodeConstants.PROCESSING_STATUS_APPROVAL}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_APPROVAL}" disabled="disabled"/>
                                            </label>
                                            <span class="check-circle">Yes</span>
                                            <label>
                                                <input type="radio" name="${facilityActivityInfo.id}" data-radio-type="facilityActivityNo" <c:if test="${facilityActivityInfo.status eq MasterCodeConstants.PROCESSING_STATUS_REJECT or facilityActivityInfo.status eq null}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_REJECT}" disabled="disabled"/>
                                            </label>
                                            <span class="check-circle">No</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:set var="batApprovalTypes" value="${[MasterCodeConstants.APPROVAL_TYPE_POSSESS, MasterCodeConstants.APPROVAL_TYPE_LSP, MasterCodeConstants.APPROVAL_TYPE_SP_HANDLE, MasterCodeConstants.APPROVAL_TYPE_HANDLE_FST_EXEMPTED]}"/>
                <c:forEach var="approvalType" items="${batApprovalTypes}">
                    <%--@elvariable id="batMap" type="java.util.Map<java.lang.String, java.util.List<sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityBiologicalAgentInfo>>"--%>
                    <c:set var="batList" value="${batMap.get(approvalType)}"/>
                    <c:if test="${batList ne null && batList.size() > 0}">
                        <div class="alert alert-info" role="alert">
                            <strong>
                                <c:choose>
                                    <c:when test="${approvalType eq MasterCodeConstants.APPROVAL_TYPE_POSSESS}">
                                        <h4>Recommendation for Approval to Possess</h4>
                                    </c:when>
                                    <c:when test="${approvalType eq MasterCodeConstants.APPROVAL_TYPE_LSP}">
                                        <h4>Recommendation for Approval to Large Scale Produce</h4>
                                    </c:when>
                                    <c:when test="${approvalType eq MasterCodeConstants.APPROVAL_TYPE_SP_HANDLE}">
                                        <h4>Recommendation for Special Approval to Handle</h4>
                                    </c:when>
                                    <c:when test="${approvalType eq MasterCodeConstants.APPROVAL_TYPE_HANDLE_FST_EXEMPTED}">
                                        <h4>Recommendation for Handling of Fifth Schedule Toxin for Exempted Purpose</h4>
                                    </c:when>
                                </c:choose>
                            </strong>
                        </div>
                        <div class="panel-collapse">
                            <div class="panel-body">
                                <div class="row" style="font-weight: 700;text-align: center">
                                    <div class="col-md-1">S/N</div>
                                    <div class="col-md-3">Schedule</div>
                                    <div class="col-md-5">Name of Biological Agent/Toxin</div>
                                    <div class="col-md-3">Approve</div>
                                </div>
                                <c:forEach var="batInfo" items="${batList}" varStatus="status">
                                    <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                        <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                        <div class="col-md-3"><iais:code code="${batInfo.schedule}"/></div>
                                        <div class="col-md-5"><c:out value="${batInfo.batName}"/></div>
                                        <div class="col-md-3">
                                            <div class="row">
                                                <label>
                                                    <input type="radio" name="${batInfo.id}" data-radio-type="facilityAgentRadio" data-bat-activityId="${batInfo.facilityActivityId}" <c:if test="${batInfo.status eq MasterCodeConstants.PROCESSING_STATUS_APPROVAL}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_APPROVAL}" disabled="disabled"/>
                                                </label>
                                                <span class="check-circle">Yes</span>
                                                <label>
                                                    <input type="radio" name="${batInfo.id}" data-radio-type="facilityAgentRadio" data-bat-activityId="${batInfo.facilityActivityId}" <c:if test="${batInfo.status eq MasterCodeConstants.PROCESSING_STATUS_REJECT or batInfo.status eq null}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_REJECT}" disabled="disabled"/>
                                                </label>
                                                <span class="check-circle">No</span>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<c:choose>
    <%--@elvariable id="goBackUrl" type="java.lang.String"--%>
    <c:when test="${goBackUrl ne null}">
        <a class="back" href="${goBackUrl}" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:when>
    <c:otherwise>
        <a class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList" style="float:left"><em class="fa fa-angle-left"></em> Previous</a>
    </c:otherwise>
</c:choose>