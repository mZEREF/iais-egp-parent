<c:set var="patientDto" value="${arSuperDataSubmissionDto.patientInfoDto.patient}" />
<h3>
  <c:out value="${patientDto.name}"/><span style="font-weight:normal;">&nbsp;${empty patientDto.idNumber ? "" : "("}<c:out value="${patientDto.idNumber}"/>${empty patientDto.idNumber ? "" : ")"} </span>
</h3>
