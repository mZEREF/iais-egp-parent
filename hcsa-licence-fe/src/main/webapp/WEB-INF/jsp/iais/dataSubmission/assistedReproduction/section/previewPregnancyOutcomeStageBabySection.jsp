<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div <c:if test="${pregnancyOutcomeStageDto.babyDetailsUnknown}">style="display:none;"</c:if>>
    <c:forEach items="${pregnancyOutcomeStageDto.pregnancyOutcomeBabyDtos}"
               var="pregnancyOutcomeBabyDto"
               varStatus="status">
        <c:set value="${defectTypesArray[status.index]}" var="defectTypes"/>
        <c:set value="${otherDefectTypes[status.index]}" var="otherDefectType"/>
        <c:set var="displayNum" value="${status.index + 1}"/>
        <iais:row>
            <iais:field width="7" value="Baby ${displayNum} Birth Weight"
                        cssClass="col-md-7"/>
            <iais:value width="5" cssClass="col-md-5">
                <iais:code code="${pregnancyOutcomeBabyDto.birthWeight}"/>
            </iais:value>
        </iais:row>
        <iais:row>
            <iais:field width="7" value="Baby ${displayNum} Birth Defect"
                        cssClass="col-md-7"/>
            <iais:value width="5" cssClass="col-md-5">
                <c:out value="${pregnancyOutcomeBabyDto.birthDefect}"/>
            </iais:value>
        </iais:row>
        <div name="defectTypeSectionName"
             <c:if test="${pregnancyOutcomeBabyDto.birthDefect != 'yes'}">style="display:none;"</c:if>>
            <iais:row>
                <iais:field width="7" value="Baby ${displayNum} Defect Type" mandatory="true"/>
                <iais:value width="5" cssClass="col-md-5">
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
                </iais:value>
            </iais:row>
            <div name="otherDefectTypeDivName"
                 <c:if test="${!fn:contains(defectTypes,'other')}">style="display:none;"</c:if>>
                <iais:row>
                    <iais:field width="7" value="Baby ${displayNum} Defect Type (Others)" mandatory="true"/>
                    <iais:value width="5" cssClass="col-md-5">
                        <c:out value=""/>
                        <iais:input maxLength="20" type="text" cssClass="otherDefectType"
                                    name="otherDefectType${status.index}" id="otherDefectType${status.index}"
                                    value="${otherDefectType}"/>
                    </iais:value>
                </iais:row>
            </div>
        </div>
    </c:forEach>
</div>