<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/transferInOutStageSection.js"></script>
<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 95px;">
        <h4 class="panel-title">
            <strong>
                Transfer In And Out
            </strong>
        </h4>
    </div>
    <div id="transferInOutDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <c:set var="transferInOutStageDto" value="${arSuperDataSubmissionDto.transferInOutStageDto}" />
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="Is this a Transfer In or Out?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="transferType"
                                   value="in"
                                   id="transferTypeIn"
                                   <c:if test="${transferInOutStageDto.transferType eq 'in'}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="transferTypeIn"><span
                                    class="check-circle"></span>Transfer In</label>
                        </div>
                        <span class="error-msg" name="iaisErrorMsg" id="error_transferType"></span>
                    </iais:value>

                    <iais:value width="4" cssClass="col-md-4">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="transferType"
                                   value="out"
                                   id="transferTypeOut"
                                   <c:if test="${transferInOutStageDto.transferType eq 'out'}">checked</c:if>
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
                        <c:forEach items="${transferreds}" var="transferred" varStatus="s">
                            <c:set var="transferredCode" value="${transferred.code}"/>
                            <div class="form-check col-xs-12" >
                                <input class="form-check-input" type="checkbox"
                                       name="transferredList"
                                       value="${transferredCode}"
                                       id="transferredCheck${transferredCode}"
                                       onchange="toggleOnCheck(this,'transferred${s.index}')"
                                <c:forEach var="transferredObj" items="${transferInOutStageDto.transferredList}">
                                       <c:if test="${transferredObj == transferredCode}">checked</c:if>
                                </c:forEach>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="transferredCheck${transferredCode}"><span
                                        class="check-square"></span>${transferred.codeValue}</label>
                            </div>
                        </c:forEach>
                        <span class="error-msg" name="iaisErrorMsg" id="error_transferredList"></span>
                    </iais:value>
                </iais:row>
                        <iais:row id="transferred0">
                            <iais:field width="5" value="No. of Oocyte(s) Transferred" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="2" type="text" name="oocyteNum"
                                            value="${transferInOutStageDto.oocyteNum}"/>
                                <span class="error-msg" name="iaisErrorMsg" id="error_oocyteNum"></span>
                            </iais:value>
                        </iais:row>
                    <iais:row id="transferred1">
                        <iais:field width="5" value="No. of Embryo(s) Transferred" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="2" type="text" name="embryoNum"
                                        value="${transferInOutStageDto.embryoNum}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_embryoNum"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row id="transferred2">
                        <iais:field width="5" value="Vials of Sperm Transferred" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:input maxLength="2" type="text" name="spermVialsNum"
                                        value="${transferInOutStageDto.spermVialsNum}"/>
                            <span class="error-msg" name="iaisErrorMsg" id="error_spermVialsNum"></span>
                        </iais:value>
                    </iais:row>
                <iais:row>
                    <iais:field width="5" value="Were the Gamete(s) or Embryo(s) from a Donor?" mandatory="true"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="radio"
                                   name="fromDonor"
                                   value="true"
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
                                   value="false"
                                   id="fromDonorNo"
                                   <c:if test="${!transferInOutStageDto.fromDonor}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="fromDonorNo"><span
                                    class="check-circle"></span>No</label>
                        </div>
                    </iais:value>
                </iais:row>
                <div class="inFromParts" <c:if test="${transferInOutStageDto.transferType !='in'}">style="display: none;"</c:if>>
                        <iais:row>
                                <iais:field width="5" value="Transferred In From" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <c:set value="${transferInOutStageDto.transInFromLicenseeId.concat('/').concat(transferInOutStageDto.transInFromHciCode)}"
                                       var="selecctInValue"/>
                                <iais:select name="transInFromHciCode" firstOption="Please Select"
                                             options="transferOutInPremisesSelect" value="${selecctInValue}"
                                             onchange="toggleOnSelect(this, 'AR_TIF_003', 'othersInFrom')"/>
                            </iais:value>
                        </iais:row>

                        <iais:row id="othersInFrom" style="${transferInOutStageDto.transInFromHciCode eq DataSubmissionConsts.TRANSFERRED_IN_FROM_OTHERS ? '' : 'display: none'}">
                            <iais:field width="5" value="Transferred In From (Others)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input  maxLength="100" type="text" name="transInFromOthers" value="${transferInOutStageDto.transInFromOthers}" />
                            </iais:value>
                        </iais:row>
                </div>
                <div class="outFromParts" <c:if test="${transferInOutStageDto.transferType !='out'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transfer Out To" mandatory="true"/>
                        <iais:value width="7" cssClass="col-md-7">
                            <c:set value="${transferInOutStageDto.transOutToLicenseeId.concat('/').concat(transferInOutStageDto.transOutToHciCode)}"
                                   var="selecctOutValue"/>
                            <iais:select name="transOutToHciCode" firstOption="Please Select"
                                         options="transferOutInPremisesSelect" value="${selecctOutValue}"
                                         onchange="toggleOnSelect(this, 'AR_TIF_003', 'othersOutFrom')"/>
                        </iais:value>
                    </iais:row>
                        <iais:row id="othersOutFrom" style="${transferInOutStageDto.transOutToHciCode eq DataSubmissionConsts.TRANSFERRED_IN_FROM_OTHERS ? '' : 'display: none'}">
                            <iais:field width="5" value="Transfer Out To (Others)" mandatory="true"/>
                            <iais:value width="7" cssClass="col-md-7">
                                <iais:input maxLength="100" type="text" name="transOutToOthers" value="${transferInOutStageDto.transOutToOthers}" />
                            </iais:value>
                        </iais:row>
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