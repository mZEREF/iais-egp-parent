<div class="form-check-gp">

    <p class="form-check-title">Choose the personnel who's information you wish to amend</p>

    <iais:row>
        <iais:select name="personnel"/>
    </iais:row>

    <p class="form-check-title">The following licences will be affected by the change of personal Info</p>

    <iais:pagination  param="amendUpdateContactSearchParam" result="amendUpdateContactSearchResult"/>
    <div class="table-gp">
        <table class="table">
            <thead>
            <tr align="center">
                <iais:sortableHeader needSort="true"  field="SVC_NAME" value="Type"/>
                <iais:sortableHeader needSort="true"  field="LICENCE_NO" value="Licence No."/>
                <iais:sortableHeader needSort="true"  field="ROLE" value="Role"/>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${empty amendUpdateContactSearchResult.rows}">
                    <tr>
                        <td colspan="15">
                            <iais:message key="ACK018" escape="true"/>
                        </td>
                    </tr>
                </c:when>
                <c:otherwise>
                    <c:forEach var="pool" items="${amendUpdateContactSearchResult.rows}" varStatus="status">
                        <tr>
                            <td>${pool.svcId}</td>
                            <td>${pool.licenceNo}</td>
                            <td>CGO\PO\DPO</td>
                        </tr>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
    <a class="btn btn-primary " onclick="guideSubmit('amendLic7','second')">NEXT</a>
</div>
