<%@ page import="com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto" %>
<%@ page import="com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto" %>
<%
    ArChangeInventoryDto arChangeInventoryDto = DataSubmissionHelper.getCurrentArChangeInventoryDto(request);
    ArCurrentInventoryDto arCurrentInventoryDto = ArCurrentInventoryDto.addChange(DataSubmissionHelper.getCurrentArCurrentInventoryDto(request), arChangeInventoryDto);
    ArChangeInventoryDto secondArChangeInventoryDto = DataSubmissionHelper.getSecondCurrentArChangeInventoryDto(request);
    ArCurrentInventoryDto secondArCurrentInventoryDto = ArCurrentInventoryDto.addChange(DataSubmissionHelper.getSecondCurrentArCurrentInventoryDto(request), secondArChangeInventoryDto);
%>

<div style="${not empty donorSampleDto.idNumber || not empty donorSampleDto.idNumberMale?'':'display: none'}">
    <c:if test="${donorSampleDto.sampleType eq 'DONTY003'}"><h3>Female Donor's Inventory</h3></c:if>
    <c:if test="${donorSampleDto.sampleType ne 'DONTY003'}"><h3>Donor's Inventory</h3></c:if>
    <div class="table-responsive">
        <div class="table-gp">
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
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <span id="error_inventoryLessZero" name="iaisErrorMsg" class="error-msg col-md-12" style="padding: 0px;"></span>
</div>

<div style="${donorSampleDto.sampleType eq 'DONTY003' and not empty donorSampleDto.idNumberMale?'':'display: none'}">
    <h3>Male Donor's Inventory</h3>
    <div class="table-responsive">
        <div class="table-gp">
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
                <tbody class="form-horizontal">
                <tr>
                    <th>
                        <p class="visible-xs visible-sm table-row-title"></p>
                        Changes
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Frozen Oocytes</p>
                        <%=ArChangeInventoryDto.getDisplayNum(secondArChangeInventoryDto.getFrozenOocyteNum())%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Thawed Oocytes</p>
                        <%=ArChangeInventoryDto.getDisplayNum(secondArChangeInventoryDto.getThawedOocyteNum())%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Fresh Oocytes</p>
                        <%=ArChangeInventoryDto.getDisplayNum(secondArChangeInventoryDto.getFreshOocyteNum())%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
                        <%=ArChangeInventoryDto.getDisplayNum(secondArChangeInventoryDto.getFrozenEmbryoNum())%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
                        <%=ArChangeInventoryDto.getDisplayNum(secondArChangeInventoryDto.getThawedEmbryoNum())%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
                        <%=ArChangeInventoryDto.getDisplayNum(secondArChangeInventoryDto.getFreshEmbryoNum())%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
                        <%=ArChangeInventoryDto.getDisplayNum(secondArChangeInventoryDto.getFrozenSpermNum())%>
                    </th>
                </tr>
                <tr>
                    <th>
                        <p class="visible-xs visible-sm table-row-title"></p>
                        Current
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Frozen Oocytes</p>
                        <%=secondArCurrentInventoryDto.getFrozenOocyteNum()%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Thawed Oocytes</p>
                        <%=secondArCurrentInventoryDto.getThawedOocyteNum()%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Fresh Oocytes</p>
                        <%=secondArCurrentInventoryDto.getFreshOocyteNum()%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Frozen Embryos</p>
                        <%=secondArCurrentInventoryDto.getFrozenEmbryoNum()%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Thawed Embryos</p>
                        <%=secondArCurrentInventoryDto.getThawedEmbryoNum()%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Fresh Embryos</p>
                        <%=secondArCurrentInventoryDto.getFreshEmbryoNum()%>
                    </th>
                    <th>
                        <p class="visible-xs visible-sm table-row-title">Frozen Sperms</p>
                        <%=secondArCurrentInventoryDto.getFrozenSpermNum()%>
                    </th>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>

