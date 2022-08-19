<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>

<c:set var="itemConfigDto" value="${item.itemConfigDto}"/>

<c:set var="isCheckBox" value="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX}"/>

<iais:row cssClass="${isCheckBox ? 'form-check':''} form-level-${item.level} item-record ${itemConfigDto.id}">
    <c:choose>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE}">
            <div class="col-xs-12">
                <div class="app-title item-label" data-base="${itemConfigDto.id}"
                     data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                     data-parent="${itemConfigDto.parentItemId}" data-group="${groupId}"
                     data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                     data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}">
            <div class="col-xs-12">
                <div class="bold item-label" data-base="${itemConfigDto.id}"
                     data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                     data-parent="${itemConfigDto.parentItemId}" data-group="${groupId}"
                     data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                     data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${itemConfigDto.mandatoryType == 1}"><span class="mandatory">*</span></c:if>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}">
            <c:if test="${'SPECCON01' == item.specialCondition}">
                <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${itemConfigDto.mandatoryType == 1}"
                            value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
                <input type="hidden" name="${itemConfigDto.id}${item.seqNum}" value="" data-base="${itemConfigDto.id}" data-seq="${item.seqNum}"/>
                <div class="col-sm-7 col-xs-7 col-md-7" data-base="${itemConfigDto.id}"
                     data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                     data-parent="${itemConfigDto.parentItemId}" data-group="${groupId}"
                     data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                     data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}">
                    <p></p>
                </div>
            </c:if>
            <c:if test="${'SPECCON01' != item.specialCondition}">
            <div class="col-xs-12">
                <div class="item-label" data-base="${itemConfigDto.id}"
                     data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                     data-parent="${itemConfigDto.parentItemId}" data-group="${groupId}"
                     data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                     data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${itemConfigDto.mandatoryType == 1}"><span class="mandatory">*</span></c:if>
                </div>
            </div>
            </c:if>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO}">
            <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${itemConfigDto.mandatoryType == 1}"
                        value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
            <iais:value width="7" cssClass="col-md-7" style="padding-left: 0;">
                <c:forEach var="idx" begin="0" end="${item.codes.size() - 1}">
                    <div class="col-xs-12 col-md-${item.labelWidth} form-check">
                        <input class="form-check-input" type="radio" name="${itemConfigDto.id}${item.seqNum}" data-base="${itemConfigDto.id}"
                               data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                               data-parent="${itemConfigDto.parentItemId}" data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}"
                               data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                               data-group="${groupId}" data-id="${itemConfigDto.id}${item.codes[idx]}"
                               value="${item.codes[idx]}" id="${itemConfigDto.id}${item.codes[idx]}${item.seqNum}"
                               <c:if test="${item.codes[idx] == item.inputValue}">checked="checked"</c:if>/>
                        <label class="form-check-label" for="${itemConfigDto.id}${item.codes[idx]}${item.seqNum}">
                            <span class="check-circle"></span><c:out value="${item.labels[idx]}"/>
                        </label>
                    </div>
                </c:forEach>
            </iais:value>
            <iais:value cssClass="col-md-offset-5 col-md-7 col-xs-12">
                <span class="error-msg " name="iaisErrorMsg" id="error_${itemConfigDto.id}${item.seqNum}"></span>
            </iais:value>
        </c:when>
        <c:when test="${isCheckBox}">
            <input class="form-check-input" type="checkbox" name="${itemConfigDto.radioBatchNum}${item.seqNum}" data-base="${itemConfigDto.radioBatchNum}"
                   data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                   data-parent="${itemConfigDto.parentItemId}" data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}"
                   data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                   data-group="${groupId}" data-id="${itemConfigDto.id}"
                   value="${itemConfigDto.id}" id="${itemConfigDto.id}${item.seqNum}"
                   <c:if test="${item.inputValue == itemConfigDto.id}">checked="checked"</c:if> />
            <label class="form-check-label" for="${itemConfigDto.id}${item.seqNum}">
                <span class="check-square"></span><c:out value="${itemConfigDto.displayInfo}"/>
            </label>
            <c:if test="${item.lastChild}">
            <div class="special-error">
                <span class="error-msg " name="iaisErrorMsg" id="error_${itemConfigDto.id}${item.seqNum}"></span>
            </div>
            </c:if>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
            <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${itemConfigDto.mandatoryType == 1}"
                        data="${item.labelData}" value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
            <iais:value width="7" cssClass="col-md-7">
                <c:if test="${item.date}">
                    <input maxlength="${itemConfigDto.maxLength}" type="text" name="${itemConfigDto.id}${item.seqNum}" data-base="${itemConfigDto.id}"
                           data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                           data-parent="${itemConfigDto.parentItemId}" data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}"
                           data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                           data-group="${groupId}" value="${item.inputValue}"
                           autocomplete="off" placeholder="dd/mm/yyyy"
                           class="date_picker" ${item.endHtml} ${item.startHtml}/>
                </c:if>
                <c:if test="${not item.date}">
                    <input maxlength="${itemConfigDto.maxLength}" type="text" name="${itemConfigDto.id}${item.seqNum}" data-base="${itemConfigDto.id}"
                           data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}"
                           data-parent="${itemConfigDto.parentItemId}" data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}"
                           data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
                           data-group="${groupId}" value="${item.inputValue}" autocomplete="off"/>
                </c:if>
                <span class="error-msg " name="iaisErrorMsg" id="error_${itemConfigDto.id}${item.seqNum}"></span>
            </iais:value>
        </c:when>
    </c:choose>
</iais:row>