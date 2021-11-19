<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Details of Patient
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="suffix" value="" />
                <c:set var="person" value="${patient}" />
                <%@include file="personSection.jsp" %>
                <iais:row>
                    <iais:field width="5" value="Is AR Centre aware of patient's previous identification?" mandatory="true"/>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${patient.previousIdentification}">checked="checked"</c:if>
                               type="radio" name="previousIdentification" value = "1" aria-invalid="false"
                               onclick="toggleOnCheck(this, 'previousData')">
                        <label class="form-check-label" ><span class="check-circle"></span>Yes</label>
                    </div>
                    <div class="form-check col-md-3 col-xs-3">
                        <input class="form-check-input" <c:if test="${!patient.previousIdentification}">checked="checked"</c:if>
                               type="radio" name="previousIdentification" value = "0" aria-invalid="false"
                               onclick="toggleOnCheck(this, 'previousData', true)">
                        <label class="form-check-label" ><span class="check-circle"></span>No</label>
                    </div>
                    <span class="error-msg col-md-7" name="iaisErrorMsg" id="error_previousIdentification"></span>
                </iais:row>
                <div id="previousData" <c:if test="${!patient.previousIdentification}">style="display:none"</c:if> >
                    <h3>Patient's Previous Identification</h3>
                    <iais:row>
                        <iais:field width="5" value="ID No." mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:select name="preIdType" firstOption="Please Select" codeCategory="CATE_ID_DS_ID_TYPE"
                                         value="${previous.idType}" cssClass="idTypeSel"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <iais:input maxLength="20" type="text" name="preIdNumber" value="${previous.idNumber}" />
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Nationality" mandatory="true"/>
                        <iais:value width="4" cssClass="col-md-4">
                            <iais:select name="preNationality" firstOption="Please Select" codeCategory="CATE_ID_NATIONALITY"
                                         value="${previous.nationality}" cssClass="preNationalitySel"/>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3" display="true">
                            <a class="retrieveIdentification" onclick="retrieveIdentification()">
                                Retrieve Identification
                            </a>
                            <input type="hidden" name="retrievePrevious" value="${not empty previous ? '1' : '0'}"/>
                            <span class="error-msg col-md-12" name="iaisErrorMsg" id="error_retrievePrevious"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Name"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="preName">
                            ${previous.name}
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="Date of Birth"/>
                        <iais:value width="7" cssClass="col-md-7" display="true" id="preBirthDate">
                            ${previous.birthDate}
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>
<iais:confirm msg="DS_MSG006" callBack="$('#noPatientDiv').modal('hide');" popupOrder="noPatientDiv" needCancel="false"
              needFungDuoJi="false"/>
<iais:confirm msg="${ageMsg}" callBack="$('#ageMsgDiv').modal('hide');" popupOrder="ageMsgDiv" needCancel="false"
              yesBtnCls="btn btn-secondary" yesBtnDesc="Close"
              needFungDuoJi="false" />

