<c:set var="oocyteRetrievalStageDto" value="${arSuperDataSubmissionDto.oocyteRetrievalStageDto}"/>
<div class="panel panel-default">
    <div class="panel-heading completed">
        <h4 class="panel-title">
            <a class="collapsed" data-toggle="collapse" href="#">
                Details of Patient
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="6" value="Oocyte(s) was retrieved from?" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:if test="${oocyteRetrievalStageDto.isFromPatient}"><p>Patient</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromPatientTissue}"><p>Patient's Ovarian
                            Tissue</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonor}"><p>Directed Donor</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonorTissue}"><p>Directed Donor's Ovarian
                            Tissue</p></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Mature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${oocyteRetrievalStageDto.matureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Immature)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${oocyteRetrievalStageDto.immatureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Others)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${oocyteRetrievalStageDto.otherRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="No. Retrieved (Total)" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:out value="${totalRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Severe Ovarian Hyperstimulation Syndrome" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <c:if test="${oocyteRetrievalStageDto.isOvarianSyndrome}">Yes</c:if>
                        <c:if test="${not oocyteRetrievalStageDto.isOvarianSyndrome}">No</c:if>
                    </iais:value>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>