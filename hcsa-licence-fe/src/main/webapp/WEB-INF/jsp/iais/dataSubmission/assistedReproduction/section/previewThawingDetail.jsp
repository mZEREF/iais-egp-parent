<c:set var="thawingStageDto" value="${arSuperDataSubmissionDto.thawingStageDto}"/>
<c:set var="headingSign" value="completed"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse">
                Thawing (Oocytes & Embryos)
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}"/>
                <p>
                    <label style="font-family:'Arial Negreta', 'Arial Normal', 'Arial';font-weight:700;font-size: 2.2rem;">
                        <c:out value="${patientDto.name}"/>&nbsp
                    </label>
                    <label style="font-family:'Arial Normal', 'Arial';font-weight:400;">${empty patientDto.idNumber ? "" : "("}
                        <c:out value="${patientDto.idNumber}"/>
                        ${empty patientDto.idNumber ? "" : ")"}
                    </label>
                </p>
                <hr/>
                <iais:row>
                    <iais:field width="6" value="Thawing Oocyte(s) or Embryo(s)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6" style="padding:0px;" display="true">
                        <c:if test="${thawingStageDto.hasOocyte}"><p class="col-12">Oocyte(s)</p></c:if>
                        <c:if test="${thawingStageDto.hasEmbryo}"><p class="col-12">Embryo(s)</p></c:if>
                    </iais:value>
                </iais:row>
                <c:if test="${thawingStageDto.hasOocyte}">
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Thawed" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Mature)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesSurvivedMatureNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Immature)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesSurvivedImmatureNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Oocytes Survived after Thawing (Others)"
                                    cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${thawingStageDto.thawedOocytesSurvivedOtherNum}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <c:if test="${thawingStageDto.hasEmbryo}">
                    <iais:row>
                        <iais:field width="6" value="No. of Embryos Thawed" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${thawingStageDto.thawedEmbryosNum}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row>
                        <iais:field width="6" value="No. of Embryos Survived after Thawing" cssClass="col-md-6"/>
                        <iais:value width="6" cssClass="col-md-6" display="true">
                            <c:out value="${thawingStageDto.thawedEmbryosSurvivedNum}"/>
                        </iais:value>
                    </iais:row>
                </c:if>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>