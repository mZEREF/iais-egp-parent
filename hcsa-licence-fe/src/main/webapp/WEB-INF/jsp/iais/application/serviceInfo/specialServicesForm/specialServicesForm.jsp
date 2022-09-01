<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>

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
            <div class="col-xs-12 app-title">
                <p><c:out value="${appSvcSpecialServiceInfo.premName}"/></p>
                <p>Address: <c:out value="${appSvcSpecialServiceInfo.premAddress}"/></p>
            </div>
        </iais:row>
        <div class="panel-group" id="specialService" role="tablist" aria-multiselectable="true">
            <c:forEach var="specialServiceSectionDto" items="${appSvcSpecialServiceInfo.specialServiceSectionDtoList}" varStatus="subSvcRelStatus">
                <div class="panel panel-default">
                    <div class="panel-heading " role="tab">
                        <h4 class="panel-title">
                            <a role="button" class="" data-toggle="collapse" href="#${status.index}${subSvcRelStatus.index}SSI" aria-expanded="true" aria-controls="${status.index}${subSvcRelStatus.index}SSI">
                                <strong><c:out value="${specialServiceSectionDto.svcName}"/></strong>
                            </a>
                        </h4>
                        <c:set var="appSvcSuplmFormDto" value="${specialServiceSectionDto.appSvcSuplmFormDto}"/>
                        <c:set var="DirMaxCount" value="0"/>
                        <c:set var="NurMaxCount" value="0"/>
                        <c:forEach var="maxCount" items="${specialServiceSectionDto.maxCount}">
                            <c:if test="${maxCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR}">
                                <c:set var="DirMaxCount" value="${maxCount.value}"/>
                            </c:if>
                            <c:if test="${maxCount.key == ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR}">
                                <c:set var="NurMaxCount" value="${maxCount.value}"/>
                            </c:if>
                        </c:forEach>
                        <input type="hidden" class ="DirMaxCount" value="${DirMaxCount}"/>
                        <input type="hidden" class ="NurMaxCount" value="${NurMaxCount}"/>
                    </div>
                    <div id="${status.index}${subSvcRelStatus.index}SSI" class="panel-collapse collapse in">
                        <input type="hidden" class ="isPartEdit" name="isPartEdit${status.index}" value="0"/>
                        <div class="panel-body">
                            <c:choose>
                                <c:when test="${DirMaxCount==0&&NurMaxCount==0&&empty appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
                                    <div class="panel-main-content">
                                        <p><h4><iais:message key="NEW_ACK039"/></h4></p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:if test="${DirMaxCount!=0}">
                                        <div class="panel-main-content">
                                            <c:choose>
                                                <c:when test="${specialServiceSectionDto.appSvcDirectorDtoList != null && specialServiceSectionDto.appSvcDirectorDtoList.size()>1}">
                                                    <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                                                    <c:set var="DirectorDtoListLength" value="${specialServiceSectionDto.appSvcDirectorDtoList.size()}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}DirectorDtoListLength" value="1"/>
                                                    <c:set var="DirectorDtoListLength" value="1"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:forEach begin="0" end="${DirectorDtoListLength - 1}" step="1" varStatus="direStatus">
                                                <c:set var="index" value="${direStatus.index}"/>
                                                <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcDirectorDtoList[index]}"/>
                                                <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}dir"/>
                                                <c:set var="title" value="Emergency Department Director"/>
                                                <%@include file="specialServiceDetail.jsp" %>
                                            </c:forEach>
                                            <iais:row>
                                                <div class="col-md-12 col-xs-12 addDiv <c:if test="${DirectorDtoListLength >= DirMaxCount}">hidden</c:if>">
                                                    <input type="hidden" class ="disDiOrNu" name="disDiOrNu" value="Di"/>
                                                    <span class="addBtn" style="color:deepskyblue;cursor:pointer;">
                                                    <span style="">Add more</span>
                                                </span>
                                                </div>
                                            </iais:row>
                                        </div>
                                    </c:if>
                                    <c:if test="${NurMaxCount!=0}">
                                        <div class="panel-main-content">
                                            <c:choose>
                                                <c:when test="${specialServiceSectionDto.appSvcChargedNurseDtoList != null && specialServiceSectionDto.appSvcChargedNurseDtoList.size()>1}">
                                                    <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}NurseDtoListLength" value="${specialServiceSectionDto.appSvcChargedNurseDtoList.size()}"/>
                                                    <c:set var="NurseDtoListLength" value="${specialServiceSectionDto.appSvcChargedNurseDtoList.size()}"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <input class="length" type="hidden" name="${status.index}${subSvcRelStatus.index}NurseDtoListLength" value="1"/>
                                                    <c:set var="NurseDtoListLength" value="1"/>
                                                </c:otherwise>
                                            </c:choose>
                                            <c:forEach begin="0" end="${NurseDtoListLength - 1}" step="1" varStatus="nurStatus">
                                                <c:set var="index" value="${nurStatus.index}"/>
                                                <c:set var="appSvcPersonnelDto" value="${specialServiceSectionDto.appSvcChargedNurseDtoList[index]}"/>
                                                <c:set var="prefix" value="${status.index}${subSvcRelStatus.index}nur"/>
                                                <c:set var="title" value="Emergency Department Nurse-in-charge"/>
                                                <%@include file="specialServiceDetail.jsp"%>
                                            </c:forEach>
                                            <iais:row>
                                                <div class="col-md-12 col-xs-12 addDiv <c:if test="${NurseDtoListLength >= NurMaxCount}">hidden</c:if>">
                                                    <input type="hidden" class ="disDiOrNu" name="disDiOrNu" value="Nu"/>
                                                    <span class="addBtn" style="color:deepskyblue;cursor:pointer;">
                                                    <span style="">Add more</span>
                                                </span>
                                                </div>
                                            </iais:row>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
                                        <div class="panel-main-content">
                                            <c:set var="itemPrefix" value="${status.index}${subSvcRelStatus.index}"/>
                                            <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
                                                <c:set var="count" value="${appSvcSuplmGroupDto.count}"/>
                                                <c:set var="baseSize" value="${appSvcSuplmGroupDto.baseSize}"/>
                                                <c:if test="${count > 0}">
                                                    <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
                                                    <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="suplmFormStatus">
                                                        <c:if test="${not empty groupId && suplmFormStatus.index % baseSize == 0}">
                                                            <iais:row cssClass="removeEditRow">
                                                                <div class="col-xs-12 text-right removeEditDiv" data-group="${groupId}" data-seq="${item.seqNum}" data-prefix="${itemPrefix}">
                                                                    <h4 class="text-danger text-right">
                                                                        <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                                                                    </h4>
                                                                </div>
                                                            </iais:row>
                                                        </c:if>
                                                        <%@ include file="/WEB-INF/jsp/iais/application/serviceInfo/supplementaryForm/item.jsp"%>
                                                    </c:forEach>
                                                    <iais:value cssClass="col-xs-12 error_${groupId}">
                                                        <span class="error-msg " name="iaisErrorMsg" id="error_${groupId}"></span>
                                                    </iais:value>
                                                    <c:if test="${not empty groupId}">
                                                        <div class="form-group col-md-12 col-xs-12 addMoreDiv" data-group="${groupId}" data-prefix="${itemPrefix}">
                                                            <input class="not-clear" type="hidden" value="${count}" name="${itemPrefix}${groupId}"/>
                                                            <input class="not-clear" type="hidden" value="${appSvcSuplmGroupDto.maxCount}" name="${itemPrefix}${groupId}-max"/>
                                                            <span class="addMoreBtn" style="color:deepskyblue;cursor:pointer;">
                                                                <span style="">+ Add more</span>
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
<script>
    $(function() {
        $('.addBtn').on('click', function () {
            var dis=$(this).closest('div.addDiv').find('input.disDiOrNu').val();
            addPersonnel($(this).closest('div.panel-main-content'),dis);
        });
    });
</script>
