
<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Transfer In And Out
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <c:set var="transferInOutStageDto" value="${arSuperDataSubmissionDto.transferInOutStageDto}"/>
                <h3>
                    <label><c:out value="${arSuperDataSubmissionDto.patientInfoDto.patient.name}"/></label>
                    <span style="font-weight:normal"><c:out
                            value="(${arSuperDataSubmissionDto.patientInfoDto.patient.idNumber})"/>
                    </span>
                </h3>
                <iais:row>
                    <iais:field width="5" value="" />
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="Current Version"/>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Is this a Transfer In or Out?"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
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
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:forEach items="${transferInOutStageDto.transferredList}" var="transferred" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${transferred}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <c:forEach var="transferredObj" items="${transferInOutStageDto.transferredList}">
                    <c:if test="${transferredObj =='AR_WWT_001'}">
                        <iais:row>
                            <iais:field width="5" value="No. of Oocyte(s) Transferred"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${transferInOutStageDto.oocyteNum}"/>
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <c:if test="${transferredObj =='AR_WWT_002'}">
                        <iais:row>
                            <iais:field width="5" value="No. of Embryo(s) Transferred"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${transferInOutStageDto.embryoNum}"/>
                            </iais:value>
                        </iais:row>
                    </c:if>
                    <c:if test="${transferredObj =='AR_WWT_003'}">
                        <iais:row>
                            <iais:field width="5" value="Vials of Sperm Transferred"/>
                            <iais:value width="7" cssClass="col-md-7" display="true">
                                <c:out value="${transferInOutStageDto.spermVialsNum}"/>
                            </iais:value>
                        </iais:row>
                    </c:if>
                </c:forEach>
                <iais:row>
                    <iais:field width="5" value="Were the Gamete(s) or Embryo(s) from a Donor?"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${transferInOutStageDto.fromDonor ? 'Yes' : 'No'}"/>
                    </iais:value>
                </iais:row>
                <div class="inFromParts"
                     <c:if test="${transferInOutStageDto.transferType !='in'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transferred In From"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:code code="${transferInOutStageDto.transInFromHciCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row style="${transferInOutStageDto.transInFromHciCode eq'Others' ? '' : 'display:none;'}">
                        <iais:field width="5" value="Transferred In From (Others)"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${transferInOutStageDto.transInFromOthers}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <div class="outFromParts"
                     <c:if test="${transferInOutStageDto.transferType !='out'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transfer Out To"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <iais:code code="${transferInOutStageDto.transOutToHciCode}"/>
                        </iais:value>
                    </iais:row>
                    <iais:row style="${transferInOutStageDto.transOutToHciCode eq'Others' ? '' : 'display:none;'}">
                        <iais:field width="5" value="Transfer Out To (Others)"/>
                        <iais:value width="7" cssClass="col-md-7" display="true">
                            <c:out value="${transferInOutStageDto.transOutToOthers}"/>
                        </iais:value>
                    </iais:row>
                </div>
                <iais:row>
                    <iais:field width="5" value="Date of Transfer"/>
                    <iais:value width="7" cssClass="col-md-7" display="true">
                        <c:out value="${transferInOutStageDto.transferDate}"/>
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>