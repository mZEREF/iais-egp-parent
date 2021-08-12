<div class="form-check-gp">
    <p class="form-check-title">Please select the licence to amend:</p>
    <iais:pagination  param="renewLicSearchParam" result="renewLicSearchResult"/>
    <div class="table-gp">
        <table aria-describedby="" class="table">
            <thead>
            <tr >
                <th scope="col" ></th>
                <iais:sortableHeader needSort="true"  field="p.HCI_NAME" value="HCI Name" style="width:15%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Type" style="width:15%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No." style="width:20%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="PREMISES_TYPE" value="Mode of Service Delivery" style="width:15%" isFE="true"/>
                <iais:sortableHeader needSort="true"  field="ADDRESS" value="Address" style="width:30%" isFE="true"/>
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
                            <td>
                                <p class="visible-xs visible-sm table-row-title">HCI Name</p>
                                ${pool.hciName}<c:if test="${empty pool.hciName}">N/A</c:if>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                ${pool.svcId}
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                                ${pool.licenceNo}
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Mode of Service Delivery</p>
                                <c:if test="${'ONSITE'==pool.premisesType}">
                                    <c:out value="Premises"/>
                                </c:if>
                                <c:if test="${'CONVEYANCE'==pool.premisesType}">
                                    <c:out value="Conveyance"/>
                                </c:if>
                                <c:if test="${'EASMTS'==pool.premisesType}">
                                    <c:out value="Conveyance (in a mobile clinic / ambulance)"/>
                                </c:if>
                                <c:if test="${'OFFSITE'==pool.premisesType}">
                                    <c:out value="Off-site"/>
                                </c:if>
                            </td>
                            <td>
                                <p class="visible-xs visible-sm table-row-title">Address</p>
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
        <c:when test="${!empty renewLicSearchResult.rows}">
            <a class="btn btn-primary " onclick="guideSubmit('renew2','second')" href="javascript:void(0);">NEXT</a>
        </c:when>
    </c:choose>
</div>
