<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="guardianAppliedPartDto" value="${vssTreatmentDto.guardianAppliedPartDto}" />
<c:set var="treatmentDto" value="${vssTreatmentDto.treatmentDto}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#pccoDetails">
                Particulars of Person Who Applied for Court Order
            </a>
        </h4>
    </div>
    <div id="pccoDetails" class="panel-collapse collapse">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <div style="${treatmentDto.age<21 && treatmentDto.maritalStatus != 'VSSMS002' ? '' : 'display: none'}">
                    <iais:row style="border-bottom: 1px solid #BABABA;" >
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <strong class="app-font-size-22 premHeader">Particulars of Consent</strong>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Name of Person"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <c:out value="${guardianAppliedPartDto.guardianName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="ID No."/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <c:out value="${guardianAppliedPartDto.guardianIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Date of Birth" />
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <fmt:formatDate value='${guardianAppliedPartDto.guardianBirthday}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Relationship to Person Who Was Sterilized"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                           <%-- <iais:code code="${guardianAppliedPartDto.guardianRelationship}"/>--%>
                            <c:out value="${guardianAppliedPartDto.guardianRelationship}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div style="${treatmentDto.sterilizationReason =='VSSRFS005' ? '' : 'display: none'}">
                    <iais:row style="border-bottom: 1px solid #BABABA; ">
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <strong class="app-font-size-22 premHeader">Particulars of Court Order</strong>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Name of Person"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:out value="${guardianAppliedPartDto.appliedPartName}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="ID No." />
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <c:out value="${guardianAppliedPartDto.appliedPartIdNo}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Date of Birth"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <fmt:formatDate value='${guardianAppliedPartDto.appliedPartBirthday}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Relationship to Person Who Was Sterilized"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                           <%-- <iais:code code="${guardianAppliedPartDto.appliedPartRelationship}"/>--%>
                            <c:out value="${guardianAppliedPartDto.appliedPartRelationship}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Date Court Order Issued"/>
                        <iais:value width="6" display="true" cssClass="col-md-6">
                            <fmt:formatDate value='${guardianAppliedPartDto.courtOrderIssueDate}' pattern='dd/MM/yyyy' />
                        </iais:value>
                    </iais:row>
                        <div class="col-xs-12 col-md-12" style="padding-left: 0px">
                            <h3>Court Order Document</h3>
                            <div class="file-upload-gp">
                                <div name="selectedVssFileShowId" id="selectedVssFileShowId">
                                    <c:forEach items="${vssFiles}" var="vssFile"
                                                       varStatus="ind">
                                        <div id="selectedVssFileDiv${vssFile.seqNum}">
                                            <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                                                <iais:downloadLink fileRepoIdName="fileRo${ind.index}" fileRepoId="${vssFile.fileRepoId}" docName="${vssFile.docName}"/>
                                            </span>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>
                    </div>
            </div>
        </div>
    </div>
</div>


