<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#patientDetails">
                Submission Details
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="drugPrescribedDispensedDto" value="${dpSuperDataSubmissionDto.drugPrescribedDispensedDto}" />
                <c:set var="drugSubmission" value="${drugPrescribedDispensedDto.drugSubmission}" />
                <iais:row>
                    <iais:field width="5" value="Patient's ID Type" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <iais:code code="${drugSubmission.idType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's ID No." />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${drugSubmission.idNumber}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Nationality" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${drugSubmission.nationality}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Patient's Name" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${drugSubmission.name}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Doctor's Professional Registration No." />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${drugSubmission.doctorReignNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Doctor's Name" />
                    <iais:value width="7" display="true" cssClass="col-md-7">
                        <c:out value="${drugSubmission.doctorName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Drug Prescribed or Dispensed" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7"  display="true">
                        <iais:code code="${drugSubmission.drugType}"/>
                    </iais:value>
                </iais:row>
                <div class="" <c:if test="${drugSubmission.drugType != 'DPD001'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Date of Prescription" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${drugSubmission.prescriptionDate}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div class="" <c:if test="${drugSubmission.drugType != 'DPD002'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Prescription Submission ID" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${drugSubmission.prescriptionSubmissionId}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Dispensing" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${drugSubmission.dispensingDate}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Medication" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7"  display="true">
                        <iais:code code="${drugSubmission.medication}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Prescribing Duration Start Date" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${drugSubmission.startDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="End Date of Dispensing" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${drugSubmission.endDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Diagnosis" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${drugSubmission.diagnosis}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>


