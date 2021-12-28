<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/thawingSection.js"></script>
<c:set var="thawingStageDto" value="${arSuperDataSubmissionDto.thawingStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading" style="padding-left: 90px;">
        <h4 class="panel-title">
            <strong>
                Thawing (Oocytes & Embryos)
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
                    <iais:field width="6" value="Thawing Oocyte(s) or Embryo(s)" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <div class="form-check col-xs-12 col-md-6" style="padding: 0px;">
                            <input class="form-check-input" type="checkbox"
                                   name="hasOocyte"
                                   value="true"
                                   id="hasOocyte"
                                   <c:if test="${thawingStageDto.hasOocyte}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="hasOocyte"><span
                                    class="check-square"></span>Oocyte(s)</label>
                        </div>
                        <div class="form-check col-xs-12 col-md-6">
                            <input class="form-check-input" type="checkbox"
                                   name="hasEmbryo"
                                   value="true"
                                   id="hasEmbryo"
                                   <c:if test="${thawingStageDto.hasEmbryo}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="hasEmbryo"><span
                                    class="check-square"></span>Embryo(s)</label>
                        </div>
                        <span id="error_thawings" name="iaisErrorMsg" class="error-msg col-md-12"
                              style="padding: 0px;"></span>
                    </iais:value>
                </iais:row>
                <div class="oocytesParts" <c:if test="${!thawingStageDto.hasOocyte}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Thawed" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="thawedOocytesNum" id="thawedOocytesNum"
                                        value="${thawingStageDto.thawedOocytesNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Mature)" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="thawedOocytesSurvivedMatureNum"
                                        id="thawedOocytesSurvivedMatureNum"
                                        value="${thawingStageDto.thawedOocytesSurvivedMatureNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Immature)" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="thawedOocytesSurvivedImmatureNum"
                                        id="thawedOocytesSurvivedImmatureNum"
                                        value="${thawingStageDto.thawedOocytesSurvivedImmatureNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Others)" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="thawedOocytesSurvivedOtherNum"
                                        id="thawedOocytesSurvivedOtherNum"
                                        value="${thawingStageDto.thawedOocytesSurvivedOtherNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div class="embryosParts" <c:if test="${!thawingStageDto.hasEmbryo}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. of Embryos Thawed" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="thawedEmbryosNum" id="thawedEmbryosNum"
                                        value="${thawingStageDto.thawedEmbryosNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Embryos Survived after Thawing" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <iais:input maxLength="2" type="text" name="thawedEmbryosSurvivedNum"
                                        id="thawedEmbryosSurvivedNum"
                                        value="${thawingStageDto.thawedEmbryosSurvivedNum}"/>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>