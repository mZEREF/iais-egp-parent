<c:set var="thawingStageDto" value="${arSuperDataSubmissionDto.thawingStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                Thawing (Oocytes & Embryos)
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <iais:row style="margin-bottom: 0;">
                    <label class="col-xs-4 col-md-4 control-label"><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/>
                        <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                    </label>
                    <label class="col-xs-8 col-md-8 control-label">Submission ID : <span style="font-weight:normal"><c:out value="${arSuperDataSubmissionDto.dataSubmissionDto.submissionNo}"/></span>
                    </label>
                </iais:row>
                <hr/>
                <iais:row>
                    <iais:field width="5"  value="" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Thawing Oocyte(s) or Embryo(s)" />
                    <iais:value width="7" cssClass="col-md-7" style="padding:0px;" display="true">
                        <c:if test="${thawingStageDto.hasOocyte}"><p class="col-12">Oocyte(s)</p></c:if>
                        <c:if test="${thawingStageDto.hasEmbryo}"><p class="col-12">Embryo(s)</p></c:if>
                    </iais:value>
                </iais:row>
                <c:if test="${thawingStageDto.hasOocyte}">
                    <iais:row>
                        <iais:field width="5" value="No. of Oocytes Thawed" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. of Oocytes Survived after Thawing (Mature)"
                                    />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesSurvivedMatureNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. of Oocytes Survived after Thawing (Immature)"
                                    />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesSurvivedImmatureNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. of Oocytes Survived after Thawing (Others)"
                                    />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesSurvivedOtherNum}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <c:if test="${thawingStageDto.hasEmbryo}">
                    <iais:row>
                        <iais:field width="5" value="No. of Embryos Thawed" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${thawingStageDto.thawedEmbryosNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="5" value="No. of Embryos Survived after Thawing" />
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${thawingStageDto.thawedEmbryosSurvivedNum}"/>
                        </iais:value>
                    </iais:row>
                </c:if>

            </div>
        </div>
    </div>
</div>