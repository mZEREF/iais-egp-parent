<script type="text/javascript" src="<%=webroot1%>js/dataSubmission/thawingSection.js"></script>
<c:set var="thawingStageDto" value="${arSuperDataSubmissionDto.thawingStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading">
        <h4 class="panel-title">
            <strong>
                Thawing (Oocytes & Embryos)
            </strong>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal">
                <iais:row>
                    <iais:field width="6" value="Thawing Oocyte(s) or Embryo(s)" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check col-xs-12" style="padding: 0px;">
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
                    </iais:value>
                    <iais:value width="3" cssClass="col-md-3">
                        <div class="form-check col-xs-12">
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
                    </iais:value>
                    <span id="error_thawings" name="iaisErrorMsg" class="error-msg col-md-6"></span>
                </iais:row>
                <div class="oocytesParts" <c:if test="${!thawingStageDto.hasOocyte}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Thawed" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="number" id="thawedOocytesNum" name="thawedOocytesNum"
                                   value="${thawingStageDto.thawedOocytesNum}"
                                   oninput="if(value.length>2)value=value.slice(0,2)">
                            <span id="error_thawedOocytesNum" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Mature)" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="number" id="thawedOocytesSurvivedMatureNum"
                                   name="thawedOocytesSurvivedMatureNum"
                                   value="${thawingStageDto.thawedOocytesSurvivedMatureNum}"
                                   oninput="if(value.length>2)value=value.slice(0,2)">
                            <span id="error_thawedOocytesSurvivedMatureNum" name="iaisErrorMsg"
                                  class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Immature)" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="number" id="thawedOocytesSurvivedImmatureNum"
                                   name="thawedOocytesSurvivedImmatureNum"
                                   value="${thawingStageDto.thawedOocytesSurvivedImmatureNum}"
                                   oninput="if(value.length>2)value=value.slice(0,2)">
                            <span id="error_thawedOocytesSurvivedImmatureNum" name="iaisErrorMsg"
                                  class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Others)" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="number" id="thawedOocytesSurvivedOtherNum" name="thawedOocytesSurvivedOtherNum"
                                   value="${thawingStageDto.thawedOocytesSurvivedOtherNum}"
                                   oninput="if(value.length>2)value=value.slice(0,2)">
                            <span id="thawedOocytesSurvivedOtherNum" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                </div>
                <div class="embryosParts" <c:if test="${!thawingStageDto.hasEmbryo}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="6" value="No. of Embryos Thawed" mandatory="true" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="number" id="thawedEmbryosNum" name="thawedEmbryosNum"
                                   value="${thawingStageDto.thawedEmbryosNum}"
                                   oninput="if(value.length>2)value=value.slice(0,2)">
                            <span id="error_thawedEmbryosNum" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Embryos Survived after Thawing" mandatory="true"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6">
                            <input type="number" id="thawedEmbryosSurvivedNum" name="thawedEmbryosSurvivedNum"
                                   value="${thawingStageDto.thawedEmbryosSurvivedNum}"
                                   oninput="if(value.length>2)value=value.slice(0,2)">
                            <span id="error_thawedEmbryosSurvivedNum" name="iaisErrorMsg" class="error-msg"></span>
                        </iais:value>
                    </iais:row>
                </div>
            </div>
        </div>
    </div>
</div>