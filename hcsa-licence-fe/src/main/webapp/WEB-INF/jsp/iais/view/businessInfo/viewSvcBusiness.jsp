<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts" %>
<div class="amended-service-info-gp">
    <iais:row>
        <label class="app-title">${currStepName}</label>
    </iais:row>
    <div class="amend-preview-info form-horizontal min-row">
        <c:forEach items="${currentPreviewSvcInfo.appSvcBusinessDtoList}" var="businessDto" varStatus="status">
            <iais:row>
                <div  class="col-xs-12" style="margin-bottom: 1%;margin-top: 1%">
                    <p><strong >${businessDto.premType}: ${businessDto.premAddress}</strong></p>
                </div>
            </iais:row>
            <c:set var="isSpecialService" value="${businessDto.currService==AppServicesConsts.SERVICE_CODE_ACUTE_HOSPITAL||businessDto.currService==AppServicesConsts.SERVICE_CODE_COMMUNITY_HOSPITAL}"/>
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


            <c:if test="${!isSpecialService}">
                <iais:row>
                    <div class="col-md-12 col-xs-12">
                        <label class="control-label">Operating Hours</label>
                    </div>
                </iais:row>
                <iais:row>
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <label class="control-label">Weekly</label>
                    </div>
                    <div class="col-md-6 col-sm-6 col-xs-6">
                        <div class="col-md-4  col-sm-4 col-xs-4 input-padding hidden-xs" style="padding-left: 0;">
                            <label class="control-label">Start</label>
                        </div>
                        <div class="col-md-4 col-sm-4 col-xs-4 input-padding hidden-xs">
                            <label class="control-label">End</label>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-3 hidden-xs">
                            <label class="control-label">24 Hours</label>
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
