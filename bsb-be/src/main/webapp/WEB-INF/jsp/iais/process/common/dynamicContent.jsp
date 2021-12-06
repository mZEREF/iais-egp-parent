<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<div class="alert alert-info" role="alert">
    <strong>
        <h4>Dynamic Content Update</h4>
    </strong>
</div>
<iais:section title="">
    <div>
        <iais:row>
            <iais:field value="Subject" required="false"></iais:field>
            <iais:value width="10">
                <input type="text" name="subject" value="" maxlength="10" class="form-control"/>
            </iais:value>
        </iais:row>
    </div>
    <div>
        <iais:row>
            <iais:field value="Dynamic Content" required="false" width="12"/>
            <iais:value width="10">
                <div class="input-group">
                    <div class="ax_default text_area">
                        <textarea maxlength="4000" name="messageContent" class="textarea" id="htmlEditor" title="content"></textarea>
                    </div>
                </div>
            </iais:value>
        </iais:row>
    </div>
</iais:section>
<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<script>
    $(window).on("load", function(){
        setTimeout("intiTinymce()", 1000);
    });

    function intiTinymce() {
        tinymce.init({
            selector: "#htmlEditor",  // change this value according to your HTML
            menubar: 'file edit view insert format tools',
            plugins: ['print preview fullpage',
                'advlist autolink lists link image charmap print preview anchor',
                'searchreplace visualblocks code fullscreen',
                'insertdatetime media table paste code help wordcount',
                'noneditable'
            ],
            toolbar: 'undo redo | formatselect | ' +
                ' bold italic backcolor | alignleft aligncenter ' +
                ' alignright alignjustify | bullist numlist outdent indent |' +
                ' removeformat | help',
            height: 500,
            width: 800
        });
    }
</script>