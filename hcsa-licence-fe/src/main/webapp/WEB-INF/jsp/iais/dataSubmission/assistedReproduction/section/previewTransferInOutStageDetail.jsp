<c:set var="headingSign" value="completed"/>
<c:set var="transferInOutStageDto" value="${arSuperDataSubmissionDt.transferInOutStageDto}" />
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Transfer In And Out
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="5" value="Is this a Transfer In or Out?"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:if test="${transferInOutStageDto.transferType =='in'}">
                            <c:out value="${transferInOutStageDto.transferType}" />
                        </c:if>
                        <c:if test="${transferInOutStageDto.transferType =='out'}">
                            <c:out value="${transferInOutStageDto.transferType}" />
                        </c:if>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="What was Transferred?"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <c:forEach items="${transferInOutStageDto.transferredList}" var="transferred" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${transferred}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Oocyte(s) Transferred" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${transferInOutStageDto.oocyteNum}" />
                   </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Embryo(s) Transferred" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${transferInOutStageDto.embryoNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Vials of Sperm Transferred" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${transferInOutStageDto.spermVialsNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="Were the Gamete(s) or Embryo(s) from a Donor?" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:if test="${transferInOutStageDto.transferType ==true}">
                            <c:out value="Yes"/>
                        </c:if>
                        <c:if test="${transferInOutStageDto.transferType ==false}">
                            <c:out value="No"/>
                        </c:if>
                    </iais:value>
                </iais:row>
                <div class="inFromParts" <c:if test="${transferInOutStageDto.transferType !='in'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transferred In From" />
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:code code="${transferInOutStageDto.transInFromHciCode}"/>
                        </iais:value>
                    </iais:row>
                    <div class="inFromOthers"<c:if test="${transferInOutStageDto.transInFromHciCode != DataSubmissionConsts.TRANSFERRED_IN_FROM_OTHERS}">style="display: none;"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Transferred In From (Others)" />
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${transferInOutStageDto.transInFromOthers}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
                <div class="outFromParts" <c:if test="${transferInOutStageDto.transferType !='in'}">style="display: none;"</c:if>>
                    <iais:row>
                        <iais:field width="5" value="Transfer Out To" />
                        <iais:value width="7" cssClass="col-md-7">
                            <iais:code code="${transferInOutStageDto.transOutToHciCode}"/>
                        </iais:value>
                    </iais:row>
                    <div class="outFromOthers"<c:if test="${transferInOutStageDto.transOutToHciCode != DataSubmissionConsts.TRANSFERRED_IN_FROM_OTHERS}">style="display: none;"</c:if>>
                        <iais:row>
                            <iais:field width="5" value="Transfer Out To (Others)" />
                            <iais:value width="7" cssClass="col-md-7">
                                <c:out value="${transferInOutStageDto.transOutToOthers}"/>
                            </iais:value>
                        </iais:row>
                    </div>
                </div>
                <iais:row>
                    <iais:field width="5" value="Date of Transfer" />
                    <iais:value width="7" cssClass="col-md-7">
                        <c:out value="${transferInOutStageDto.transferDate}"/>
                    </iais:value>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>