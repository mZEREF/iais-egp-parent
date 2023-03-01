<div class="personnel-content">
    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="4" cssClass="col-md-4" display="true">
            <c:out value="${appSvcPersonnelDto.name}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Salutation"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <iais:code code="${appSvcPersonnelDto.salutation}"/>
        </iais:value>
    </iais:row>

    <iais:row>
        <iais:field width="5" value="Qualification"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.qualification}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Relevant working experience (Years)"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.wrkExpYear}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="7" value="Number of AR procedures done under supervision"/>
        <iais:value width="5" cssClass="col-md-7" display="true">
            <c:out value="${appSvcPersonnelDto.numberSupervision}"/>
        </iais:value>
    </iais:row>
    <iais:row>
        <iais:field width="5" value="Is the Embryologist authorized?"/>
        <iais:value width="7" cssClass="col-md-7" display="true">
            <c:if test="${not empty appSvcPersonnelDto.embryologistAuthorized && appSvcPersonnelDto.embryologistAuthorized}">
                <c:out value="Yes"/>
            </c:if>
            <c:if test="${not empty appSvcPersonnelDto.embryologistAuthorized && !appSvcPersonnelDto.embryologistAuthorized}">
                <c:out value="No"/>
            </c:if>
        </iais:value>
    </iais:row>
</div>



