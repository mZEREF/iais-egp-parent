<div class="person-detail">

    <iais:row>
        <div class="col-xs-12 col-md-6">
            <p class="bold">${title}</p>
        </div>
    </iais:row>

    <c:if test="${isShowSaluation==1}">
        <iais:row>
            <iais:field width="5" value="Salutation"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <iais:code code="${person.salutation}"/>
            </iais:value>
        </iais:row>
    </c:if>

    <iais:row>
        <iais:field width="5" value="Name"/>
        <iais:value width="4" cssClass="col-md-4" display="true">
            <c:out value="${person.name}"/>
        </iais:value>
    </iais:row>

    <c:if test="${isShowMore==1}">
        <iais:row>
            <iais:field width="5" value="Qualification"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.qualification}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="5" value="Working Experience (in terms of years)"/>
            <iais:value width="7" cssClass="col-md-7" display="true">
                <c:out value="${person.wrkExpYear}"/>
            </iais:value>
        </iais:row>
    </c:if>
</div>


