<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>

<c:set var="itemConfigDto" value="${item.itemConfigDto}"/>

<iais:row cssClass="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX? 'form-check':''} form-level-${item.level} item-record">
    <c:choose>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE}">
            <div class="col-xs-12">
                <div class="app-title item-label">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_GROUP_TITLE}">
            <div class="col-xs-12">
                <div class="app-title item-label">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${batchSize > 1}">
                        <span>${item.seqNum + 1}</span>
                    </c:if>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}">
            <div class="col-xs-12">
                <div class="bold item-label">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_GROUP_SUB_TITLE}">
            <div class="col-xs-12">
                <div class="bold item-label">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                    <c:if test="${batchSize > 1}">
                        <span>${item.seqNum + 1}</span>
                    </c:if>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}">
            <iais:field width="5" cssClass="col-md-5" value="${itemConfigDto.displayInfo}"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${item.inputValue}"/>
            </iais:value>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO}">
            <iais:field width="5" cssClass="col-md-5 item-label" value="${itemConfigDto.displayInfo}"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:forEach var="idx" begin="0" end="${item.codes.size() - 1}">
                    <c:if test="${item.codes[idx] == item.inputValue}"><c:out value="${item.labels[idx]}"/></c:if>
                </c:forEach>
            </iais:value>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX}">
            <c:if test="${itemConfigDto.id == item.inputValue}">
                <div class="form-check active">
                    <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                        ${itemConfigDto.displayInfo}
                    </div>
                </div>
            </c:if>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
            <c:if test="${!empty item.inputValue}">
                <iais:field width="5" cssClass="col-md-5" value="${itemConfigDto.displayInfo}"/>
                <iais:value width="7" cssClass="col-md-7" display="true">
                    <c:out value="${item.inputValue}"/>
                </iais:value>
            </c:if>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SELECT}">
            <iais:field width="5" cssClass="col-md-5" value="${itemConfigDto.displayInfo}"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:code code="${item.inputValue}" />
            </iais:value>
        </c:when>
    </c:choose>
</iais:row>