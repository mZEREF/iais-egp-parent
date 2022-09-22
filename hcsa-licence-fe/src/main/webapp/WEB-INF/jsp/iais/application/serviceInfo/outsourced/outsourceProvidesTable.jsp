<br><br>
<iais:pagination  param="outSourceParam" result="outSourceResult"/>
<div class="main-content" style="width: 100%;!important;overflow-x: scroll;">
    <div class="col-lg-12 col-xs-12 col-md-12">
        <div class="intranet-content">
            <table aria-describedby="" class="table">
                <thead>
                <tr>
                    <iais:sortableHeader needSort="true" field="SVC_NAME" value="Serivce" style="width:15%;"/>
                    <iais:sortableHeader needSort="true" field="LICENCE_NO" value="Licence No." style="width:15%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="BUSINESS_NAME" value="Business Name" style="width:15%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="ADDRESS" value="Address" style="width:10%;" customSpacing="12"/>
                    <iais:sortableHeader needSort="true" field="EXPIRY_DATE" value="Licence Tenure" style="width:15%;" customSpacing="30"/>
                    <th style="width: 18%;">
                        <p style="margin-left: 16px;">
                            Date of Agreement<span class="mandatory">*</span>
                        </p>
                    </th>
                    <th style="width: 18%;">
                        <p style="margin-left: 16px;">
                            End Date of Agreement<span class="mandatory">*</span>
                        </p>
                    </th>
                    <th style="width: 10%;">
                        <p style="margin-left: 12px;">
                            Scope of Outsourcing
                        </p>
                    </th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="outSourceResult" items="${outSourceResult.rows}" varStatus="status">
                <tr>
                    <td>
                        <p class="visible-xs visible-sm table-row-title" style="width: 30px;!important;">Service</p>
                        <p style="width: 200px;">${outSourceResult.svcName}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Licence No.</p>
                        <p>${outSourceResult.licenceNo}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Business Name</p>
                        <p style="width: 220px;">${outSourceResult.businessName}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Address</p>
                        <p style="width: 260px">${outSourceResult.address}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Licence Tenure</p>
                        <p>${outSourceResult.expiryDate}</p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">Date of Agreement</p>
                        <p>
                            <%String dateStart = request.getParameter("dateStart");%>
                            <iais:datePicker id="dateStart" name="dateStart" value="<%=dateStart%>"/>
                        </p>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                        <%String dateEnd = request.getParameter("dateEnd");%>
                        <iais:datePicker id="dateEnd" name="dateEnd" value="<%=dateEnd%>"/>
                    </td>
                    <td>
                        <p class="visible-xs visible-sm table-row-title">End Date of Agreement</p>
                        <%String scopeOfOutsourcing = request.getParameter("scopeOfOutsourcing");%>
                        <iais:input maxLength="300" type="text" cssClass="scopeOfOutsourcing" name="scopeOfOutsourcing" value="<%=scopeOfOutsourcing%>"/>
                    </td>
                    <td>
                        <button type="button" class="btn btn-default btn-sm btn-add">Add</button>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
<script>
    window.onload = function () {
        console.log("ssssssssssssssss")
        let allBtn = document.getElementsByClassName("btn-add");
        for (let i = 0; i < allBtn.length; i++) {
            allBtn[i].onclick = function (){
                showWaiting();
                $('input[name="crud_action_type"]').val("add");
                let tr =this.parentNode.parentNode;
                tr.parentNode.removeChild(tr);
            };
        }
    };
</script>
