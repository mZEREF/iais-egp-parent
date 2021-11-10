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
                        cssClass="col-md-7"/>
            <iais:value width="5" cssClass="col-md-6">
                <iais:code code="${pregnancyOutcomeBabyDto.birthWeight}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="6" value="Baby ${displayNum} Birth Defect"
                        cssClass="col-md-7"/>
            <iais:value width="5" cssClass="col-md-6">
                <c:out value="${pregnancyOutcomeBabyDto.birthDefect}"/>
            </iais:value>
        </iais:row>
        <div name="defectTypeSectionName"
             <c:if test="${pregnancyOutcomeBabyDto.birthDefect != 'Yes'}">style="display:none;"</c:if>>
            <iais:row>
                <iais:field width="6" value="Baby ${displayNum} Defect Type" mandatory="true" cssClass="col-md-6"/>
                <iais:value width="5" cssClass="col-md-6" style="padding:0px;">
                    <c:forEach var="pregnancyOutcomeBabyDefectDto"
                               items="${pregnancyOutcomeBabyDto.pregnancyOutcomeBabyDefectDtos}"
                               varStatus="defectStatus">
                        <div class="form-check active">
                            <span class="form-check-label"><span
                                    class="check-square"></span>
                                <c:choose>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='CA'}">Chromosomal Anomalies</c:when>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='HA'}">Heart Anomalies</c:when>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='MA'}">Musculoskeletal Anomalies</c:when>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='NSA'}">Nervous System Anomalies</c:when>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='OFA'}">Other Fetal Anomalies</c:when>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='RSA'}">Respiratory System Anomalies</c:when>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='USA'}">Urinary System Anomalies</c:when>
                                    <c:when test="${pregnancyOutcomeBabyDefectDto.defectType=='other'}">Others</c:when>
                                    <c:otherwise>${checkList.chkName}</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </c:forEach>
                </iais:value>
            </iais:row>
            <div name="otherDefectTypeDivName"
                 <c:if test="${!fn:contains(defectTypes,'other')}">style="display:none;"</c:if>>
                <iais:row>
                    <iais:field width="6" value="Baby ${displayNum} Defect Type (Others)" mandatory="true"
                                cssClass="col-md-6"/>
                    <iais:value width="5" cssClass="col-md-6">
                        <c:out value="${otherDefectType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </c:forEach>
</div>