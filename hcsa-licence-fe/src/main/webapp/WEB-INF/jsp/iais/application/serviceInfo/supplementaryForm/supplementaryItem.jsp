<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="item" value=""/>
<c:set var="itemConfigDto" value="${item.itemConfigDto}"/>

<iais:row cssClass="form-level-${item.level} ${itemConfigDto.addMoreBatchNum}${item.seqNum}">
<c:choose>
    <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE || itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE || itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}}">
        <c:set var="class1" value=""/>
        <c:if test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE}"><c:set var="class1" value="app-title"/></c:if>
        <c:if test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}"><c:set var="class1" value="bold"/></c:if>
        <div class="col-xs-12">
            <div class="${class1}"><c:out value="${itemConfigDto.displayInfo}"/></div>
        </div>
    </c:when>
    <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO}">
        <iais:field width="5" cssClass="col-md-5" mandatory="${itemConfigDto.mandatoryType == 1}" value="${itemConfigDto.displayInfo}"/>
        <div class="col-xs-12">
            <c:forEach var="idx" begin="1" end="${item.codes.size()}">
                <div class="col-md-${item.labelWidth} form-check">
                    <input <c:if test="${item.codes[idx] == item.inputValue}">checked="checked"</c:if> class="form-check-input" type="radio" name="${itemConfigDto.id}${item.seqNum}" value="${item.codes[idx]}" data-base="${itemConfigDto.id}" data-parent="${itemConfigDto.parentItemId}">
                    <label class="form-check-label" ><span class="check-circle"><c:out value="${item.labels[idx]}"/></span></label>
                </div>
            </c:forEach>
        </div>
        <iais:value cssClass="col-md-offset-4 col-md-8 col-xs-12">
            <span class="error-msg " name="iaisErrorMsg" id="error_${itemConfigDto.id}${item.seqNum}"></span>
        </iais:value>
    </c:when>
    <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
        <iais:row cssClass="form-level-${item.level} ${itemConfigDto.addMoreBatchNum}${item.seqNum}">
            <iais:field width="5" cssClass="col-md-5" mandatory="${itemConfigDto.mandatoryType == 1}" value="${itemConfigDto.displayInfo}"/>
            <iais:value width="7" cssClass="col-md-3">
                <iais:input maxLength="${itemConfigDto.maxLength}" type="text" cssClass="idNo" name="${itemConfigDto.id}${item.seqNum}" value="${item.inputValue}" data-base="${itemConfigDto.id}" data-parent="${itemConfigDto.parentItemId}"/>
            </iais:value>
        </iais:row>
    </c:when>
</c:choose>
</iais:row>
<c:if test="${item.batchEnd}">
    <div class="col-md-12 col-xs-12 addMoreDiv">
        <span class="addMoreBtn" style="color:deepskyblue;cursor:pointer;" data-target="${itemConfigDto.addMoreBatchNum}">
            <span style="">+ Add more</span>
        </span>
    </div>
</c:if>