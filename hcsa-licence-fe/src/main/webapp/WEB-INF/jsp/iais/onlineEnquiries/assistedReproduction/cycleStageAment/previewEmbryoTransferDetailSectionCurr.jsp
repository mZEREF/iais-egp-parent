

    <c:forEach var="embryoTransferDetailDto"
               items="${embryoTransferStageDto.embryoTransferDetailDtos}"
               varStatus="status">
        <div id="${status.index}Embryo">
            <iais:row>
                <c:choose>
                    <c:when test="${seq.index eq '0'}"><iais:field value="1st Embryo" mandatory="false" cssClass="col-md-8"/></c:when>
                    <c:when test="${seq.index eq '1'}"><iais:field value="2nd Embryo" mandatory="false" cssClass="col-md-8"/></c:when>
                    <c:when test="${seq.index eq '2'}"><iais:field  value="3rd Embryo" mandatory="false" cssClass="col-md-8"/></c:when>
                    <c:otherwise><iais:field  value="${seq.index+1}th Embryo" mandatory="false" cssClass="col-md-8"/></c:otherwise>
                </c:choose>
                <iais:value  display="true" cssClass="col-md-4">
                    <iais:code code="${embryoTransferDetailDto.embryoAge}"/>
                </iais:value>
            </iais:row>
            <iais:row>
                <c:choose>
                    <c:when test="${seq.index eq '0'}"><iais:field  value="Was the 1st Embryo Transferred a fresh or thawed embryo?"
                                                                   mandatory="false" cssClass="col-md-8"/></c:when>
                    <c:when test="${seq.index eq '1'}"><iais:field  value="Was the 2nd Embryo Transferred a fresh or thawed embryo?"
                                                                   mandatory="false" cssClass="col-md-8"/></c:when>
                    <c:when test="${seq.index eq '2'}"><iais:field  value="Was the 3rd Embryo Transferred a fresh or thawed embryo?"
                                                                   mandatory="false" cssClass="col-md-8"/></c:when>
                    <c:otherwise><iais:field  value="Was the ${seq.index+1}th Embryo Transferred a fresh or thawed embryo?"
                                             mandatory="false" cssClass="col-md-8"/></c:otherwise>
                </c:choose>
                <iais:value  display="true" cssClass="col-md-4">
                    <c:out value="${embryoTransferDetailDto.embryoType == 'fresh'?  'Fresh Embryo' : 'Thawed Embryo'}"/>
                </iais:value>
            </iais:row>
        </div>
    </c:forEach>

