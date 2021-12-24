<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="sexualSterilizationDto" value="${vssTreatmentDto.sexualSterilizationDto}" />
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
                <iais:row>
                    <iais:field width="5" value="Doctor Professional Registration No." />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${sexualSterilizationDto.doctorReignNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Doctor who performed the sterilization" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${sexualSterilizationDto.doctorName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Hospital/Clinic where the sterilization was performed" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${sexualSterilizationDto.doctorName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Method of Sterilization" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${sexualSterilizationDto.sterilizationMethod}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Operation" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${sexualSterilizationDto.operationDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Reviewed by Hospital Ethics Committee (HEC) (state name of hospital)"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${sexualSterilizationDto.reviewedByHec}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of HEC Review" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${sexualSterilizationDto.hecReviewDate}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>







