<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto" %>
<%
    ArChangeInventoryDto arChangeInventoryDto = DataSubmissionHelper.getCurrentArChangeInventoryDto(request);
    ArCurrentInventoryDto arCurrentInventoryDto = ArCurrentInventoryDto.addChange(DataSubmissionHelper.getCurrentArCurrentInventoryDto(request), arChangeInventoryDto);
%>
<c:if test="${empty perStageInfo}">
<h3>Patient's Inventory</h3>
<div class="table-responsive">
    <div class="table-gp">
<table aria-describedby="" class="table-responsive view-print-width">
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
        <th scope="col">Fresh Sperms</th>
    </tr>
    </thead>
    <tbody class="form-horizontal">
    <tr>
        <th>
            <p class="visible-xs visible-sm table-row-title"></p>
            Changes
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Frozen Oocytes</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenOocyteNum())%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Thawed Oocytes</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getThawedOocyteNum())%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Fresh Oocytes</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFreshOocyteNum())%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenEmbryoNum())%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getThawedEmbryoNum())%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFreshEmbryoNum())%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFrozenSpermNum())%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Fresh Sperms</p>
            <%=ArChangeInventoryDto.getDisplayNum(arChangeInventoryDto.getFreshSpermNum())%>
        </th>
    </tr>
    <tr>
        <th>
            <p class="visible-xs visible-sm table-row-title"></p>
            Current
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Frozen Oocytes</p>
            <%=arCurrentInventoryDto.getFrozenOocyteNum()%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Thawed Oocytes</p>
            <%=arCurrentInventoryDto.getThawedOocyteNum()%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Fresh Oocytes</p>
            <%=arCurrentInventoryDto.getFreshOocyteNum()%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
            <%=arCurrentInventoryDto.getFrozenEmbryoNum()%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
            <%=arCurrentInventoryDto.getThawedEmbryoNum()%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
            <%=arCurrentInventoryDto.getFreshEmbryoNum()%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
            <%=arCurrentInventoryDto.getFrozenSpermNum()%>
        </th>
        <th>
            <p class="visible-xs visible-sm table-row-title">Fresh Sperms</p>
            <%=arCurrentInventoryDto.getFreshSpermNum()%>
        </th>
    </tr>
    </tbody>
</table>
    </div>
</div>
<span id="error_inventoryLessZero" name="iaisErrorMsg" class="error-msg col-md-12" style="padding: 0px;"></span>
</c:if>
