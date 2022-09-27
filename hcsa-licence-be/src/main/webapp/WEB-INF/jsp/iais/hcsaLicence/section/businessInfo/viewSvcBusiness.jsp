<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<div class="amended-service-info-gp">
    <label class="title-font-size">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <c:forEach var="businessDto" items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" varStatus="status">
                    <c:set var="oldBusiness" value="${currentPreviewSvcInfo.oldAppSvcRelatedInfoDto.appSvcBusinessDtoList[status.index]}"/>
                    <c:set var="isSpecialService" value="${currentPreviewSvcInfo.serviceCode==AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL||currentPreviewSvcInfo.serviceCode==AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}"/>
                    <div class="col-xs-12">
                        <span class="newVal" attr="${businessDto.premType}:${businessDto.premAddress}">
                            <strong >${businessDto.premTypeNameOnly}</strong>: ${businessDto.premAddress}
                        </span>
                    </div>
                    <div class="col-xs-12">
                        <span class="oldVal" style="display: none" attr="${oldBusiness.premType}:${oldBusiness.premAddress}">
                            <strong >${oldBusiness.premTypeNameOnly}</strong>: ${oldBusiness.premAddress}
                        </span>
                    </div>
                    <span class="col-md-6 col-xs-6"></span>
                    <table  class="col-xs-12" aria-describedby="">
                        <thead style="display: none">
                        <tr>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tr>
                            <td class="col-md-6 col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Business Name</p>
                            </td>
                            <td>
                                <div class="col-md-6 col-xs-6">
                                    <div class="newVal" attr="${businessDto.businessName}">
                                        <c:out value="${businessDto.businessName}"></c:out>
                                    </div>
                                </div>
                                <div class="col-md-6 col-xs-6">
                                    <div class="oldVal" style="display: none" attr="${oldBusiness.businessName}">
                                        <c:out value="${oldBusiness.businessName}"></c:out>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-md-6 col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Contact No.</p>
                            </td>
                            <td>
                                <div class="col-md-6 col-xs-6">
                                    <div class="newVal" attr="${businessDto.contactNo}">
                                        <c:out value="${businessDto.contactNo}"></c:out>
                                    </div>
                                </div>
                                <div class="col-md-6 col-xs-6">
                                    <div class="oldVal" style="display: none" attr="${oldBusiness.contactNo}">
                                        <c:out value="${oldBusiness.contactNo}"></c:out>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="col-md-6 col-xs-6">
                                <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>Email</p>
                            </td>
                            <td>
                                <div class="col-md-6 col-xs-6">
                                    <div class="newVal" attr="${businessDto.emailAddr}">
                                        <c:out value="${businessDto.emailAddr}"></c:out>
                                    </div>
                                </div>
                                <div class="col-md-6 col-xs-6">
                                    <div class="oldVal" style="display: none" attr="${oldBusiness.emailAddr}">
                                        <c:out value="${oldBusiness.emailAddr}"></c:out>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                    <c:if test="${!isSpecialService}">
                        <tr>
                            <div class="col-md-12 col-xs-12">
                                <label class="control-label">Operating Hours</label>
                            </div>
                        </tr>
                        <tr>
                            <td class="col-md-6 col-xs-6">
                                <div class="col-md-6 col-sm-6 col-xs-6">
                                    <label class="control-label">Weekly</label>
                                </div>
                            </td>
                            <td>
                                <div class="col-md-6 col-sm-6 col-xs-6">
                                    <div class="col-md-4 col-sm-4 col-xs-4 hidden-xs" style="margin-left: -5px">
                                        <label class="control-label">Start</label>
                                    </div>
                                    <div class="col-md-4 col-sm-4 col-xs-4 hidden-xs">
                                        <label class="control-label">End</label>
                                    </div>
                                    <div class="col-md-3 col-sm-3 col-xs-3 hidden-xs">
                                        <label class="control-label">24 Hours</label>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <c:if test="${businessDto.weeklyDtoList.size()>0}">
                            <%@include file="viewWeeklyDetail.jsp"%>
                        </c:if>
                        <c:if test="${businessDto.phDtoList.size()>0}">
                            <%@include file="viewPHDetail.jsp"%>
                        </c:if>
                        <c:if test="${businessDto.eventDtoList.size()>0}">
                            <%@include file="viewEventDetail.jsp"%>
                        </c:if>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </div>
</div>