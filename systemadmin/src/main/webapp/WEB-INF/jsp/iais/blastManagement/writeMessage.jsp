<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://www.ecq.com/iais" prefix="iais"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant" %>
<%
    String webroot = IaisEGPConstant.CSS_ROOT + IaisEGPConstant.COMMON_CSS_ROOT;
%>
<%
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<script src="<%=webroot%>js/tinymce/tinymce.min.js"></script>
<script src="<%=webroot%>js/initTinyMce.js"></script>
<webui:setLayout name="iais-intranet"/>
<div class="main-content">
    <form class="form-horizontal" method="post" id="mainForm" enctype="multipart/form-data"  action=<%=process.runtime.continueURL()%>>
        <div class="row">
            <div class="col-lg-12 col-xs-12">
                <div class="center-content">
                    <div class="intranet-content">
                        <ul class="progress-tracker">
                            <li class="tracker-item active">Fill in Message Details</li>
                            <li class="tracker-item active">Write Message</li>
                            <li class="tracker-item ">Select Recipients to send</li>
                        </ul>
                        <h3>New Mass Email</h3>
                        <div class="form-group">
                            <iais:field value="Subject" required="true"/>
                            <iais:value>
                                <div class="col-xs-8 col-sm-6 col-md-5">
                                    <input id="subject" type="text" name="subject" maxlength="255" value="${edit.getSubject()}">
                                    <span id="error_subject" name="iaisErrorMsg" class="error-msg"></span>
                                </div>
                            </iais:value>
                        </div>

                        <div class="form-group">
                            <iais:field value="Content" required="true"/>
                            <div class="col-xs-8 col-sm-8 col-md-8">
                                <textarea maxlength="4000" rows="30" name="messageContent" class="textarea" id="htmlEditroAreaWriteMessage" title="content">${edit.getMsgContent()}</textarea>
                                <span id="error_content" name="iaisErrorMsg" class="error-msg"></span>
                            </div>
                        </div>

                        <%--<div class="form-group">--%>
                            <%--<iais:field value="Attachments" required="false"/>--%>
                            <%--<div class="document-upload-gp col-xs-8 col-md-8">--%>
                                <%--<div class="document-upload-list">--%>
                                    <%--<div class="file-upload-gp">--%>
                                        <%--<div class="filename fileNameDisplay">--%>
                                            <%--<c:out value="${fileName}"/>--%>
                                        <%--</div>--%>
                                        <%--<input id="selectedFile" name="selectedFile" type="file" multiple="multiple" style="display: none;" aria-label="selectedFile1">--%>
                                        <%--<a class="btn btn-file-upload btn-secondary" href="#">Upload</a>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                                <%--<span id="error_fileUploadError" name="iaisErrorMsg" class="error-msg"></span>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                        <div class="form-group">
                            <iais:field value="Attachments" required="false"/>
                            <div class="document-upload-gp col-xs-8 col-md-8">
                                <div class="document-upload-list">
                                    <div class="file-upload-gp">
                                        <input id="selectFile0" name="selectFile0" type="file" class="iptFile" >
                                        <a class="btn btn-file-upload btn-secondary" href="#">Upload</a>
                                        <ul class="upload-enclosure-ul">
                                        </ul>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <a class="back" id="back"><em class="fa fa-angle-left"></em> Back</a>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <div class="text-right text-center-mobile">
                                <a class="btn btn-primary" id="saveDis" >Continue</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <iais:confirm msg="Content cannot be exceeded 4000 characters"  needCancel="false" callBack="cancel()" popupOrder="support" ></iais:confirm>
        <input hidden value="${id}" id="blastId" >
        <input hidden value="" id="action" name="action">
        <input hidden value="0" id="fileChange" name="fileChange">
        <input hidden value="0" id="filecount" name="filecount">
    </form>
    <%@include file="/WEB-INF/jsp/include/validation.jsp"%>
</div>

<%@include file="/WEB-INF/jsp/include/utils.jsp"%>

<script type="text/javascript">
    $('#saveDis').click(function(){
        var length = tinymce_getContentLength();
        console.log(length)
        if(length > 4000){
            $('#support').modal('show');
        }else{
            $("#action").val("save")
            $("#mainForm").submit();
        }

    });

    function tagConfirmCallbacksupport() {
        $('#support').modal('hide');
    }
    function cancel() {
        $('#support').modal('hide');
    }
    $('#back').click(function(){
        $("#action").val("back");
        $("#mainForm").submit();

    });

    $('#selectedFile').change(function (event) {
        var maxFileSize = 7;
        var error = validateUploadSizeMaxOrEmpty(maxFileSize, 'selectedFile');
        console.log(error)
        console.log($("#selectedFile"))
        if (error == "N"){
            $('#error_fileUploadError').html('The file has exceeded the maximum upload size of '+ maxFileSize + 'M.');
            $("#selectedFile").val('');
            $(".filename").html("");
        }else{
            var files = event.target.files;
            console.log(files)
            $(".filename").html("");
            for (var i = 0; i < files.length; i++) {
                $(".filename").append("<div class='fileNameDisplay'>" + files[i].name + "</div>");
            }
            $("#fileChange").val("1")
            $('#error_fileUploadError').html('');
        }
    });


    $(window).on("load", function(){
        $("#htmlEditroAreaWriteMessage").hide();
        setTimeout("intiTinymce()", 1000);
    });

    function intiTinymce() {
        console.log('init1')
        $("#htmlEditroAreaWriteMessage").show();
        tinymce.init({
            selector: "#htmlEditroAreaWriteMessage",  // change this value according to your HTML
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
            max_chars: 4000,
            setup: function (ed) {
                var content;
                var allowedKeys = [8,13, 46]; // backspace, delete and cursor keys
                ed.on('keydown', function (e) {
                    if (allowedKeys.indexOf(e.keyCode) != -1) return true;
                    if (tinymce_getContentLength() + 1 >= this.settings.max_chars) {
                        e.preventDefault();
                        e.stopPropagation();
                        return false;
                    }
                    return true;
                });
                ed.on('keyup', function (e) {
                    tinymce_updateCharCounter(this, tinymce_getContentLength());
                });
            },
            init_instance_callback: function () { // initialize counter div
                $('#' + this.id).prev().append('<div class="char_count" style="text-align:right"></div>');
                tinymce_updateCharCounter(this, tinymce_getContentLength());
            },
            paste_preprocess: function (plugin, args) {
                var editor = tinymce.get(tinymce.activeEditor.id);
                var len = editor.contentDocument.body.innerText.length;
                var text = $(args.content).text();

                if (tinymce_getContentLength() > editor.settings.max_chars) {
                    $('#support').modal('show');
                    args.content = '';
                } else {
                    tinymce_updateCharCounter(editor, len + text.length);
                }
            }
        });

    }

    function tinymce_updateCharCounter(el, len) {
        $('#' + el.id).prev().find('.char_count').text(len + '/' + el.settings.max_chars);
    }
    function removeHTMLTag(str) {
        str = str.replace(/<\/?[^>]*>/g, '');
        str = str.replace(/[\r\n]/g,"");
        return str;
    }

    function tinymce_getContentLength() {
        var count = removeHTMLTag(tinymce.get(tinymce.activeEditor.id).contentDocument.body.innerText).length;
        console.log(count)
        return count;
    }

    $('#selectFile'+$('#filecount')).change(function (event) {
        $('#filecount').val($('#filecount').val()+1)
        var flag = $(this);
        if (!flag[0].files || !flag[0].files[0]) {
            return;
        }
        var name = flag[0].files[0].name;
        var suffix =suffixName(name)[0].toLowerCase();
        var size = flag[0].files[0].size;
        var fr = new FileReader();
        var files = flag[0].files[0];
        fr.readAsDataURL(files);
        fr.onload = function (e) {
            var timeStamp = Math.floor(Math.random() * 10000);
            $(flag).addClass('li' + timeStamp).hide();
            var name = "selectFile"+$('#filecount').val();
            var html = '<input id="'+name+'" name="'+name+'" type="file" class="iptFile" >';
            $('.btn-file-upload').before(html);

            var li;
            if (suffix == '.jpg' || suffix == '.jpeg' || suffix == '.png' || suffix == '.bmp' || suffix == '.gif') { //图片格式
                li =   '<li> <span><i class="fa fa-file-image-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            } else if (suffix == '.doc' || suffix == '.docx') {
                li = '<li><span><i class="fa fa-file-word-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            } else if (suffix == '.xls' || suffix == '.xlsx') {
                li = '<li><span><i class="fa fa-file-excel-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            } else if (suffix == '.ppt' || suffix == '.pptx') {
                li = '<li><span><i class="fa fa-file-pdf-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            }else if (suffix == '.ppt' || suffix == '.pdf') {
                li = '<li><span><i class="fa fa-file-pdf-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            }else if (suffix == '.zip' || suffix == '.rar'|| suffix == '.7z') {
                li = '<li><span><i class="fa fa-file-excel-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            }else if (suffix == '.wav' || suffix == '.mp3'|| suffix == '.aac'|| suffix == '.wma') {
                li = '<li><span><i class="fa fa-file-video-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            }else if (suffix == '.avi' || suffix == '.mp4'|| suffix == '.mov'|| suffix == '.mkv'||suffix == '.rm' || suffix == '.rmvb'|| suffix == '.mpg'|| suffix == '.mpeg') {
                li = '<li><span><i class="fa fa-file-video-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            }else if (suffix == '.txt' ) {
                li = '<li><span><i class="fa fa-file-text-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            }else {
                li = '<li><span><i class="fa fa-file-o">&nbsp;</i>' + name + '</span> <span class="size">(' + parseInt(files.size / 1000) + 'kb)</span> <span class="remove" data-class="li' + timeStamp + '"  onclick="deleteLi(this)">Delete</span> </li>';
            }

            $('.upload-enclosure-ul').append(li);

        };


    });



    function deleteLi(a) {
        $(a).parent().remove();
        var attr = $(a).attr('data-class');
        var newAttr = '.' + attr;
        $('.inspection_item_con_file  ' + newAttr + '').remove();

        if ($('.upload-enclosure-ul li').length != 0) {
            $('.no_file').hide();
        } else {
            $('.no_file').show();
        }
    }



    function suffixName(file_name){
        var result = /\.[^\.]+/.exec(file_name);
        return result;
    }
</script>