<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Submission Details
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="suffix" value="" />
                <c:set var="drug" value="${drugSubmission}" />
                <iais:row>
                    <iais:field width="5" value="Patient's ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="doctorreignno" firstOption="Please Select" codeCategory="" value=""
                                     cssClass=""/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="" value="" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="">
                        ${name}
                    </iais:value>
                </iais:row>


                <iais:row>
                    <iais:field width="5" value="Doctor’s Professional Registration No" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="20" type="text" name="doctorreignno" value="${drugSubmission.doctorreignno}"/>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3" display="true">
                        <a class="ValidateDoctor" onclick="">
                            Validate Doctor
                        </a>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Doctor’s Name"/>
                    <iais:value width="7" cssClass="col-md-7" display="true" id="">
                        ${name}
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="Drug Prescribed or Dispensed" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select cssClass="drugtype"  name="drugtype" firstOption="Please Select" codeCategory="DP_DRUG_PRESCRIBED_OR_DISPENSED" value="${drugSubmission.drugtype}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Prescription" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="prescriptiondate" value="${drugSubmission.prescriptiondate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Dispensing" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="dispensingdate" value="${drugSubmission.dispensingdate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Medication" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select cssClass="medication"  name="medication" firstOption="Please Select" codeCategory="DP_MEDICATION" value="${drugSubmission.medication}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Start Date" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="startdate" value="${drugSubmission.startdate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="End Date" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker name="enddate" value="${drugSubmission.enddate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Diagnosis" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="50" type="text" name="diagnosis" value="${drugSubmission.diagnosis}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>