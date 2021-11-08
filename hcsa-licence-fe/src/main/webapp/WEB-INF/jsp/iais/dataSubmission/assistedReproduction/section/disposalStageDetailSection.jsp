<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/disposal.js"></script>

<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                disposal
            </strong>
        </h4>
    </div>
    <div id="disposalDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" value="What was disposed?" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:select cssClass="disposedType"  name="disposedType" firstOption="Please Select" options="disposalTypeSelectOption" value="${arSuperDataSubmissionDto.disposalStageDto.disposedType}"></iais:select>
                        <span class="error-msg" name="iaisErrorMsg" id="error_disposedType"></span>
                    </iais:value>
                </iais:row>
                <div id="oocyteDisplay" <c:if test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay!=1}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="Immature" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <input type="text" maxlength="2" id="immature" name="immature" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.disposalStageDto.immature}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_immature"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Abnormally Fertilised" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <input type="text" maxlength="2" id="abnormallyFertilised" name="abnormallyFertilised" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.disposalStageDto.abnormallyFertilised}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_abnormallyFertilised"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Unfertilised" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <input type="text" maxlength="2" id="unfertilised" name="unfertilised" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.disposalStageDto.unfertilised}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_unfertilised"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Atretic" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <input type="text" maxlength="2" id="atretic" name="atretic" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.disposalStageDto.atretic}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_atretic"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Damaged" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <input type="text" maxlength="2" id="damaged" name="damaged" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.disposalStageDto.damaged}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_damaged"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="Lysed / Degenerated" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <input type="text" maxlength="2" id="lysedOrDegenerated" name="lysedOrDegenerated" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.disposalStageDto.lysedOrDegenerated}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_lysedOrDegenerated"></span>
                        </iais:value>
                    </iais:row>
                </div>


                <div id="embryoDisplay" <c:if test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay!=2}">style="display: none"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. of Poor Quality / Unhealthy / Abnormal Discarded" />
                        <iais:value width="6" cssClass="col-md-6" >
                            <input type="text" maxlength="2" id="unhealthyNum" name="unhealthyNum" onkeypress="keyNumericPress()" value="${arSuperDataSubmissionDto.disposalStageDto.unhealthyNum}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_unhealthyNum"></span>
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <c:choose >
                        <c:when test="${arSuperDataSubmissionDto.disposalStageDto.disposedTypeDisplay==3}">
                            <iais:field width="6" id="otherDiscardedNumField" value="Discarded for Other Reasons" mandatory="true"/>
                        </c:when>
                        <c:otherwise>
                            <iais:field width="6" id="otherDiscardedNumField" value="Discarded for Other Reasons" />
                        </c:otherwise>
                    </c:choose>
                    <iais:value width="6" cssClass="col-md-6" >
                        <input type="text" maxlength="2" id="otherDiscardedNum" name="otherDiscardedNum" onkeypress="keyNumericPress()" onchange="" value="${arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum}"/>
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherDiscardedNum"></span>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <c:if test="${empty arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum || arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum<=0}">
                        <iais:field width="6" id="otherDiscardedReasonField" value="Other Reasons for Discarding" />
                    </c:if>
                    <c:if test="${ arSuperDataSubmissionDto.disposalStageDto.otherDiscardedNum>0}">
                        <iais:field width="6" id="otherDiscardedReasonField" value="Other Reasons for Discarding" mandatory="true"  />
                    </c:if>

                    <iais:value width="6" cssClass="col-md-6">
                        <input type="text" maxlength="20"   name="otherDiscardedReason" value="${arSuperDataSubmissionDto.disposalStageDto.otherDiscardedReason}" >
                        <span class="error-msg" name="iaisErrorMsg" id="error_otherDiscardedReason"></span>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Total No. Disposed Of" mandatory="false"/>
                    <iais:value width="6" display="true">
                        <div id="totalNum" name="totalNum">${arSuperDataSubmissionDto.disposalStageDto.totalNum}</div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_totalNum"></span>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>
