<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="guardianAppliedPartDto" value="${vssTreatmentDto.guardianAppliedPartDto}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Particulars of Person Who Applied for Court Order
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <div>
                    <iais:row>
                        <iais:field width="5" value="Name Of Person"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.guardianName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="ID No."/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.guardianIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Birth" />
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.guardianBirthday}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Relationship to Person Who Was Sterilized"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:code code="${guardianAppliedPartDto.guardianRelationship}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div>
                    <iais:row>
                        <iais:field width="5" value="Name Of Person"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.appliedPartName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="ID No." />
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.appliedPartIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Birth"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.appliedPartBirthday}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Relationship to Person Who Was Sterilized"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:code code="${guardianAppliedPartDto.appliedPartRelationship}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date Court Order Issued"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.courtOrderIssueDate}"/>
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>


