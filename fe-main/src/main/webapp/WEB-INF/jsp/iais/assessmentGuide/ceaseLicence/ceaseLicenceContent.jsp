<p class="form-check-title">Please select licence(s) to cease:</p>
<div class="form-check">
    <iais:pagination param="ceaseLicenceSearchParam" result="ceaseLicenceSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr >
                <th></th>
                <iais:sortableHeader needSort="true" field="HCI_NAME" value="HCI Name"/>
                <iais:sortableHeader needSort="true" field="ADDR_TYPE" value="Type"/>
                <iais:sortableHeader needSort="true" field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true" field="PREMISES_TYPE" value="Premises Type"/>
                <iais:sortableHeader needSort="true" field="ADDRESS" value="Address"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty ceaseLicenceSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${ceaseLicenceSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <div class="form-check hover">
                                    <input class="form-check-input" id="ceaseLicence" type="checkbox" name="ceaseLicIds" aria-invalid="false" value="ceaseLicence${status.index}">
                                    <label class="form-check-label" for="ceaseLicence"><span class="check-square"></span></label>
                                </div>
                                <input type="hidden" name="ceaseLicence${status.index}" value="<iais:mask name= "ceaseLicence${status.index}" value="${pool.licenceId}"/>"/>
                            </td>
                            <td>${pool.hciName}<c:if test="${empty pool.hciName}">N/A</c:if></td>
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
        <c:when test="${!empty ceaseLicenceSearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('ceaseLic','second')">NEXT</a>
        </c:when>
    </c:choose>
</div>