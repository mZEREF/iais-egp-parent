<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="sexualSterilizationDto" value="${vssTreatmentDto.sexualSterilizationDto}" />
<c:set var="doctorInformationDto" value="${vssSuperDataSubmissionDto.doctorInformationDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#pssDetails">
                Particulars of Treatment for Sexual Sterilization Performed
            </a>
        </h4>
    </div>
    <div id="pssDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="6" value="Doctor Professional Registration No." />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sexualSterilizationDto.doctorReignNo}"/>
                    </iais:value>
                </iais:row>
              <%--  <iais:row>
                    <iais:field width="6" value="Name of Doctor who performed the sterilization" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sexualSterilizationDto.doctorName}"/>
                    </iais:value>
                </iais:row>--%>

                    <iais:row>
                        <iais:field width="5" value="Doctor's Name"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="names">
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'true'}">
                                <c:out value="${doctorInformationDto.name}"/>
                            </c:if>
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'false' || sexualSterilizationDto.doctorInformations == null}">
                                <c:out value="${sexualSterilizationDto.doctorName}"/>
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="specialty">
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'true'}">
                                <c:out value="${doctorInformationDto.speciality}"/>
                            </c:if>
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'false' || sexualSterilizationDto.doctorInformations == null}">
                                <c:out value="${sexualSterilizationDto.specialty}"/>
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Sub-Specialty"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="subSpecialty">
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'true'}">
                                <c:out value="${doctorInformationDto.subSpeciality}"/>
                            </c:if>
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'false' || sexualSterilizationDto.doctorInformations == null}">
                                <c:out value="${sexualSterilizationDto.subSpecialty}"/>
                            </c:if>
                        </iais:value>
                    </iais:row>
                    <iais:row >
                        <iais:field width="5" value="Qualification"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="qualification">
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'true'}">
                                <c:out value="${doctorInformationDto.qualification}"/>
                            </c:if>
                            <c:if test="${sexualSterilizationDto.doctorInformations eq 'false' || sexualSterilizationDto.doctorInformations == null}">
                                <c:out value="${sexualSterilizationDto.qualification}"/>
                            </c:if>
                        </iais:value>
                    </iais:row>

                <iais:row>
                    <iais:field width="6" value="Hospital/Clinic where the sterilization was performed" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:out value="${sexualSterilizationDto.sterilizationHospital}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Method of Sterilization" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${sexualSterilizationDto.sterilizationMethod}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Date of Operation" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <fmt:formatDate value='${sexualSterilizationDto.operationDate}' pattern='dd/MM/yyyy' />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Reviewed by Hospital Ethics Committee (HEC) (state name of hospital)"/>
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <c:if test="${sexualSterilizationDto.reviewedByHec == true}"><c:out value="Yes"/></c:if>
                        <c:if test="${sexualSterilizationDto.reviewedByHec == false}"><c:out value="No"/></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Name of Hospital" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <iais:code code="${sexualSterilizationDto.hecReviewedHospital}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Date of HEC Review" />
                    <iais:value width="6" display="true" cssClass="col-md-6">
                        <fmt:formatDate value='${sexualSterilizationDto.hecReviewDate}' pattern='dd/MM/yyyy' />
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>







