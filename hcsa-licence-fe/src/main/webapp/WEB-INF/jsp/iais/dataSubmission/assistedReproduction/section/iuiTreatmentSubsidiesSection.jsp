<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                IUI Treatment Subsidies
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="iuiTreatmentSubsidiesDto" value="${arSuperDataSubmissionDto.iuiTreatmentSubsidiesDto}" />
                <iais:row>
                    <iais:field width="5" value="Please indicate IUI Co-funding?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:forEach items="${pleaseIndicateIuiCoFunding}" var="pleaseIndicateIui">
                            <c:set var="pleaseIndicateIuiCode" value="${pleaseIndicateIui.code}"/>
                            <div class="form-check col-xs-7">
                                <input class="form-check-input"
                                       type="radio"
                                       name="pleaseIndicateIui"
                                       value="${pleaseIndicateIuiCode}"
                                       id="pleaseIndicateIuiCoFundCheck${pleaseIndicateIuiCode}"
                                       <c:if test="${iuiTreatmentSubsidiesDto.artCoFunding}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="pleaseIndicateIuiCoFundCheck${pleaseIndicateIuiCode}"><span>
                                    class="check-circle"></span>${pleaseIndicateIui.codeValue}</label>
                            </div>
                        </c:forEach>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>