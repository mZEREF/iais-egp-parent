<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="itemConfigDto" value="${item.itemConfigDto}"/>

<iais:row cssClass="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX? 'form-check':''} form-level-${item.level} item-record ${itemConfigDto.id}">
    <c:choose>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE}">
            <div class="col-xs-12">
                <div class="app-title item-label" data-base="${itemConfigDto.id}" data-mandatory="${itemConfigDto.mandatoryType}"
                     data-parent="${itemConfigDto.parentItemId}" data-group="${groupId}"
                     data-conditionItemId="${itemConfigDto.conditionItemId}" data-specialCondition="${itemConfigDto.specialConditionType}"
                     data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}">
            <div class="col-xs-12">
                <div class="bold item-label" data-base="${itemConfigDto.id}" data-mandatory="${itemConfigDto.mandatoryType}"
                     data-parent="${itemConfigDto.parentItemId}" data-group="${groupId}"
                     data-conditionItemId="${itemConfigDto.conditionItemId}" data-specialCondition="${itemConfigDto.specialConditionType}"
                     data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}">
            <div class="col-xs-12">
                <div class="item-label" data-base="${itemConfigDto.id}" data-mandatory="${itemConfigDto.mandatoryType}"
                     data-parent="${itemConfigDto.parentItemId}" data-group="${groupId}"
                     data-conditionItemId="${itemConfigDto.conditionItemId}" data-specialCondition="${itemConfigDto.specialConditionType}"
                     data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}">
                        <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO}">
            <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${itemConfigDto.mandatoryType == 1}"
                        value="${itemConfigDto.displayInfo}"/>
            <div class="col-xs-12">
                <c:forEach var="idx" begin="1" end="${item.codes.size()}">
                    <div class="col-md-${item.labelWidth} form-check">
                        <input class="form-check-input" type="radio" name="${itemConfigDto.id}${item.seqNum}"
                               data-base="${itemConfigDto.id}" data-mandatory="${itemConfigDto.mandatoryType}"
                               data-parent="${itemConfigDto.parentItemId}" data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}"
                               data-conditionItemId="${itemConfigDto.conditionItemId}" data-specialCondition="${itemConfigDto.specialConditionType}"
                               data-group="${groupId}" data-id="${itemConfigDto.id}${item.codes[idx]}"
                               value="${item.codes[idx]}" id="${itemConfigDto.id}${item.codes[idx]}${item.seqNum}"
                               <c:if test="${item.codes[idx] == item.inputValue}">checked="checked"</c:if>/>
                        <label class="form-check-label" for="${itemConfigDto.id}${item.codes[idx]}${item.seqNum}">
                            <span class="check-circle"><c:out value="${item.labels[idx]}"/></span>
                        </label>
                    </div>
                </c:forEach>
            </div>
            <iais:value cssClass="col-md-offset-4 col-md-8 col-xs-12">
                <span class="error-msg " name="iaisErrorMsg" id="error_${itemConfigDto.id}${item.seqNum}"></span>
            </iais:value>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX}">
            <input type="checkbox" name="${itemConfigDto.radioBatchNum}${item.seqNum}"
                   data-base="${itemConfigDto.radioBatchNum}" data-mandatory="${itemConfigDto.mandatoryType}"
                   data-parent="${itemConfigDto.parentItemId}" data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}"
                   data-conditionItemId="${itemConfigDto.conditionItemId}" data-specialCondition="${itemConfigDto.specialConditionType}"
                   data-group="${groupId}" data-id="${itemConfigDto.id}"
                   value="${itemConfigDto.id}" id="${itemConfigDto.id}${item.seqNum}"
                   <c:if test="${item.inputValue == itemConfigDto.id}">checked="checked"</c:if> />
            <label class="form-check-label" for="${itemConfigDto.id}${item.seqNum}">
                <span class="check-square"></span><c:out value="${itemConfigDto.displayInfo}"/>
            </label>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
            <iais:field width="5" cssClass="col-md-5" mandatory="${itemConfigDto.mandatoryType == 1}"
                        value="${itemConfigDto.displayInfo}"/>
            <iais:value width="7" cssClass="col-md-3">
                <c:if test="${SUPFORM_ITEM_TYPE_TEXT.SUPFORM_DATA_TYPE_DATE == itemConfigDto.dataType}">

                </c:if>
                <c:if test="${}">

                </c:if>
                <input maxlength="${itemConfigDto.maxLength}" type="text" name="${itemConfigDto.id}${item.seqNum}"
                       data-base="${itemConfigDto.id}" data-parent="${itemConfigDto.parentItemId}" data-seq="${item.seqNum}"
                       data-conditionItemId="${itemConfigDto.conditionItemId}" data-specialCondition="${itemConfigDto.specialConditionType}"
                       data-group="${groupId}" value="${item.inputValue}" />
            </iais:value>
        </c:when>
    </c:choose>
</iais:row>