<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<c:set var="mosdName" value="${ApplicationConsts.MODE_OF_SVC_DELIVERY}" />

<c:if test="${showHeadingSign}">
    <c:set var="headingSign" value="${coMap.premises == 'premises' ? 'completed' : 'incompleted'}"/>
</c:if>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}" id="headingPremise" role="tab">
        <h4 class="panel-title">
            <a role="button" class="collapsed" data-toggle="collapse" href="#collapsePremise${documentIndex}" aria-expanded="true" aria-controls="collapsePremise" name="printControlNameForApp">
                ${mosdName}
            </a>
        </h4>
    </div>
    <div class="panel-collapse collapse <c:if test="${!empty printFlag}">in</c:if>" id="collapsePremise${documentIndex}" role="tabpanel" aria-labelledby="headingPremise">
        <div class="panel-body">
            <c:if test="${(AppSubmissionDto.appEditSelectDto==null||AppSubmissionDto.appEditSelectDto.premisesEdit)
                    && empty printView && isSingle != 0}">
                <p><div class="text-right app-font-size-16"><a href="#" id="premisesEdit"><em class="fa fa-pencil-square-o"></em>Edit</a></div></p>
            </c:if>
            <c:forEach var="appGrpPremDto" items="${AppSubmissionDto.appGrpPremisesDtoList}"
                       varStatus="status">
                <iais:row>
                    <div class="app-title">${mosdName} ${appGrpPremDto.seqNum}</div>
                </iais:row>
                <div class="panel-main-content form-horizontal min-row">
                    <%@include file="premises/viewPremisesContent.jsp"%>
                </div>
            </c:forEach>
            <c:if test="${empty retriggerGiro && FirstView && !('APTY004' == AppSubmissionDto.appType || 'APTY005' == AppSubmissionDto.appType)}">
                <iais:row>
                    <p class="font-size-14">Please note that you will not be able to add  or remove any mode of service delivery here.</p>
                    <p class="font-size-14">If you wish to do so, please click <a href="#">here</a>.</p>
                </iais:row>
            </c:if>
        </div>
    </div>
</div>
