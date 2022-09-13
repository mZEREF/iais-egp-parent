<div class="panel panel-default">
            <div class="panel-heading">
              <h4 class="panel-title">
                <a href="#iuiStageDetails" data-toggle="collapse">
                  Intrauterine Insemination Cycle
                </a>
              </h4>
            </div>
            <div id="iuiStageDetails" class="panel-collapse collapse in">
              <c:set var="iuiCycleStageDto" value="${arSuperDataSubmissionDto.iuiCycleStageDto}"/>
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <%@include file="patientCommon.jsp"%>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Premises where IUI is Performed" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3" >
                      <div class="form-check" style="padding-left: 0px;">
                        <input class="form-check-input"
                               type="radio"
                               name="ownPremises"
                               value="1"
                               id="ownPremisesRadioYes"
                               <c:if test="${iuiCycleStageDto.ownPremises}">checked</c:if>
                               aria-invalid="false" onchange="showOtherPremises(1)">
                        <label class="form-check-label"
                               for="ownPremisesRadioYes"><span
                                class="check-circle"></span>Own premises</label>
                      </div>
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3" >
                      <div class="form-check" style="padding-left: 0px;">
                        <input class="form-check-input" type="radio"
                               name="ownPremises"
                               value="0"
                               id="ownPremisesRadioNo"
                               <c:if test="${!iuiCycleStageDto.ownPremises}">checked</c:if>
                               aria-invalid="false" onchange="showOtherPremises(0)">
                        <label class="form-check-label"
                               for="ownPremisesRadioNo"><span
                                class="check-circle"></span>Others</label>
                      </div>
                    </iais:value>
                  </iais:row>
                  <iais:row id="otherPremisesRow">
                    <iais:field width="6" cssClass="col-md-6" value="IUI Treatment performed in Other Premises" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                      <iais:input type="text" maxLength="50" value="${iuiCycleStageDto.otherPremises}" name="otherPremises" />
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Date Started" mandatory="true" style="margin-bottom: 0px;"/>
                    <iais:value width="6" cssClass="col-md-6" >
                      <iais:datePicker id = "iuiCycleStartDate" name = "iuiCycleStartDate" dateVal="${iuiCycleStageDto.startDate}"></iais:datePicker>
                      <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value width="6" cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px;padding-left: 0px;" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.userAgeShow}"></c:out></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="No. of Children from Current Marriage" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" >
                      <iais:select name="curMarrChildNum" firstOption="Please Select" options="curMarrChildNumOption"
                                   cssClass="curMarrChildNumSel" value="${iuiCycleStageDto.curMarrChildNum}"/>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="No. of Children from Previous Marriage" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                      <iais:select name="prevMarrChildNum" firstOption="Please Select" options="prevMarrChildNumOption"
                                   cssClass="prevMarrChildNumSel" value="${iuiCycleStageDto.prevMarrChildNum}"/>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <label class="col-xs-4 col-md-6 control-label">No. of Children Delivered under IUI <span class="mandatory">*</span>
                      <a class="btn-tooltip styleguide-tooltip" data-toggle="tooltip" data-html="true" href="javascript:void(0);"
                         title="${DSACK003Message}"
                         style="z-index: 10"
                         data-original-title="">i</a>
                    </label>
                    <iais:value width="6" cssClass="col-md-6">
                      <iais:select name="iuiDeliverChildNum" firstOption="Please Select" options="iuiDeliverChildNumOption"
                                   cssClass="iuiDeliverChildNumSel" value="${iuiCycleStageDto.iuiDeliverChildNum}"/>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="Source of Semen" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                      <c:forEach var="sourceOfSemen" items="${sourceOfSemenOption}" varStatus="index">
                        <div class="form-check" col-xs-7 style="padding-left: 0px;">
                          <c:set var="value" value="${sourceOfSemen.value}"></c:set>
                            <input class="form-check-input" value = "<c:out value="${value}"/>" aria-invalid="false"
                                   type="checkbox" name="sourceOfSemenOp" id="sourceOfSemenOp${value}" <c:if test="${value == DataSubmissionConsts.AR_SOURCE_OF_SEMEN_DONOR || value == DataSubmissionConsts.AR_SOURCE_OF_D_SEMEN_TESTICULAR}"> onclick="showDonorArea('sourceOfSemenOpAR_SOS_003','sourceOfSemenOpAR_SOS_004',1)" </c:if>
                            <c:forEach items="${iuiCycleStageDto.semenSources}" var="checkSemen">
                                   <c:if test="${checkSemen eq value}">checked</c:if>
                            </c:forEach> >
                            <label class="form-check-label" for="sourceOfSemenOp${value}">
                              <span class="check-square"></span><c:out value="${sourceOfSemen.text}"/>
                            </label>

                        </div>
                      </c:forEach>
                      <span class="error-msg" name="iaisErrorMsg" id="error_semenSource"></span>
                    </iais:value>
                    <input type="hidden" name="sourceOfSemenShowDonorNum" id="sourceOfSemenShowDonorNum" value="0">
                  </iais:row>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="How many vials of sperm were extracted?" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6" >
                      <input type="text" oninput="if(value.length>2)value=value.slice(0,2)"  maxlength="2" style="margin-bottom: 0px;" name="extractVialsOfSperm" value="${iuiCycleStageDto.extractVialsOfSperm}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_extractVialsOfSperm"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="6" cssClass="col-md-6" value="How many vials of sperm were used in this cycle?" mandatory="true"/>
                    <iais:value width="6" cssClass="col-md-6">
                      <input type="text" oninput="if(value.length>2)value=value.slice(0,2)"  maxlength="2" style="margin-bottom: 0px;" name="usedVialsOfSperm" value="${iuiCycleStageDto.usedVialsOfSperm}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_usedVialsOfSperm"></span>
                    </iais:value>
                  </iais:row>
                  <%@include file="hasDisposalRow.jsp"%>
                </div>
              </div>
            </div>
</div>
<c:set var="donorFrom" value="iui"/>
<c:set var="donorDtos" value="${iuiCycleStageDto.donorDtos}"/>
<%@include file="donorSection.jsp"%>