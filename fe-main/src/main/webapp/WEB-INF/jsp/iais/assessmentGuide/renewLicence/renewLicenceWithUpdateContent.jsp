<div class="form-check-gp">
    <p class="form-check-title">Select the licence(s) to renew:</p>
    <iais:pagination  param="renewLicUpdateSearchParam" result="renewLicUpdateSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <th></th>
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name" style="width:15%"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type" style="width:15%"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No." style="width:20%"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises type" style="width:15%"/>
                <iais:sortableHeader needSort="false"  field="ADDRESS" value="Address" style="width:30%"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty renewLicUpdateSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${renewLicUpdateSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <div class="form-check">
                                    <input class="form-check-input licenceCheck" id="licenceNo" type="checkbox"
                                           name="renewLicenId" value="renew2LicenId${status.index}" aria-invalid="false" <c:if test="${fn:contains(licence_err_list, pool.licenceId)}">checked</c:if> onclick="renewChk()"/>
                                    <label class="form-check-label" for="licenceNo"><span
                                            class="check-square"></span>
                                    </label>
                                    <input type="hidden" name="renew2LicenId${status.index}" value="<iais:mask name= "renew2LicenId${status.index}" value="${pool.licenceId}"/>"/>
                                </div>
                            </td>
                            <td>${pool.hciName}</td>
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
    <c:choose>
        <c:when test="${!empty renewLicUpdateSearchResult.rows}">
            <a class="btn btn-primary" id="renew-next-btn" onclick="guideSubmit('renew','second')">NEXT</a>
        </c:when>
    </c:choose>
</div>
