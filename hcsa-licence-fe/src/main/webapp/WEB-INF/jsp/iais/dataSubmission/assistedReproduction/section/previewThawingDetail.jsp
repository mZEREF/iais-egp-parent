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
                    <iais:field width="7" value="Thawing Oocyte(s) or Embryo(s)" cssClass="col-md-7"/>
                    <iais:value width="5" display="true">
                        <c:if test="${thawingStageDto.hasOocyte}"><p class="col-12">Oocyte(s)</p></c:if>
                        <c:if test="${thawingStageDto.hasEmbryo}"><p class="col-12">Embryo(s)</p></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="7" value="No. of Oocytes Thawed" cssClass="col-md-7"/>
                    <iais:value width="5" display="true">
                        <c:out value="${thawingStageDto.thawedOocytesNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="7" value="No. of Oocytes Survived after Thawing (Mature)" cssClass="col-md-7"/>
                    <iais:value width="5" display="true">
                        <c:out value="${thawingStageDto.thawedOocytesSurvivedMatureNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="7" value="No. of Oocytes Survived after Thawing (Immature)" cssClass="col-md-7"/>
                    <iais:value width="5" display="true">
                        <c:out value="${thawingStageDto.thawedOocytesSurvivedImmatureNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="7" value="No. of Oocytes Survived after Thawing (Others)" cssClass="col-md-7"/>
                    <iais:value width="5" display="true">
                        <c:out value="${thawingStageDto.thawedOocytesSurvivedOtherNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="7" value="No. of Embryos Thawed" cssClass="col-md-7"/>
                    <iais:value width="5" display="true">
                        <c:out value="${thawingStageDto.thawedEmbryosNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="7" value="No. of Embryos Survived after Thawing" cssClass="col-md-7"/>
                    <iais:value width="5" display="true">
                        <c:out value="${thawingStageDto.thawedEmbryosSurvivedNum}"/>
                    </iais:value>
                </iais:row>
                <h3>Patient's Inventory</h3>
                <table aria-describedby="" class="table discipline-table">
                    <thead>
                    <tr>
                        <th scope="col"></th>
                        <th scope="col">Frozen Oocytes</th>
                        <th scope="col">Thawed Oocytes</th>
                        <th scope="col">Fresh Oocytes</th>
                        <th scope="col">Frozen Embryos</th>
                        <th scope="col">Thawed Embryos</th>
                        <th scope="col">Fresh Embryos</th>
                        <th scope="col">Frozen Sperms</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="col">Changes</th>
                        <th scope="col">${changeFrozenOocytes}</th>
                        <th scope="col">${changeThawedOocytes}</th>
                        <th scope="col">0</th>
                        <th scope="col">${changeFrozenEmbryos}</th>
                        <th scope="col">${changeThawedEmbryos}</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                    </tr>
                    <tr>
                        <th scope="col">Current</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                        <th scope="col">0</th>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>