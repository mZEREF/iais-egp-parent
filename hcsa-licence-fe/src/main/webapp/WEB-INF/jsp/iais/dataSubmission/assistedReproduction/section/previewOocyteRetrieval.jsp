
<c:set var="headingSign" value="completed"/>
<c:set var="oocyteRetrievalStageDto" value="${arSuperDataSubmissionDto.oocyteRetrievalStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal "><%--min-row--%>
                <iais:row>
                    <iais:field width="5" value="Oocyte(s) was retrieved from?"/>
                    <iais:value width="7" display="true">
                        <c:if test="${oocyteRetrievalStageDto.isFromPatient}"><p class="col-12">Patient</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromPatientTissue}"><p class="col-12">Patient's Ovarian Tissue</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonor}"><p class="col-12">Directed Donor</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonorTissue}"><p class="col-12">Directed Donor's Ovarian Tissue</p></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Retrieved (Mature)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${oocyteRetrievalStageDto.matureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Retrieved (Immature)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${oocyteRetrievalStageDto.immatureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Retrieved (Others)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${oocyteRetrievalStageDto.otherRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. Retrieved (Total)"/>
                    <iais:value width="7" display="true">
                        <c:out value="${totalRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Severe Ovarian Hyperstimulation Syndrome"/>
                    <iais:value width="7" display="true">
                        <c:if test="${oocyteRetrievalStageDto.isOvarianSyndrome}">Yes</c:if>
                        <c:if test="${not oocyteRetrievalStageDto.isOvarianSyndrome}">No</c:if>
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
                            <th scope="col">0</th>
                            <th scope="col">0</th>
                            <th scope="col">${freshOocytes}</th>
                            <th scope="col">0</th>
                            <th scope="col">0</th>
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