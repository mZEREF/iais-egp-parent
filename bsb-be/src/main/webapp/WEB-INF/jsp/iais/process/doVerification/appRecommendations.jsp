<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@page import="sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants" %>
<%--@elvariable id="rfFacilityDetailsInfo" type="sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.RFFacilityDetailsInfo"--%>
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
                        <td style="font-weight: bold; text-align: right" class="col-sm-6">Facility Classification</td>
                        <td style="padding-left: 20px"><iais:code code="${rfFacilityDetailsInfo.classification}"/></td>
                    </tr>
                    <tr>
                        <td style="font-weight: bold; text-align: right" class="col-sm-6">Facility Activity Type</td>
                        <td style="padding-left: 20px"><iais:code code="${rfFacilityDetailsInfo.facilityActivityType}"/></td>
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
                <c:choose>
                    <c:when test="${MasterCodeConstants.ACTIVITY_SP_HANDLE_PV_POTENTIAL eq rfFacilityDetailsInfo.facilityActivityType}">
                        <%--this activity type can apply multi facility, only display facility info--%>
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
                                <c:forEach var="rfFacilityInfo" items="${rfFacilityDetailsInfo.rfFacilityInfoList}" varStatus="status">
                                    <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                        <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                        <div class="col-md-8"><iais:code code="${rfFacilityInfo.facilityName}"/></div>
                                        <div class="col-md-3">
                                            <div class="row">
                                                <label>
                                                    <input type="radio" name="${rfFacilityInfo.id}" <c:if test="${rfFacilityInfo.status eq MasterCodeConstants.PROCESSING_STATUS_APPROVAL}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_APPROVAL}"/>
                                                </label>
                                                <span class="check-circle">Yes</span>
                                                <label>
                                                    <input type="radio" name="${rfFacilityInfo.id}" <c:if test="${rfFacilityInfo.status eq MasterCodeConstants.PROCESSING_STATUS_REJECT or rfFacilityInfo.status eq null}">checked="checked"</c:if> value="${MasterCodeConstants.PROCESSING_STATUS_REJECT}"/>
                                                </label>
                                                <span class="check-circle">No</span>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:when>
                    <c:when test="${MasterCodeConstants.ACTIVITY_SP_HANDLE_FIFTH_SCHEDULE_EXEMPTED eq rfFacilityDetailsInfo.facilityActivityType}">
                        <%--this activity type can apply multi bat, only display bat info--%>
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
                                <c:forEach var="rfBatInfo" items="${rfFacilityDetailsInfo.rfBatInfoList}" varStatus="status">
                                    <div class="row" style="text-align: center;border-top:1px solid #D1D1D1;padding: 10px 0 ">
                                        <div class="col-md-1"><c:out value="${status.index + 1}"/></div>
                                        <div class="col-md-5"><iais:code code="${rfBatInfo.schedule}"/></div>
                                        <div class="col-md-6"><c:out value="${rfBatInfo.nameOfToxin}"/></div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                    </c:otherwise>
                </c:choose>
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