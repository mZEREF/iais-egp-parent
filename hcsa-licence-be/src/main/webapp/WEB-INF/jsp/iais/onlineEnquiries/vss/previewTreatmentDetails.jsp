<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}"/>
<c:set var="treatmentDto" value="${vssTreatmentDto.treatmentDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a  data-toggle="collapse" href="#treatmentDetails">
                Treatment Details
            </a>
        </h4>
    </div>
    <div id="treatmentDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="6" value="Name of Patient" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.patientName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="ID No." />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.idNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Date of Birth"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.birthData}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Gender" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${treatmentDto.gender}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Residence Status"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${treatmentDto.residenceStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row style="${treatmentDto.residenceStatus eq 'VSSRS003' ? '' : 'display: none'}">
                    <iais:field value="Other Residence Status" width="6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.otherResidenceStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Ethnic Group" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${treatmentDto.ethnicGroup}"/>
                    </iais:value>
                </iais:row>
                <iais:row style="${treatmentDto.ethnicGroup eq 'ECGP004' ? '' : 'display: none'}">
                    <iais:field value="Other Ethnic Group" width="6"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.otherEthnicGroup}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Marital Status"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${treatmentDto.maritalStatus}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Education Level" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${treatmentDto.educationLevel}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Occupation" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${treatmentDto.occupation}"/>
                    </iais:value>
                </iais:row>
                <iais:row style="${treatmentDto.occupation eq 'VSSOP011' ? '' : 'display: none'}" >
                    <iais:field width="6" value="Other Occupation" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.otherOccupation}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Main Reason for Sterilization" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${treatmentDto.sterilizationReason}"/>
                    </iais:value>
                </iais:row>
                <iais:row  style="${treatmentDto.sterilizationReason eq 'VSSRFS009' ? '' : 'display: none'}">
                    <iais:field width="6" value="Other Main Reason for Sterilization"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.otherSterilizationReason}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="No. of Living Children " width="6" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.livingChildrenNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Date of Birth of Last Child"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${treatmentDto.lastChildBirthday}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
