<c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
<h3>
  <label><c:out value="${patientDto.name}"/>&nbsp;</label><span style="font-weight:normal;">${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </span>
</h3>
