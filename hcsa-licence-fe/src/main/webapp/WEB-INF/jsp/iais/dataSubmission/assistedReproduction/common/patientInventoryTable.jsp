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
        <td >
            <p class="visible-xs visible-sm table-row-title">Changes</p>
            Changes
        </td>
        <td >
            <p class="visible-xs visible-sm table-row-title">Frozen Oocytes</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenOocyteNum())%>
        </td>
        <td >
            <p class="visible-xs visible-sm table-row-title">Thawed Oocytes</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getThawedOocyteNum())%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Fresh Oocytes</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFreshOocyteNum())%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenEmbryoNum())%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getThawedEmbryoNum())%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFreshEmbryoNum())%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenSpermNum())%>
        </td>
    </tr>
    <tr>
        <td>
            <p class="visible-xs visible-sm table-row-title">Current</p>
            Current</td>
        <td >
            <p class="visible-xs visible-sm table-row-title">Frozen Oocytes</p>
            <%=arCurrentInventoryDto.getFrozenOocyteNum()%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Thawed Oocytes</p>
            <%=arCurrentInventoryDto.getThawedOocyteNum()%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Fresh Oocytes</p>
            <%=arCurrentInventoryDto.getFreshOocyteNum()%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
            <%=arCurrentInventoryDto.getFrozenEmbryoNum()%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
            <%=arCurrentInventoryDto.getThawedEmbryoNum()%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
            <%=arCurrentInventoryDto.getFreshEmbryoNum()%>
        </td>
        <td>
            <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
            <%=arCurrentInventoryDto.getFrozenSpermNum()%>
        </td>
    </tr>
    </tbody>
</table>
<span id="error_inventoryLessZero" name="iaisErrorMsg" class="error-msg col-md-12" style="padding: 0px;"></span>
</c:if>
