<div class="person-detail">

    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">${title}</p>
        </div>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Salutation"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcPersonnelDto.salutation}" />
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="3" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.name}" />
        </iais:value>
    </iais:row>

    <c:if test="${type == 'ro'}">
        <iais:row>
            <iais:field width="5" value="Is the RO employed on a full-time basis?"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '1'}">
                    <c:out value="Yes"/>
                </c:if>
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '0'}">
                    <c:out value="NO"/>
                </c:if>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="5" value="Relevant working experience (Years)"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="5" value="SMC Registration No."/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${appSvcPersonnelDto.ssiRegnNo}"/>
            </iais:value>
        </iais:row>
    </c:if>



    <c:if test="${type == 'md'}">
        <iais:row>
            <iais:field width="5" value="Is the Medical Dosimetrist employed on a full-time basis?"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '1'}">
                    <c:out value="Yes"/>
                </c:if>
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '0'}">
                    <c:out value="NO"/>
                </c:if>
            </iais:value>
        </iais:row>
    </c:if>

    <c:if test="${type == 'rt'}">
        <iais:row>
            <iais:field width="5" value="Is the RT employed on a full-time basis?"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '1'}">
                    <c:out value="Yes"/>
                </c:if>
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '0'}">
                    <c:out value="NO"/>
                </c:if>
            </iais:value>
        </iais:row>
        <%--    AHPC Registration No.--%>
        <iais:row>
            <iais:field width="5" value="AHPC Registration No."/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${appSvcPersonnelDto.ssiRegnNo}"/>
            </iais:value>
        </iais:row>
    </c:if>

    <c:if test="${type == 'cqmp'}">
        <iais:row>
            <iais:field width="5" value="Is the CQMP employed on a full-time basis?"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '1'}">
                    <c:out value="Yes"/>
                </c:if>
                <c:if test="${appSvcPersonnelDto.ssiEmployedBasis == '0'}">
                    <c:out value="NO"/>
                </c:if>
            </iais:value>
        </iais:row>
    </c:if>
</div>
