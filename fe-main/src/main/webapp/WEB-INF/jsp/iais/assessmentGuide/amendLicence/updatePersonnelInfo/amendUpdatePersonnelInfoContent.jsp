<div class="form-check-gp">

    <p class="form-check-title">You may select one HCI to amend at a time:</p>

    <iais:pagination  param="amendUpdatePersonnelSearchParam" result="amendUpdatePersonnelSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises type"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendUpdatePersonnelSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendUpdatePersonnelSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <label>
                                    <input id="licence${status.index + 1}" type="checkbox" name="appNos" value="${pool.licenceId}"   >${pool.hciName}
                                </label>
                            </td>
                            <td>${pool.addrType}</td>
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

    <a class="btn btn-primary "  onclick="Utils.submit('mainForm','amend4_1')" style="background: #1F92FF; color: white"  >NEXT</a>
</div>
