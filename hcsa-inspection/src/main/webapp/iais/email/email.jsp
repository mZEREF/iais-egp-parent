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
                remarks:
            </p>
        </td>
        <td>
            <div class="col-sm-9">
                <p><textarea name="remarks" cols="80" rows="8" class="wenbenkuang" id="remarks" title="content"  >${insEmailDto.remarks}</textarea></p>
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