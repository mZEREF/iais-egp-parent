<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:forEach items="${pregnancyOutcomeStageDto.pregnancyOutcomeBabyDtos}" var="pregnancyOutcomeBabyDto"
           varStatus="status">
    <div class="pregnancyOutcomeStageBabySection" id="pregnancyOutcomeStageBabySection${status.index}"
         <c:if test="${pregnancyOutcomeStageDto.babyDetailsUnknown}">style="display:none;"</c:if>>
        <c:set var="displayNum" value="${status.index + 1}"/>
        <iais:row>
            <iais:field width="5" value="Baby ${displayNum} Birth Weight" mandatory="true"/>
            <iais:value width="7" cssClass="col-md-7">
                <iais:select id="birthWeight" name="birthWeight" options="birthWeightSelectOption"
                             value="${pregnancyOutcomeBabyDto.birthWeight}"></iais:select>
            </iais:value>
        </iais:row>

        <iais:row>
            <iais:field width="5" value="Baby ${displayNum} Birth Defect"/>
            <div class="col-md-7">
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
        <div name="defectTypeSectionName"
             <c:if test="${pregnancyOutcomeBabyDto.birthDefect != 'yes'}">style="display:none;"</c:if>>
            <iais:row>
                <iais:field width="5" value="Baby ${displayNum} Defect Type" mandatory="true"/>
                <iais:value width="7" cssClass="col-md-7">
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="CA"
                               id="defectType${status.index}CA"
                               <c:if test="${fn:contains(defectTypes,'CA')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}CA"><span
                                class="check-square"></span>Chromosomal Anomalies</label>
                    </div>
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="HA"
                               id="defectType${status.index}HA"
                               <c:if test="${fn:contains(defectTypes,'HA')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}HA"><span
                                class="check-square"></span>Heart Anomalies</label>
                    </div>
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="MA"
                               id="defectType${status.index}MA"
                               <c:if test="${fn:contains(defectTypes,'MA')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}MA"><span
                                class="check-square"></span>Musculoskeletal Anomalies</label>
                    </div>
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="NSA"
                               id="defectType${status.index}NSA"
                               <c:if test="${fn:contains(defectTypes,'NSA')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}NSA"><span
                                class="check-square"></span>Nervous System Anomalies</label>
                    </div>
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="OFA"
                               id="defectType${status.index}OFA"
                               <c:if test="${fn:contains(defectTypes,'OFA')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}OFA"><span
                                class="check-square"></span>Other Fetal Anomalies</label>
                    </div>
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="RSA"
                               id="defectType${status.index}RSA"
                               <c:if test="${fn:contains(defectTypes,'RSA')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}RSA"><span
                                class="check-square"></span>Respiratory System Anomalies</label>
                    </div>
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="USA"
                               id="defectType${status.index}USA"
                               <c:if test="${fn:contains(defectTypes,'USA')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}USA"><span
                                class="check-square"></span>Urinary System Anomalies</label>
                    </div>
                    <div class="form-check col-xs-12">
                        <input class="form-check-input defectType" type="checkbox"
                               name="defectType${status.index}"
                               value="other"
                               id="defectType${status.index}Other"
                               <c:if test="${fn:contains(defectTypes,'other')}">checked</c:if>
                               aria-invalid="false">
                        <label class="form-check-label"
                               for="defectType${status.index}Other"><span
                                class="check-square"></span>Others</label>
                    </div>
                    <span id="error_oocyteRetrievalFrom" name="iaisErrorMsg" class="error-msg"></span>
                </iais:value>
            </iais:row>
            <div name="otherDefectTypeDivName"
                 <c:if test="${pregnancyOutcomeBabyDto.defectTypes != 'other'}">style="display:none;"</c:if>>
                <iais:row>
                    <iais:field width="5" value="Baby ${displayNum} Defect Type (Others)" mandatory="true"/>
                    <iais:value width="7" cssClass="col-md-7">
                        <iais:input maxLength="20" type="text" cssClass="otherDefectType"
                                    name="otherDefectType${status.index}" id="otherDefectType${status.index}"
                                    value="${otherDefectType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </div>
</c:forEach>
