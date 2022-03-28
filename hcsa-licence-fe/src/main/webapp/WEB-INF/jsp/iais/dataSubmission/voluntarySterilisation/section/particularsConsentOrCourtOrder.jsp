<c:set var="vssTreatmentDto" value="${vssSuperDataSubmissionDto.vssTreatmentDto}" />
<c:set var="guardianAppliedPartDto" value="${vssTreatmentDto.guardianAppliedPartDto}" />
<c:set var="treatmentDto" value="${vssTreatmentDto.treatmentDto}" />
<div class="form-horizontal treatmentDetails">
    <%--<iais:row>
        <iais:value width="6" cssClass="col-md-6">
            &lt;%&ndash;<strong class="app-font-size-22 premHeader">title</strong>&ndash;%&gt;
        </iais:value>
        <iais:value width="6" cssClass="col-md-6 text-right editDiv">
            <c:if test="${canEdit}">
                <input id="isEditHiddenVal" type="hidden" name="isEdit" value="0"/>
                <a id="edit" class="text-right app-font-size-16">
                    <em class="fa fa-pencil-square-o">&nbsp;</em> Edit
                </a>
            </c:if>
        </iais:value>
    </iais:row>--%>
<div style="${treatmentDto.age<21 && treatmentDto.maritalStatus != 'VSSMS002' ? '' : 'display: none'}">
    <iais:row style="border-bottom: 1px solid #BABABA; ">
        <iais:value width="7" cssClass="col-md-7">
            <strong class="app-font-size-22 premHeader">Particulars of Consent</strong>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name Of Person" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="guardianName" value="${guardianAppliedPartDto.guardianName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="person"/></c:set>
        <iais:field width="5" value="ID No." mandatory="true" info="${toolMsg}"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="guardianIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${guardianAppliedPartDto.guardianIdType}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianIdType"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="guardianIdNo" value="${guardianAppliedPartDto.guardianIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="guardianBirthday" dateVal="${guardianAppliedPartDto.guardianBirthday}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianBirthday"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relationship to Person Who Was Sterilized" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="200" type="text" name="guardianRelationship" value="${guardianAppliedPartDto.guardianRelationship}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_guardianRelationship"></span>
        </iais:value>
    </iais:row>
</div>

<div style="${treatmentDto.sterilizationReason =='VSSRFS005' ? '' : 'display: none'}">
    <iais:row style="border-bottom: 1px solid #BABABA; ">
        <iais:value width="7" cssClass="col-md-7">
            <strong class="app-font-size-22 premHeader">Particulars of Court Order</strong>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Name Of Person" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="66" type="text" name="appliedPartName" value="${guardianAppliedPartDto.appliedPartName}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartName"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <c:set var="toolMsg"><iais:message key="DS_MSG014" paramKeys="1" paramValues="person"/></c:set>
        <iais:field width="5" value="ID No." mandatory="true" info="${toolMsg}"/>
        <iais:value width="3" cssClass="col-md-3">
            <iais:select name="appliedPartIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                         value="${guardianAppliedPartDto.appliedPartIdType}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartIdType"></span>
        </iais:value>
        <iais:value width="4" cssClass="col-md-4">
            <iais:input maxLength="15" type="text" name="appliedPartIdNo" value="${guardianAppliedPartDto.appliedPartIdNo}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartIdNo"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date of Birth" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker id="birthDate" name="appliedPartBirthday" dateVal="${guardianAppliedPartDto.appliedPartBirthday}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartBirthday"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relationship to Person Who Was Sterilized" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="200" type="text" name="appliedPartRelationship" value="${guardianAppliedPartDto.appliedPartRelationship}" />
            <span class="error-msg" name="iaisErrorMsg" id="error_appliedPartRelationship"></span>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Date Court Order Issued" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:datePicker name="courtOrderIssueDate"  dateVal="${guardianAppliedPartDto.courtOrderIssueDate}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_courtOrderIssueDate"></span>
        </iais:value>
    </iais:row>
    <div class="center-content">
        <div class="">
            <div class="document-upload-gp">
                <div class="document-upload-list">
                    <h3>Court Order Document</h3>
                            <div class="file-upload-gp">
                                <div name="selectedVssFileShowId" id="selectedVssFileShowId">
                                    <c:forEach items="${vssFiles}" var="vssFile"
                                               varStatus="ind">
                                        <div id="selectedVssFileDiv${vssFile.seqNum}">
                                            <span name="fileName" style="font-size: 14px;color: #2199E8;text-align: center">
                                                <iais:downloadLink fileRepoIdName="fileRo${ind.index}" fileRepoId="${vssFile.fileRepoId}" docName="${vssFile.docName}"/>
                                            </span>
                                            <span class="error-msg" name="iaisErrorMsg"
                                                  id="file${ind.index}"></span>
                                            <span class="error-msg" name="iaisErrorMsg"
                                                  id="error_${configIndex}error"></span>
                                            <button type="button" class="btn btn-secondary btn-sm"
                                                    onclick="javascript:deleteFileFeAjax('selectedVssFile',${vssFile.seqNum});">
                                                Delete</button>  <button type="button" class="btn btn-secondary btn-sm"
                                                                         onclick="javascript:reUploadFileFeAjax('selectedVssFile',${vssFile.seqNum},'mainForm');">
                                            ReUpload</button>
                                        </div>
                                    </c:forEach>
                                </div>
                                <input id="selectedFile" name="selectedFile"
                                       class="selectedFile commDoc"
                                       type="file" style="display: none;"
                                       aria-label="selectedFile1"
                                       onclick="(event)"
                                       onchange="doUserRecUploadConfirmFile(event)"/><a href="javascript:void(0);"
                                                                                        class="btn btn-file-upload btn-secondary"
                                                                                        onclick="clearFlagValueFEFile()">Upload</a>
                            </div>
                    <span id="error_selectedFileError" name="iaisErrorMsg" class="error-msg"></span>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<%@ include file="../../../appeal/FeFileCallAjax.jsp" %>
<script>

    function doUserRecUploadConfirmFile(event) {
        uploadFileValidate();
    }

    function uploadFileValidate() {
        ajaxCallUploadForMax('mainForm', "selectedVssFile",true);
    }
</script>