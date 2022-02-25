<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto" %>
<%
    ArChangeInventoryDto arChangeInventoryDto = DataSubmissionHelper.getCurrentArChangeInventoryDto(request);
    ArCurrentInventoryDto arCurrentInventoryDto = ArCurrentInventoryDto.addChange(DataSubmissionHelper.getCurrentArCurrentInventoryDto(request), arChangeInventoryDto);
%>
<c:if test="${empty perStageInfo}">
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
        <th scope="col"><%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenOocyteNum())%>
        </th>
        <th scope="col"><%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getThawedOocyteNum())%>
        </th>
        <th scope="col"><%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFreshOocyteNum())%>
        </th>
        <th scope="col"><%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenEmbryoNum())%>
        </th>
        <th scope="col"><%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getThawedEmbryoNum())%>
        </th>
        <th scope="col"><%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFreshEmbryoNum())%>
        </th>
        <th scope="col"><%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenSpermNum())%>
        </th>
    </tr>
    <tr>
        <th scope="col">Current</th>
        <th scope="col"><%=arCurrentInventoryDto.getFrozenOocyteNum()%>
        </th>
        <th scope="col"><%=arCurrentInventoryDto.getThawedOocyteNum()%>
        </th>
        <th scope="col"><%=arCurrentInventoryDto.getFreshOocyteNum()%>
        </th>
        <th scope="col"><%=arCurrentInventoryDto.getFrozenEmbryoNum()%>
        </th>
        <th scope="col"><%=arCurrentInventoryDto.getThawedEmbryoNum()%>
        </th>
        <th scope="col"><%=arCurrentInventoryDto.getFreshEmbryoNum()%>
        </th>
        <th scope="col"><%=arCurrentInventoryDto.getFrozenSpermNum()%>
        </th>
    </tr>
    </tbody>
</table>
<span id="error_inventoryLessZero" name="iaisErrorMsg" class="error-msg col-md-12" style="padding: 0px;"></span>
</c:if>
