<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit" value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row form-horizontal">
    <c:if test="${AppSubmissionDto.needEditController }">
        <c:if test="${(isRfc || isRenew) && !isRfi}">
            <iais:row>
                <div class="text-right app-font-size-16">
                    <a class="back" id="RfcSkip" href="javascript:void(0);">
                        Skip<span style="display: inline-block;">&nbsp;</span><em class="fa fa-angle-right"></em>
                    </a>
                </div>
            </iais:row>
        </c:if>
        <c:set var="canEdit" value="${AppSubmissionDto.appEditSelectDto.serviceEdit}"/>
    </c:if>
    <c:forEach var="appSvcSpecialServiceInfo" items="${appSvcSpecialServiceInfoList}" varStatus="status">
        <iais:row>
            <div class="col-xs-12">
                <div class="app-title"><c:out value="${appSvcSpecialServiceInfo.premName}"/></div>
                <div class="font-18 bold">Address: <c:out value="${appSvcSpecialServiceInfo.premAddress}"/></div>
            </div>
        </iais:row>
        <div class="panel-group" id="specialService" role="tablist" aria-multiselectable="true">
            <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">
                <div class="panel panel-default">
                    <div class="panel-heading " role="tab">
                        <h4 class="panel-title">
                            <a role="button" class="collapsed" data-toggle="collapse" href="#${status.index}${subSvcRelStatus.index}SSI" aria-expanded="true" aria-controls="${status.index}${subSvcRelStatus.index}SSI">
                                <c:out value="${specialServiceSectionDto.newSvcName}"/>
                            </a>
                        </h4>
                        <c:set var="appSvcSuplmFormDto" value="${specialServiceSectionDto.appSvcSuplmFormDto}"/>
                    </div>
                    <div id="${status.index}${subSvcRelStatus.index}SSI" class="panel-collapse collapse">
                        <input type="hidden" class ="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                        <div class="panel-body">
                            <c:choose>
                                <c:when test="${specialServiceSectionDto.emptyDto}">
                                    <div class="panel-main-content">
                                        <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <%@include file="specialServicePersonnel.jsp" %>
                                    <c:if test="${not empty appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
                                        <div class="panel-main-content normal-label personnel-content">
                                            <iais:row>
                                                <div class="col-md-12 col-xs-12 edit-content">
                                                    <c:if test="${'true' == canEdit}">
                                                        <div class="text-right app-font-size-16">
                                                            <a class="edit" href="javascript:void(0);">
                                                                <em class="fa fa-pencil-square-o"></em><span>&nbsp;</span>Edit
                                                            </a>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </iais:row>
                                            <c:set var="itemPrefix" value="${status.index}${subSvcRelStatus.index}"/>
                                            <input type="hidden" class="isPartEdit" name="${itemPrefix}isPartEditSup" value="0"/>
                                            <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
                                                <c:set var="count" value="${appSvcSuplmGroupDto.count}"/>
                                                <c:set var="baseSize" value="${appSvcSuplmGroupDto.baseSize}"/>
                                                <c:if test="${count > 0}">
                                                    <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
                                                    <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="suplmFormStatus">
                                                        <%@ include file="/WEB-INF/jsp/iais/application/serviceInfo/supplementaryForm/item.jsp"%>
                                                    </c:forEach>
                                                    <c:if test="${not empty groupId}">
                                                        <iais:value cssClass="col-xs-12 error_${groupId}">
                                                            <span class="error-msg " name="iaisErrorMsg" id="error_${groupId}"></span>
                                                        </iais:value>
                                                        <div class="form-group col-md-12 col-xs-12 addMoreDiv" data-group="${groupId}" data-prefix="${itemPrefix}">
                                                            <input class="not-clear" type="hidden" value="${count}" name="${itemPrefix}${groupId}"/>
                                                            <input class="not-clear" type="hidden" value="${appSvcSuplmGroupDto.maxCount}" name="${itemPrefix}${groupId}-max"/>
                                                            <span class="addMoreBtn" style="color:deepskyblue;cursor:pointer;">
                                                                <span style=""><c:out value="${appSvcSuplmGroupDto.addMoreName}"/></span>
                                                            </span>
                                                        </div>
                                                    </c:if>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:forEach>
</div>
<%@include file="specialServicesFormFun.jsp" %>
<%@ include file="/WEB-INF/jsp/iais/application/serviceInfo/supplementaryForm/itemFun.jsp" %>