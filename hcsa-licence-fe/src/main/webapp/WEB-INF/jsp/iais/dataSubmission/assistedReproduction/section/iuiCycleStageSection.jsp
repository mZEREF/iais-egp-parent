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
              <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
              <div class="panel-body">
                <div class="panel-main-content form-horizontal">
                  <h3>
                    <p><label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;"><c:out value="${patientDto.name}"/>&nbsp</label><label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </label></p>
                  </h3>
                  <iais:row>
                    <iais:field  width="5" value="Premises where IUI is Performed" mandatory="false"/>
                    <iais:value width="7" cssClass="col-md-7">
                      <span >
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                      </span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="5" value="Date Started" mandatory="true" style="margin-bottom: 0px;"/>
                    <iais:value width="7" cssClass="col-md-7" >
                      <iais:datePicker id = "iuiCycleStartDate" name = "iuiCycleStartDate" dateVal="${iuiCycleStageDto.startDate}"></iais:datePicker>
                      <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="5" value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value width="7" cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px;padding-left: 0px;" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.userAgeShow}"></c:out></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="5" value="No. of Children with Current Marriage" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7" >
                      <iais:select name="curMarrChildNum" firstOption="Please Select" options="curMarrChildNumOption" value="${iuiCycleStageDto.curMarrChildNum}"></iais:select>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="5" value="No. of Children with Previous Marriage" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                      <iais:select name="prevMarrChildNum" firstOption="Please Select" options="prevMarrChildNumOption" value="${iuiCycleStageDto.prevMarrChildNum}"></iais:select>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="5" value="Total No. of Children Delivered under IUI" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="iuiDeliverChildNum" value="${iuiCycleStageDto.iuiDeliverChildNum}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_iuiDeliverChildNum"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="5" value="Source of Semen" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                      <c:forEach var="sourceOfSemen" items="${sourceOfSemenOption}" varStatus="index">
                        <div class="form-check" col-xs-7 style="padding-left: 0px;">
                          <c:set var="value" value="${sourceOfSemen.value}"></c:set>
                            <input class="form-check-input" value = "<c:out value="${value}"/>" aria-invalid="false"
                                   type="checkbox" name="sourceOfSemenOp" id="sourceOfSemenOp${value}" <c:if test="${value == DataSubmissionConsts.AR_SOURCE_OF_SEMEN_DONOR}"> onclick="showDonorArea('sourceOfSemenOpAR_SOS_003',1)" </c:if>
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
                  </iais:row>
                  <iais:row>
                    <iais:field  width="5" value="How many vials of sperm were extracted" mandatory="true"/>
                    <iais:value  width="7" cssClass="col-md-7" >
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="extractVialsOfSperm" value="${iuiCycleStageDto.extractVialsOfSperm}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_extractVialsOfSperm"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field width="5" value="How many vials of sperm were used in this cycle" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="usedVialsOfSperm" value="${iuiCycleStageDto.usedVialsOfSperm}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_usedVialsOfSperm"></span>
                    </iais:value>
                  </iais:row>
                </div>
              </div>
            </div>
</div>
<c:set var="donorFrom" value="iui"/>
<c:set var="donorDtos" value="${iuiCycleStageDto.donorDtos}"/>
<%@include file="donorSection.jsp"%>