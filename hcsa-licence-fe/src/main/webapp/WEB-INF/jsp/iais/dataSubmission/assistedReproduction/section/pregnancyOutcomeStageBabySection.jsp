<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:forEach items="${pregnancyOutcomeStageDto.pregnancyOutcomeBabyDtos}" var="pregnancyOutcomeBabyDto" varStatus="status">
  <iais:row>
    <iais:field width="5" value="Baby 1 Birth Weight" mandatory="true"/>
    <iais:value width="7" cssClass="col-md-7">
      <iais:select name="birthWeight" options="birthWeightSelectOption" value="${pregnancyOutcomeBabyDto.birthWeight}"></iais:select>
    </iais:value>
  </iais:row>

  <iais:row>
    <iais:field width="5" value="Baby 1 Birth Defect"/>
    <div class="col-md-7">
      <div class="form-check col-12">
        <input class="form-check-input"
               type="radio"
               name="birthDefect"
               value="Yes"
               id="birthDefectYes"
               <c:if test="${pregnancyOutcomeBabyDto.birthDefect == 'Yes'}">checked</c:if>
               aria-invalid="false">
        <label class="form-check-label"
               for="birthDefectYes"><span
                class="check-circle"></span>Yes</label>
      </div>
      <div class="form-check col-12">
        <input class="form-check-input"
               type="radio"
               name="birthDefect"
               value="No"
               id="birthDefectNo"
               <c:if test="${pregnancyOutcomeBabyDto.birthDefect == 'No'}">checked</c:if>
               aria-invalid="false">
        <label class="form-check-label"
               for="birthDefectNo"><span
                class="check-circle"></span>No</label>
      </div>
      <div class="form-check col-12">
        <input class="form-check-input"
               type="radio"
               name="birthDefect"
               value="Unknown"
               id="birthDefectUnknown"
               <c:if test="${pregnancyOutcomeBabyDto.birthDefect == 'Unknown'}">checked</c:if>
               aria-invalid="false">
        <label class="form-check-label"
               for="birthDefectUnknown"><span
                class="check-circle"></span>Unknown</label>
      </div>
    </div>
  </iais:row>
  <div <c:if test="${pregnancyOutcomeBabyDto.birthDefect != 'yes'}">style="display:none;"</c:if>>
    <iais:row>
      <iais:field width="5" value="Baby 1 Defect Type" mandatory="true"/>
      <iais:value width="7" cssClass="col-md-7">
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="CA"
                 id="defectTypeCA"
                 <c:if test="${fn:contains(defectTypes,'CA')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeCA"><span
                  class="check-square"></span>Chromosomal Anomalies</label>
        </div>
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="HA"
                 id="defectTypeHA"
                 <c:if test="${fn:contains(defectTypes,'HA')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeHA"><span
                  class="check-square"></span>Heart Anomalies</label>
        </div>
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="MA"
                 id="defectTypeMA"
                 <c:if test="${fn:contains(defectTypes,'MA')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeMA"><span
                  class="check-square"></span>Musculoskeletal Anomalies</label>
        </div>
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="NSA"
                 id="defectTypeNSA"
                 <c:if test="${fn:contains(defectTypes,'NSA')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeNSA"><span
                  class="check-square"></span>Nervous System Anomalies</label>
        </div>
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="OFA"
                 id="defectTypeOFA"
                 <c:if test="${fn:contains(defectTypes,'OFA')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeOFA"><span
                  class="check-square"></span>Other Fetal Anomalies</label>
        </div>
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="RSA"
                 id="defectTypeRSA"
                 <c:if test="${fn:contains(defectTypes,'RSA')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeRSA"><span
                  class="check-square"></span>Respiratory System Anomalies</label>
        </div>
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="USA"
                 id="defectTypeUSA"
                 <c:if test="${fn:contains(defectTypes,'USA')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeUSA"><span
                  class="check-square"></span>Urinary System Anomalies</label>
        </div>
        <div class="form-check col-xs-12" >
          <input class="form-check-input" type="checkbox"
                 name="defectType"
                 value="other"
                 id="defectTypeOther"
                 <c:if test="${fn:contains(defectTypes,'other')}">checked</c:if>
                 aria-invalid="false">
          <label class="form-check-label"
                 for="defectTypeOther"><span
                  class="check-square"></span>Others</label>
        </div>
        <span id="error_oocyteRetrievalFrom" name="iaisErrorMsg" class="error-msg"></span>
      </iais:value>
    </iais:row>
    <div <c:if test="${pregnancyOutcomeBabyDto.defectTypes != 'other'}">style="display:none;"</c:if>>
      <iais:row>
        <iais:field width="5" value="Baby 1 Defect Type (Others)" mandatory="true"/>
        <iais:value width="7" cssClass="col-md-7">
          <iais:input maxLength="20" type="text" name="otherDefectType" id="otherDefectType" value="${otherDefectType}"/>
        </iais:value>
      </iais:row>
    </div>
  </div>
</c:forEach>
