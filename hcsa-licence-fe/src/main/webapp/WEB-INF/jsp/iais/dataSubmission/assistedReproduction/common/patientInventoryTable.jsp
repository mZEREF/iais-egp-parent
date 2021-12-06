<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<% PatientInventoryDto patientInventoryDto = DataSubmissionHelper.getCurrentPatientInventory(request);%>
<h3>Patient's Inventory</h3>
<table aria-describedby="" class="table discipline-table">
  <thead>
  <tr>
    <th scope="col"></th>
    <th scope="col">Frozen Oocytes</th>
    <th scope="col">Thawed Oocytes</th>
    <th scope="col">Fresh Oocytes</th>
    <th scope="col">Frozen Embryos</th>
    <th scope="col">Thawed Embryos</th>
    <th scope="col">Fresh Embryos</th>
    <th scope="col">Frozen Sperms</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <th scope="col">Changes</th>
    <th scope="col"><%=PatientInventoryDto.getDisplayNum(patientInventoryDto.getChangeFrozenOocytes())%>
    </th>
    <th scope="col"><%=PatientInventoryDto.getDisplayNum(patientInventoryDto.getChangeThawedOocytes())%>
    </th>
    <th scope="col"><%=PatientInventoryDto.getDisplayNum(patientInventoryDto.getChangeFreshOocytes())%>
    </th>
    <th scope="col"><%=PatientInventoryDto.getDisplayNum(patientInventoryDto.getChangeFrozenEmbryos())%>
    </th>
    <th scope="col"><%=PatientInventoryDto.getDisplayNum(patientInventoryDto.getChangeThawedEmbryos())%>
    </th>
    <th scope="col"><%=PatientInventoryDto.getDisplayNum(patientInventoryDto.getChangeFreshEmbryos())%>
    </th>
    <th scope="col"><%=PatientInventoryDto.getDisplayNum(patientInventoryDto.getChangeFrozenSperms())%>
    </th>
  </tr>
  <tr>
    <th scope="col">Current</th>
    <th scope="col">${arSuperDataSubmissionDto.patientInventoryDto.currentFrozenOocytes}</th>
    <th scope="col">${arSuperDataSubmissionDto.patientInventoryDto.currentThawedOocytes}</th>
    <th scope="col">${arSuperDataSubmissionDto.patientInventoryDto.currentFreshOocytes}</th>
    <th scope="col">${arSuperDataSubmissionDto.patientInventoryDto.currentFrozenEmbryos}</th>
    <th scope="col">${arSuperDataSubmissionDto.patientInventoryDto.currentThawedEmbryos}</th>
    <th scope="col">${arSuperDataSubmissionDto.patientInventoryDto.currentFreshEmbryos}</th>
    <th scope="col">${arSuperDataSubmissionDto.patientInventoryDto.currentFrozenSperms}</th>
  </tr>
  </tbody>
</table>
