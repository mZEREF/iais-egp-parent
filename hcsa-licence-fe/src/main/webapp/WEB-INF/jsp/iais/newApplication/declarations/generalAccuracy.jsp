<c:set var="appDeclarationMessageDto" value="${AppSubmissionDto.appDeclarationMessageDto}"/>

<div class="panel-body">
    <div class="row">
        <h2>General Accuracy Declaration</h2>
    </div>
    <br>
    <div class="row">
        <div>
            <div class="col-xs-12 form-group">
                <iais:message key="DECLARATION_GENERAL_ACCURACY_ITME_1" escape="false"/>
                <%--The granting of this application is dependent on full compliance with the requirements under the Healthcare Services Act 2020; any regulations, rules, code of practices and directions issued under it; and any conditions imposed by the Director of Medical Services.
                <br/>
                <br/>
                Any person that, in relation to any matter under the Healthcare Services Act 2020 (i.e. including the making of this application), makes any statement, or provides any information or document, that is false or misleading in a material particular; and knows or ought reasonably to know that, or is reckless as to whether, the statement, information or document is false or misleading in a material particular, shall be guilty of an offence and shall be liable on conviction to a fine not exceeding $20,000 or to imprisonment for a term not exceeding 12 months or to both.
                <br/>
                <br/>
                Regulatory action (e.g. revocation or suspension of a licence, forfeiture of the whole or part of any security deposit given by the licensee, directing a licensee to pay a financial penalty) may be also taken out against a licensee if the Director of Medical Services is satisfied that the licence has been obtained by the licensee by fraud, or the licensee has, in connection with the application for the grant of the licence, made a statement or provided any information or document that is false, misleading or inaccurate in a material particular.
                <br/>
                <br/>
                The information provided in this application and any document submitted together with the application is not false, misleading or inaccurate in any particular manner.
                --%>
            </div>
            <div class="form-check col-xs-3">
                <input class="form-check-input" type="radio" name="generalAccuracyItem1" value = "0" aria-invalid="false"
                    <c:if test="${appDeclarationMessageDto.generalAccuracyItem1 == '0'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>Yes</label>
            </div>
            <div class="form-check form-group col-xs-3">
                <input class="form-check-input" type="radio" name="generalAccuracyItem1" value = "1" aria-invalid="false"
                       <c:if test="${appDeclarationMessageDto.generalAccuracyItem1 == '0'}">checked="checked"</c:if> />
                <label class="form-check-label"><span class="check-circle"></span>No</label>
            </div>
            <span class="error-msg col-xs-12 form-group" name="iaisErrorMsg" id="error_generalAccuracyItem1"></span>
        </div>
    </div>
</div>
