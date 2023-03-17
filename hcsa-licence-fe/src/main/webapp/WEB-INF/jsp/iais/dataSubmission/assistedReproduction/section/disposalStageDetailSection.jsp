<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/disposal.js"></script>

<div class="panel panel-default" id="disposalDiv" style="${arSuperDataSubmissionDto.disposalStageDto eq null?'display:none;':''}">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Disposal
            </strong>
        </h4>
    </div>
    <div id="disposalDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:if test="${'AR_STG014' eq arSuperDataSubmissionDto.dataSubmissionDto.cycleStage}">
                    <h3>
                        <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                        <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                        </span>
                    </h3>
                </c:if>
                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="What was disposed?" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="disposedType"  name="disposedType" firstOption="Please Select" codeCategory="CATE_ID_DISPOSAL_TYPE" value="${arSuperDataSubmissionDto.disposalStageDto.disposedType}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_disposedType"></span>
                    </iais:value>
                </iais:row>
                <div id="oocyteDisplay" <c:if test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay!=1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Immature" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <iais:input maxLength="2" type="text" id="immature" name="immature"  value="${arSuperDataSubmissionDto.disposalStageDto.immatureString==null?arSuperDataSubmissionDto.disposalStageDto.immature:arSuperDataSubmissionDto.disposalStageDto.immatureString}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_immature"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Abnormally Fertilised" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <iais:input maxLength="2" type="text" id="abnormallyFertilised" name="abnormallyFertilised"  value="${arSuperDataSubmissionDto.disposalStageDto.abnormallyFertilisedString==null?arSuperDataSubmissionDto.disposalStageDto.abnormallyFertilised:arSuperDataSubmissionDto.disposalStageDto.abnormallyFertilisedString}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_abnormallyFertilised"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Unfertilised" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <iais:input maxLength="2" type="text" id="unfertilised" name="unfertilised"  value="${arSuperDataSubmissionDto.disposalStageDto.unfertilisedString==null?arSuperDataSubmissionDto.disposalStageDto.unfertilised:arSuperDataSubmissionDto.disposalStageDto.unfertilisedString}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_unfertilised"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Atretic" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <iais:input maxLength="2" type="text" id="atretic" name="atretic"  value="${arSuperDataSubmissionDto.disposalStageDto.atreticString==null?arSuperDataSubmissionDto.disposalStageDto.atretic:arSuperDataSubmissionDto.disposalStageDto.atreticString}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_atretic"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Damaged" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <iais:input maxLength="2" type="text" id="damaged" name="damaged"  value="${arSuperDataSubmissionDto.disposalStageDto.damagedString==null?arSuperDataSubmissionDto.disposalStageDto.damaged:arSuperDataSubmissionDto.disposalStageDto.damagedString}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_damaged"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="Lysed / Degenerated" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <iais:input maxLength="2" type="text" id="lysedOrDegenerated" name="lysedOrDegenerated"  value="${arSuperDataSubmissionDto.disposalStageDto.lysedOrDegeneratedString==null?arSuperDataSubmissionDto.disposalStageDto.lysedOrDegenerated:arSuperDataSubmissionDto.disposalStageDto.lysedOrDegeneratedString}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_lysedOrDegenerated"></span>
                        </iais:value>
                    </iais:row>
                </div>


                <div id="embryoDisplay" <c:if test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay!=2}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" cssClass="col-md-6" value="No. of Poor Quality / Unhealthy / Abnormal Discarded" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <iais:input maxLength="2" type="text" id="unhealthyNum" name="unhealthyNum"  value="${arSuperDataSubmissionDto.disposalStageDto.unhealthyNumString==null?arSuperDataSubmissionDto.disposalStageDto.unhealthyNum:arSuperDataSubmissionDto.disposalStageDto.unhealthyNumString}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_unhealthyNum"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <c:choose >
                        <c:when test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay==3}">
                            <iais:field width="6" cssClass="col-md-6" id="otherDiscardedNumField" value="Discarded for Other Reasons" mandatory="true"/>
                        </c:when>
                        <c:otherwise>
                            <iais:field width="6" cssClass="col-md-6" id="otherDiscardedNumField" value="Discarded for Other Reasons" />
                        </c:otherwise>
                    </c:choose>
                    <iais:value width="6" cssClass="col-md-6" >
                        <iais:input maxLength="2" type="text" id="otherDiscardedNum" name="otherDiscardedNum"  onchange="" value="${arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNumString==null?arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum:arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNumString}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherDiscardedNum"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <c:if test="${empty arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum || arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum<=0}">
                        <iais:field width="6" cssClass="col-md-6" id="otherDiscardedReasonField" value="Other Reasons for Discarding" />
                    </c:if>
                    <c:if test="${ arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum>0}">
                        <iais:field width="6" cssClass="col-md-6" id="otherDiscardedReasonField" value="Other Reasons for Discarding" mandatory="true"  />
                    </c:if>

                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="100"   name="otherDiscardedReason" value="<c:out value="${arSuperDataSubmissionDto.disposalStageDto.otherDiscardedReason}"/>" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherDiscardedReason"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Total No. Disposed Of" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.disposalStageDto.totalNum}</div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_totalNum"></span>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
