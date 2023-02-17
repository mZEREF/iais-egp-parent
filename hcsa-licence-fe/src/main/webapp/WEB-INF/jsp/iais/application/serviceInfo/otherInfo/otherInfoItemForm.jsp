<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="iais" uri="http://www.ecq.com/iais" %>

<input type="hidden" name="applicationType" value="${AppSubmissionDto.appType}"/>
<input type="hidden" id="isEditHiddenVal" class="person-content-edit" name="isEdit"
       value="${!isRfi && AppSubmissionDto.appType == 'APTY002'? '1' : '0'}"/>
<c:set var="itemPrefix" value="${appSvcOtherInfoDto.premisesVal}"/>
<div class="oitem <c:if test="${'1' != provideTop}">hidden</c:if> ${itemPrefix}" data-prefix="${itemPrefix}">
    <c:forEach var="appSvcSuplmGroupDto" items="${appSvcSuplmFormDto.appSvcSuplmGroupDtoList}">
        <c:set var="count" value="${appSvcSuplmGroupDto.count}"/>
        <c:set var="baseSize" value="${appSvcSuplmGroupDto.baseSize}"/>
        <c:if test="${count > 0}">
            <c:set var="groupId" value="${appSvcSuplmGroupDto.groupId}"/>
            <c:forEach var="item" items="${appSvcSuplmGroupDto.appSvcSuplmItemDtoList}" varStatus="status">
                <%@ include file="/WEB-INF/jsp/iais/application/serviceInfo/supplementaryForm/item.jsp" %>
            </c:forEach>
            <c:if test="${not empty groupId}">
                <iais:value cssClass="col-xs-12 error_${groupId}">
                    <span class="error-msg " name="iaisErrorMsg" id="error_${groupId}"></span>
                </iais:value>
                <div class="form-group col-md-12 col-xs-12 addMoreDiv hidden" data-group="${groupId}" data-prefix="${itemPrefix}  ">
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
<%@include file="/WEB-INF/jsp/include/validation.jsp" %>
<%@include file="/WEB-INF/jsp/iais/application/serviceInfo/supplementaryForm/itemFun.jsp" %>
<script type="text/javascript">
    function checkIvItemEvent() {
        let rId = $('input[name="ivRadioId"]').val();
        let $target = $('.item-record [data-curr="'+ rId +'"]');
        $target.change(function () {
            let $tag = $(this);
            if (isEmptyNode($tag)) {
                return;
            }
            let prefix = $tag.data('prefix');
            if (isEmpty(prefix)) {
                prefix = "";
            }
            console.log("prefix:"+prefix);
            let value = $(this).val();
            console.log("val:"+value);
            ivText(value,prefix);
        });

    }
</script>
