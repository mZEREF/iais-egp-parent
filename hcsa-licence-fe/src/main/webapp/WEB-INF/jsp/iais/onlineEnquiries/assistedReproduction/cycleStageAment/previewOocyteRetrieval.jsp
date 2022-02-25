<c:set var="oocyteRetrievalStageDto" value="${arSuperDataSubmissionDto.oocyteRetrievalStageDto}"/>
<c:set var="oocyteRetrievalStageDtoVersion" value="${arSuperDataSubmissionDtoVersion.oocyteRetrievalStageDto}"/>

<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a href="#cycleDetails" data-toggle="collapse" >
                Oocyte Retrieval
            </a>
        </h4>
    </div>
    <div id="cycleDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <h3>
                    <label ><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="4" cssClass="col-md-4"  value="" />
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                    <c:if test="${not empty arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}">
                        <iais:value width="4" cssClass="col-md-4" display="true">
                            <select id="oldDsSelect" name="oldDsSelect">
                                <c:forEach items="${arSuperDataSubmissionDto.oldArSuperDataSubmissionDto}" var="oldDs" varStatus="index">
                                    <option  <c:if test="${oldDs.dataSubmissionDto.id == arSuperDataSubmissionDtoVersion.dataSubmissionDto.id}">checked</c:if> value ="${oldDs.dataSubmissionDto.id}">V ${oldDs.dataSubmissionDto.version}</option>
                                </c:forEach>
                            </select>
                        </iais:value>
                    </c:if>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Oocyte(s) was retrieved from?" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4">
                        <c:if test="${oocyteRetrievalStageDto.isFromPatient}"><p>Patient</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromPatientTissue}"><p>Patient's Ovarian
                            Tissue</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonor}"><p>Directed Donor</p></c:if>
                        <c:if test="${oocyteRetrievalStageDto.isFromDonorTissue}"><p>Directed Donor's Ovarian
                            Tissue</p></c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4">
                        <c:if test="${oocyteRetrievalStageDtoVersion.isFromPatient}"><p>Patient</p></c:if>
                        <c:if test="${oocyteRetrievalStageDtoVersion.isFromPatientTissue}"><p>Patient's Ovarian
                            Tissue</p></c:if>
                        <c:if test="${oocyteRetrievalStageDtoVersion.isFromDonor}"><p>Directed Donor</p></c:if>
                        <c:if test="${oocyteRetrievalStageDtoVersion.isFromDonorTissue}"><p>Directed Donor's Ovarian
                            Tissue</p></c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. Retrieved (Mature)" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDto.matureRetrievedNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDtoVersion.matureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. Retrieved (Immature)" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDto.immatureRetrievedNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDtoVersion.immatureRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. Retrieved (Others)" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDto.otherRetrievedNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDtoVersion.otherRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. Retrieved (Total)" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDto.matureRetrievedNum + oocyteRetrievalStageDto.immatureRetrievedNum + oocyteRetrievalStageDto.otherRetrievedNum}"/>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${oocyteRetrievalStageDtoVersion.matureRetrievedNum + oocyteRetrievalStageDtoVersion.immatureRetrievedNum + oocyteRetrievalStageDtoVersion.otherRetrievedNum}"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Severe Ovarian Hyperstimulation Syndrome" cssClass="col-md-4"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${oocyteRetrievalStageDto.isOvarianSyndrome}">Yes</c:if>
                        <c:if test="${not oocyteRetrievalStageDto.isOvarianSyndrome}">No</c:if>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:if test="${oocyteRetrievalStageDtoVersion.isOvarianSyndrome}">Yes</c:if>
                        <c:if test="${not oocyteRetrievalStageDtoVersion.isOvarianSyndrome}">No</c:if>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>