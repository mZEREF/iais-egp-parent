<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="itemConfigDto" value="${item.itemConfigDto}"/>
<c:set var="oldItemConfigDto" value="${oldItem.itemConfigDto}"/>


<iais:row cssClass="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX? 'form-check':''} form-level-${item.level} item-record">
    <c:choose>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE}">
            <tr>
                <td class="col-xs-12">
                    <div class="app-title item-label">
                        <c:out value="${itemConfigDto.displayInfo}"/>
                    </div>
                </td>
            </tr>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}">
            <tr>
                <td class="col-xs-12">
                    <div class="app-title item-label">
                        <c:out value="${itemConfigDto.displayInfo}"/>
                    </div>
                </td>
            </tr>
        </c:when>


        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}">
            <tr>
                <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                         ${itemConfigDto.displayInfo}
                    </p>
                </td>

                <td>
                    <div class="col-xs-6">
                        <div class="newVal " attr="${item.inputValue}">
                              <c:out value="${item.inputValue}"/>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="oldVal " attr="${oldItem.inputValue}" style="display: none">
                             <iais:code code="${oldItem.inputValue}"/>
                        </div>
                    </div>
                </td>
            </tr>
        </c:when>


        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO}">
            <tr>
            <td class="col-xs-6">
                <p class="form-check-label" aria-label="premise-1-cytology">
                        ${itemConfigDto.displayInfo}
                </p>
            </td>

            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="${item.inputValue}">
                          <c:out value="${item.inputValue}"/>
                    </div>
                    <c:if test="${item.codes[idx] == item.inputValue || oldItem.codes[idx] == oldItem.inputValue}">
                          <span class="newVal" attr="${item.labels[idx]}"><c:out value="${item.labels[idx]}"/></span>
                    </c:if>
                </div>
                <div class="col-xs-6">
                    <c:if test="${item.codes[idx] == item.inputValue || oldItem.codes[idx] == oldItem.inputValue}">
                        <span class="oldVal" attr="${oldItem.labels[idx]}" style="display: none">
                            <c:out value="${oldItem.labels[idx]}"/>
                        </span>
                    </c:if>
                </div>
            </td>
            </tr>
        </c:when>


        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX}">
            <tr>
                <td>
                <div class="col-xs-6 form-check active">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                        <span class="check-square"></span> ${itemConfigDto.displayInfo}
                    </p>
                </div>
                </td>
                <td>
                    <div class="col-xs-6 form-check active">
                        <p class="form-check-label" aria-label="premise-1-cytology" style="display: none">
                            <span class="check-square"></span> ${oldItemConfigDto.displayInfo}
                        </p>
                    </div>
                </td>
            </tr>
        </c:when>


        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
            <tr>
                <td class="col-xs-6">
                    <p class="form-check-label" aria-label="premise-1-cytology">
                            ${itemConfigDto.displayInfo}
                    </p>
                </td>

                <td>
                    <div class="col-xs-6">
                        <span class="newVal " attr="${item.inputValue}">
                              <c:out value="${item.inputValue}"/>
                        </span>
                    </div>
                    <div class="col-xs-6">
                        <span class="oldVal " attr="${oldItem.inputValue}" style="display: none">
                             <iais:code code="${oldItem.inputValue}"/>
                        </span>
                    </div>
                </td>
            </tr>
        </c:when>
    </c:choose>
</iais:row>