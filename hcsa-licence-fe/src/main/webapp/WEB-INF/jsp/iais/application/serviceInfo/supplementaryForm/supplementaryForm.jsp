<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<style>
    .normal-label label {
        font-weight: normal;
    }

    .special-error .error-msg:not(:empty) {
        margin-top: 25px;
        display: inline-block;
    }
</style>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit"
       value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>

<div class="row form-horizontal normal-label">
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
    <iais:row>
        <div class="col-xs-12">
            <h2 class="app-title"><c:out value="${currStepName}"/></h2>
            <p><span class="error-msg" name="iaisErrorMSg" id="error_psnMandatory"></span></p>
        </div>
    </iais:row>

    <c:set var="appSvcSuplmFormDto" value="${currSvcInfoDto.appSvcSuplmFormDto}"/>

    <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
        <c:set var="count" value="${appSvcSuplmGroupDto.count}"/>
        <c:set var="baseSize" value="${appSvcSuplmGroupDto.baseSize}"/>
        <c:if test="${count > 0}">
            <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
            <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                <c:if test="${not empty groupId && status.index % baseSize == 0}">
                    <iais:row cssClass="removeEditRow">
                        <div class="col-xs-12 text-right removeEditDiv" data-group="${groupId}" data-seq="${item.seqNum}">
                            <h4 class="text-danger text-right">
                                <em class="fa fa-times-circle del-size-36 removeBtn cursorPointer"></em>
                            </h4>
                        </div>
                    </iais:row>
                </c:if>
                <%@ include file="item.jsp" %>
            </c:forEach>
            <iais:value cssClass="col-xs-12 error_${groupId}">
                <span class="error-msg " name="iaisErrorMsg" id="error_${groupId}"></span>
            </iais:value>
            <c:if test="${not empty groupId}">
                <div class="form-group col-md-12 col-xs-12 addMoreDiv" data-group="${groupId}">
                    <input type="hidden" value="${count}" name="${groupId}"/>
                    <input type="hidden" value="${appSvcSuplmGroupDto.maxCount}" name="${groupId}-max"/>
                    <span class="addMoreBtn" style="color:deepskyblue;cursor:pointer;">
                        <span style="">+ Add more</span>
                    </span>
                </div>
            </c:if>
        </c:if>
    </c:forEach>
</div>
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="itemFun.jsp" %>
<script type="text/javascript">
    $(function () {

    });

</script>
