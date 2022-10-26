<%@tag description="facility details info" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ taglib prefix="bsb" uri="http://www.ecq.com/iais-bsb" %>

<%@attribute name="facilityDetailsInfo" required="true" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo" %>
<%@attribute name="firstTab" required="true" type="java.lang.String" %>

<%@attribute name="displayValidityEndDate" required="true" type="java.lang.Boolean" %>

<bsb:global-constants classFullName="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" attributeKey="masterCodeConstants"/>
<%--@elvariable id="masterCodeConstants" type="java.util.Map<java.lang.String, java.lang.Object>"--%>
<br/><br/>
<div class="panel panel-default">
    <c:choose>
        <c:when test="${firstTab ne null && firstTab ne ''}">
            <div class="panel-heading" style="text-align: center"><strong><c:out value="${firstTab}"/></strong></div>
        </c:when>
        <c:otherwise>
            <div class="panel-heading" style="text-align: center"><strong>Facility Details</strong></div>
        </c:otherwise>
    </c:choose>
    <div class="row">
        <div class="col-xs-12">
            <div class="table-gp">
                <table aria-describedby="" class="table table-bordered" style="margin-bottom: 0">
                    <tbody>
                    <c:choose>
                        <c:when test="${facilityDetailsInfo.rfFacilityActivityType != null}">
                            <tr>
                                <th scope="col" style="display: none"></th>
                            </tr>
                            <tr>
                                <td style="font-weight: bold; text-align: right" class="col-sm-6">Facility Classification</td>
                                <td style="padding-left: 20px"><iais:code code="${facilityDetailsInfo.classification}"/></td>
                            </tr>
                            <tr>
                                <td style="font-weight: bold; text-align: right" class="col-sm-6">Facility Activity Type</td>
                                <td style="padding-left: 20px"><iais:code code="${facilityDetailsInfo.rfFacilityActivityType}"/></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
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
                                    <c:forEach var="existingFacilityActivityTypeApproval" items="${facilityDetailsInfo.existingFacilityActivityTypeApprovalList}">
                                        <div><iais:code code="${existingFacilityActivityTypeApproval}"/></div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <%--@elvariable id="submissionDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo"--%>
                            <c:if test="${displayValidityEndDate}">
                                <tr>
                                    <td style="font-weight: bold; text-align: right">Validity End Date</td>
                                    <td style="padding-left: 20px">
                                        <c:if test="${facilityDetailsInfo.validityEndDate eq null || facilityDetailsInfo.validityEndDate eq ''}">-</c:if>
                                        <c:out value="${facilityDetailsInfo.validityEndDate}"/>
                                    </td>
                                </tr>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
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
                <c:if test="${facilityDetailsInfo.rfFacilities ne null && facilityDetailsInfo.rfFacilities.size() > 0}">
                    <div class="alert alert-info" role="alert">
                        <strong>
                            <h4>Recommendation for Facility</h4>
                        </strong>
                    </div>
                    <div class="panel-collapse">
                        <div class="panel-body">
                            <div class="row" style="font-weight: 700;text-align: center">
                                <div class="col-md-1">S/N</div>
                                <div class="col-md-8">Facility Name</div>
                                <div class="col-md-3">Accept</div>
                            </div>
                            <c:forEach var="adeFacilityInfo" items="${facilityDetailsInfo.rfFacilities}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-8"><iais:code code="${adeFacilityInfo.name}"/></div>
                                    <div class="col-md-3">
                                        <div class="row">
                                            <label>
                                                <input type="radio" name="${adeFacilityInfo.facilityCode}" <c:if test="${adeFacilityInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.YES}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">Yes</span>
                                            <label>
                                                <input type="radio" name="${adeFacilityInfo.facilityCode}" <c:if test="${!adeFacilityInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.NO}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">No</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:if test="${facilityDetailsInfo.activities ne null && facilityDetailsInfo.activities.size() > 0}">
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
                            <c:forEach var="adeFacilityActivityInfo" items="${facilityDetailsInfo.activities}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-8"><iais:code code="${adeFacilityActivityInfo.activityType}"/></div>
                                    <div class="col-md-3">
                                        <div class="row">
                                            <label>
                                                <input type="radio" name="${adeFacilityActivityInfo.activityType}" data-radio-type="facilityActivityYes" <c:if test="${adeFacilityActivityInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.YES}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">Yes</span>
                                            <label>
                                                <input type="radio" name="${adeFacilityActivityInfo.activityType}" data-radio-type="facilityActivityNo" <c:if test="${!adeFacilityActivityInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.NO}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">No</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:if test="${facilityDetailsInfo.atpBats ne null && facilityDetailsInfo.atpBats.size() > 0}">
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
                            <c:forEach var="batInfo" items="${facilityDetailsInfo.atpBats}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-3"><iais:code code="${batInfo.schedule}"/></div>
                                    <div class="col-md-5"><bsb:bat-code code="${batInfo.batCode}"/></div>
                                    <div class="col-md-3">
                                        <div class="row">
                                            <label>
                                                <input type="radio" name="${batInfo.activityType}--v--${batInfo.batCode}" data-radio-type="facilityAgentRadioYes" data-bat-activityType="${batInfo.activityType}" <c:if test="${batInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.YES}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">Yes</span>
                                            <label>
                                                <input type="radio" name="${batInfo.activityType}--v--${batInfo.batCode}" data-radio-type="facilityAgentRadioNo" data-bat-activityType="${batInfo.activityType}" <c:if test="${!batInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.NO}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">No</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:if test="${facilityDetailsInfo.lspBats ne null && facilityDetailsInfo.lspBats.size() > 0}">
                    <div class="alert alert-info" role="alert">
                        <strong>
                            <h4>Recommendation for Approval to Large Scale Produce</h4>
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
                            <c:forEach var="batInfo" items="${facilityDetailsInfo.lspBats}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-3"><iais:code code="${batInfo.schedule}"/></div>
                                    <div class="col-md-5"><bsb:bat-code code="${batInfo.batCode}"/></div>
                                    <div class="col-md-3">
                                        <div class="row">
                                            <label>
                                                <input type="radio" name="${batInfo.activityType}--v--${batInfo.batCode}" data-radio-type="facilityAgentRadioYes" data-bat-activityType="${batInfo.activityType}" <c:if test="${batInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.YES}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">Yes</span>
                                            <label>
                                                <input type="radio" name="${batInfo.activityType}--v--${batInfo.batCode}" data-radio-type="facilityAgentRadioNo" data-bat-activityType="${batInfo.activityType}" <c:if test="${!batInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.NO}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">No</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:if test="${facilityDetailsInfo.sathBats ne null && facilityDetailsInfo.sathBats.size() > 0}">
                    <div class="alert alert-info" role="alert">
                        <strong>
                            <h4>Recommendation for Special Approval to Handle</h4>
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
                            <c:forEach var="batInfo" items="${facilityDetailsInfo.sathBats}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-3"><iais:code code="${batInfo.schedule}"/></div>
                                    <div class="col-md-5"><bsb:bat-code code="${batInfo.batCode}"/></div>
                                    <div class="col-md-3">
                                        <div class="row">
                                            <label>
                                                <input type="radio" name="${batInfo.activityType}--v--${batInfo.batCode}" data-radio-type="facilityAgentRadioYes" data-bat-activityType="${batInfo.activityType}" <c:if test="${batInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.YES}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">Yes</span>
                                            <label>
                                                <input type="radio" name="${batInfo.activityType}--v--${batInfo.batCode}" data-radio-type="facilityAgentRadioNo" data-bat-activityType="${batInfo.activityType}" <c:if test="${!batInfo.recommendApprove}">checked="checked"</c:if> value="${masterCodeConstants.NO}" <c:if test="${!facilityDetailsInfo.canEdit}">disabled="disabled"</c:if>/>
                                            </label>
                                            <span class="check-circle">No</span>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
                <c:if test="${facilityDetailsInfo.rfFifthBats ne null && facilityDetailsInfo.rfFifthBats.size() > 0}">
                    <div class="alert alert-info" role="alert">
                        <strong>
                            <h4>Recommendation for Handling of Fifth Schedule Toxin for Exempted Purpose</h4>
                        </strong>
                    </div>
                    <div class="panel-collapse">
                        <div class="panel-body">
                            <div class="row" style="font-weight: 700;text-align: center">
                                <div class="col-md-1">S/N</div>
                                <div class="col-md-5">Schedule</div>
                                <div class="col-md-6">Name of Toxin</div>
                            </div>
                            <c:forEach var="batInfo" items="${facilityDetailsInfo.rfFifthBats}" varStatus="status">
                                <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                    <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                    <div class="col-md-5">Fifth Schedule for Exempted Purposes</div>
                                    <div class="col-md-6"><bsb:bat-code code="${batInfo}"/></div>
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