<div class="form-check-gp">

    <p class="form-check-title">Please select a draft application to resume</p>
    <iais:pagination  param="appParam" result="appResult"/>

    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">

                <iais:sortableHeader needSort="true"  field="APPLICATION_NO" value="Application NumBer"/>
                <iais:sortableHeader needSort="true"  field="TYPE" value="Type"/>
                <iais:sortableHeader needSort="true"  field="SERVICE" value="Service"/>
                <iais:sortableHeader needSort="true"  field="STATUS" value="Status"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty appResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${appResult.rows}" varStatus="status">
                        <tr>
                            <td>
                                <label>
                                    <input id="licence${status.index + 1}" type="radio" name="resumeAppNos" value="${pool.applicationNo}"   >${pool.applicationNo}
                                </label>
                            </td>
                            <td>${pool.type}</td>
                            <td>${pool.svcId}</td>
                            <td>${pool.status}</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

    <a class="btn btn-primary "  onclick="Utils.submit('mainForm','resume')" style="background: #1F92FF; color: white"  >NEXT</a>
</div>
