<c:if test="${count=='2'}">
    <h4>
        <span>
            Search by Facility Info
            <strong>&nbsp;
                <input  id="facilityChk" type="radio"
                        <c:if test="${count=='2'}">checked</c:if>  value="2" name="searchChk"  />
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
                            <iais:select id="facilityName" name="facilityName"  options="facilityName" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Classification"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                                <iais:select name="facilityClassification" codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Type "/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facilityType" codeCategory="CATE_ID_BSB_FAC_TYPE" firstOption="Please Select" multiSelect="true"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Schedule Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="scheduleType"  codeCategory="CATE_ID_BSB_SCH_TYPE" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="biologicalAgent"  options="biologicalAgent"  firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Risk Level of the Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="riskLevelOfTheBiologicalAgent" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Expiry Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="facilityExpiryDateFrom"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Expiry Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="facilityExpiryDateTo"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Gazetted Area"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="gazettedArea" codeCategory="CATE_ID_BSB_GAZETTED_AREA" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Operator"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="facilityOperator" maxlength="60" style=" font-weight:normal;" />
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Admin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="facilityAdmin" maxlength="60" style=" font-weight:normal;" />
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Authorised Personnel working in Facility"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="authorisedPersonnelWorkingInFacility" maxlength="132" style=" font-weight:normal;" />
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biosafety Committee Personnel"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" name="biosafetyCommitteePersonnel" maxlength="132" style=" font-weight:normal;" />
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facilityStatus" codeCategory="CATE_ID_BSB_FACILITY_STATUS" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Facility Certifier"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="approvedFacilityCertifier" options="AFC" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='3'}">
    <h4>
        <span>
           Search by Approval Info
            <strong>&nbsp;
                <input  id="approvalChk" type="radio"
                        <c:if test="${count=='3'}">checked</c:if>  value="2" name="searchChk"  />
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
                            <iais:select name="facilityName" options="facilityName"  firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Classification"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facilityClassification" codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Type "/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facilityType" codeCategory="CATE_ID_BSB_FAC_TYPE"  firstOption="Please Select" multiSelect="true"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Schedule Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="scheduleType" codeCategory="CATE_ID_BSB_SCH_TYPE"  firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="biologicalAgent" options="biologicalAgent"  firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Nature of the sample"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="nature_of_the_sample" codeCategory="CATE_ID_BSB_NATURE_OF_TTE_SAMPLE"  firstOption="Please Select" multiSelect="true"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Risk Level of the Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="rlotba" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="approvalType" codeCategory="CATE_ID_BSB_APPROVAL_TYPE"  firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Submission Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="approval_sub_from_date"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Submission Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="approval_sub_To_date"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="approvalStatus" codeCategory="CATE_ID_BSB_APPROVAL_STATUS" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="approved_from_date"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="approved_to_date"></iais:datePicker>
                        </div >
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='1'}">
    <h4>
        <span >Search by Application Info
            <strong >&nbsp;
                <input  id="hciChk" type="radio" name="searchChk" <c:if test="${count=='1'}">checked</c:if>  value="1" />
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
                            <input type="text" id="applicationNo" name="applicationNo" maxlength="66" style=" font-weight:normal"  ;/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="applicationType" id="applicationType"  codeCategory="CATE_ID_BSB_APP_TYPE" firstOption="Please Select"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="applicationStatus" id="applicationStatus" codeCategory="CATE_ID_BSB_APP_STATUS" firstOption="Please Select"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Submission Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "applicationSubmissionDateFrom" name = "applicationSubmissionDateFrom"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Application Submission Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "applicationSubmissionDateTo" name = "applicationSubmissionDateTo"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "approvalDateFrom" name = "approvalDateFrom"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approval Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker id = "approvalDateTo" name = "approvalDateTo"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Classification"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="facilityClassification" id="facilityClassification" codeCategory="CATE_ID_BSB_FAC_CLASSIFICATION" firstOption="Please Select"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                           <iais:select name="facilityType" id="facilityType" codeCategory="CATE_ID_BSB_FAC_TYPE" firstOption="Please Select" multiSelect="true"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                                <iais:select name="facilityName" id="facilityName" options="facilityName" firstOption="Please Select"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Schedule Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="scheduleType" id="scheduleType" codeCategory="CATE_ID_BSB_SCH_TYPE" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="biologicalAgent" id="biologicalAgent" options="biologicalAgent" firstOption="Please Select" ></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Risk Level of the Biological Agent/Toxin"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="riskLevelOfTheBiologicalAgent" id="riskLevelOfTheBiologicalAgent" codeCategory="CATE_ID_BSB_RISK_LEVEL_OF_THE_BIOLOGICAL_AGENT" firstOption="Please Select"></iais:select>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Process Type"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="processType" id="processType" codeCategory="CATE_ID_BSB_PRO_TYPE" firstOption="Please Select"></iais:select>
                        </div >
                    </iais:row>
                </iais:section>
            </div>
        </div>
    </div>
</c:if>
<c:if test="${count=='4'}">
    <h4>
        <span >Search By Approved Facility Certifier Info
            <strong >&nbsp;
                <input  id="licenseeChk" type="radio" <c:if test="${count=='4'}">checked</c:if>   value="4"   name="searchChk"  />
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
                            <iais:select name="organisationName" options="organisationName" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Facility Administrator"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" maxlength="132" style=" font-weight:normal;"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="AFC Status"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:select name="afcStatus" options="afcStatus" firstOption="Please Select"></iais:select>
                        </div>
                    </iais:row>
                    <iais:row>
                        <iais:field value="Team Member Name"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" maxlength="132" style=" font-weight:normal;"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Team Member ID"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <input type="text" maxlength="10" style=" font-weight:normal;"/>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date From"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="approved_to_date"></iais:datePicker>
                        </div >
                    </iais:row>
                    <iais:row>
                        <iais:field value="Approved Date To"/>
                        <div class="col-sm-7 col-md-4 col-xs-10">
                            <iais:datePicker name="approval_to_date"></iais:datePicker>
                        </div >
                    </iais:row>

                </iais:section>
            </div>
        </div>
    </div>
</c:if>
