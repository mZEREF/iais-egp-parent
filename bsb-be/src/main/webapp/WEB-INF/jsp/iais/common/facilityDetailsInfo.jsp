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
                <c:if test="${facilityDetailsInfo.facilityBiologicalAgentInfoList ne null && facilityDetailsInfo.facilityBiologicalAgentInfoList.size() > 0}">
                    <div class="alert alert-info" role="alert">
                        <strong>
                            <h4>Recommendation for Approval to Possess</h4>
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
                            <c:forEach var="facilityBiologicalAgentInfo" items="${facilityDetailsInfo.facilityBiologicalAgentInfoList}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-3"><iais:code code="${facilityBiologicalAgentInfo.schedule}"/></div>
                                    <div class="col-md-5"><c:out value="${facilityBiologicalAgentInfo.batName}"/></div>
                                    <div class="col-md-3">
                                        <div class="row">
                                            <label>
                                                <input type="radio" name="${facilityBiologicalAgentInfo.id}" data-bat-activityId="${facilityBiologicalAgentInfo.facilityActivityId}" <c:if test="${facilityBiologicalAgentInfo.status eq MasterCodeConstants.PROCESSING_STATUS_APPROVAL}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_APPROVAL}" disabled="disabled"/>
                                            </label>
                                            <span class="check-circle">Yes</span>
                                            <label>
                                                <input type="radio" name="${facilityBiologicalAgentInfo.id}" data-bat-activityId="${facilityBiologicalAgentInfo.facilityActivityId}" <c:if test="${facilityBiologicalAgentInfo.status eq MasterCodeConstants.PROCESSING_STATUS_REJECT or facilityBiologicalAgentInfo.status eq null}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_REJECT}" disabled="disabled"/>
                                            </label>
                                            <span class="check-circle">No</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
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