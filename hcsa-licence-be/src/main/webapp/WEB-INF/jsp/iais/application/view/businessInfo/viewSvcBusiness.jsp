<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<style>
    @media screen and (max-width: 768px) {
        .time-padding-left {
            padding-left: 0px;
        }
        .form-check{
            padding-left: 0px;
        }
    }
</style>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" var="businessDto" varStatus="status">
            <iais:row>
                <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                    <p><strong >${businessDto.premTypeNameOnly}: ${businessDto.premAddress}</strong></p>
                </div>
            </iais:row>
            <c:set var="isSpecialService" value="${currentPreviewSvcInfo.serviceCode==AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL||currentPreviewSvcInfo.serviceCode==AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}"/>
            <iais:row>
                <iais:field width="5" value="Business Name"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${businessDto.businessName}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Contact No."/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${businessDto.contactNo}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Email"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${businessDto.emailAddr}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <iais:field width="5" value="Corporate Website"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${businessDto.corporateWebsite}"/>
                </iais:value>
            </iais:row>

            <c:if test="${!isSpecialService}">
                <iais:row>
                    <div class="col-md-12 col-xs-12">
                        <label class="control-label"><strong>Operating Hours</strong></label>
                    </div>
                </iais:row>
                <iais:row>
                    <div class="col-md-4 col-xs-4 hidden-xs">
                        <label class="control-label"><strong>Weekly</strong></label>
                    </div>
                    <div class="col-md-8 col-sm-8 col-xs-8 hidden-xs">
                        <div class="col-md-4 col-xs-4 input-padding" style="padding-left: 0;">
                            <label class="control-label"><strong>Start</strong></label>
                        </div>
                        <div class="col-md-4 col-xs-4 input-padding">
                            <label class="control-label"><strong>End</strong></label>
                        </div>
                        <div class="col-md-3 col-xs-3">
                            <label class="control-label"><strong>24 Hours</strong></label>
                        </div>
                    </div>
                </iais:row>
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
