<%@ taglib prefix="C" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="form-check-gp">

    <p class="form-check-title">You select one licence to change at a time:</p>

    <iais:pagination  param="amendUpdateLicenseeSearchParam" result="amendUpdateLicenseeSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr >
                <th></th>
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises Type"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendUpdateLicenseeSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendUpdateLicenseeSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <div class="form-check">
                                    <input class="form-check-input licenceCheck" id="amendLicenseId" type="radio" <c:choose>
                                        <c:when test="${fn:contains(licence_err_list, pool.licenceId)}">
                                            checked
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${status.index == 0}">checked</c:if>
                                        </c:otherwise>
                                    </c:choose>
                                           name="amendLicenseId" value="amendLicenseId${status.index}" aria-invalid="false"/>
                                    <label class="form-check-label" for="amendLicenseId"><span
                                            class="check-circle"></span>
                                    </label>
                                    <input type="hidden" name="amendLicenseId${status.index}" value="<iais:mask name= "amendLicenseId${status.index}" value="${pool.licenceId}"/>"/>
                                </div>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${empty pool.hciName}">N/A</c:when>
                                    <c:otherwise>
                                        <iais:code code="${pool.hciName}"/>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>${pool.svcId}</td>
                            <td>${pool.licenceNo}</td>
                            <td>
                                <c:if test="${'ONSITE'==pool.premisesType}">
                                    <c:out value="On-site"/>
                                </c:if>
                                <c:if test="${'CONVEYANCE'==pool.premisesType}">
                                    <c:out value="Conveyance"/>
                                </c:if>
                                <c:if test="${'OFFSITE'==pool.premisesType}">
                                    <c:out value="Off-site"/>
                                </c:if>
                            </td>
                            <td>${pool.address}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <c:choose>
        <c:when test="${!empty amendUpdateLicenseeSearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('amendLic3','second')">NEXT</a>
        </c:when>
    </c:choose>
</div>
