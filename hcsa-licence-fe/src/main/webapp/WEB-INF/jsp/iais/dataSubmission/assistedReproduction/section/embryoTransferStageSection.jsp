<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/embryoTransferStage.js"></script>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>
<input type="hidden" id="age" value="${age}">
<input type="hidden" id="haveStimulationCycles" value="${haveStimulationCycles}">
<input type="hidden" id="embryoTransferCount" value="${embryoTransferCount}">
<input type="hidden" id="totalEmbryos" value="${totalEmbryos}">
<input type="hidden" id="haveEmbryoTransferGreaterFiveDay" value="${haveEmbryoTransferGreaterFiveDay}">
<div id="flagTwoMessage" hidden><iais:message key="DS_ERR047"/> </div>
<div id="flagThreeMessage" hidden><iais:message key="DS_ERR049"/> </div>
<c:if test="${commonFlag}"><div id="amendMessage" hidden>There are no changes made to this submission. Please select a section to amend before submitting.</div></c:if>
<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Embryo Transfer
            </strong>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="6" value="No. Transferred" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="transferNumSelect" name="transferNum" options="transferNumSelectOption"
                                     value="${embryoTransferStageDto.transferNum}"/>
                        <span id="error_FreshEmbryosNum" name="iaisErrorMsg" class="error-msg col-md-12" style="padding-left: 0px"></span>
                        <span id="error_thawedEmbryosNum" name="iaisErrorMsg" class="error-msg col-md-12" style="padding-left: 0px"></span>
                    </iais:value>
                </iais:row>


                <c:forEach var="embryoTransferDetailDto" items="${embryoTransferStageDto.embryoTransferDetailDtos}" varStatus="seq">
                    <div id="${seq.index+1}Embryo"
                         <c:if test="${embryoTransferStageDto.transferNum < seq.index+1}">style="display: none;"</c:if>>
                    <iais:row>
                        <c:choose>
                            <c:when test="${seq.index eq '0'}"><iais:field width="6" value="1st Embryo" mandatory="true" cssClass="col-md-6"/></c:when>
                            <c:when test="${seq.index eq '1'}"><iais:field width="6" value="2nd Embryo" mandatory="true" cssClass="col-md-6"/></c:when>
                            <c:when test="${seq.index eq '2'}"><iais:field width="6" value="3rd Embryo" mandatory="true" cssClass="col-md-6"/></c:when>
                            <c:otherwise><iais:field width="6" value="${seq.index+1}th Embryo" mandatory="true" cssClass="col-md-6"/></c:otherwise>
                        </c:choose>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="${seq.index+1}EmbryoAge" firstOption="Please Select"
                                         options="${seq.index+1}EmbryoAgeSelectOption" cssClass="ageSelect"
                                         value="${embryoTransferDetailDto.embryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <c:choose>
                            <c:when test="${seq.index eq '0'}"><iais:field width="6" value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                                                           mandatory="true" cssClass="col-md-6"/></c:when>
                            <c:when test="${seq.index eq '1'}"><iais:field width="6" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                                                           mandatory="true" cssClass="col-md-6"/></c:when>
                            <c:when test="${seq.index eq '2'}"><iais:field width="6" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                                                           mandatory="true" cssClass="col-md-6"/></c:when>
                            <c:otherwise><iais:field width="6" value="Was the ${seq.index+1} Embryo Embryo Transferred a fresh or thawed embryo?"
                                                     mandatory="true" cssClass="col-md-6"/></c:otherwise>
                        </c:choose>

                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check" style="padding: 0px;">
                                <input class="form-check-input"
                                       type="radio"
                                       name="${seq.index+1}EmbryoType"
                                       value="fresh"
                                       id="${seq.index+1}EmbryoTypeFresh"
                                       <c:if test="${embryoTransferDetailDto.embryoType == 'fresh'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="${seq.index+1}EmbryoTypeFresh"><span
                                        class="check-circle"></span>Fresh Embryo</label>
                            </div>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="${seq.index+1}EmbryoType"
                                       value="thawed"
                                       id="${seq.index+1}EmbryoTypeThawed"
                                       <c:if test="${embryoTransferDetailDto.embryoType == 'thawed'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="${eseq.index+1}EmbryoTypeThawed"><span
                                        class="check-circle"></span>Thawed Embryo</label>
                            </div>
                        </iais:value>
                        <span id="error_${seq.index+1}EmbryoType" name="iaisErrorMsg" class="error-msg col-md-6"></span>
                    </iais:row>
                    </div>
                </c:forEach>

                <iais:row>
                    <iais:field width="6" value="1st Date of Transfer" mandatory="true"  cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:datePicker id="firstTransferDate" name="firstTransferDate"
                                         dateVal="${embryoTransferStageDto.firstTransferDate}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="2nd Date of Transfer (if applicable)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:datePicker id="secondTransferDate" name="secondTransferDate"
                                         dateVal="${embryoTransferStageDto.secondTransferDate}"/>
                    </iais:value>
                </iais:row>
                <%@include file="hasDisposalRow.jsp"%>
            </div>
        </div>
    </div>
</div>
<iais:confirm msg="DS_ERR047" callBack="$('#flagOutDiv').modal('hide');" popupOrder="flagOutDiv" yesBtnDesc="Close"
              yesBtnCls="btn btn-secondary" needCancel="false" needFungDuoJi="false"/>