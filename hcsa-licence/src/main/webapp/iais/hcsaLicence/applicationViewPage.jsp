

<div class="onecolumn">
    <div id="rotate">
        <div class="header">
            <div class="tab-header">
                <ul>
                    <li><a href="#fragment-1"><span>Info</span></a></li>
                    <li><a href="#fragment-2"><span>Documents</span></a></li>
                    <li><a href="#fragment-3"><span>Payment</span></a></li>
                    <li><a href="#fragment-4"><span>Processing</span></a></li>
                </ul>
            </div>
        </div>


        <div>
            <form id = "applicationForm" class = "form" name="applicationForm" method = "post"  enctype="multipart/form-data">

                <div id="fragment-1" class="tabFrame">
                    <div class="inner-accordion" id="innerAccordiion1">
                        <div class="header">
                            <span class="collapse" style="height: 20px;">Submission Details</span>
                        </div>
                        <div class="content">

                            <table class="table2 view-table"
                                   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;">
                                        No.
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${entity.appNo}"></c:out>
                                    </td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        Digital Service
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${entity.svcName}"></c:out>
                                    </td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        Submission Date
                                    </td>

                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${date}"></c:out>
                                    </td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        Current Status
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${displayCurrentStatus}"></c:out>
                                    </td>
                                </tr>

                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        Days Lapsed
                                    </td>

                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${lapsedDays}"></c:out>
                                    </td>
                                </tr>

                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        SLA
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><div
                                            style="width: 50%;" class="">
                                       </div></td>
                                </tr>

                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        SLA
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">N/A</td>
                                </tr>

                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        SLA
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">N/A</td>
                                </tr>

                                </tbody>
                            </table>

                            <div class="action-buttons" align="center"
                                 style="margin-left: 0;">
                                <button style="width: 150px;" id="openViewContent">
                                    View Form
                                </button>
                            </div>
                            <br class="clear" />
                        </div>
                    </div>

                    <div class="inner-accordion" id="innerAccordiion2">
                        <div class="header">
                            <span class="collapse" style="height: 20px;">Applicant Details</span>
                        </div>

                        <div class="content">
                            <table class="table2 view-table"
                                   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;">

                                        Applicant ID
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.applicantId}"></c:out></td>
                                </tr>
                                <tr>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Name
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.applicantName}"></c:out></td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Address
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.applicantAddress}"></c:out></td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Mailing Adress
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.applicantMailAddress }"></c:out></td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        E-mail
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.applicantEmail}"></c:out></td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Mobile No.
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.applicantMobile}"></c:out></td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">
                                        Phone No.
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.applicantTel}"></c:out></td>
                                </tr>
                                </tbody>
                            </table>
                            <br class="clear" />
                        </div>

                    </div>


                    <div class="inner-accordion" id="innerAccordiion3">
                        <div class="header">
                            <span class="collapse" style="height: 20px;">Submitter Details</span>
                        </div>


                        <div class="content">
                            <table class="table2 view-table"
                                   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;">

                                        Submitter ID
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${entity.submitterId}"></c:out>
                                    </td>
                                </tr>
                                <tr>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Name
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${entity.submitterName}"></c:out>
                                    </td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Address
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.submitterAddress }"></c:out></td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Mailing Address
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;"><c:out
                                            value="${entity.submitterMailAddress}"></c:out></td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        E-mail
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${entity.submitterEmail}"></c:out>
                                    </td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Mobile No.
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${entity.submitterMobile}"></c:out>
                                    </td>
                                </tr>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">

                                        Phone No.
                                    </td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${entity.submitterTel}"></c:out>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                            <br class="clear" />
                        </div>

                    </div>
                    <br class="clear" />
                </div>


                <div id="fragment-2" class="tabFrame">


                    <div class="inner-accordion" id="innerAccordiion4">
                        <script type="text/javascript">
                            function changeSort_resultForm1(sort, isAsc) {
                                var sortBy = sort;
                                var sortDir;
                                if(isAsc)
                                    sortDir='';
                                else
                                    sortDir='descending';
                                SOP.Crud.cfxSubmit('applicationForm', 'sortSupport', sortBy, sortDir);
                            }
                        </script>
                        <div class="header"><span class="collapse" style="height: 20px;">Supporting Documents</span></div>
                        <div class="content">
                           <p class="text">
                                <egov-smc:message key="supportingDocumentsDefine">These are documents uploaded by the applicant or an officer on behalf of the applicant. Listed documents are those defined for this digital service only.</egov-smc:message>
                            </p>
                            <table class="table2 view-table" style="border: 1px; border-style:solid;border-color:#d0d0d0;">
                                <tbody>
                                <tr style="border:0px;background-color: #A5A5A5;">
                                    <td class="th" align="left" width="16%">
                                        <div class="sort-label">Document</div></td>
                                    <td class="th" align="left" width="20%">
                                        <div class="sort-label">File</div></td>
                                    <td class="th" align="left" width="16%">
                                        <div class="sort-label">Size</div></td>
                                    <td class="th" align="left" width="16%">
                                        <div class="sort-label">Submitted By</div></td>
                                    <td class="th" align="left" width="20%">
                                        <div class="sort-label">Data Submitted</div></td>
                                    <td class="th" align="left" width="12%">
                                        <div class="sort-label">Action</div></td>
                                </tr>


                                <tr style="width:50%">
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;">/td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;">
                                        <c:if test="${isUserCanUploadDoc == true or isUserCanUpdateStatus == true}">
                                            <c:if test="${isUserCanHandleSupport}">

                                            </c:if>
                                        </c:if>
                                    </td>
                                </tr>



                                </tbody>
                            </table>
                            <br class="clear"/>
                        </div>

                    </div>



                    <div class="inner-accordion" id="innerAccordiion5">
                        <div class="header"><span class="collapse" style="height: 20px;">Internal Documents</span></div>


                        <div class="content">
                            <p class="text">
                                <egov-smc:message key="internalDocumentsDefine">These are documents uploaded by an agency officer to support back office processing.</egov-smc:message>
                            </p>
                            <script type="text/javascript">
                                function changeSort_resultForm(sort, isAsc) {
                                    var sortBy = sort;
                                    var sortDir;
                                    if(isAsc)
                                        sortDir='';
                                    else
                                        sortDir='descending';
                                    SOP.Crud.cfxSubmit('applicationForm', 'sortInternal', sortBy, sortDir);
                                }
                            </script>
                            <table class="table2 view-table" style="border: 1px; border-style:solid;border-color:#d0d0d0;">
                                <tbody>

                                <tr style="border:0px;background-color: #A5A5A5;">
                                    <td class="th" align="left" width="16%">
                                        <span class="column-sort"> <a class="sort-up" title="Sort up" onclick="changeSort_resultForm('docName', true)" href="javascript:void(0);"></a> <a class="sort-down " title="Sort down" onclick="changeSort_resultForm('docName', false)" href="javascript:void(0);"></a></span>
                                        <div class="sort-label" style="padding-left:20px;">Document</div></td>
                                    <td class="th" align="left" width="16%">
                                        <span class="column-sort"> <a class="sort-up " title="Sort up" onclick="changeSort_resultForm('docFilename', true)" href="javascript:void(0);"></a> <a class="sort-down " title="Sort down" onclick="changeSort_resultForm('docFilename', false)" href="javascript:void(0);"></a></span>
                                        <div class="sort-label" style="padding-left:20px;">File</div></td>
                                    <td class="th" align="left" width="16%"><span class="column-sort"> <a class="sort-up " title="Sort up" onclick="changeSort_resultForm('docFileSize', true)" href="javascript:void(0);"></a> <a class="sort-down " title="Sort down" onclick="changeSort_resultForm('docFileSize', false)" href="javascript:void(0);"></a></span>
                                        <div class="sort-label" style="padding-left:20px;">Size</div></td>
                                    <td class="th" align="left" width="16%"><span class="column-sort"> <a class="sort-up " title="Sort up" onclick="changeSort_resultForm('createdUserId', true)" href="javascript:void(0);"></a> <a class="sort-down  " title="Sort down" onclick="changeSort_resultForm('createdUserId', false)" href="javascript:void(0);"></a></span>
                                        <div class="sort-label" style="padding-left:20px;">Uploaded By</div></td>
                                    <td class="th" align="left" width="16%"><span class="column-sort"> <a class="sort-up " title="Sort up" onclick="changeSort_resultForm('createdDate', true)" href="javascript:void(0);"></a> <a class="sort-down  " title="Sort down" onclick="changeSort_resultForm('createdDate', false)" href="javascript:void(0);"></a></span>
                                        <div class="sort-label" style="padding-left:20px;">Date Uploaded</div></td>
                                    <c:if test="${isUserCanUploadDoc == true or isUserCanUpdateStatus == true}">
                                        <c:if test="${isUserCanHandleInternal}">
                                            <td class="th" align="left" width="16%"></td>
                                        </c:if>
                                    </c:if>

                                </tr>

                                <tr style="width:50%">
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"></td>
                                    <c:if test="${isUserCanUploadDoc == true or isUserCanUpdateStatus == true}">
                                        <c:if test="${isUserCanHandleInternal}">
                                            <td  style="border: 1px; border-style:solid; text-align: left;width: 16%;border-color:#d0d0d0;"><a href="javascript:void(0);" onclick="deleteInternalDoc('');" style="cursor: pointer;">Remove</a></td>
                                        </c:if>
                                    </c:if>
                                </tr>


                                </tbody>
                            </table>

                            <br class="clear"/>
                        </div>

                    </div>

                    <br class="clear"/>

                </div>

                <div id="fragment-3" class="tabFrame">
                    <div class="inner-accordion" id="innerAccordiion6">
                        <div class="header">
                            <span class="collapse" style="height: 20px;">Payment Details</span>
                        </div>

                        <div class="content">
                            <table class="table2 view-table"
                                   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="border: 0px; background-color: #A5A5A5;">
                                    <td align="left" width="16%">Payment</td>
                                    <td align="left" width="16%">Amount</td>
                                    <td align="left" width="16%">Date</td>
                                    <td align="left" width="16%">Status</td>
                                    <td align="left" width="16%">Reference No.</td>
                                    <td align="left" width="16%">Payment Type</td>
                                </tr>

                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                </tr>


                                </tbody>
                            </table>
                            <br class="clear" />
                        </div>
                    </div>
                </div>
                <div id="fragment-4" class="tabFrame">
                    <div class="inner-accordion" id="innerAccordiion7">
                        <div class="header">
                            <span class="collapse" style="height: 25px;">Processing Status Update</span>
                        </div>

                        <div class="content">
                            <table class="table2 view-table"
                                   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; width: 50%; border-color: #d0d0d0;">Current Status:</td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <c:out value="${displayCurrentStatus}"></c:out>
                                    </td>
                                </tr>

                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">Internal Comments:</td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <textarea rows="" cols="25" name="internalComments"></textarea>
                                        <br /> <small class="error"></small>

                                    </td>
                                </tr>
                                <tr>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: right; border-color: #d0d0d0;">Status:</td>
                                    <td align="left"
                                        style="border: 1px; border-style: solid; border-color: #d0d0d0;">


                                    </td>
                                </tr>

                                </tbody>
                            </table>

                            <div class="inner-accordion" id="innerAccordiion9" style="margin-left: 0px;margin-right: 0px;padding-left: 0px;padding-right: 0px;width: 100%">
                                <div class="header">
                                    <span class="collapse" style="height: 20px;">Endorsement Routing</span>
                                </div>

                                <div class="content">
                                    <table class="table2 view-table" style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                        <tbody>
                                        <tr style="border: 0px; background-color: #A5A5A5;">
                                            <td align="left" width="5%">
                                                <input id="selectAllEndorsement" type="checkbox" onclick="selectAllEndorsements(this.checked)"/>
                                            </td>
                                            <td align="left" width="5%">No</td>
                                            <td align="left" width="16%">Agency</td>
                                            <td align="left" width="16%">Working Group</td>
                                            <td align="left" width="16%">Assignment Type</td>
                                            <td align="left" width="16%">Officer</td>
                                        </tr>

                                        <tr style="width: 50%">
                                            <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;">
                                                <input type="checkbox" name="endorsementConfigIds" value="" onclick="selectEndorsement(this.checked)">
                                            </td>
                                            <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                            <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                            <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                            <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                            <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                        </tr>

                                        </tbody>
                                    </table>
                                </div>
                            </div>


                            <div class="action-buttons" align="center" style="margin-left: 0;">
                                <button type="button" style= "width:100px;" onclick="updateStatusConfirm();">Update</button>
                            </div>

                        </div>

                    </div>
                    <br class="clear" />

                    <div class="inner-accordion" id="innerAccordiion10">
                        <div class="header">
                            <span class="collapse" style="height: 20px;">Endorsement History</span>
                        </div>

                        <div class="content">
                            <table class="table2 view-table" style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="border: 0px; background-color: #A5A5A5;">
                                    <td align="left" width="3%">No</td>
                                    <td align="left" width="8%">Stage</td>
                                    <td align="left" width="8%">Agency</td>
                                    <td align="left" width="8%">Officer</td>
                                    <td align="left" width="16%">Working Group</td>
                                    <td align="left" width="12%">Status</td>
                                    <td align="left" width="16%">Internal Comments</td>
                                    <td align="left" width="16%">Last Updated</td>
                                </tr>

                                <tr style="width: 50%">
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                </tr>

                                </tbody>
                            </table>
                        </div>
                    </div>
                    <br class="clear" />





                    <div class="inner-accordion" id="innerAccordiion8">
                        <div class="header">
                            <span class="collapse" style="height: 20px;">Processing History</span>
                        </div>

                        <div class="content">
                            <table class="table2 view-table"
                                   style="border: 1px; border-style: solid; border-color: #d0d0d0;">
                                <tbody>
                                <tr style="border: 0px; background-color: #A5A5A5;">
                                    <td align="left" width="16%">Officer
                                    </td>
                                    <td align="left" width="16%">Working Group</td>
                                    <td align="left" width="16%">Status Update</td>
                                    <td align="left" width="16%">Internal Comments</td>
                                    <td align="left" width="17%">Last Updated</td>
                                </tr>

                                <tr style="width: 50%">
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                    <td
                                            style="border: 1px; border-style: solid; text-align: left; border-color: #d0d0d0;"></td>
                                </tr>




                                </tbody>
                            </table>
                            <br class="clear" />
                        </div>

                    </div>




                </div>
            </form>
        </div>

    </div>


<iframe id="pdfFormIframe" height="0" width="1"></iframe>