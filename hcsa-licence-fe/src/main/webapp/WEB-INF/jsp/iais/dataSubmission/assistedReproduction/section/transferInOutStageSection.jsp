<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/transferInOutStageSection.js"></script>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Transfer In And Out
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="transferInOutStageDto" value="${arSuperDataSubmissionDt.transferInOutStageDto}" />
                <iais:row>
                    <iais:field width="5" value="Is this a Transfer In or Out?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="transferType"
                                   value="in"
                                   id="transferTypeIn"
                                   <c:if test="${transferInOutStageDto.transferType == 'in'}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="transferTypeIn"><span
                                    class="check-circle"></span>Transfer In</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="transferType"
                                   value="out"
                                   id="transferTypeOut"
                                   <c:if test="${transferInOutStageDto.transferType == 'out'}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="transferTypeOut"><span
                                    class="check-circle"></span>Transfer Out</label>
                        </div>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="What was Transferred?" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:forEach items="${transferreds}" var="transferred">
                            <c:set var="transferredCode" value="${transferred.code}"/>
                            <div class="form-check col-xs-12" >
                                <input class="form-check-input" type="checkbox"
                                       name="transferredList"
                                       value="${transferredCode}"
                                       id="transferredCheck${transferredCode}"
                                <c:forEach var="transferredObj" items="${transferInOutStageDto.transferredList}">
                                       <c:if test="${transferredObj == transferredCode}">checked</c:if>
                                </c:forEach>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="transferredCheck${transferredCode}"><span
                                        class="check-square"></span>${transferred.codeValue}</label>
                            </div>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <div class="oocytesParts"
                        <c:forEach var="transferredObj" items="${transferInOutStageDto.transferredList}">
                            <c:if test="${transferredObj !='Oocyte(s)'}">style="display: none;"</c:if>
                        </c:forEach>>
                        <iais:row>
                            <iais:field width="5" value="No. of Oocyte(s) Transferred" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="2" type="text" name="oocyteNum" value="${transferInOutStageDto.oocyteNum}"  />
                            </iais:value>
                        </iais:row>
                </div>
                <div class="EmbryosParts"
                        <c:forEach var="transferredObj" items="${transferInOutStageDto.transferredList}">
                            <c:if test="${transferredObj !='Embryo(s)'}">style="display: none;"</c:if>
                        </c:forEach>>
                    <iais:row>
                        <iais:field width="5" value="No. of Embryo(s) Transferred" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="2" type="text" name="embryoNum" value="${transferInOutStageDto.embryoNum}"  />
                        </iais:value>
                    </iais:row>
                </div>
                <div class="SpermParts"
                        <c:forEach var="transferredObj" items="${transferInOutStageDto.transferredList}">
                            <c:if test="${transferredObj !='Sperm'}">style="display: none;"</c:if>
                        </c:forEach>>
                    <iais:row>
                        <iais:field width="5" value="Vials of Sperm Transferred" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="2" type="text" name="spermVialsNum" value="${transferInOutStageDto.spermVialsNum}"  />
                        </iais:value>
                    </iais:row>
                </div>

                <iais:row>
                    <iais:field width="5" value="Were the Gamete(s) or Embryo(s) from a Donor?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="fromDonor"
                                   value="1"
                                   id="fromDonorYes"
                                   <c:if test="${transferInOutStageDto.fromDonor}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="fromDonorYes"><span
                                    class="check-circle"></span>Yes</label>
                        </div>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="fromDonor"
                                   value="0"
                                   id="fromDonorNo"
                                   <c:if test="${!transferInOutStageDto.fromDonor}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="fromDonorNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>

                <div class="inFromParts" >
                    <iais:row>
                        <iais:field width="5" value="Transferred In From" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="transInFromHciCode"  codeCategory="TRANSFERRED_IN_FROM" value="${transferInOutStageDto.transInFromHciCode}" />
                        </iais:value>
                    </iais:row>
                    <div class="inFromOthersParts" >
                        <iais:row>
                            <iais:field width="5" value="Transferred In From (Others)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="20" type="text" name="transInFromOthers" value="${transferInOutStageDto.transInFromOthers}" />
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <div class="outFromParts" <c:if test="${transferInOutStageDto.transferType !='out'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transfer Out To" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:select name="transOutToHciCode"  codeCategory="TRANSFERRED_IN_FROM" value="${transferInOutStageDto.transOutToHciCode}" />
                        </iais:value>
                    </iais:row>
                    <div class="outFromOthersParts" <c:if test="${transferInOutStageDto.transInFromHciCode !='Others' && transferInOutStageDto.transInFromHciCode !=''}">style="display: none;"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Transfer Out To (Others)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="20" type="text" name="transOutToOthers" value="${transferInOutStageDto.transOutToOthers}" />
                            </iais:value>
                        </iais:row>
                    </div>
                </div>

                <iais:row>
                    <iais:field width="5" value="Date of Transfer" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:datePicker id="transferDate" name="transferDate" value="${transferInOutStageDto.transferDate}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</div>