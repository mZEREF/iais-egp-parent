<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="guardianAppliedPartDto" value="${vssTreatmentDto.guardianAppliedPartDto}" />
<c:set var="treatmentDto" value="${vssTreatmentDto.treatmentDto}" />
<div class="panel panel-default">
    <div class="panel-heading ${guardianAppliedPartDto !=null && guardianAppliedPartDto.headStatus == true ? 'completed' : 'incompleted' }">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#pccoDetails">
                Particulars of Treatment for Sexual Sterilization Performed
            </a>
        </h4>
    </div>
    <div id="pccoDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <div style="${treatmentDto.age<21 && treatmentDto.maritalStatus != 'VSSMS002' ? '' : 'display: none'}">
                    <iais:row style="border-bottom: 1px solid #BABABA; ">
                        <iais:value width="7" cssClass="col-md-7">
                            <strong class="app-font-size-22 premHeader">Particulars of Consent</strong>
                        </iais:value>
                    </iais:row>
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
                            <fmt:formatDate value='${guardianAppliedPartDto.guardianBirthday}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Relationship to Person Who Was Sterilized"/>
                        <iais:value width="7" cssClass="col-md-7">
                           <%-- <iais:code code="${guardianAppliedPartDto.guardianRelationship}"/>--%>
                            <c:out value="${guardianAppliedPartDto.appliedPartRelationship}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div style="${treatmentDto.sterilizationReason =='VSSRFS002' ? '' : 'display: none'}">
                    <iais:row style="border-bottom: 1px solid #BABABA; ">
                        <iais:value width="7" cssClass="col-md-7">
                            <strong class="app-font-size-22 premHeader">Particulars of Court Order</strong>
                        </iais:value>
                    </iais:row>
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
                            <fmt:formatDate value='${guardianAppliedPartDto.appliedPartBirthday}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Relationship to Person Who Was Sterilized"/>
                        <iais:value width="7" cssClass="col-md-7">
                           <%-- <iais:code code="${guardianAppliedPartDto.appliedPartRelationship}"/>--%>
                            <c:out value="${guardianAppliedPartDto.appliedPartRelationship}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date Court Order Issued"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <fmt:formatDate value='${guardianAppliedPartDto.courtOrderIssueDate}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                </div>

            </div>
        </div>
    </div>
</div>


