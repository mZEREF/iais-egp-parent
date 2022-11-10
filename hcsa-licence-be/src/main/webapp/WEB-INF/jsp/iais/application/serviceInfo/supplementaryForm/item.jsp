<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil" %>

<c:set var="itemConfigDto" value="${item.itemConfigDto}"/>
<c:set var="isCheckBox" value="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX}"/>
<c:set var="itemData">
    data-base="${itemConfigDto.id}" data-group="${groupId}"
    data-mandatory="${itemConfigDto.mandatoryType}" data-mandatory-cond="${item.mandatoryCondition}" data-parent="${itemConfigDto.parentItemId}"
    data-condition="${itemConfigDto.conditionItemId}" data-specialcondition="${item.specialCondition}"
    data-curr="${itemConfigDto.id}" data-seq="${item.seqNum}" data-prefix="${itemPrefix}"
</c:set>
<iais:row cssClass="${isCheckBox ? 'form-check ':''}form-level-${item.level} item-record ${itemConfigDto.id} ${itemPrefix}">
    <c:choose>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE}">
            <div class="col-xs-12">
                <div class="app-title item-label ${not empty groupId ? 'group-title' : ''}" ${itemData}>
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_GROUP_TITLE}">
            <div class="col-xs-12">
                <div class="app-title item-label ${not empty groupId ? 'group-title' : ''}" ${itemData}>
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${not empty groupId}">
                        <span class="${itemPrefix}-${groupId}"></span>
                    </c:if>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}">
            <div class="col-xs-12">
                <div class="bold item-label" ${itemData}>
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${itemConfigDto.mandatoryType == 1}"><span class="mandatory">*</span></c:if>
                    <c:if test="${not empty groupId}">
                        <span class="${itemPrefix}-${groupId}"></span>
                    </c:if>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_GROUP_SUB_TITLE}">
            <div class="col-xs-12">
                <div class="bold item-label ${not empty groupId ? 'group-title' : ''}" ${itemData}>
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${itemConfigDto.mandatoryType == 1}"><span class="mandatory">*</span></c:if>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}">
            <c:if test="${'SPECCON01' == item.specialCondition || 'SPECCON04' == item.specialCondition}" var="speLabel">
                <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${itemConfigDto.mandatoryType == 1}"
                            value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
                <input type="hidden" name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" value=""
                       data-base="${itemPrefix}${itemConfigDto.id}" data-seq="${item.seqNum}"/>
                <div class="col-sm-7 col-xs-7 col-md-7" ${itemData}>
                    <p></p>
                </div>
            </c:if>
            <c:if test="${not speLabel}">
            <div class="col-xs-12">
                <div class="item-label ${itemData}">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${itemConfigDto.mandatoryType == 1}"><span class="mandatory">*</span></c:if>
                </div>
            </div>
            </c:if>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL_PLUS}">
            <c:if test="${'SPECCON01' == item.specialCondition || 'SPECCON04' == item.specialCondition}" var="speLabel">
                <iais:field width="5" cssClass="col-md-5 item-label font-weight" mandatory="${itemConfigDto.mandatoryType == 1}"
                            value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
                <input type="hidden" name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" value=""
                       data-base="${itemPrefix}${itemConfigDto.id}" data-seq="${item.seqNum}"/>
                <div class="col-sm-7 col-xs-7 col-md-7" ${itemData}>
                    <p class="font-weight"></p>
                </div>
            </c:if>
            <c:if test="${not speLabel}">
                <div class="col-xs-12">
                    <div class="item-label font-weight ${itemData}">
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
                        <input class="form-check-input" type="radio" ${itemData}
                               name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" value="${item.codes[idx]}"
                               data-id="${itemPrefix}${itemConfigDto.id}${item.codes[idx]}"
                               id="${itemPrefix}${itemConfigDto.id}${item.codes[idx]}${item.seqNum}"
                               <c:if test="${item.codes[idx] == item.inputValue}">checked="checked"</c:if>/>
                        <label class="form-check-label" for="${itemPrefix}${itemConfigDto.id}${item.codes[idx]}${item.seqNum}">
                            <span class="check-circle"></span><c:out value="${item.labels[idx]}"/>
                        </label>
                    </div>
                </c:forEach>
            </iais:value>
            <iais:value cssClass="col-md-offset-5 col-md-7 col-xs-12">
                <span class="error-msg " name="iaisErrorMsg" id="error_${itemPrefix}${itemConfigDto.id}${item.seqNum}"></span>
            </iais:value>
        </c:when>

        <c:when test="${isCheckBox}">
            <input class="form-check-input" type="checkbox" ${itemData}
                   name="${itemPrefix}${itemConfigDto.radioBatchNum}${item.seqNum}" value="${itemConfigDto.id}"
                   data-id="${itemPrefix}${itemConfigDto.id}" id="${itemPrefix}${itemConfigDto.id}${item.seqNum}"
                   <c:if test="${item.inputValue == itemConfigDto.id}">checked="checked"</c:if> />
            <label class="form-check-label" for="${itemPrefix}${itemConfigDto.id}${item.seqNum}">
                <span class="check-square"></span>${itemConfigDto.displayInfo}
            </label>
            <c:if test="${item.lastChild}">
            <div class="special-error">
                <span class="error-msg " name="iaisErrorMsg" id="error_${itemPrefix}${itemConfigDto.id}${item.seqNum}"></span>
            </div>
            </c:if>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
            <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${itemConfigDto.mandatoryType == 1}"
                        data="${item.labelData}" value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
            <iais:value width="7" cssClass="col-md-7">
                <c:if test="${item.date}">
                    <iais:input type="text" data="${itemData} ${item.endHtml} ${item.startHtml}" cssClass="date_picker"
                                name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" value="${item.inputValue}" placeholder="dd/mm/yyyy" />
                </c:if>
                <c:if test="${not item.date}">
                    <iais:input type="text" data="${itemData}" maxLength="${itemConfigDto.maxLength}"
                                name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" value="${item.inputValue}" />
                </c:if>
            </iais:value>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_BOLD}">
            <iais:field width="5" cssClass="col-md-5 item-label font-weight" mandatory="${itemConfigDto.mandatoryType == 1}"
                        data="${item.labelData}" value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
            <iais:value width="7" cssClass="col-md-7">
                <c:if test="${item.date}">
                    <iais:input type="text" data="${itemData} ${item.endHtml} ${item.startHtml}" cssClass="date_picker"
                                name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" value="${item.inputValue}" placeholder="dd/mm/yyyy" />
                </c:if>
                <c:if test="${not item.date}">
                    <iais:input type="text" data="${itemData}" maxLength="${itemConfigDto.maxLength}"
                                name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" value="${item.inputValue}" />
                </c:if>
            </iais:value>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SELECT}">
            <iais:field width="5" cssClass="col-md-5 item-label" mandatory="${itemConfigDto.mandatoryType == 1}"
                        data="${item.labelData}" value="${itemConfigDto.displayInfo}${itemConfigDto.mandatoryType == 2 ? ' ' : ''}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select data="${itemData}" name="${itemPrefix}${itemConfigDto.id}${item.seqNum}" codeCategory="${MasterCodeUtil.CATE_TDO_TYPE}" value="${item.inputValue}" firstOption="Please Select"/>
            </iais:value>
        </c:when>
    </c:choose>
</iais:row>