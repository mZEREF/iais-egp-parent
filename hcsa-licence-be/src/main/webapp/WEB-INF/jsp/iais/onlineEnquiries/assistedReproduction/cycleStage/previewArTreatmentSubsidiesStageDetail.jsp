<c:set var="arTreatmentSubsidiesStageDto" value="${arSuperDataSubmissionDto.arTreatmentSubsidiesStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading  ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                AR Treatment Co-funding
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5"  value="" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is the ART cycle being co-funded" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <iais:code code="${arTreatmentSubsidiesStageDto.coFunding}"/>
                    </iais:value>
                </iais:row>
                <c:if test="${isDisplayAppeal}">
                    <iais:row>
                        <iais:field width="5" value="Is there an approved appeal?" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${arTreatmentSubsidiesStageDto.isThereAppeal?'Yes':'No'}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
            </div>
        </div>
    </div>
</div>