<div class="form-check-gp">
    <p class="form-check-title">You may select one HCI to amend at a time:</p>
    <iais:pagination  param="amendHCISearchParam" result="amendHCISearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <th></th>
                <iais:sortableHeader needSort="true"  field="NAME" value="Name"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises type"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Adderss"/>
                <iais:sortableHeader needSort="true"  field="HCI_CONTACT_NO" value="Contact"/>
                <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Actice Licence"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendHCISearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendHCISearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <div class="form-check">
                                    <input class="form-check-input licenceCheck" id="amendLicenseId" type="radio" <c:if test="${status.index == 0}">checked</c:if>
                                           name="amendLicenseId" value="amendLicenseId${status.index}" aria-invalid="false"/>
                                    <label class="form-check-label" for="amendLicenseId"><span
                                            class="check-circle"></span>
                                    </label>
                                    <input type="hidden" name="amendLicenseId${status.index}" value="<iais:mask name= "amendLicenseId${status.index}" value="${pool.licenceId}"/>"/>
                                    <input type="hidden" name="amendLicenseId${status.index}hiddenIndex" value="<iais:mask name= "amendLicenseId${status.index}hiddenIndex" value="${status.index}"/>"/>
                                    <input type="hidden" name="amendLicenseId${status.index}premiseId" value="<iais:mask name= "amendLicenseId${status.index}premiseId" value="${pool.premisesId}"/>"/>
                                </div>
                            </td>
                            <td>${pool.hciName}</td>
                            <td>${pool.premisesType}</td>
                            <td>${pool.address}</td>
                            <td>${pool.hciContactNo}</td>
                            <td>${pool.svcId}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <c:choose>
        <c:when test="${!empty amendHCISearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('amendLic2','second')">NEXT</a>
        </c:when>
    </c:choose>
</div>
