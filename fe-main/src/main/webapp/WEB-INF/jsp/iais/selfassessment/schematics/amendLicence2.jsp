<div class="form-check-gp">

    <p class="form-check-title">You may select one HCI to amend at a time:</p>
    <iais:pagination  param="PremisesSearchParam" result="PremisesSearchResult"/>

    <div class="table-gp">
        <table class="table">
            <thead>
            <tr >
                <iais:sortableHeader needSort="true"  field="NAME" value="Name"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises Type"/>
                <iais:sortableHeader needSort="false"  field="ADDRESS" value="Address"/>
                <iais:sortableHeader needSort="true"  field="HCI_CONTACT_NO" value="Contact"/>
                <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Active Licence"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty PremisesSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${PremisesSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <label>
                                    <input id="licence${status.index + 1}" type="checkbox" name="rfc2LicIds" value="${pool.licenceId}"   >${pool.hciName}
                                </label>
                            </td>
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
                            <td>${pool.hciContactNo}</td>
                            <td>${pool.svcId}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

    <a class="btn btn-primary "  onclick="Utils.submit('mainForm','amend2')"  >NEXT</a>
</div>
