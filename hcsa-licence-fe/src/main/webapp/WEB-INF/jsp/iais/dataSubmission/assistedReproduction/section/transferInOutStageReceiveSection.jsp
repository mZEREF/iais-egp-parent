<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/transferInOutStageReceiveSection.js"></script>
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
                <c:set var="transferInOutStageDto" value="${arSuperDataSubmissionDto.transferInOutStageDto}"/>
                <h3>
                    <label><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal">
                        <c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>

                <iais:row>
                    <iais:field width="5" value="Is this a Transfer In or Out?"/>
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                        <c:if test="${transferInOutStageDto.transferType eq 'in'}">
                            <c:out value=" Transfer In"/>
                        </c:if>
                        <c:if test="${transferInOutStageDto.transferType eq'out'}">
                            <c:out value=" Transfer Out"/>
                        </c:if>
                    </iais:value>
                </iais:row>

                <iais:row>
                    <iais:field width="5" value="What was Transferred?"/>
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                        <c:forEach items="${transferInOutStageDto.transferredList}" var="transferred" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${transferred}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>

                <c:if test="${fn:contains(transferInOutStageDto.transferredList,'AR_WWT_001')}">
                    <iais:row id="transferred0">
                        <iais:field width="5" value="No. of Oocyte(s) Transferred" mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:input maxLength="2" type="text" name="oocyteNum"
                                        value="${transferInOutStageDto.oocyteNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <input type="hidden" value="${outStageOocyte}">
                            <div class="form-check col-xs-12">
                                <input class="form-check-input" type="checkbox"
                                       name="sameAmount"
                                       value="true"
                                       id="sameAmountOocyte"
                                       <c:if test="${transferInOutStageDto.oocyteNum == outStageOocyte}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="sameAmountOocyte"><span
                                        class="check-square"></span>Received Same Amount</label>
                            </div>
                        </iais:value>
                    </iais:row>
                </c:if>

                <c:if test="${fn:contains(transferInOutStageDto.transferredList,'AR_WWT_002')}">
                    <iais:row id="transferred1">
                        <iais:field width="5" value="No. of Embryo(s) Transferred" mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:input maxLength="2" type="text" name="embryoNum"
                                        value="${transferInOutStageDto.embryoNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <input type="hidden" value="${outStageEmbryo}">
                            <div class="form-check col-xs-12">
                                <input class="form-check-input" type="checkbox"
                                       name="sameAmount"
                                       value="true"
                                       id="sameAmountEmbryo"
                                       <c:if test="${transferInOutStageDto.embryoNum == outStageEmbryo}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="sameAmountEmbryo"><span
                                        class="check-square"></span>Received Same Amount</label>
                            </div>
                        </iais:value>
                    </iais:row>
                </c:if>

                <c:if test="${fn:contains(transferInOutStageDto.transferredList,'AR_WWT_003')}">
                    <iais:row id="transferred2">
                        <iais:field width="5" value="Vials of Sperm Transferred" mandatory="true"/>
                        <iais:value width="3" cssClass="col-md-3">
                            <iais:input maxLength="2" type="text" name="spermVialsNum"
                                        value="${transferInOutStageDto.spermVialsNum}"/>
                        </iais:value>
                        <iais:value width="4" cssClass="col-md-4">
                            <input type="hidden" value="${outStageSpermVial}">
                            <div class="form-check col-xs-12">
                                <input class="form-check-input" type="checkbox"
                                       name="sameAmount"
                                       value="true"
                                       id="sameAmountSpermVial"
                                       <c:if test="${transferInOutStageDto.spermVialsNum == outStageSpermVial}">checked</c:if>
                                       aria-invalid="false">
                                <label class="form-check-label"
                                       for="sameAmountSpermVial"><span
                                        class="check-square"></span>Received Same Amount</label>
                            </div>
                        </iais:value>
                    </iais:row>
                </c:if>

                <iais:row>
                    <iais:field width="5" value="Were the Gamete(s) or Embryo(s) from a Donor?"/>
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                        <c:out value="${transferInOutStageDto.fromDonor ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>

                <c:if test="${transferInOutStageDto.transferType =='in'}">
                    <iais:row>
                        <iais:field width="5" value="Transferred In From"/>
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:set value="${transferInOutStageDto.transInFromLicenseeId.concat('/').concat(transferInOutStageDto.transInFromHciCode)}"
                                   var="selecctInValue"/>
                            <c:forEach items="${transferOutInPremisesSelect}" var="premisesSelect" varStatus="s">
                                <c:if test="${premisesSelect.value eq selecctInValue}">
                                    <c:out value="${premisesSelect.text}"/>
                                </c:if>
                            </c:forEach>
                        </iais:value>
                    </iais:row>

                    <iais:row style="${transferInOutStageDto.transInFromHciCode eq'Others' ? '' : 'display:none;'}">
                        <iais:field width="5" value="Transferred In From (Others)"/>
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:out value="${transferInOutStageDto.transInFromOthers}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <c:if test="${transferInOutStageDto.transferType =='out'}">
                    <iais:row>
                        <iais:field width="5" value="Transferred Out To"/>
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:set value="${transferInOutStageDto.transOutToLicenseeId.concat('/').concat(transferInOutStageDto.transOutToHciCode)}"
                                   var="selecctInValue"/>
                            <c:forEach items="${transferOutInPremisesSelect}" var="premisesSelect" varStatus="s">
                                <c:if test="${premisesSelect.value eq selecctInValue}">
                                    <c:out value="${premisesSelect.text}"/>
                                </c:if>
                            </c:forEach>
                        </iais:value>
                    </iais:row>

                    <iais:row style="${transferInOutStageDto.transOutToHciCode eq'Others' ? '' : 'display:none;'}">
                        <iais:field width="5" value="Transferred Out To (Others)"/>
                        <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                            <c:out value="${transferInOutStageDto.transOutToOthers}"/>
                        </iais:value>
                    </iais:row>
                </c:if>

                <iais:row>
                    <iais:field width="5" value="Date of Transfer"/>
                    <iais:value width="7" cssClass="col-md-7" label="true" style="padding-top: 13px;">
                        <c:out value="${transferInOutStageDto.transferDate}"/>
                    </iais:value>
                </iais:row>
            </div>

        </div>
    </div>
</div>
<c:if test="${hasDraft}">
    <iais:confirm
            msg="DS_MSG002"
            callBack="$('#_draftModal').modal('hide');submit('resume');" popupOrder="_draftModal" yesBtnDesc="Resume from draft"
            cancelBtnCls="btn btn-primary" yesBtnCls="btn btn-secondary" needFungDuoJi="false"
            cancelBtnDesc="Continue" cancelFunc="mySubmit('delete')"/>
</c:if>