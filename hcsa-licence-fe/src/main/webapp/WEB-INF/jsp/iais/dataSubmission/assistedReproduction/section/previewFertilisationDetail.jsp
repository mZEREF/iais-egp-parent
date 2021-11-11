<c:set var="headingSign" value="completed"/>
<c:set var="fertilisationDto" value="${arSuperDataSubmissionDto.fertilisationDto}"/>
<div class="panel panel-default">
    <div class="panel-heading ${headingSign}">
        <h4 class="panel-title">
            <a class="" data-toggle="collapse" href="#patientDetails">
                Fertilisation
            </a>
        </h4>
    </div>
    <div id="patientDetails" class="panel-collapse collapse in">
        <div class="panel-body">
            <div class="panel-main-content form-horizontal ">
                <iais:row>
                    <iais:field width="5" value="Source of Semen"/>
                    <iais:value width="7" cssClass="col-md-7">
                         <c:forEach items="${fertilisationDto.sosList}" var="sos" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${sos}"/>
                         </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="How many vials of sperm were extracted?"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.extractedSpermVialsNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="How many vials of sperm were used in this cycle?"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.usedSpermVialsNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="AR Techniques Used"/>
                    <iais:value width="7" display="true">
                        <c:forEach items="${fertilisationDto.atuList}" var="atu" varStatus="staus">
                            <c:if test="${staus.index !=0}"> <br></c:if> <iais:code code="${atu}"/>
                        </c:forEach>
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Inseminated"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.freshOocytesInseminatedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Microinjected"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.freshOocytesMicroInjectedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for GIFT"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.freshOocytesGiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Fresh Oocytes Used for ZIFT"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.freshOocytesZiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Inseminated"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesInseminatedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Microinjected"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesMicroinjectedNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for GIFT"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesGiftNum}" />
                    </iais:value>
                </iais:row>
                <iais:row>
                    <iais:field width="5" value="No. of Thawed Oocytes Used for ZIFT"/>
                    <iais:value width="7" display="true">
                        <c:out value="${fertilisationDto.thawedOocytesZiftNum}" />
                    </iais:value>
                </iais:row>
                <%@include file="../common/patientInventoryTable.jsp" %>
            </div>
        </div>
    </div>
</div>