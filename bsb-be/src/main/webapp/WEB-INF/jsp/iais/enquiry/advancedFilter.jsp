<c:if test="${count=='fac'}">
    <h4>
        <span>
            Search by Facility Info
            <strong>&nbsp;
                <input  id="facilityChk" type="radio"
                        <c:if test="${count eq 'fac'}">checked</c:if>  value="fac" name="searchChk"  />
            </strong>
        </span>
    </h4>
    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" >
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="">
                    <iais:row>
                        <iais:field value="Facility Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select id="facName" name="facilityName"  options="facilityName" firstOption="Please Select" value="${facSearchDto.facName}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Classification"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                                <iais:select name="facClassification" codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION" firstOption="Please Select" value="${facSearchDto.facClassification}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Type "/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facTypes" codeCategory="CATE_ID_BSB_ACTIVE_TYPE" firstOption="Please Select" multiSelect="true" multiValues="${facSearchDto.facTypes}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Schedule Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="scheduleType"  codeCategory="CATE_ID_BSB_SCH_TYPE" firstOption="Please Select" value="${facSearchDto.scheduleType}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="batName"  options="biologicalAgent"  firstOption="Please Select" value="${facSearchDto.batName}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Risk Level of the Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="riskLevel" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select" value="${facSearchDto.riskLevel}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Expiry Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="facExpiryDtFrom" id="facExpiryDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${facSearchDto.facExpiryDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Expiry Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="facExpiryDtTo" id="facExpiryDtTo" data-date-start-date="01/01/1900" value="<c:out value="${facSearchDto.facExpiryDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                            <span id="error_facExpiryDtTo" name="iaisErrorMsg" class="error-msg"></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Gazetted Area"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="gazettedArea" codeCategory="CATE_ID_BSB_GAZETTED_AREA" firstOption="Please Select" value="${facSearchDto.gazettedArea}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Operator"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="facOperator" maxlength="60" style=" font-weight:normal;" value="${facSearchDto.facOperator}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Admin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="facAdmin" maxlength="60" style=" font-weight:normal;" value="${facSearchDto.facAdmin}" />
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Authorised Personnel working in Facility"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="facAuthorisedPerson" maxlength="132" style=" font-weight:normal;" value="${facSearchDto.facAuthorisedPerson}" />
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biosafety Committee Personnel"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="facCommittee" maxlength="132" style=" font-weight:normal;" value="${facSearchDto.facCommittee}" />
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facStatus" codeCategory="CATE_ID_BSB_FACILITY_STATUS" firstOption="Please Select" value="${facSearchDto.facStatus}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Facility Certifier"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="afcName" options="AFC" firstOption="Please Select" value="${facSearchDto.afcName}"/>
                        </div>
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='approval'}">
    <h4>
        <span>
           Search by Approval Info
            <strong>&nbsp;
                <input  id="approvalChk" type="radio"
                        <c:if test="${count eq 'approval'}">checked</c:if>  value="approval" name="searchChk"  />
            </strong>
        </span>
    </h4>
    <div class="panel-collapse collapse in" id="collapseOne" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" >
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="">
                    <iais:row>
                        <iais:field value="Facility Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facName" options="facilityName"  firstOption="Please Select" value="${approvalSearchDto.facName}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Classification"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facClassification" codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION" firstOption="Please Select" value="${approvalSearchDto.facClassification}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Type "/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facTypes" codeCategory="CATE_ID_BSB_ACTIVE_TYPE"  firstOption="Please Select" multiSelect="true" multiValues="${approvalSearchDto.facTypes}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Schedule Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="scheduleType" codeCategory="CATE_ID_BSB_SCH_TYPE"  firstOption="Please Select" value="${approvalSearchDto.scheduleType}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="batName" options="biologicalAgent"  firstOption="Please Select" value="${approvalSearchDto.batName}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Nature of the sample"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="sampleNatures" codeCategory="CATE_ID_BSB_NATURE_OF_TTE_SAMPLE"  firstOption="Please Select" multiSelect="true" multiValues="${approvalSearchDto.sampleNatures}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Risk Level of the Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="riskLevel" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select" value="${approvalSearchDto.riskLevel}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="approvalType" codeCategory="CATE_ID_BSB_APPROVAL_TYPE"  firstOption="Please Select" value="${approvalSearchDto.approvalType}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Submission Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvalSubDtFrom" id="approvalSubDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvalSubDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                            <span id="error_approvalSubDtFrom" name="iaisErrorMsg" class="error-msg"></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Submission Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvalSubDtTo" id="approvalSubDtTo" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvalSubDtTo}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                            <span id="error_approvalSubDtTo" name="iaisErrorMsg" class="error-msg"></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="approvalStatus" codeCategory="CATE_ID_BSB_APPROVAL_STATUS" firstOption="Please Select" value="${approvalSearchDto.approvalStatus}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvedDtFrom" id="approvedDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvedDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvedDtTo" id="approvedDtTo" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvedDtTo}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                            <span id="error_approvedDtTo" name="iaisErrorMsg" class="error-msg"></span>
                        </div >
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='app'}">
    <h4>
        <span >Search by Application Info
            <strong >&nbsp;
                <input  id="hciChk" type="radio" name="searchChk" <c:if test="${count eq 'app'}">checked</c:if>  value="app" />
            </strong>
        </span>
    </h4>

    <div class="panel-collapse collapse in" id="collapseTwo" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" style="">
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="">
                    <iais:row>
                        <iais:field value="Application No"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" id="appNo" name="appNo" maxlength="66" style=" font-weight:normal" value="${appSearchDto.appNo}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="appType" id="appType"  codeCategory="CATE_ID_BSB_APP_TYPE" firstOption="Please Select" value="${appSearchDto.appType}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="appStatus" id="appStatus" codeCategory="CATE_ID_BSB_APP_STATUS" firstOption="Please Select" value="${appSearchDto.appStatus}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Submission Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="appSubmissionDtFrom" id="appSubmissionDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.appSubmissionDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Submission Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="appSubmissionDtTo" id="appSubmissionDtTo" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.appSubmissionDtTo}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                            <span id="error_appSubmissionDtTo" name="iaisErrorMsg" class="error-msg"></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvalDtFrom" id="approvalDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvalDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvalDtTo" id="approvalDtTo" data-date-start-date="01/01/1900" value="<c:out value="${approvalSearchDto.approvalDtTo}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                            <span id="error_approvalDtTo" name="iaisErrorMsg" class="error-msg"></span>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Classification"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facClassification" id="facClassification" codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION" firstOption="Please Select" value="${appSearchDto.facClassification}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                           <iais:select name="facTypes" id="facTypes" codeCategory="CATE_ID_BSB_ACTIVE_TYPE" firstOption="Please Select" multiSelect="true" multiValues="${appSearchDto.facTypes}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                                <iais:select name="facName" id="facName" options="facilityName" firstOption="Please Select" value="${appSearchDto.facName}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Schedule Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="scheduleType" id="scheduleType" codeCategory="CATE_ID_BSB_SCH_TYPE" firstOption="Please Select" value="${appSearchDto.scheduleType}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="batName" id="batName" options="biologicalAgent" firstOption="Please Select" value="${appSearchDto.batName}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Risk Level of the Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="riskLevel" id="riskLevel" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select" value="${appSearchDto.riskLevel}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Process Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="processType" id="processType" codeCategory="CATE_ID_BSB_PRO_TYPE" firstOption="Please Select" value="${appSearchDto.processType}"/>
                        </div >
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='afc'}">
    <h4>
        <span >Search By Approved Facility Certifier Info
            <strong >&nbsp;
                <input  id="licenseeChk" type="radio" <c:if test="${count eq 'afc'}">checked</c:if>   value="afc"   name="searchChk"  />
            </strong>
        </span>
    </h4>

    <div class="panel-collapse collapse in" id="collapseThree" role="tabpanel" aria-labelledby="headingOne" aria-expanded="true" >
        <div class="panel-body">
            <div class="panel-main-content">
                <iais:section title="">
                    <iais:row>
                        <iais:field value="Organisation Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="orgName" options="orgName" firstOption="Please Select" value="${afcSearchDto.orgName}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Administrator"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input name="adminName" type="text" maxlength="132" value="${afcSearchDto.adminName}" style=" font-weight:normal;"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="AFC Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                                <iais:select name="afcStatus" codeCategory="CATE_ID_BSB_APPROVAL_STATUS" firstOption="Please Select" value="${afcSearchDto.afcStatus}"/>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Team Member Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input name="teamMemberName" type="text" maxlength="132" style=" font-weight:normal;" value="${afcSearchDto.teamMemberName}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Team Member ID"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input name="teamMemberId" type="text" maxlength="10" style=" font-weight:normal;" value="${afcSearchDto.teamMemberId}"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvedAfcDtFrom" id="approvedAfcDtFrom" data-date-start-date="01/01/1900" value="<c:out value="${afcSearchDto.approvedDtFrom}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" autocomplete="off" name="approvedAfcDtTo" id="approvedAfcDtTo" data-date-start-date="01/01/1900" value="<c:out value="${afcSearchDto.approvedDtTo}"/>" placeholder="dd/mm/yyyy" maxlength="10" class="date_picker form-control"/>
                        </div >
                    </iais:row>

                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<%@ include file="/WEB-INF/jsp/include/validation.jsp" %>
