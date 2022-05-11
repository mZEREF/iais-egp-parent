<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div <c:if test="${pregnancyOutcomeStageDtoVersion.babyDetailsUnknown}">style="display:none;"</c:if>>
    <iais:row>
        <iais:field cssClass="col-md-4"  value="" />
        <iais:value cssClass="col-md-8" display="true">
            <c:out value="Version "/>${arSuperDataSubmissionDtoVersion.dataSubmissionDto.version}
        </iais:value>
    </iais:row>
    <c:forEach items="${pregnancyOutcomeStageDtoVersion.pregnancyOutcomeBabyDtos}"
               var="pregnancyOutcomeBabyDto"
               varStatus="status">
        <c:set value="${defectTypesArray[status.index]}" var="defectTypes"/>
        <c:set value="${otherDefectTypes[status.index]}" var="otherDefectType"/>
        <c:set var="displayNum" value="${status.index + 1}"/>
        <iais:row>
            <iais:field cssClass="col-md-8" value="Baby ${displayNum} Birth Weight"/>
            <iais:value cssClass="col-md-4" display="true">
                <iais:code code="${pregnancyOutcomeBabyDto.birthWeight}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field cssClass="col-md-8" value="Baby ${displayNum} Birth Defect"/>
            <iais:value cssClass="col-md-4" display="true">
                <c:out value="${pregnancyOutcomeBabyDto.birthDefect}"/>
            </iais:value>
        </iais:row>
        <div name="defectTypeSectionName"
             <c:if test="${pregnancyOutcomeBabyDto.birthDefect != 'Yes'}">style="display:none;"</c:if>>
            <iais:row>
                <iais:field cssClass="col-md-8" value="Baby ${displayNum} Defect Type" />
                <iais:value cssClass="col-md-4" display="true">
                    <c:forEach var="pregnancyOutcomeBabyDefectDto"
                               items="${pregnancyOutcomeBabyDto.pregnancyOutcomeBabyDefectDtos}"
                               varStatus="defectStatus">
                        <p><iais:code code="${pregnancyOutcomeBabyDefectDto.defectType}"/></p>
                    </c:forEach>
                </iais:value>
            </iais:row>
            <div name="otherDefectTypeDivName"
                 <c:if test="${!fn:contains(defectTypes,'POSBDT008')}">style="display:none;"</c:if>>
                <iais:row>
                    <iais:field cssClass="col-md-8" value="Baby ${displayNum} Defect Type (Others)"/>
                    <iais:value cssClass="col-md-4" display="true">
                        <c:out value="${otherDefectType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </c:forEach>
</div>