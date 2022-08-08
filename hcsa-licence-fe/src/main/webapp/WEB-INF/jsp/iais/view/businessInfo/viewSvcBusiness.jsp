<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<div class="amended-service-info-gp">
    <label class="app-title">${currStepName}</label>
    <div class="amend-preview-info">
        <div class="form-check-gp">
            <div class="row">
                <div class="col-xs-12">
                    <c:forEach items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" var="businessDto" varStatus="status">
                        <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                            <p class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                                <strong >${businessDto.premType}: ${businessDto.premAddress}</strong>
                            </p>
                        </div>
                        <c:set var="isSpecialService" value="${businessDto.currService==AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL||businessDto.currService==AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}"/>
                        <c:set var="isSpecialMOSD" value="${businessDto.premisesType==ApplicationConsts.PREMISES_TYPE_MOBILE||businessDto.premisesType==ApplicationConsts.PREMISES_TYPE_REMOTE}"/>

                        <iais:row>
                            <c:set var="info"><iais:message key="NEW_ACK028"></iais:message></c:set>
                            <iais:field width="5" value="Business Name" info="${info}"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${businessDto.businessName}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="5" value="Contact No."/>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${businessDto.contactNo}"/>
                            </iais:value>
                        </iais:row>
                        <iais:row>
                            <iais:field width="5" value="Email"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${businessDto.emailAddr}"/>
                            </iais:value>
                        </iais:row>

                        <c:if test="${!isSpecialService&&!isSpecialMOSD}">
                            <iais:row>
                                <div class="col-md-12 col-xs-12">
                                    <label class="control-label">Operating Hours</label>
                                </div>
                                <div class="col-md-4 col-xs-4  hidden-xs hidden-sm">
                                    <label class="control-label">Weekly <span class="mandatory">*</span></label>
                                </div>
                                <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
                                    <label class="control-label">Start</label>
                                </div>
                                <div class="col-md-3 col-xs-3 input-padding hidden-xs hidden-sm">
                                    <label class="control-label">End</label>
                                </div>
                                <div class="col-md-2 col-xs-2 hidden-xs hidden-sm">
                                    <label class="control-label">24 Hours</label>
                                </div>
                            </iais:row>
                            <%@include file="viewWeeklyDetail.jsp"%>
                            <%@include file="viewPHDetail.jsp"%>
                            <%@include file="viewEventDetail.jsp"%>
                        </c:if>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
