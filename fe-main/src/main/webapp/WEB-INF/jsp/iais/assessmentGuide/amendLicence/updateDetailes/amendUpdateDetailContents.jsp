<div class="form-check-gp">

    <p class="form-check-title">Select the licence where the subsumed
        service(s) and/or service subcategory(ies) will be provided:
    </p>

    <iais:pagination  param="PremisesSearchParam" result="PremisesSearchResult"/>

    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <th></th>
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises type"/>
                <iais:sortableHeader needSort="false"  field="ADDRESS" value="Address"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty PremisesSearchResult.rows }">
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
                                    <input class="form-check-input licenceCheck" id="amendDetail" type="checkbox"
                                           name="amendDetailId" value="amendDetailId${status.index}" aria-invalid="false"/>
                                    <label class="form-check-label" for="amendDetail"><span
                                            class="check-square"></span>
                                    </label>
                                    <input type="hidden" name="amendDetailId${status.index}" value="<iais:mask name= "amendDetailId${status.index}" value="${pool.licenceId}"/>"/>
                                </div>
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

    <a class="btn btn-primary "  onclick="Utils.submit('mainForm','cease')" style="background: #1F92FF; color: white"  >NEXT</a>
</div>
