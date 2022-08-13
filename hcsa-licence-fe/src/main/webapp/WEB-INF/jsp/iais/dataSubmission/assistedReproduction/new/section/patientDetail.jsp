<c:set var="suffix" value="" />
<c:set var="person" value="${patient}" />
<p style="border-bottom: 1px solid;font-weight: 600;font-size: 2rem">Details of Patient</p>

<div id="idTypeSection" style="display: none">
    <iais:row>
        <iais:field width="5" value = "NRIC/FIN number Type" mandatory="true" />
        <iais:value width="7" cssClass="col-md-7">
            <select name="idType${suffix}" id="idType${suffix}">
                <option value="" <c:if test="${person.idType eq ''}">selected</c:if>>Please Select</option>
                <option value="AR_IT_001" <c:if test="${person.idType eq 'AR_IT_001'}">selected</c:if>>Pink IC</option>
                <option value="AR_IT_002" <c:if test="${person.idType eq 'AR_IT_002'}">selected</c:if>>Blue IC</option>
                <option value="AR_IT_003" <c:if test="${person.idType eq 'AR_IT_003'}">selected</c:if>>FIN</option>
            </select>
            <span class="error-msg" name="iaisErrorMsg" id="error_idType"></span>
        </iais:value>
    </iais:row>
</div>

<%@include file="personSection.jsp" %>
<iais:row>
    <iais:field width="5" value="Has patient registered for AR/IUI Treatment using another Identification Number before?" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
        <div class="form-check form-check-inline">
            <input class="form-check-input triggerObj" id="isArIUIRegisteredN" type="radio" name="previousIdentification${suffix}" value = "0" <c:if test="${patient.previousIdentification ne null and !patient.previousIdentification}">checked</c:if> />
            <label class="form-check-label" for="isArIUIRegisteredN">
                <span class="check-circle"></span>No
            </label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input triggerObj" id="isArIUIRegisteredY" type="radio" name="previousIdentification${suffix}" value = "1" <c:if test="${patient.previousIdentification}">checked</c:if> />
            <label class="form-check-label" for="isArIUIRegisteredY">
                <span class="check-circle"></span>Yes
            </label>
        </div>
        <span class="error-msg" name="iaisErrorMsg" id="error_previousIdentification"></span>
    </iais:value>
</iais:row>
