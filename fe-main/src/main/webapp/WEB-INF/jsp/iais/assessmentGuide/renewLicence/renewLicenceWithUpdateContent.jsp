<div class="form-check-gp">
    <p class="form-check-title">Select the licence(s) to renew:</p>
    <iais:pagination  param="renewLicUpdateSearchParam" result="renewLicUpdateSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr >
                <th></th>
                <iais:sortableHeader needSort="true"  field="HCI_NAME" value="HCI Name" style="width:15%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="ADDR_TYPE" value="Type" style="width:15%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No." style="width:20%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Premises Type" style="width:15%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address" style="width:30%" isFE="true"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty renewLicUpdateSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="GENERAL_ACK018" escape="true"/>
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
                            <td>
                                <c:choose>
                                    <c:when test="${pool.premisesDtoList.size() == 1}">
                                        <P>${pool.premisesDtoList[0]}</P>
                                    </c:when>
                                    <c:otherwise>
                                        <select>
                                            <option value ="">Multiple</option>
                                            <c:forEach items="${pool.premisesDtoList}" var="address" varStatus="index">
                                                <option value ="${address}">${address}</option>
                                            </c:forEach>
                                        </select>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <c:choose>
        <c:when test="${!empty renewLicUpdateSearchResult.rows}">
            <a class="btn btn-primary" id="renew-next-btn" onclick="guideSubmit('renew','second')" href="javascript:void(0);">NEXT</a>
        </c:when>
    </c:choose>
</div>
