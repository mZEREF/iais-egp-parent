<div class="form-check-gp">
    <p class="form-check-title">You select one licence to amend at a time:</p>
    <iais:pagination  param="PremisesSearchParam" result="PremisesSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <th></th>
                <iais:sortableHeader needSort="true"  field="NAME" value="Name"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises type"/>
                <iais:sortableHeader needSort="false"  field="ADDRESS" value="Adderss"/>
                <iais:sortableHeader needSort="true"  field="HCI_CONTACT_NO" value="Contact"/>
                <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Actice Licence"/>
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
                                <div class="form-check">
                                    <input class="form-check-input licenceCheck" id="amendHciId" type="checkbox"
                                           name="amendHciId" value="amendHciId${status.index}" aria-invalid="false"/>
                                    <label class="form-check-label" for="amendHciId"><span
                                            class="check-square"></span>
                                    </label>
                                    <input type="hidden" name="amendHciId${status.index}" value="<iais:mask name= "amendHciId${status.index}" value="${pool.licenceId}"/>"/>
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

    <a class="btn btn-primary "  onclick="Utils.submit('mainForm','amend2')" style="background: #1F92FF; color: white"  >NEXT</a>
</div>
