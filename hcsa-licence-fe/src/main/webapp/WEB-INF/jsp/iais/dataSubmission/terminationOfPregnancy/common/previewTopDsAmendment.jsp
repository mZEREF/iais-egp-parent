<c:if test="${topSuperDataSubmissionDto.appType eq 'DSTY_002'}">
    <c:set var="dataSubmission" value="${topSuperDataSubmissionDto.dataSubmissionDto}" />
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a class="collapsed" data-toggle="collapse" href="#patientAmentment">
                    Amendment
                </a>
            </h4>
        </div>
        <div id="patientAmentment" class="panel-collapse collapse">
            <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                    <iais:row>
                        <iais:field width="5" value="Reason for Amendment"/>
                        <iais:value width="7" display="true">
                            <iais:code code="${dataSubmission.amendReason}" />
                        </iais:value>
                    </iais:row>
                    <c:if test="${!empty dataSubmission.amendReasonOther}">
                        <iais:row>
                            <iais:field width="5" value="Reason for Amendment (Others)"/>
                            <iais:value width="7" display="true">
                                <c:out value="${dataSubmission.amendReasonOther}" />
                            </iais:value>
                        </iais:row>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</c:if>