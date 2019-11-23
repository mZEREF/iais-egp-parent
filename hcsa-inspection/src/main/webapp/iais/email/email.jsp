<%--
  Created by IntelliJ IDEA.
  User: hyr
  Date: 2019/11/22
  Time: 9:04
  To change this template use File | Settings | File Templates.
--%>
<jsp:useBean id="insEmailDto" scope="request" type="com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto"/>

<table class="control-grid columns1">
    <tbody>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--1" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--1--label" class="control-label control-set-font control-font-label">
                            Theme template
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><select id="emailTemplateType">
                            <option value=7>MOH IAIS - Required Clarification - Licence Number</option>
                            <option value=0 selected>No theme</option>
                            <option value=1>MOH IAIS - Review Outcome of Non-Compliance / Best Practices</option>
                            <option value=2>MOH IAIS - Reminder of NC Rectification</option>
                            <option value=3>MOH IAIS - Self-assessment Checklist Submission</option>
                            <option value=4>MOH IAIS - Post Licensing Inspection Notification</option>
                            <option value=5>MOH IAIS - Inspection Task Readiness</option>
                            <option value=6>MOH IAIS - Inspection Task Readiness</option>
                        </select></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
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
                        <p><input name="to" type="email" id="to" title="addressee" size="60"></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="first last" style="width: 100%;">
            <div id="control--runtime--3" class="control control-caption-horizontal">
                <div class=" form-group form-horizontal formgap">
                    <div class="col-sm-4 control-label formtext ">
                        <label id="control--runtime--3--label" class="control-label control-set-font control-font-label">
                            verifier:
                        </label>
                    </div>
                    <div class="col-sm-5">
                        <p><input name="form" type="email" id="form" title="verifier" size="60"></p>
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

                        <p><input name="subject" type="text" id="subject" title="subject" size="60" value="${insEmailDto.templateName}"></p>
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
                        <p><textarea name="content" cols="80" rows="25" class="wenbenkuang" id="cotent" title="content" >${insEmailDto.messageContent}</textarea></p>
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
                        <p><input name="SN"  id="SN"  type="text" ></p>
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
                        <p><input name="checklistItem"  id="checklistItem" type="text" value="Checklist item where NC is found" ></p>
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
                        <p><input name="Regulation"  id="Regulation" type="text" value="Regulation clause that is related to the NC" ></p>
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
                        <p><input name="Remarks"  id="Remarks"  type="text" value="Remarks indicated by inspector in the checklist" ></p>
                    </div>
                </div>
            </div>
        </td>
    </tr>

    </tbody>
</table>
<p class="text-right text-center-mobile">
    <iais:input type="button" id="validation" cssClass="btn btn-primary" value="validation"></iais:input>
    <iais:input type="button" id="reset" cssClass="btn btn-primary" value="reset"></iais:input>
    <iais:input type="button" id="send" cssClass="btn btn-primary" value="send"></iais:input></p>
<script type="text/javascript">
    $('#validation').click(function () {
        location.href= '';
    });
    $('#reset').click(function () {
        location.href= '';
    });
    $('#send').click(function () {
        location.href= '';
    });
    $('#emailTemplateType').change(function () {
        if(this.value!=7){
            $('#non-compliance-sn').hidden=true;
            $('#non-compliance-ci').hidden=true;
            $('#non-compliance-r').hidden=true;
            $('#non-compliance-rc').hidden=true;
        }
        $.ajax({
            type:"Post",
            url:"http://localhost:8886/inspection/email-template",
            data:"templateId="+this.serialize(),
            success:function (response) {
                ${insEmailDto}=response;
            }
        })
    })
</script>