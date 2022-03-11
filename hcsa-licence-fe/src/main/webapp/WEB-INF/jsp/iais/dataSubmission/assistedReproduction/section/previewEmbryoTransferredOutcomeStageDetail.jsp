<c:set var="embryoTransferredOutcomeStageDto" value="${arSuperDataSubmissionDto.embryoTransferredOutcomeStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#etoDetails">
                Outcome of Embryo Transferred
            </a>
        </h4>
    </div>
    <div id="etoDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Outcome of Embryo Transferred"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" >
                        <iais:code code="${embryoTransferredOutcomeStageDto.transferedOutcome}"/>
                    </iais:value>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>