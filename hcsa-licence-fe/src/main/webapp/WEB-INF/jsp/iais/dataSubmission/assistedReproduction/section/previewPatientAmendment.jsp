<c:set var="dataSubmission" value="${arSuperDataSubmissionDto.dataSubmissionDto}" />
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientAmentment">
                Amendment
            </a>
        </h4>
    </div>
    <div id="patientAmentment" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="5" value="Reason for Amendment"/>
                    <iais:value width="7" display="true">
                        <iais:code code="${dataSubmission.amentReason}" />
                    </iais:value>
                </iais:row>
                <div class="form-group" style="<c:if test="${dataSubmission.amentReason ne 'PTA_003'}">display:none</c:if>">
                    <iais:field width="5" value="Reason for Amendment (Others)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${dataSubmission.amendReasonOther}" />
                    </iais:value>
                </div>
            </div>
        </div>
    </div>
</div>