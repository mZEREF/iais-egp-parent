<p class="form-check-title">Please select a draft application to resume</p>
<iais:pagination param="draftAppSearchParam" result="draftAppSearchResult"/>
<div class="table-gp">
    <table class="table">
        <thead>
        <tr align="center">
            <th></th>
            <iais:sortableHeader needSort="true" field="APPLICATION_NO" value="Application Number"/>
            <iais:sortableHeader needSort="true" field="app_type" value="Type"/>
            <iais:sortableHeader needSort="true" field="service_id" value="Service"/>
            <iais:sortableHeader needSort="true" field="STATUS" value="Status"/>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty draftAppSearchResult.rows}">
                <tr>
                    <td colspan="15">
                        <iais:message key="ACK018" escape="true"/>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="pool" items="${draftAppSearchResult.rows}" varStatus="status">
                    <tr>
                        <td>
                            <div class="form-check hover">
                                <input class="form-check-input" id="draftApp" type="radio"
                                       name="resumeAppNos" aria-invalid="false" value="draftApp${status.index}">
                                <label class="form-check-label" for="draftApp"><span
                                        class="check-circle"></span></label>
                            </div>
                            <input type="hidden" name="draftApp${status.index}No" value="${pool.applicationNo}"/>
                            <input type="hidden" name="draftApp${status.index}Type" value="${pool.applicationType}"/>
                        </td>
                        <td>${pool.applicationNo}</td>
                        <td><iais:code code="${pool.applicationType}"/></td>
                        <td>${pool.serviceId}</td>
                        <td><iais:code code="${pool.status}"/></td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
<a class="btn btn-primary " onclick="guideSubmit('resumDraft','second')">NEXT</a>
