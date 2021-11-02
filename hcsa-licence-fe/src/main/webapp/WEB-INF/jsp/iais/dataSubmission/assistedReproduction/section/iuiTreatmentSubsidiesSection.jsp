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
                    <iais:value width="3" cssClass="col-md-3">
                        <c:forEach items="${pleaseIndicateIuiCoFunding}" var="pleaseIndicateIui">
                            <c:set var="pleaseIndicateIuiValue" value="${pleaseIndicateIui.value}"/>
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="pleaseIndicateIui"
                                       value="${pleaseIndicateIuiValue}"
                                       id="pleaseIndicateIuiCoFundCheck${pleaseIndicateIuiCode}"
                                       <c:if test="${iuiTreatmentSubsidiesDto.artCoFunding}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label">
                                    <span>class="check-circle"></span><c:out value="${pleaseIndicateIui.text}"></c:out>
                                </label>
                            </div>
                        </c:forEach>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>