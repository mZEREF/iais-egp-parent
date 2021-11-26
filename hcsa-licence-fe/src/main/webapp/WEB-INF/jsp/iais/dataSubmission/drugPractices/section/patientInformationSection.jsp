<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Patient Details
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="patientDto" value="${dpSuperDataSubmissionDto.patientDto}"/>
                <iais:row>
                    <iais:field width="5" value="ID No." mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:select name="idType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                     value="${patientDto.idType}" cssClass="idTypeSel"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="idNumber" value="${patientDto.idNumber}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Nationality" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-4">
                        <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="${patientDto.nationality}" cssClass="nationalitySel"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Patient" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-4">
                        <iais:input maxLength="66" type="text" name="name" value="${patientDto.name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-4">
                        <iais:datePicker id="birthDate" name="birthDate" value="${patientDto.birthDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Postal Code" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-4">
                        <iais:input maxLength="6" type="text" name="postalCode" value="${patientDto.postalCode}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Address Type" mandatory="true"/>
                    <iais:value width="7">
                        <iais:select name="addrType" codeCategory="CATE_ID_ADDRESS_TYPE" firstOption="Please Select"
                                     value="${patientDto.addrType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Blk No." />
                    <iais:value width="7">
                        <iais:input maxLength="10" type="text" name="blkNo" value="${patientDto.blkNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Floor No./Unit No." width="5"/>
                    <iais:value width="3">
                        <iais:input maxLength="3" type="text" name="floorNo" id="floorNo" value="${patientDto.floorNo}"/>
                    </iais:value>
                    <iais:value width="2" cssClass="col-sm-2 col-md-1 text-center"><p>-</p></iais:value>
                    <iais:value width="3">
                        <iais:input maxLength="5" type="text" name="unitNo" id="unitNo" value="${patientDto.unitNo}"/>
                    </iais:value>
                </iais:row>
               <iais:row>
                <iais:field width="5" value="Street Name" mandatory="true" />
                <iais:value width="7">
                    <iais:input maxLength="32" type="text" name="streetName" value="${patientDto.streetName}" />
                </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Building Name" width="5"/>
                    <iais:value width="7">
                        <iais:input maxLength="45" type="text" name="buildingName" id="buildingName" value="${patientDto.buildingName}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Gender" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="gender"
                                   value="1"
                                   id="genderMale"
                                   <c:if test="${patientDto.gender eq '1'}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="genderMale"><span
                                    class="check-circle"></span>Male</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="gender"
                                   value="0"
                                   id="genderFemale"
                                   <c:if test="${patientDto.gender eq '0'}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="genderFemale"><span
                                    class="check-circle"></span>Female</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Race" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-4">
                        <iais:select name="race" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                                     value="${patientDto.race}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Mobile No" width="5"/>
                    <iais:value width="7">
                        <iais:input maxLength="8" type="text" name="mobileNo" id="mobileNo" value="${patientDto.mobileNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Home Telephone No" width="5"/>
                    <iais:value width="7">
                        <iais:input maxLength="8" type="text" name="homeTelNo" id="homeTelNo" value="${patientDto.homeTelNo}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Email Address" mandatory="true" width="11"/>
                    <iais:value width="11">
                        <iais:input type="text" name="emailAddr" maxLength="66" value="${patientDto.emailAddr}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
