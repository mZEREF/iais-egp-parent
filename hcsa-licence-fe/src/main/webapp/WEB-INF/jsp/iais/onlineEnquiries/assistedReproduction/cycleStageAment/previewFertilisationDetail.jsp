
<c:set var="fertilisationDto" value="${arSuperDataSubmissionDto.fertilisationDto}"/>
<c:set var="fertilisationDtoVersion" value="${arSuperDataSubmissionDtoVersion.fertilisationDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Fertilisation
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <%@include file="comPart.jsp" %>
                <iais:row>
                    <iais:field width="6" value="Source of Oocyte?"/>
                    <iais:value  width="6" display="true" cssClass="col-md-6">
                        <c:if test="${fertilisationDto.sourceOfOocyte != null}">
                            <c:out value="${fertilisationDto.sourceOfOocyte}" />
                        </c:if>

                        <c:if test="${fertilisationDto.sourceOfOocyte eq null
                                    && fertilisationDto.sourceOfOocytePatient != null}">
                            <c:out value="${fertilisationDto.sourceOfOocytePatient}" />
                        </c:if>

                        <c:if test="${fertilisationDto.sourceOfOocyte eq null
                                    && fertilisationDto.sourceOfOocytePatient eq null
                                    && fertilisationDto.sourceOfOocytePot != null}">
                            <c:out value="${fertilisationDto.sourceOfOocytePot}" />
                        </c:if>
                    </iais:value>
                </iais:row>

                <c:if test="${fertilisationDto.sourceOfOocyte != null
                             && fertilisationDto.sourceOfOocytePatient != null}">
                    <iais:row>
                        <iais:field width="6" value=""/>
                        <iais:value  width="6" display="true" cssClass="col-md-6">
                            <c:out value="${fertilisationDto.sourceOfOocytePatient}" />
                        </iais:value>
                    </iais:row>
                </c:if>

                <c:if test="${fertilisationDto.sourceOfOocytePatient != null
                              && fertilisationDto.sourceOfOocytePot != null}">
                    <iais:row>
                        <iais:field width="6" value=""/>
                        <iais:value  width="6" display="true" cssClass="col-md-6">
                            <c:out value="${fertilisationDto.sourceOfOocytePot}" />
                        </iais:value>
                    </iais:row>
                </c:if>

                <iais:row>
                    <iais:field width="6" value="Was fresh or frozen oocyte(s) used?"/>
                    <iais:value  width="6" display="true" cssClass="col-md-6">
                        <c:out value="${fertilisationDto.oocyteUsed}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="How many oocytes were used in this cycle?"/>
                    <iais:value  width="6" display="true" cssClass="col-md-6">
                        <c:out value="${fertilisationDto.usedOocytesNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="Source of Semen"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                         <c:forEach items="${fertilisationDto.sosList}" var="sos" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${sos}"/>
                         </c:forEach>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:forEach items="${fertilisationDtoVersion.sosList}" var="sos" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${sos}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="6" value="Was fresh or frozen sperm used?"/>
                    <iais:value  width="6" display="true" cssClass="col-md-6">
                        <c:out value="${fertilisationDto.spermUsed}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="How many vials of sperm were extracted?"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.extractedSpermVialsNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.extractedSpermVialsNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="How many vials of sperm were used in this cycle?"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.usedSpermVialsNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.usedSpermVialsNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="AR Techniques Used"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:forEach items="${fertilisationDto.atuList}" var="atu" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${atu}"/>
                        </c:forEach>
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:forEach items="${fertilisationDtoVersion.atuList}" var="atu" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${atu}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Fresh Oocytes Inseminated"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.freshOocytesInseminatedNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.freshOocytesInseminatedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Fresh Oocytes Microinjected"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.freshOocytesMicroInjectedNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.freshOocytesMicroInjectedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Fresh Oocytes Used for GIFT"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.freshOocytesGiftNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.freshOocytesGiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Fresh Oocytes Used for ZIFT"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.freshOocytesZiftNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.freshOocytesZiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Thawed Oocytes Inseminated"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesInseminatedNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.thawedOocytesInseminatedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Thawed Oocytes Microinjected"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesMicroinjectedNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.thawedOocytesMicroinjectedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Thawed Oocytes Used for GIFT"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesGiftNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.thawedOocytesGiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="4" value="No. of Thawed Oocytes Used for ZIFT"/>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesZiftNum}" />
                    </iais:value>
                    <iais:value width="4" cssClass="col-md-4" display="true">
                        <c:out value="${fertilisationDtoVersion.thawedOocytesZiftNum}" />
                    </iais:value>
                </iais:row>

            </div>
        </div>
    </div>
</div>