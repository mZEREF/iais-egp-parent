<div class="form-check-gp">

    <p class="form-check-title">Scenario: if "Change in licensee's information" and system detects that the licensee is a "solo/company" entity
    </p>

    <iais:pagination  param="amendUpdateLicenseeSearchParam" result="amendUpdateLicenseeSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <th></th>
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises type"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendUpdateLicenseeSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendUpdateLicenseeSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <div class="form-check">
                                    <input class="form-check-input licenceCheck" id="amendLicenseId" type="radio"
                                           name="amendLicenseId" value="amendLicInfoId${status.index}" aria-invalid="false"/>
                                    <label class="form-check-label" for="amendLicenseId"><span
                                            class="check-circle"></span>
                                    </label>
                                    <input type="hidden" name="amendLicenseId${status.index}" value="<iais:mask name= "amendLicenseId${status.index}" value="${pool.licenceId}"/>"/>
                                </div>
                            </td>
                            <td><iais:code code="${pool.addrType}"/></td>
                            <td>${pool.licenceNo}</td>
                            <td>${pool.premisesType}</td>
                            <td>${pool.address}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <a class="btn btn-primary " onclick="guideSubmit('amendLic6','second')">NEXT</a>
</div>
