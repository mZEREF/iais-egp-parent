<p class="form-check-title">Please select an application to withdraw:</p>
<iais:pagination param="withdrawAppParam" result="withdrawAppResult"/>
<div class="table-gp">
    <table class="table">
        <thead>
        <tr align="center">
            <th width="2%"></th>
            <iais:sortableHeader needSort="false" field="APPLICATION_NO" value="Application Number" style="width:20%"/>
            <iais:sortableHeader needSort="false" field="app_type" value="Type" style="width:20%"/>
            <iais:sortableHeader needSort="false" field="service_id" value="Service" style="width:20%"/>
            <iais:sortableHeader needSort="false" field="STATUS" value="Status" style="width:20%"/>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${empty withdrawAppResult.rows}">
                <tr>
                    <td colspan="15">
                        <iais:message key="ACK018" escape="true"/>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="pool" items="${withdrawAppResult.rows}" varStatus="status">
                    <tr>
                        <td>
                            <div class="form-check hover">
                                <input class="form-check-input" id="withdrawApp" type="radio"
                                       name="withdrawApp" aria-invalid="false" value="withdrawApp${status.index}">
                                <label class="form-check-label" for="withdrawApp"><span
                                        class="check-circle"></span></label>
                            </div>
                            <input type="hidden" name="withdrawAppId" value="<iais:mask name= "withdrawAppId" value="${pool.id}"/>"/>
                        </td>
                        <td>${pool.applicationNo}
                            <input type="hidden" name="withdrawAppNo" value="<iais:mask name= "withdrawAppNo" value="${pool.applicationNo}"/>"/>
                        </td>
                        <td><iais:code code="${pool.applicationType}"/>
                        </td>
                        <td>${pool.serviceId}</td>
                        <td><iais:code code="${pool.status}"/></td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>
<a class="btn btn-primary " onclick="guideSubmit('withdraw','second')">NEXT</a>
