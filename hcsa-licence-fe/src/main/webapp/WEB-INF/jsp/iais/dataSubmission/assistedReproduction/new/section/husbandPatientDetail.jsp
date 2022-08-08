<c:set var="suffix" value="Hbd" />
<c:set var="person" value="${husband}" />
<p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Details of Husband</p>
<iais:row>
    <iais:field width="5" value="Does the patient's husband have a NRIC/FIN number?" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <div class="form-check form-check-inline">
            <input class="form-check-input" id="hbNoIdNUmber" type="radio" name="hasIdNumber${suffix}" value="N" <c:if test="${person.hasIdNumber eq 'N'}">checked</c:if> />
            <label class="form-check-label" for="hbNoIdNUmber">
                <span class="check-circle"></span>No
            </label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" id="hbHasIdNUmber" type="radio" name="hasIdNumber${suffix}" value="Y" <c:if test="${person.hasIdNumber eq 'Y'}">checked</c:if> />
            <label class="form-check-label" for="hbHasIdNUmber">
                <span class="check-circle"></span>Yes
            </label>
        </div>
        <span class="error-msg" name="iaisErrorMsg" id="error_hasIdNumber${suffix}"></span>
    </iais:value>
</iais:row>

<div id="hbIdNumberSection" style="display: none" >
    <div id="hbIdTypeSection" style="display: none">
        <iais:row>
            <iais:field width="5" value = "NRIC/FIN number Type" mandatory="true" />
            <iais:value width="7" cssClass="col-md-7">
                <select name="idType${suffix}" id="idType${suffix}">
                    <option value="" <c:if test="${person.idType eq ''}">selected</c:if>>Please Select</option>
                    <option value="AR_IT_001" <c:if test="${person.idType eq 'AR_IT_001'}">selected</c:if>>Pink IC</option>
                    <option value="AR_IT_002" <c:if test="${person.idType eq 'AR_IT_002'}">selected</c:if>>Blue IC</option>
                    <option value="AR_IT_003" <c:if test="${person.idType eq 'AR_IT_003'}">selected</c:if>>FIN</option>
                </select>
                <span class="error-msg" name="iaisErrorMsg" id="error_idType${suffix}"></span>
            </iais:value>
        </iais:row>
    </div>

    <iais:row>
        <iais:field width="5" value="Please indicate the husband's NRIC/FIN number" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="idNumber${suffix}" id="hbIdNumber" value="${person.idNumber}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_idNumber${suffix}"></span>
        </iais:value>
    </iais:row>
</div>
<div id="hbNoNumberSection" style="display: none" >
    <iais:row>
        <iais:field width="5" value="Please indicate the husband's passport number" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
            <iais:input maxLength="20" type="text" name="idNumber${suffix}" id="hbPassport" value="${person.idNumber}"/>
            <span class="error-msg" name="iaisErrorMsg" id="error_idNumber${suffix}"></span>
        </iais:value>
    </iais:row>
</div>
<%@include file="personSection.jsp" %>
