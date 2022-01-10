<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}"/>
<c:set var="treatmentDto" value="${vssTreatmentDto.treatmentDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#treatmentDetails">
                Treatment Details
            </a>
        </h4>
    </div>
    <div id="treatmentDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="5" value="Name Of Patient" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${treatmentDto.patientName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="ID No." />
                    <iais:value width="4" cssClass="col-md-4">
                        <c:out value="${treatmentDto.idNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${treatmentDto.birthData}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Gender" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${treatmentDto.gender}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Residence Status"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${treatmentDto.residenceStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Other Residence Status" width="5"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${treatmentDto.otherResidenceStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Ethnic Group" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${treatmentDto.ethnicGroup}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Other Ethnic Group" width="5"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${treatmentDto.otherEthnicGroup}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Marital Status"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${treatmentDto.maritalStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Education Leve" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${treatmentDto.educationLevel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Occupation" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${treatmentDto.occupation}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Main Reason for Sterilization" />
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:code code="${treatmentDto.sterilizationReason}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="No. of Living Children " width="5" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${treatmentDto.livingChildrenNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth of Last Child"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${treatmentDto.lastChildBirthday}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
