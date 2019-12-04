<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/jquery-3.4.1.min.js"></script>
<jsp:useBean id="insEmailDto" scope="session" type="com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto"/>
<table class="table">
    <tbody>
    <tr height="1">
        <td class="col-xs-2" >
            <p >
                subject:
            </p>
        </td>
        <td>
            <div class="col-sm-9">
                <p><input name="subject" type="text" id="subject" title="subject"  readonly value="${insEmailDto.subject}"></p>
            </div>
        </td>

    </tr>
    <tr height="1">
        <td class="col-xs-2" >
            <p >
                content:
            </p>
        </td>
        <td>
            <div class="col-sm-9">
                <p><textarea name="messageContent" cols="80" rows="23" class="wenbenkuang" id="htmlEditroArea" title="content"  >${insEmailDto.messageContent}</textarea></p>
            </div>
        </td>
    </tr>
    <tr height="1">
        <td class="col-xs-2" >
            <p >
                Processing Decision:
            </p>
        </td>
        <td>
            <div class="col-sm-9">
                <select name="decision">
                    <option value="Sends email/letter to Applicant">Sends email/letter to Applicant</option>
                    <option value="Route email/letter to AO1 for review">Route email/letter to AO1 for review</option>
                </select>
            </div>
        </td>
    </tr>


    </tbody>
</table>
<p class="text-right text-center-mobile">

    <iais:action>
        <button type="button" class="search btn" onclick="javascript:doSend();">Submit</button>
    </iais:action>
    <iais:action>
        <button type="button" class="search btn" onclick="javascript:doPreview();">Preview</button>
    </iais:action>
</p>

