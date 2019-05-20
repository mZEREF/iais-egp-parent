<%@page import="com.ecquaria.cloud.entity.docupload.DocUploadService"%>
<%@page import="com.ecquaria.cloud.entity.svcreg.DocumentSetupService"%>
<%@page import="com.ecquaria.cloud.entity.svcreg.ServiceRegistryService"%>
<%@page import="com.ecquaria.egov.core.helper.SupportedDocHelper"%>
<%@page import="com.ecquaria.egov.core.svcreg.DocumentSetup"%>
<%@page import="com.ecquaria.egov.core.svcreg.EServiceInfo"%>
<%@page import="com.ecquaria.egov.core.svcreg.ServiceRegistry"%>
<%@page import="com.ecquaria.egp.api.EGPCaseHelper"%>
<%@page import="com.ecquaria.egp.core.bat.BATControllerHelper"%>
<%@page import="com.ecquaria.egp.core.bat.GeneratorFactory"%>
<%@page import="com.ecquaria.egp.core.docUpload.DocUpload"%>
<%@page import="com.ecquaria.egp.core.forms.util.FileUploadUtil"%>
<%@page import="com.ecquaria.egp.core.forms.util.entity.FileUploadButton"%>
<%@page import="com.ecquaria.egp.core.helper.EGPCommonHelper"%>
<%@page import="ecq.commons.exception.BaseRuntimeException"%>
<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="ecq.crud.helper.View"%>
<%@page import="org.apache.commons.io.FileUtils"%>
<%@page import="sop.config.ConfigUtil"%>
<%@page import="sop.webflow.eservice.EGPCase"%>
<%@page import="sop.webflow.rt.api.BaseProcessClass"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<%@ page import="com.ecquaria.cloud.RedirectUtil" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="sop.i18n.MultiLangUtil" %>
<%@ page import="com.ecquaria.egov.core.common.constants.AppConstants" %>

<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>

<link href="<%=EngineHelper.getResourcePath()%>/sample/saas-new/assets/css/fileUpload.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath()%>/sample/saas-new/assets/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath()%>/sample/saas-new/assets/css/general-new.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath()%>/sample/saas-new/assets/css/responsive.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath()%>/sample/saas-new/assets/css/sop2.css" rel="stylesheet">
<link href="<%=EngineHelper.getResourcePath()%>/sample/saas-new/assets/css/font-awesome.min.css" rel="stylesheet">
<%
    Logger logger = LoggerFactory.getLogger(this.getClass());
    BaseProcessClass process = (BaseProcessClass)request.getAttribute("process");

    String action = "empty";
    long sopEngineTabRef = 0;

    action = process.runtime.continueURL();
    sopEngineTabRef = process.rtStatus.getTabRef();
    int size = ConfigUtil.getInteger("commons.MultipartRequest.maxPostSize", 10485760);
    String maxSize = EGPCommonHelper.formatFileSizeWithoutDecimal(size);

    boolean embedMode = false;
	if(request.getAttribute("embedMode") != null && request.getAttribute("embedMode").equals("true")) {
		embedMode = true;
	}
    
	String elementID = null;
	String classNames = null;
	
    long batchId;
    String batch_id = null;
	String service_id = null;
	
	boolean canUpload = true;
	boolean canDownload = true;
	boolean canDelete = true;
	
	boolean isUploadOnly = true;
	
	String tagFilter = null;
	String tabRef = null;
	
	ServiceRegistry svc = null;
	
	String buttonText = "Upload All";
	
	
	EGPCase currentCase = process.currentCase;
	
	if(embedMode){
		if(request.getAttribute("tabRef") != null){
			tabRef = (String) request.getAttribute("tabRef");
			sopEngineTabRef = Long.parseLong(tabRef);
			Object obj = request.getSession().getAttribute(tabRef);
			
			if(obj == null || !(obj instanceof EGPCase)){
				throw new BaseRuntimeException("Can't get case for session");
			}else{
				currentCase = (EGPCase) obj;
			}
		}
	}
	
	String[] supportDocTags = EGPCaseHelper.getSupportDocumentFilter(currentCase);
	
	if(embedMode) {
		if(request.getAttribute("batchId") != null) {
			batch_id = (String) request.getAttribute("batchId");
		}
		if(request.getAttribute("serviceId") != null) {
			service_id = (String) request.getAttribute("serviceId");
			svc = ServiceRegistryService.getInstance().retrieveById(service_id);
		}

		if(request.getAttribute("upload") != null && request.getAttribute("upload").equals("false"))  {
			canUpload = false;
		}
		if(request.getAttribute("download") != null && request.getAttribute("download").equals("false"))  {
			canDownload = false;
		}
		if(request.getAttribute("delete") != null && request.getAttribute("delete").equals("false"))  {
			canDelete = false;
		}

		if(request.getAttribute("uploadOnly") != null && request.getAttribute("uploadOnly").equals("false"))  {
			isUploadOnly = false;
		}
		
		if(request.getAttribute("buttonText") != null)  {
			buttonText = (String) request.getAttribute("buttonText");
		}
		if(request.getAttribute("elementID") != null)  {
			elementID = (String) request.getAttribute("elementID");
		}
		if(request.getAttribute("classNames") != null)  {
			classNames = (String) request.getAttribute("classNames");
		}
		if(request.getAttribute("tagFilter") != null) {
			tagFilter = (String) request.getAttribute("tagFilter");
			supportDocTags = SupportedDocHelper.parseTags(tagFilter);
		}

	} else {
		batch_id = EGPCaseHelper.getSupportDocumentBatchId(currentCase);
		svc = BATControllerHelper.getServiceInfo(process);
	}

%>
<div <% if(elementID != null) { out.print("id=" + elementID);} %> class="pop-up upload <%= classNames %>">
    <form action="<%= action%>" name="fileUploadForm" method="post">
        <input type="hidden" name="sopEngineTabRef" value="<%=sopEngineTabRef%>">
        <%=View.outputCrudHiddenAction() %>
        <div class="pop-up text-center">

            <%



                logger.debug("EMBED MODE : " +embedMode);
                logger.debug("BATCH ID : " +batch_id);
                logger.debug("SERVICE ID : " +service_id);


                logger.debug("CAN UPLOAD : " +canUpload);
                logger.debug("CAN DOWNLOAD : " +canDownload);
                logger.debug("CAN DELETE : " +canDelete);

                logger.debug("UPLOAD ONLY : " + isUploadOnly);

                logger.debug("TAG FILTER : " + tagFilter);

                DocUploadService docUploadService = DocUploadService.getInstance();
                DocUpload[] docUploads = null;
                if(batch_id == null) {
                    batchId = GeneratorFactory.generateLongNextSeqNo("SEQ_EGOV_DOCUPLOAD_BATCH");
                    EGPCaseHelper.setSupportDocumentBatchId(currentCase, batchId);
                } else {
                    batchId = Long.parseLong(batch_id);
                    docUploads = docUploadService.retrieveByBatchIdExcludeDelete(batchId);
                }
				
                // Loading document setup
                DocumentSetupService service = DocumentSetupService.getInstance();
                List<DocumentSetup> documentSetup = new ArrayList<DocumentSetup>(); 
        		if(svc != null) {
	            	documentSetup = svc.getDocumentSetups(supportDocTags);
             	}

        		EServiceInfo info = EGPCaseHelper.getEServiceInfo(currentCase);
        		
                if(documentSetup.size() > 0) {
                    for(int k=0;k<documentSetup.size();k++) {
                    	DocumentSetup doc = documentSetup.get(k);
						doc.setLanguage(MultiLangUtil.getSiteLanguage());
            %>
            <div class="content-body fileUploadContainer">
            	<%
            		String mStr = doc.getMandatory();
            		String m = doc.getMandatory();
            		Boolean req = info.getDocRequired(doc.getName());
            		boolean isRequired = false;
            		if(req!=null){
            			if(req){
            				isRequired = true;
            				mStr = "Yes";
            			}else{
            				isRequired = false;
            				mStr = "No";
            			}
            		}else{
            			if(StringHelper.equals("Yes", m)){
            				isRequired = true;
            			}
            		}
                    String mf = "("+MultiLangUtil.translate(request, AppConstants.KEY_TRANSLATION_MODULE_MESSAGE,"supportFileType", "Only support file type")+": "+doc.getFileType()+")";
            		String asterisk = "";
            		if(isRequired){
            			asterisk = "<span style=\"color: #FF0000;\">* </span>";
            		}
            		
//            		String desc = "";
//            		if(!StringHelper.isEmpty(doc.getDescription())){
//            			desc = asterisk + "("+doc.getDescription()+")";
//            		}else{
//            			desc = asterisk;
//            		}
					String desc = "";
					if(!StringHelper.isEmpty(doc.getDescriptionMultiLang())){
						desc = asterisk + "("+doc.getDescriptionMultiLang()+")";
					}else if(!StringHelper.isEmpty(doc.getDescription())){
						desc = asterisk + "("+doc.getDescription()+")";
					}else{
						desc = asterisk;
					}

					String label = "";
					if(!StringHelper.isEmpty(doc.getLabelMultiLang())){
						label = doc.getLabelMultiLang();
					}else if(!StringHelper.isEmpty(doc.getLabel())){
						label = doc.getLabel();
					}else if(!StringHelper.isEmpty(doc.getName())){
						label = doc.getName();
					}
            	%>
				<div class="field" style="vertical-align: top"><label style="width: 100%"><div style="width: 90%;display: inline-block;float: left;"><%= label %></div> <%= desc %> :</label></div>
				<span class="fileDisplayName" style="display:none"><%= label %></span>
                <span class="fileType" style="display:none"><%= doc.getName() %></span>
				<span class="fileFilter" style="display:none"><%= doc.getFileType() %></span>
				<span class="fileMandatory" style="display:none"><%= mStr %></span>



				<div class="control">
					<% if(canUpload) {%>
					<label id="browseFile-<%=doc.getId()%>" class="browseFile" title="<%= doc.getFileType() %>"><i class="fa fa-cloud-upload"></i> <egov-smc:commonLabel>Browse Files</egov-smc:commonLabel></label> <span class="mf"><%=mf %></span>
					<% }%>                    
					<div class="fileList">
                        <%
                            if(docUploads != null) {
                                for(DocUpload docUpload : docUploads) {
                                    if(docUpload.getDocType().equals(doc.getName())) {
                        %>
                        <span class="filename server-site" id="<%= docUpload.getDocId()%>">
					<% if(canDownload) {
						String url = request.getContextPath() + "/process/EGOV/DownloadSupportDoc_EmbedFileUpload?docId="+docUpload.getDocId()+"&tabRef="+process.rtStatus.getTabRef();
						url = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url, request);
					%>
						<a title="Download" href="<%=url%>">
					<% }%>                    		
						<%= docUpload.getDocFilename()%>
					<% if(canDownload) {%>
						</a> 
					<% }%>                    		
		
		(<%= FileUtils.byteCountToDisplaySize(docUpload.getDocFileSize()) %>) 
		<% if(canDelete) {%>
			<a href="javascript:void(0);" class="removeServerFile" title="Delete"><img src="<%=EngineHelper.getResourcePath() %>/_statics/egov-fileupload/images/delete-file.png"></a>
		<% }%>                    		
		
		</span>
                        <%
                                    }
                                }
                            }
                        %>


                    </div>
				</div>
            </div>


            <%
                }
            } else {
            %>
            <div style="padding-bottom:10px;" class="content-body fileUploadContainer">
                <div class="field"><label><egov-smc:commonLabel>Upload your files</egov-smc:commonLabel>:</label></div>
                <span class="fileType" style="display:none"></span>
                <span class="fileMandatory" style="display:none">No</span>
                <div class="control">
					<% if(canUpload) {%>
						<a id="browseFile"class="browseFile"><egov-smc:commonLabel>Browse Files</egov-smc:commonLabel></a><br />
					<% }%>                    
					
                    <div class="fileList">
                        <%
                            if(docUploads != null) {
                                for(DocUpload docUpload : docUploads) {
                        %>
                        <span class="filename" id="<%= docUpload.getDocId()%>">
							<% if(canDownload) {%>
								<a title="Download" href="#" onclick="doDownload('<%=docUpload.getDocId()%>')">
							<% }%>                    
		
								<%= docUpload.getDocFilename()%>
		
							<% if(canDownload) {%>
								</a> 
							<% }%>                    
		
		(<%= FileUtils.byteCountToDisplaySize(docUpload.getDocFileSize()) %>) 
		
		<% if(canDelete) {%>
			<a href="javascript:void(0);" class="removeServerFile"><img src="<%=EngineHelper.getResourcePath() %>/_statics/egov-fileupload/images/delete-file.png"></a>
		<% }%>                    
		
		</span>
                        <%
                                }
                            }
                        %>

                    </div>

                </div>
            </div>

            <%
                }

            %>
        </div>
        <div class="action-button form-group text-center btntop" style="display:none;">
		    <%
		        Map<String, Object> buttons = (Map<String, Object>)request.getAttribute(FileUploadUtil.PARAM_FILE_UPLOAD_BUTTONS);
		        if(buttons!=null){
		            for(String key : buttons.keySet()){
		                FileUploadButton formButton = (FileUploadButton)buttons.get(key);
		                formButton.setCssClass("btn btn-round-lg btn-blue2 btn-st");
		                formButton.setPosition("center");
		                if(formButton != null){
		       				out.print(formButton.generateButton());
		                }
		            }
		        }else if(!embedMode) {
		        	out.print(new FileUploadButton(MultiLangUtil.translate(request, AppConstants.KEY_TRANSLATION_MODULE_LABEL,FileUploadUtil.BUTTON_SUBMIT_DEFAULT_LABEL), "upload").generateButton());
		        }
		    %>
		    
		    <% if(embedMode && canUpload) {%>
				<a class=" red uploadAll"><egov-smc:commonLabel><%= buttonText %></egov-smc:commonLabel></a>
			<% } %>
			<div style="clear: both;"></div>
		</div>
    </form>
</div>
<script>
	var fileExtentionError = "<%=MultiLangUtil.translate(request,AppConstants.KEY_TRANSLATION_MODULE_MESSAGE, "fileExtentionError", "File extension error.")%>";
</script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath() %>/_statics/js/egovforms/browserplus-min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath() %>/_statics/js/egovforms/moxie.min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath() %>/_statics/js/egovforms/plupload.min.js"></script>

<script type="text/javascript">
    // Custom example logic
    var uploaders = [];
    var preDocUploadSuccess = true;
    var uploaderCounter = 0;
    var url = '<%=RedirectUtil.changeUrlToCsrfGuardUrlUrl(request.getContextPath() + "/servlet/fileUpload", request)%>';
    $(function() {
        var sopEngineTabRef = $("input[name='sopEngineTabRef']").val();

        $(".fileUploadContainer").each(function() {
            var browseFile = $(this).find(".browseFile").attr("id");
			var fileDisplayName = $(this).find(".fileDisplayName").text();
            var fileType = $(this).find(".fileType").text();
            var fileFilter = $(this).find('.fileFilter').text();
            var fileMandatory;
            var txt = $(this).find('.fileMandatory').text();
            if(txt=='No'){
            	fileMandatory = false;
            }else{
            	fileMandatory = true;
            };
            
            var uploadOpt = {
                    runtimes : 'gears,html5,flash,silverlight,html4',
                    browse_button : browseFile,
                    //container : container,
                    max_file_size : '<%=maxSize%>',
                	url: url,
                    flash_swf_url : '<egov-core:webURL source="js/egovforms/Moxie.swf"/>',
                    silverlight_xap_url : '<egov-core:webURL source="js/egovforms/Moxie.xap"/>',
                    multipart_params: {
						"fileDisplayName": fileDisplayName,
                        "file_type": fileType,
                        "sopEngineTabRef": sopEngineTabRef,
                        "batch_id": <%= batchId %>
                    },
                    multi_selection: false,
                    mandatory: fileMandatory
                }
            if(!!fileFilter){
            	uploadOpt.filters = [{title : fileType, extensions : fileFilter}]
            }
            var uploader = new plupload.Uploader(uploadOpt);
            
            uploader.init();

            uploader.bind('FilesAdded', function(up, files) {
            	var browseButtonId = up.settings.browse_button.id
                var fileList = $("#"+ browseButtonId).nextAll(".fileList");
            	
            	$.each(up.files, function (i, file) {
            		if(up.files.length > 0 && fileList.find('.server-site').length>0){
                        scrollToCenter();
                        SOP.Common.alert("<egov-smc:message key='needRemove'>You need to remove the file from server first.</egov-smc:message>");
            			up.removeFile(file);
            			return;
            		}
            		
            		if (up.files.length <= 1) {
		
		                $.each(files, function(i, file) {
		                    fileList.append('<span class="filename" id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <a href="#" class="removeFile"><img src="<%=EngineHelper.getResourcePath()%>/_statics/egov-fileupload/images/delete-file.png" /></a></span>');
		                });
		                up.refresh(); // Reposition Flash/Silverlight
            		}else{
            			up.removeFile(file);
            			$('#'+file.id).remove();
            		}
            	});
            	resizeBody();
            	onDocumentChange();
            });
            
            uploader.bind('Error', function(up, err) {
            	var types = up.settings.filters.mime_types;
            	//if(types && types.length>0)
               		//SOP.Common.alert("The Document(" + types[0].title + ") only support types: "+types[0].extensions);
            	//else{
            		preDocUploadSuccess = false;
            		SOP.Common.hideMask();
                    scrollToCenter();
            		SOP.Common.alert(err.message);
            	//}
            });
            
            uploader.bind('FileUploaded', function(up, files, info) {
            	handlerFildUploaded(up, files, info);
            });
            
            uploaders.push(uploader);
        });

        $(".removeFile").live("click", function() {
            var index = $(this).parents(".fileUploadContainer").index();
            var fileId = $(this).parent().attr("id");
            uploaders[index].removeFile(uploaders[index].getFile(fileId));
            $(this).parent().remove();
            resizeBody();
            onDocumentChange();
        });

        $(".removeServerFile").live("click",function() {
            var index = $(this).parents(".fileUploadContainer").index();
            var fileId = $(this).parent().attr("id");
            var $me = $(this);
            scrollToCenter();
            SOP.Common.confirm({"message": '<egov-smc:message key="sureRemove">Are you sure you want to remove the file from server?</egov-smc:message>', func: function(){
				$.ajax('<%=request.getContextPath() %>/servlet/fileDelete', {
	                type: "POST",
	                data: {
	                    index: index,
	                    fileId: fileId
	                }
	            });
	            $me.parent().remove();
			}});
        });
        
        $('.action-button').show();
        
        $('.uploadAll').live('click', function() {
        	doUploadFile();
        });
        
        resizeBody();
        onDocumentChange();
        
        try {
            if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common)
            	window.parent.EGP.Common.onEgovFormReady(Math.max(document.documentElement.scrollHeight, 200));
        } catch (e) {}
    });
	function scrollToCenter(){
		var iframeParentScrollTop=$('#__egovform-iframe',parent.document).offset().top;
		var iframeHeight=document.body.scrollHeight;
		var clientHeight =  window.parent.document.documentElement.clientHeight;
		window.parent.document.documentElement.scrollTop = (iframeHeight-clientHeight)/2+iframeParentScrollTop;
		window.parent.document.body.scrollTop = (iframeHeight-clientHeight)/2+iframeParentScrollTop;
	}
    function doUploadFile(action, options){
    	if(!options) {
    		options = {};
    	} 
    	
    	SOP.Common.showMask();
    	preDocUploadSuccess = true;
    	var needFile = false;
    	var needNames = "";
    	
    	if(options.checkMandatory){
	    	for(var i=0; i<uploaders.length; i++) {
	    		var browseButtonId = uploaders[i].settings.browse_button.id;
	            var fileList = $("#"+ browseButtonId).nextAll(".fileList");
	    		
	    		if(uploaders[i].settings.mandatory && (uploaders[i].files.length == 0 && fileList.find('.server-site').length == 0)){
	    			needFile = true;
	    			
	    			needNames += (needNames?",":"") + uploaders[i].settings.multipart_params.fileDisplayName;
	    		}

	    	}
			if(needFile){
				SOP.Common.alert({
					message: "<egov-smc:message key="plsUploadDoc">Please upload the mandatory document(s)</egov-smc:message>:<br>"+needNames,
					modal: true,
					func: function(){
						$( this ).dialog( "close" );
						try {
							if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common) {
								$('#__egovform-iframe', window.parent.document).height($(document.documentElement).height());
							}
						} catch (e) {
						}
					}
				});
				scrollToCenter();
				try {
					if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common) {
						$('#__egovform-iframe', window.parent.document).height(Math.max($(document.documentElement).height(),$(".ui-dialog").outerHeight(true)));
					}
				} catch (e) {
				}
				SOP.Common.hideMask();
				return;
			}
    	}
    	
        uploaderCounter = 0;
		var hasFile = false;
		if(uploaders.length > 0) {
			for(var i=0; i<uploaders.length; i++) {
				if(uploaders[i].files.length > 0) {
					uploaderCounter++;
					hasFile = true;
					if(!uploaders[i].hasEventListener('UploadComplete')){
						uploaders[i].bind("UploadComplete", function(up) {
							var thisIndex = uploaders.indexOf(up);
							
							if(!preDocUploadSuccess){
								preDocUploadSuccess = true;
								return;
							}
							
							uploaderCounter--;
							for(var j=thisIndex;j<uploaders.length-1;j++) {
								if(uploaders[j+1].files.length>0){
									uploaders[j+1].start();
									break;
								}
							} 
							if(uploaderCounter == 0) {
							    <% if(embedMode && isUploadOnly) {%>
							    SOP.Common.hideMask();						    
							    <%} else {%>
							    doSubmit(action, options);
								//$('form[name="fileUploadForm"]').submit();
							    <%} %>
							}
		                });	
					}
                }
			}
			if(!hasFile){
				SOP.Common.hideMask();
                <%--//SOP.Common.alert('<egov-smc:message>You must at least upload one file.</egov-smc:message>');--%>
                <%--//$('form[name="fileUploadForm"]').submit();--%>
				
			    <% if(embedMode && isUploadOnly) {%>
			    <%} else {%>
				doSubmit(action, options);
			    <%} %>
			}
			
			setTimeout(function() {
				if(uploaders.length > 0) {
					for(var i=0; i<uploaders.length; i++) {
						if(uploaders[i].files.length > 0) {
							uploaders[i].start();
							break;
						}
					}
				}
			}, 1000);
			
		}else{
			//$('form[name="fileUploadForm"]').submit();
			doSubmit(action, options);
		}
    };
    
    function handlerFildUploaded(up, files, info){
    	if(info && info.response){
    		var message = eval("(" + info.response + ")");
    		if(message && message.error && message.error.message){
    			preDocUploadSuccess = false;
    			removeFile(up, files);
    			
				SOP.Common.hideMask();
                <%--//var msgTemp = "<egov-smc:message>{0} uploaded failed, file too large.</egov-smc:message>";--%>
				
				//var msg = msgTemp.replace("{0}", files.name);
                scrollToCenter();
    			SOP.Common.alert(message.error.message);
    		}
    	}
    }
    
    function removeFile(up, files){
    	up.removeFile(files);
    	$('#' + files.id).remove();
    	resizeBody();
    }
    
    function doDownload(docId){
    	options = {};
    	$('[name="crud_action_value"]').val(docId);
    	doSubmit('docDownload',options);
    }
    
    function doSubmit(action, options){
    	$('[name="crud_action_type"]').val(action);
    	var form = $('form[name="fileUploadForm"]');
    	if(options.formTarget && options.formTarget != "null"){
    		form.attr("target", options.formTarget);
    	}
    	$(form).submit();
    }
    $(window.top).resize(function() {
    	resizeBody();
   	});
    function resizeBody(){
    	$formDialogSpace = $(window.top.document).find('#formDialogSpace');
    	if($formDialogSpace.length == 0) {
    		return;
    	}
    	var windowh = $(window.self).height();
    	var dialogh = __height($(window.top.document).find('#formDialogSpace'));
    	var bodyh = __height($('.pop-up').find('.pop-up-body'));
    	var footh = __height($('.action-button').find('button'));
    	//alert("windowh: "+windowh+", bodyh: " + bodyh +", footh: " + footh+", dialogh: " + dialogh);
    	//alert(windowh > (bodyh + footh));
    	if(windowh > (bodyh + footh + 30)){
    		$('.pop-up').find('.action-button').css('margin-top', windowh-footh-bodyh-30);
    		$('.pop-up').find('.action-button').css('margin-bottom', 0);
    	}else {
    		$('.pop-up').find('.action-button').css('margin-top', 0);
    		$('.pop-up').find('.action-button').css('margin-bottom', 0);
    	}
        var outerHeight = $("#formDialogSpace", window.parent.document).height();
        var innerHeight = $('.pop-up').find('.pop-up-body').height()+100;
        
        var thisFrame = $("#formDialogSpace iframe", window.parent.document)
         thisFrame.css("height", Math.max(outerHeight, innerHeight));


    }
    
    
    var timerId;
	window.onDocumentChange = function() {
		(timerId && clearTimeout(timerId));
		timerId = setTimeout(function(){
			if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common)
             	window.parent.EGP.Common.onFrameDocumentChange($("html").height()+28);
		}, 1);
	}
    
    var __height = function(ele){
    	return $(ele).outerHeight();// + parseInt($(ele).css("margin-top"), 0) + parseInt($(ele).css("margin-bottom"), 0);
    }
</script>
