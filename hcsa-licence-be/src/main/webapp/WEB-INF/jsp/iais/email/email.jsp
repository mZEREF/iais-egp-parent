<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<iais:section title="" id = "process_NcEmail">
    <iais:row>
        <label class="col-xs-0 col-md-2 control-label col-sm-2">Subject</label>
        <div class="col-sm-9">
            <p><input name="subject" type="text" id="subject"
                      title="subject" readonly
                      value="${insEmailDto.subject}"></p>
        </div>
    </iais:row>
    <iais:row>
        <label class="col-xs-0 col-md-2 control-label col-sm-2">Content</label>
        <div class="col-sm-9">
                                                                <textarea name="messageContent" cols="108" rows="50"
                                                                          id="htmlEditroArea"
                                                                          title="content">${insEmailDto.messageContent}</textarea>
        </div>
    </iais:row>
</iais:section>
<p class="text-right text-center-mobile">

    <iais:action style="text-align:right;">
        <button type="button" class="btn btn-secondary" onclick="javascript:doPreview();">Preview</button>
    </iais:action>
</p>


