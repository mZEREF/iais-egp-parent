<c:set var="thawingStageDto" value="${arSuperDataSubmissionDto.thawingStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Thawing (Oocytes & Embryos)
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <iais:row>
                    <iais:field width="6" value="Thawing Oocyte(s) or Embryo(s)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:if test="${thawingStageDto.hasOocyte}"><p class="col-12">Oocyte(s)</p></c:if>
                        <c:if test="${thawingStageDto.hasEmbryo}"><p class="col-12">Embryo(s)</p></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Oocytes Thawed" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${thawingStageDto.thawedOocytesNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Oocytes Survived after Thawing (Mature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${thawingStageDto.thawedOocytesSurvivedMatureNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Oocytes Survived after Thawing (Immature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${thawingStageDto.thawedOocytesSurvivedImmatureNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Oocytes Survived after Thawing (Others)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${thawingStageDto.thawedOocytesSurvivedOtherNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Embryos Thawed" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${thawingStageDto.thawedEmbryosNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. of Embryos Survived after Thawing" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${thawingStageDto.thawedEmbryosSurvivedNum}"/>
                    </iais:value>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>