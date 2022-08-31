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
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}">
            <div class="col-xs-12">
                <div class="bold item-label">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}">
            <div class="col-xs-12">
                <div class="item-label">
                    <c:out value="${itemConfigDto.displayInfo}"/>
                </div>
            </div>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO}">
            <iais:field width="5" cssClass="col-md-5 item-label" value="${itemConfigDto.displayInfo}"/>
            <iais:value width="7" cssClass="col-md-7">
                <c:forEach var="idx" begin="0" end="${item.codes.size() - 1}">
                    <div class="form-check <c:if test="${item.codes[idx] == item.inputValue}">active"</c:if>>
                        <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-circle"></span>
                            <c:out value="${item.labels[idx]}"/>
                        </div>
                    </div>
                </c:forEach>
            </iais:value>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX}">
            <c:if test="${itemConfigDto.id == item.inputValue}">
                <div class="form-check active">
                    <div class="form-check-label" aria-label="premise-1-cytology"><span class="check-square"></span>
                        <c:out value="${itemConfigDto.displayInfo}"/>
                    </div>
                </div>
            </c:if>
        </c:when>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
            <iais:field width="5" cssClass="col-md-5" value="${itemConfigDto.displayInfo}"/>
            <iais:value width="7" cssClass="col-md-7">
                <c:out value="${item.inputValue}"/>
            </iais:value>
        </c:when>
    </c:choose>
</iais:row>