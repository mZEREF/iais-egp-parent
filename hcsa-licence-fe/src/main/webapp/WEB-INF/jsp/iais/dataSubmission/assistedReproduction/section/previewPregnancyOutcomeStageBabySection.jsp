<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div <c:if test="${pregnancyOutcomeStageDto.babyDetailsUnknown}">style="display:none;"</c:if>>
    <c:forEach items="${pregnancyOutcomeStageDto.pregnancyOutcomeBabyDtos}"
               var="pregnancyOutcomeBabyDto"
               varStatus="status">
        <c:set value="${defectTypesArray[status.index]}" var="defectTypes"/>
        <c:set value="${otherDefectTypes[status.index]}" var="otherDefectType"/>
        <c:set var="displayNum" value="${status.index + 1}"/>
        <iais:row>
            <iais:field width="6" value="Baby ${displayNum} Birth Weight"
                        cssClass="col-md-6"/>
            <iais:value width="5" cssClass="col-md-6" display="true">
                <iais:code code="${pregnancyOutcomeBabyDto.birthWeight}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="6" value="Baby ${displayNum} Birth Defect"
                        cssClass="col-md-6"/>
            <iais:value width="5" cssClass="col-md-6" display="true">
                <c:out value="${pregnancyOutcomeBabyDto.birthDefect}"/>
            </iais:value>
        </iais:row>
        <div name="defectTypeSectionName"
             <c:if test="${pregnancyOutcomeBabyDto.birthDefect != 'Yes'}">style="display:none;"</c:if>>
            <iais:row>
                <iais:field width="6" value="Baby ${displayNum} Defect Type" cssClass="col-md-6"/>
                <iais:value width="5" cssClass="col-md-6" style="padding:0px;">
                    <c:forEach var="pregnancyOutcomeBabyDefectDto"
                               items="${pregnancyOutcomeBabyDto.pregnancyOutcomeBabyDefectDtos}"
                               varStatus="defectStatus">
                        <div class="form-check active">
                            <span class="form-check-label"><span
                                    class="check-square"></span>
                                <iais:code code="${pregnancyOutcomeBabyDefectDto.defectType}"/>
                            </span>
                        </div>
                    </c:forEach>
                </iais:value>
            </iais:row>
            <div name="otherDefectTypeDivName"
                 <c:if test="${!fn:contains(defectTypes,'POSBDT008')}">style="display:none;"</c:if>>
                <iais:row>
                    <iais:field width="6" value="Baby ${displayNum} Defect Type (Others)"
                                cssClass="col-md-6"/>
                    <iais:value width="5" cssClass="col-md-6" display="true">
                        <c:out value="${otherDefectType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </c:forEach>
</div>