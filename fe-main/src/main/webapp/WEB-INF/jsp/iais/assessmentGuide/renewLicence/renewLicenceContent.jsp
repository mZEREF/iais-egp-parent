<div class="form-check-gp">
    <p class="form-check-title">You may select one licence at a time to update the changes:</p>
    <iais:pagination  param="renewLicSearchParam" result="renewLicSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr >
                <th></th>
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name" style="width:15%"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type" style="width:15%"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No." style="width:20%"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises Type" style="width:15%"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address" style="width:30%"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty renewLicSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${renewLicSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <div class="form-check">
                                    <input class="form-check-input licenceCheck" id="licenceNo" type="radio"
                                           <c:choose>
                                               <c:when test="${fn:contains(licence_err_list, pool.licenceId)}">
                                                   checked
                                               </c:when>
                                               <c:otherwise>
                                                   <c:if test="${status.index == 0}">checked</c:if>
                                               </c:otherwise>
                                           </c:choose>
                                           <c:if test="${status.index == 0}">checked</c:if>
                                           name="renewLicenId" value="renew1LicenId${status.index}" aria-invalid="false"/>
                                    <label class="form-check-label" for="licenceNo"><span
                                            class="check-circle"></span>
                                    </label>
                                    <input type="hidden" name="renew1LicenId${status.index}" value="<iais:mask name= "renew1LicenId${status.index}" value="${pool.licenceId}"/>"/>
                                </div>
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
        <c:when test="${!empty renewLicSearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('renew2','second')">NEXT</a>
        </c:when>
    </c:choose>
</div>
