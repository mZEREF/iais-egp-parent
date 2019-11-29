<jsp:useBean id="insEmailDto" scope="session" type="com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto"/>

<table class="control-grid columns1">
    <tbody>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--2" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--2--label" class="control-label control-set-font control-font-label">
                            addressee:
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="addressee" type="email" id="to" title="addressee" size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--4" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--4--label" class="control-label control-set-font control-font-label">
                            subject:
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="subject" type="text" id="subject" title="subject" size="60" readonly value="${insEmailDto.subject}"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--5" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--6--label" class="control-label control-set-font control-font-label">
                            content:
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><textarea name="messageContent" cols="80" rows="25" class="wenbenkuang" id="cotent" title="content" readonly >${insEmailDto.messageContent}</textarea></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="name-of-applicant">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--11" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--11--label" class="control-label control-set-font control-font-label">
                            Name of Applicant
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="applicantName"  id="Name-Applicant"  type="text"  size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="Application-Number">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--12" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--12--label" class="control-label control-set-font control-font-label">
                            Application Number
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="applicationNumber"  id="Application-Num"  type="text"  size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="HCI_Code">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--13" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--13--label" class="control-label control-set-font control-font-label">
                            HCI Code
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="hciCode"  id="HCI-Code"  type="text"  size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="HCI_Name/Address">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--14" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--14--label" class="control-label control-set-font control-font-label">
                            HCI Name / Address
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="hciNameOrAddress"  id="HCI-Name/Address"  type="text"  size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="Service_Name">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--15" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--15--label" class="control-label control-set-font control-font-label">
                            Service Name
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="serviceName"  id="Service-Name"  type="text"  size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="non-compliance-sn">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--7" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--7--label" class="control-label control-set-font control-font-label">
                            SN
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><select name="sn">
                            <option value="Yse">Yse</option>
                            <option value="No">No</option>
                            <option value="NA">NA</option>
                        </select>

                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="non-compliance-ci">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--8" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--8--label" class="control-label control-set-font control-font-label">
                            Checklist Item
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="checklistItem"  id="checklistItem" type="text" value="Checklist item where NC is found" size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="non-compliance-rc">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--9" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--9--label" class="control-label control-set-font control-font-label">
                            Regulation Clause
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="regulationClause"  id="Regulation" type="text" value="Regulation clause that is related to the NC" size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="non-compliance-r">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--10" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--10--label" class="control-label control-set-font control-font-label">
                            Remarks
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="remarks"  id="Remarks"  type="text" value="Remarks indicated by inspector in the checklist" size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1" id="Best-Practices">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--16" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--16--label" class="control-label control-set-font control-font-label">
                            BestPractices
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="BestPractices"  id="BestPractices"  type="text" value="Best practices recommended by inspector" size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>

    </tbody>
</table>
<p class="text-right text-center-mobile">
    <iais:action>
        <button type="button" class="search btn" onclick="javascript:doValidation();">validation</button>
    </iais:action>
    <iais:action>
        <button type="button" class="search btn" onclick="javascript:doReset();">reset</button>
    </iais:action>
    <iais:action>
        <button type="button" class="search btn" onclick="javascript:doSend();">send</button>
    </iais:action>
    <iais:action>
        <button type="button" class="search btn" onclick="javascript:doPreview();">preview</button>
    </iais:action>
</p>
<script type="text/javascript">
    function doPreview(){
        SOP.Crud.cfxSubmit("mainForm", "preview");
    }
    function doValidation(){
        SOP.Crud.cfxSubmit("mainForm", "validation");
    }

    function doSend(){
        SOP.Crud.cfxSubmit("mainForm", "send");
    }

    function doReset(){
        document.getElementById("addressee").value="";
        document.getElementById("Name-Applicant").value="";
        document.getElementById("Application-Num").value="";
        document.getElementById("HCI-Code").value="";
        document.getElementById("HCI-Name/Address").value="";
        document.getElementById("addressee").value="";
        document.getElementById("addressee").value="";
        document.getElementById("addressee").value="";
        document.getElementById("addressee").value="";

    }
</script>