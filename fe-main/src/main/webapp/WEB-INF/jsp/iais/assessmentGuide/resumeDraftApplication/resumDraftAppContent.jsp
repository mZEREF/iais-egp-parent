<div class="form-check-gp">
<p class="form-check-title">Please select a draft application to resume</p>
<iais:pagination param="draftAppSearchParam" result="draftAppSearchResult"/>
<div class="table-gp">
    <table aria-describedby="" class="table">
        <thead>
        <tr >
            <th scope="col" ></th>
            <iais:sortableHeader needSort="true" field="APPLICATION_NO" value="Application Number" isFE="true"/>
            <iais:sortableHeader needSort="true" field="app_type" value="Type" isFE="true"/>
            <iais:sortableHeader needSort="true" field="code" value="Service" isFE="true"/>
            <iais:sortableHeader needSort="false" field="STATUS" value="Status" isFE="true"/>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty draftAppSearchResult.rows}">
                <tr>
                    <td colspan="15">
                        <iais:message key="GENERAL_ACK018" escape="true"/>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="pool" items="${draftAppSearchResult.rows}" varStatus="status">
                    <tr>
                        <td>
                            <div class="form-check hover">
                                <input class="form-check-input" id="draftApp" type="radio" <c:if test="${status.index == 0}">checked</c:if>
                                       name="resumeAppNos" aria-invalid="false" value="draftApp${status.index}">
                                <label class="form-check-label" for="draftApp"><span
                                        class="check-circle"></span></label>
                            </div>
                            <input type="hidden" name="draftApp${status.index}No" value="${pool.applicationNo}"/>
                            <input type="hidden" name="draftApp${status.index}Type" value="${pool.applicationType}"/>
                        </td>
                        <td>
                                <p class="visible-xs visible-sm table-row-title">Application Number</p>
                                ${pool.applicationNo}
                        </td>
                        <td>
                                <p class="visible-xs visible-sm table-row-title">Type</p>
                                <iais:code code="${pool.applicationType}"/>
                        </td>
                        <td>
                                <p class="visible-xs visible-sm table-row-title">Service</p>
                                ${pool.serviceId}
                        </td>
                        <td>
                                <p class="visible-xs visible-sm table-row-title">Status</p>
                                <iais:code code="${pool.status}"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
<c:choose>
    <c:when test="${!empty draftAppSearchResult.rows}">
        <a class="btn btn-primary " onclick="guideSubmit('resumDraft','second')" href="javascript:void(0);">NEXT</a>
    </c:when>
</c:choose>
</div>

