<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>

<%--@elvariable id="emailSubject" type="java.lang.String"--%>
<%--@elvariable id="emailContent" type="java.lang.String"--%>

<iais:section title="" id="process_NcEmail">
    <iais:row>
        <label class="col-xs-0 col-md-2 control-label col-sm-2">Subject</label>
        <div class="col-sm-9">
            <p><input name="subject" type="text"
                      title="subject" readonly
                      value="${emailSubject}"></p>
        </div>
    </iais:row>
    <iais:row>
        <label class="col-xs-0 col-md-2 control-label col-sm-2">Content</label>
        <div class="col-sm-9">
            <textarea id="htmlEditroArea" name="messageContent" cols="108" rows="50"
                      title="content" style="display: none">${emailContent}</textarea>
        </div>
    </iais:row>
</iais:section>
<p class="text-right text-center-mobile">
    <a style="float:left;padding-top: 1.1%;text-decoration:none;" class="back" href="/bsb-web/eservice/INTRANET/MohBsbTaskList"><em class="fa fa-angle-left"></em> Back</a>

    <iais:action style="text-align:right;">
        <button id="previewEmail" type="button" class="btn btn-secondary">Preview</button>
        <button id="updateEmail" type="button" class="btn btn-primary">Update</button>
    </iais:action>
</p>

