<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/dp_patientInfomation.js"></script>
<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 95px;">
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
                        <iais:select name="idType" onchange ="toggleSelect(this, 'AR_IT_004', 'nationalityStar')" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                     value="${patientDto.idType}" cssClass="idTypeSel"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <iais:input maxLength="20" type="text" name="idNumber" value="${patientDto.idNumber}" />
                        <span class="error-msg" name="iaisErrorMsg" id="error_idNumber"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <%--<iais:field width="5" value="Nationality" mandatory="false"/>--%>
                    <label class="col-xs-5 col-md-4 control-label">Nationality
                        <span id="nationalityStar" class="mandatory">
                                <c:if test="${patientDto.idType =='AR_IT_004'}">*</c:if>
                        </span>
                    </label>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="nationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                     value="${patientDto.nationality}" cssClass="nationalitySel"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_nationality"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Name of Patient" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="66" type="text" name="name" value="${patientDto.name}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Date of Birth" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker id="birthDate" name="birthDate" value="${patientDto.birthDate}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_birthDate"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Postal Code" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <%--<input type="number" oninput="if(value.length>6)value=value.slice(0,6)" style="margin-bottom: 0px;"
                               name="postalCode" value="${patientDto.postalCode}"/>--%>
                        <iais:input maxLength="6" type="text" name="postalCode" id="postalCode" value="${patientDto.postalCode}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_postalCode"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Address Type" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="addrType" codeCategory="CATE_ID_ADDRESS_TYPE" onchange="test(this, 'ADDTY001', 'floorNoStar','blkNoStar')"  firstOption="Please Select"
                                     value="${patientDto.addrType}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <%--<iais:field width="5" value="Block No." />--%>
                    <label class="col-xs-5 col-md-4 control-label">Block No.
                        <span id="blkNoStar" class="mandatory">
                                <c:if test="${patientDto.addrType =='ADDTY001'}">*</c:if>
                        </span>
                    </label>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="10" type="text" name="blkNo" value="${patientDto.blkNo}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                   <%-- <iais:field value="Floor No. / Unit No." width="5"/>--%>
                    <label class="col-xs-5 col-md-4 control-label">Floor No. / Unit No.
                        <span id="floorNoStar" class="mandatory">
                                <c:if test="${patientDto.addrType =='ADDTY001'}">*</c:if>
                        </span>
                    </label>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:input maxLength="3" type="text" name="floorNo" id="floorNo" value="${patientDto.floorNo}"/>
                    </iais:value>
                    <div class="col-sm-4 col-xs-1 col-md-1 text-center"><p>-</p></div>
                    <iais:value width="3" cssClass="col-md-3">
                        <iais:input maxLength="5" type="text" name="unitNo" id="unitNo" value="${patientDto.unitNo}"/>
                    </iais:value>
                </iais:row>
               <iais:row>
                <iais:field width="5" value="Street Name" mandatory="true" />
                <iais:value width="7" cssClass="col-md-7">
                    <iais:input maxLength="32" type="text" name="streetName" value="${patientDto.streetName}" />
                </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Building Name" width="5"/>
                    <iais:value width="7" cssClass="col-md-7">
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
                        <span class="error-msg" name="iaisErrorMsg" id="error_gender"></span>
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
                    <iais:field width="5" value="Ethnic Group" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:select name="ethnicGroup" firstOption="Please Select" codeCategory="CATE_ID_ETHNIC_GROUP"
                                     value="${patientDto.ethnicGroup}" onchange ="toggleOnSelect(this, 'ETHG005', 'ethnicOthers')" />
                    </iais:value>
                </iais:row>
                <iais:row id="ethnicOthers" style="${patientDto.ethnicGroup eq 'ETHG005' ? '' : 'display: none'}">
                    <iais:field value="Ethnic Group (Others)" width="5"  mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="20" type="text" name="ethnicGroupOther" id="ethnicGroupOther" value="${patientDto.ethnicGroupOther}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_ethnicGroupOther"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Mobile No." width="5"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="8" type="text" name="mobileNo" id="mobileNo" value="${patientDto.mobileNo}"/>
                        <%--<input type="number" oninput="if(value.length>8)value=value.slice(0,8)" style="margin-bottom: 0px;"
                               name="mobileNo" value="${patientDto.mobileNo}"/>--%>
                        <span class="error-msg" name="iaisErrorMsg" id="error_mobileNo"></span>
                    </iais:value>

                </iais:row>
                <iais:row>
                    <iais:field value="Home Telephone No." width="5"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="8" type="text" name="homeTelNo" id="homeTelNo" value="${patientDto.homeTelNo}"/>
                        <%--<input type="number" oninput="if(value.length>8)value=value.slice(0,8)" style="margin-bottom: 0px;"
                               name="homeTelNo" value="${patientDto.homeTelNo}"/>--%>
                        <span class="error-msg" name="iaisErrorMsg" id="error_homeTelNo"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field value="Email Address"  width="5"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input type="text" name="emailAddr" maxLength="66" value="${patientDto.emailAddr}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>