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
                  <h3 class="panel-title">
                    <strong>
                      <c:out value="${patientDto.name}"></c:out>
                    </strong>
                    <c:out value="(${patientDto.idNumber})"></c:out>
                  </h3>
                  <iais:row>
                    <iais:field value="Premises where IUI is Performed" mandatory="false"/>
                    <iais:value width="3" cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px;padding-left: 0px;" class="col-xs-6 col-md-6 control-label">
                        <c:out value="${arSuperDataSubmissionDto.premisesDto.premiseLabel}"/>
                      </span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Date Started" mandatory="true" style="margin-bottom: 0px;"/>
                    <iais:value cssClass="col-md-3">
                      <iais:datePicker id = "iuiCycleStartDate" name = "iuiCycleStartDate" dateVal="${iuiCycleStageDto.startDate}"></iais:datePicker>
                      <span class="error-msg" name="iaisErrorMsg" id="error_startDate"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Patient's Age as of This Treatment" mandatory="false"/>
                    <iais:value cssClass="col-xs-5 col-md-6 control-label">
                      <span style="font-size: 16px;padding-left: 0px;" class="col-xs-6 col-md-6 control-label"><c:out value="${iuiCycleStageDto.userAgeShow}"></c:out></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. of Children with Current Marriage" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <iais:select name="curMarrChildNum" firstOption="Please Select" options="curMarrChildNumOption" value="${iuiCycleStageDto.curMarrChildNum}"></iais:select>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="No. of Children with Previous Marriage" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <iais:select name="prevMarrChildNum" firstOption="Please Select" options="prevMarrChildNumOption" value="${iuiCycleStageDto.prevMarrChildNum}"></iais:select>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Total No. of Children Delivered under IUI" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="iuiDeliverChildNum" value="${iuiCycleStageDto.iuiDeliverChildNum}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_iuiDeliverChildNum"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="Source of Semen" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <c:forEach var="sourceOfSemen" items="${sourceOfSemenOption}" varStatus="index">
                        <div class="form-check" col-xs-7 style="padding-left: 0px;">
                          <c:if test="${iuiCycleStageDto.semenSources == null}">
                            <input class="form-check-input" type="checkbox" name="sourceOfSemenOp" value = "<c:out value="${sourceOfSemen.value}"/>"
                                   aria-invalid="false" id="sourceOfSemenOp${index.index}"
                            >
                            <label class="form-check-label" for="sourceOfSemenOp${index.index}">
                              <span class="check-square"></span><c:out value="${sourceOfSemen.text}"/>
                            </label>
                          </c:if>
                          <c:if test="${iuiCycleStageDto.semenSources != null}">
                            <input class="form-check-input" value = "<c:out value="${sourceOfSemen.value}"/>" aria-invalid="false"
                                   type="checkbox" name="sourceOfSemenOp" id="sourceOfSemenOp${index.index}"
                            <c:forEach items="${iuiCycleStageDto.semenSources}" var="checkSemen">
                                   <c:if test="${checkSemen eq sourceOfSemen.value}">checked="checked"</c:if>
                            </c:forEach>
                            >
                            <label class="form-check-label" for="sourceOfSemenOp${index.index}">
                              <span class="check-square"></span><c:out value="${sourceOfSemen.text}"/>
                            </label>
                          </c:if>
                        </div>
                      </c:forEach>
                      <span class="error-msg" name="iaisErrorMsg" id="error_semenSource"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="How many vials of sperm were extracted" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="extractVialsOfSperm" value="${iuiCycleStageDto.extractVialsOfSperm}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_extractVialsOfSperm"></span>
                    </iais:value>
                  </iais:row>
                  <iais:row>
                    <iais:field value="How many vials of sperm were used in this cycle" mandatory="true"/>
                    <iais:value cssClass="col-md-3">
                      <input type="number" oninput="if(value.length>2)value=value.slice(0,2)" style="margin-bottom: 0px;" name="usedVialsOfSperm" value="${iuiCycleStageDto.usedVialsOfSperm}"/>
                      <span class="error-msg" name="iaisErrorMsg" id="error_usedVialsOfSperm"></span>
                    </iais:value>
                  </iais:row>
                </div>
              </div>
            </div>
</div>
<c:set var="donorDtos" value="${iuiCycleStageDto.donorDtos}"/>
<%@include file="donorSection.jsp"%>