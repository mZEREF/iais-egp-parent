<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/tinymce/tinymce.min.js"></script>
<script src="<%=IaisEGPConstant.CSS_ROOT+IaisEGPConstant.COMMON_CSS_ROOT%>js/initTinyMce.js"></script>
<iais:section title="" id="process_NcEmail">
    <iais:row>
        <label class="col-xs-0 col-md-2 control-label col-sm-2">Subject</label>
        <div class="col-sm-9">
            <p><input name="subject" type="text" id="subject"
                      title="subject" readonly
                      value="${appPremisesUpdateEmailDto.subject}"></p>
        </div>
    </iais:row>
    <iais:row>
        <label class="col-xs-0 col-md-2 control-label col-sm-2">Content</label>
        <div class="col-sm-9">
            <textarea name="mailContent" cols="108" rows="50"
                      id="htmlEditroArea"
                      title="content">${appPremisesUpdateEmailDto.mailContent}</textarea>
        </div>
    </iais:row>
</iais:section>
<p class="text-right text-center-mobile">

    <iais:action >
        <a style="float:left;padding-top: 1.1%;" class="back" href="/main-web/eservice/INTRANET/MohHcsaBeDashboard?dashProcessBack=1"><em class="fa fa-angle-left"></em> Back</a>

        <button type="button" style="float:right" class="btn btn-secondary" onclick="javascript:doOpenEmailView();">Preview</button>
        <button class="btn btn-primary next" style="float:right" type="button" onclick="javascript:doSaveDraftEmail();">Save Draft</button>

    </iais:action>
</p>

<script>
    function doSaveDraftEmail(){
        showWaiting();
        var subject = $('#subject').val();
        var mailContent = $('#htmlEditroArea').val();

        var data = {
            'subject':subject,
            'mailContent':mailContent
        };
        $.ajax({
            'url':'${pageContext.request.contextPath}/save-draft-email',
            'dataType':'json',
            'data':data,
            'type':'POST',
            'success':function (data) {

                // setValue();
            },
            'error':function () {

            }
        });
        dismissWaiting();
    }

    function doOpenEmailView() {
        window.open ("/hcsa-licence-web/eservice/INTRANET/EmailView");
    }
</script>


