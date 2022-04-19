<c:if test="${LdtSuperDataSubmissionDto.appType eq 'DSTY_005'}">
    <c:set var="dataSubmission" value="${LdtSuperDataSubmissionDto.dataSubmissionDto}"/>
    <iais:row>
        <iais:field width="5" cssClass="col-md-5" value="Reason for Amendment"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <iais:code code="${dataSubmission.amendReason}"/>
        </iais:value>
    </iais:row>
    <c:if test="${!empty dataSubmission.amendReasonOther}">
        <iais:row>
            <iais:field width="5" cssClass="col-md-5" value="Reason for Amendment (Others)"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${dataSubmission.amendReasonOther}"/>
            </iais:value>
        </iais:row>
    </c:if>
</c:if>