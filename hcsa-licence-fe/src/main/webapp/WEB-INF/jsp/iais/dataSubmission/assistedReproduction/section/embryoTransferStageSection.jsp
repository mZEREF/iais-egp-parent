<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/embryoTransferStage.js"></script>
<c:set var="embryoTransferStageDto" value="${arSuperDataSubmissionDto.embryoTransferStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Embryo Transfer
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" value="No. Transferred" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="transferNumSelect" name="transferNum" options="transferNumSelectOption"
                                     value="${embryoTransferStageDto.transferNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Age of 1st Embryo Transferred" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select name="firstEmbryoAge" firstOption="Please Select"
                                     options="firstEmbryoAgeSelectOption"
                                     value="${embryoTransferStageDto.firstEmbryoAge}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="firstEmbryoType"
                                   value="fresh"
                                   id="firstEmbryoTypeFresh"
                                   <c:if test="${embryoTransferStageDto.firstEmbryoType == 'fresh'}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="firstEmbryoTypeFresh"><span
                                    class="check-circle"></span>Fresh Embryo</label>
                        </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="firstEmbryoType"
                                   value="thawed"
                                   id="firstEmbryoTypeThawed"
                                   <c:if test="${embryoTransferStageDto.firstEmbryoType == 'thawed'}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="firstEmbryoTypeThawed"><span
                                    class="check-circle"></span>Thawed Embryo</label>
                        </div>
                    </iais:value>
                    <span id="error_firstEmbryoType" name="iaisErrorMsg" class="error-msg col-md-6"></span>
                </iais:row>
                <div id="section2nd"
                     <c:if test="${embryoTransferStageDto.transferNum < 2}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Age of 2nd Embryo Transferred" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="secondEmbryoAge" firstOption="Please Select"
                                         options="secondEmbryoAgeSelectOption"
                                         value="${embryoTransferStageDto.secondEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                    mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="secondEmbryoType"
                                       value="fresh"
                                       id="secondEmbryoTypeFresh"
                                       <c:if test="${embryoTransferStageDto.secondEmbryoType == 'fresh'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="firstEmbryoTypeFresh"><span
                                        class="check-circle"></span>Fresh Embryo</label>
                            </div>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="secondEmbryoType"
                                       value="thawed"
                                       id="secondEmbryoTypeThawed"
                                       <c:if test="${embryoTransferStageDto.secondEmbryoType == 'thawed'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="secondEmbryoTypeThawed"><span
                                        class="check-circle"></span>Thawed Embryo</label>
                            </div>
                        </iais:value>
                        <span id="error_secondEmbryoType" name="iaisErrorMsg" class="error-msg col-md-6"></span>
                    </iais:row>
                </div>
                <div id="section3rd"
                     <c:if test="${embryoTransferStageDto.transferNum < 3}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Age of 3rd Embryo Transferred" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:select name="thirdEmbryoAge" firstOption="Please Select"
                                         options="thirdEmbryoAgeSelectOption"
                                         value="${embryoTransferStageDto.thirdEmbryoAge}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                    mandatory="true"  cssClass="col-md-6"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="thirdEmbryoType"
                                       value="fresh"
                                       id="thirdEmbryoTypeFresh"
                                       <c:if test="${embryoTransferStageDto.thirdEmbryoType == 'fresh'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="thirdEmbryoTypeFresh"><span
                                        class="check-circle"></span>Fresh Embryo</label>
                            </div>
                        </iais:value>
                        <iais:value width="3" cssClass="col-md-3">
                            <div class="form-check">
                                <input class="form-check-input"
                                       type="radio"
                                       name="thirdEmbryoType"
                                       value="thawed"
                                       id="thirdEmbryoTypeThawed"
                                       <c:if test="${embryoTransferStageDto.thirdEmbryoType == 'thawed'}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="thirdEmbryoTypeThawed"><span
                                        class="check-circle"></span>Thawed Embryo</label>
                            </div>
                        </iais:value>
                        <span id="error_thirdEmbryoType" name="iaisErrorMsg" class="error-msg col-md-6"></span>
                    </iais:row>
                </div>
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
            </div>
        </div>
    </div>
</div>