<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<table class="table">
    <tbody>
    <tr height="1">
        <td class="col-xs-2" >
            <p >
                Subject
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
                Content
            </p>
        </td>
        <td style="height: 1180px;">
            <div class="col-sm-9">
                <textarea name="messageContent" cols="108" rows="50" id="htmlEditroArea" title="content"  >${insEmailDto.messageContent}</textarea>
            </div>
        </td>
    </tr>

    </tbody>
</table>
<p class="text-right text-center-mobile">

    <iais:action style="text-align:right;">
        <button type="button" class="btn btn-secondary" onclick="javascript:doPreview();">Preview</button>
    </iais:action>
</p>


