<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="itemConfigDto" value="${item.itemConfigDto}"/>
<c:set var="oldItemConfigDto" value="${oldItem.itemConfigDto}"/>


<iais:row cssClass="form-level-${item.level} item-record">
    <c:choose>
        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TITLE}">
            <%--<tr>
                <td class="col-xs-6" colspan="2">
                    <div class="app-title item-label">
                        <c:out value="${itemConfigDto.displayInfo}"/>
                    </div>
                </td>
            </tr>--%>
            <div class="col-xs-12">
                <div class="app-title item-label form-level">
                    <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                </div>
            </div>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_GROUP_TITLE}">
            <%--<tr>
                <td class="col-xs-6" colspan="2">
                    <div class="app-title item-label">
                        <c:out value="${itemConfigDto.displayInfo}"/>
                    </div>
                </td>
            </tr>--%>
            <div class="col-xs-12">
                <div class="bold item-label form-level">
                    <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                    <c:if test="${batchSize > 1}">
                        <span>${item.seqNum + 1}</span>
                    </c:if>
                </div>
            </div>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SUB_TITLE}">
            <%--<tr>
                <td class="col-xs-6" colspan="2">
                    <div class="app-title item-label">
                        <c:out value="${itemConfigDto.displayInfo}"/>
                    </div>
                </td>
            </tr>--%>
            <div class="col-xs-12">
                <div class="bold item-label form-level">
                    <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                </div>
            </div>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_GROUP_SUB_TITLE}">
            <%--<tr>
                <td class="col-xs-6" colspan="2">
                    <div class="app-title item-label">
                        <c:out value="${itemConfigDto.displayInfo}"/>
                    </div>
                </td>
            </tr>--%>
            <div class="col-xs-12">
                <div class="bold item-label form-level">
                    <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                    <c:if test="${batchSize > 1}">
                        <span>${item.seqNum + 1}</span>
                    </c:if>
                </div>
            </div>
        </c:when>


        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_LABEL}">
            <%--<tr>
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
                            <c:out value="${oldItem.inputValue}"/>
                        </div>
                    </div>
                </td>
            </tr>--%>
            <div class="col-xs-6 col-md-6">
                <div class="form-check-label form-level" aria-label="premise-1-cytology">
                    <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                </div>
            </div>
            <div class="col-xs-6 col-md-6 margin-15">
                <div class="col-xs-6">
                    <div class="newVal display-padding" attr="${item.inputValue}">
                        <c:out value="${item.inputValue}"/>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="oldVal display-padding" attr="${oldItem.inputValue}" style="display: none">
                        <c:out value="${oldItem.inputValue}"/>
                    </div>
                </div>
            </div>
        </c:when>


        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_RADIO}">
            <%--<tr>
            <td class="col-xs-6">
                <div class="form-check-label" aria-label="premise-1-cytology">
                        ${itemConfigDto.displayInfo}
                </div>
            </td>

            <td>
                <div class="col-xs-6">
                    <div class="newVal " attr="${item.inputValue}">
                        <c:forEach var="idx" begin="0" end="${item.codes.size() - 1}">
                            <c:if test="${item.codes[idx] == item.inputValue}"><c:out
                                    value="${item.labels[idx]}"/></c:if>
                        </c:forEach>
                    </div>
                </div>

                <div class="col-xs-6">
                    <div class="oldVal " attr="${oldItem.inputValue}">
                        <c:forEach var="idx" begin="0" end="${item.codes.size() - 1}">
                            <c:if test="${oldItem.codes[idx] == oldItem.inputValue}"><c:out
                                    value="${oldItem.labels[idx]}"/></c:if>
                        </c:forEach>
                    </div>
                </div>
            </td>
            </tr>--%>
            <div class="col-xs-6 col-md-6">
                <div class="form-check-label form-level" aria-label="premise-1-cytology">
                    <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                </div>
            </div>
            <div class="col-xs-6 col-md-6 margin-15">
                <div class="col-xs-6">
                    <div class="newVal display-padding" attr="${item.inputValue}">
                        <c:forEach var="idx" begin="0" end="${item.codes.size() - 1}">
                            <c:if test="${item.codes[idx] == item.inputValue}"><c:out
                                    value="${item.labels[idx]}"/></c:if>
                        </c:forEach>
                    </div>
                </div>

                <div class="col-xs-6">
                    <div class="oldVal display-padding" attr="${oldItem.inputValue}">
                        <c:forEach var="idx" begin="0" end="${item.codes.size() - 1}">
                            <c:if test="${oldItem.codes[idx] == oldItem.inputValue}"><c:out
                                    value="${oldItem.labels[idx]}"/></c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:when>


        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_CHECKBOX}">
            <%--<tr>
                <td>
                    <div class="col-xs-6">
                        <div class="newVal form-check active" attr="${item.inputValue}">
                            <div class="form-check-label" aria-label="premise-1-cytology">
                                <span class="check-square"></span>
                                <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                            </div>
                        </div>
                    </div>
                </td>
                <td>
                    <div class="col-xs-6">
                        <div class="oldVal form-check active" attr="${oldItem.inputValue}">
                            <div class="form-check-label" aria-label="premise-1-cytology" style="display: none">
                                <span class="check-square"></span>
                                <c:out value="${oldItemConfigDto.displayInfo}" escapeXml="false"/>
                            </div>
                        </div>
                    </div>
                </td>
            </tr>--%>
            <div class="col-xs-6 col-md-6">
                <div class="newVal form-check active form-level" attr="${item.inputValue}">
                    <div class="form-check-label" aria-label="premise-1-cytology">
                        <span class="check-square"></span>
                        <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                    </div>
                </div>
            </div>
            <div class="col-xs-6 col-md-6">
                <div class="oldVal form-check active display-padding" attr="${oldItem.inputValue}">
                    <div class="form-check-label" aria-label="premise-1-cytology" style="display: none">
                        <span class="check-square"></span>
                        <c:out value="${oldItemConfigDto.displayInfo}" escapeXml="false"/>
                    </div>
                </div>
            </div>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_TEXT}">
            <c:if test="${!empty item.inputValue}">
                <%--<tr>
                    <td class="col-xs-6">
                        <div class="form-check-label" aria-label="premise-1-cytology">
                                ${itemConfigDto.displayInfo}
                        </div>
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
                </tr>--%>
                <div class="col-xs-6 col-md-6">
                    <div class="form-check-label form-level" aria-label="premise-1-cytology">
                        <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                    </div>
                </div>
                <div class="col-xs-6 col-md-6 margin-15">
                    <div class="col-xs-6">
                        <div class="newVal display-padding" attr="${item.inputValue}">
                            <c:out value="${item.inputValue}"/>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="oldVal display-padding" attr="${oldItem.inputValue}" style="display: none">
                            <iais:code code="${oldItem.inputValue}"/>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:when>

        <c:when test="${itemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SELECT || oldItemConfigDto.itemType == HcsaConsts.SUPFORM_ITEM_TYPE_SELECT}">
            <%--<tr>
                <td class="col-xs-6">
                    <div class="form-check-label" aria-label="premise-1-cytology">
                            ${itemConfigDto.displayInfo}
                    </div>
                </td>
                <td>
                    <div class="col-xs-6">
                        <div class="newVal " attr="${item.inputValue}">
                            <iais:code code="${item.inputValue}"/>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="oldVal " attr="${oldItem.inputValue}">
                            <iais:code code="${oldItem.inputValue}"/>
                        </div>
                    </div>
                </td>
            </tr>--%>
            <div class="col-xs-6 col-md-6">
                <div class="form-check-label form-level" aria-label="premise-1-cytology">
                    <c:out value="${itemConfigDto.displayInfo}" escapeXml="false"/>
                </div>
            </div>
            <div class="col-xs-6 col-md-6 margin-15">
                <div class="col-xs-6">
                    <div class="newVal display-padding" attr="${item.inputValue}">
                        <iais:code code="${item.inputValue}"/>
                    </div>
                </div>

                <div class="col-xs-6">
                    <div class="oldVal display-padding" attr="${oldItem.inputValue}">
                        <iais:code code="${oldItem.inputValue}"/>
                    </div>
                </div>
            </div>
        </c:when>
    </c:choose>
</iais:row>