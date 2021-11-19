<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:forEach items="${pregnancyOutcomeStageDto.pregnancyOutcomeBabyDtos}" var="pregnancyOutcomeBabyDto"
           varStatus="status">
    <c:set value="${defectTypesArray[status.index]}" var="defectTypes"/>
    <c:set value="${otherDefectTypes[status.index]}" var="otherDefectType"/>
    <div class="pregnancyOutcomeStageBabySection" id="pregnancyOutcomeStageBabySection${status.index}">
        <c:set var="displayNum" value="${status.index + 1}"/>
        <iais:row>
            <iais:field width="6" value="Baby ${displayNum} Birth Weight" mandatory="true" cssClass="col-md-6"/>
            <iais:value width="6" cssClass="col-md-6">
                <iais:select id="birthWeight${status.index}" firstOption="Please Select"
                             name="birthWeight${status.index}" codeCategory="CATE_ID_BABY_BIRTH_WEIGHT"
                             value="${pregnancyOutcomeBabyDto.birthWeight}"/>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="6" value="Baby ${displayNum} Birth Defect" cssClass="col-md-6"/>
            <div class="col-md-6">
                <div class="form-check col-12">
                    <input class="form-check-input birthDefect"
                           type="radio"
                           name="birthDefect${status.index}"
                           value="Yes"
                           id="birthDefect${status.index}Yes"
                           <c:if test="${pregnancyOutcomeBabyDto.birthDefect == 'Yes'}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="birthDefect${status.index}Yes"><span
                            class="check-circle"></span>Yes</label>
                </div>
                <div class="form-check col-12">
                    <input class="form-check-input birthDefect"
                           type="radio"
                           name="birthDefect${status.index}"
                           value="No"
                           id="birthDefect${status.index}No"
                           <c:if test="${pregnancyOutcomeBabyDto.birthDefect == 'No'}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="birthDefect${status.index}No"><span
                            class="check-circle"></span>No</label>
                </div>
                <div class="form-check col-12">
                    <input class="form-check-input birthDefect"
                           type="radio"
                           name="birthDefect${status.index}"
                           value="Unknown"
                           id="birthDefect${status.index}Unknown"
                           <c:if test="${pregnancyOutcomeBabyDto.birthDefect == 'Unknown'}">checked</c:if>
                           aria-invalid="false">
                    <label class="form-check-label"
                           for="birthDefect${status.index}Unknown"><span
                            class="check-circle"></span>Unknown</label>
                </div>
            </div>
        </iais:row>
        <div name="defectTypeSectionName">
            <iais:row>
                <iais:field width="6" value="Baby ${displayNum} Defect Type" mandatory="true" cssClass="col-md-6"/>
                <iais:value width="6" cssClass="col-md-6">
                    <c:forEach var="defectTypeItem" items="${defectTypeOptions}" varStatus="defectTypeStatus">
                        <div class="form-check col-xs-12">
                            <input class="form-check-input defectType" type="checkbox"
                                   name="defectType${status.index}"
                                   value="${defectTypeItem.value}"
                                   id="defectType${status.index}${defectTypeItem.value}"
                                   <c:if test="${fn:contains(defectTypes,defectTypeItem.value)}">checked</c:if>
                                   aria-invalid="false">
                            <label class="form-check-label"
                                   for="defectType${status.index}${defectTypeItem.value}"><span
                                    class="check-square"></span>${defectTypeItem.text}</label>
                        </div>
                    </c:forEach>
                    <span id="error_oocyteRetrievalFrom" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
            <div name="otherDefectTypeDivName">
                <iais:row>
                    <iais:field width="6" value="Baby ${displayNum} Defect Type (Others)" mandatory="true" cssClass="col-md-6"/>
                    <iais:value width="6" cssClass="col-md-6">
                        <iais:input maxLength="20" type="text" cssClass="otherDefectType"
                                    name="otherDefectType${status.index}" id="otherDefectType${status.index}"
                                    value="${otherDefectType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</c:forEach>
