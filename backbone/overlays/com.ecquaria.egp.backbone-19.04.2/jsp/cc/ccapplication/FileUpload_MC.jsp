<%@page import="ecq.commons.helper.StringHelper"%>
<%@page import="ecq.commons.helper.ArrayHelper"%>
<%@page import="com.ecquaria.egp.api.EGPConstants"%>
<%@page import="com.ecquaria.egp.core.bat.BATControllerHelper"%>
<%@page import="com.ecquaria.egov.core.svcreg.ServiceRegistry"%>
<%@page import="com.ecquaria.egp.core.bat.GeneratorFactory"%>
<%@page import="sop.webflow.eservice.EGPCaseData"%>
<%@page import="com.ecquaria.egp.api.EGPCaseHelper"%>
<%@page import="sop.webflow.eservice.EGPCase"%>
<%@page import="sop.webflow.eservice.EGPCaseService"%>
<%@page import="sop.webflow.rt.api.BaseProcessClass"%>

<%@ page import="com.ecquaria.egov.core.svcreg.DocumentSetup" %>
<%@ page import="com.ecquaria.egp.core.docUpload.DocUpload" %>
<%@ page import="org.apache.commons.io.FileUtils" %>
<%@ taglib uri="ecquaria/sop/egov-core" prefix="egov-core"%>
<%@ taglib uri="ecquaria/sop/egov-smc" prefix="egov-smc"%>

<%@ include file="/WEB-INF/jsp/inc/egp-common-include.jsp" %>
<meta content="width=device-width, initial-scale=1.0" name="viewport">
<link href="<%=EgpcloudPortFactory.webContext%>//_statics/css/fileUpload.css" rel="stylesheet" type="text/css" media="all" />
<%
    BaseProcessClass process = (BaseProcessClass)request.getAttribute("process");
	String appNo = (String)request.getAttribute("egov.appNo");
	String languageCode = (String)request.getAttribute("egov.languageCode");

    String action = "empty";
    long sopEngineTabRef = 0;

    action = process.runtime.continueURL();
    sopEngineTabRef = process.rtStatus.getTabRef();
%>
<div class="pop-up">
    <form action="<%= action%>" name="fileUploadForm" method="post">
        <input type="hidden" name="sopEngineTabRef" value="<%=sopEngineTabRef%>">
        <div class="pop-up-body">

            <%
           	 	DocUpload[] docUploads = (DocUpload[])request.getAttribute("egov.docupload");
            	DocumentSetup[] documentSetup = (DocumentSetup[])request.getAttribute("egov.documentSetup");

                if(!ArrayHelper.isEmpty(documentSetup)) {
                    for(DocumentSetup doc : documentSetup) {
                    	//doc.setLanguage(languageCode);
                    	String desc = "";
                    	String m = doc.getMandatory();
                		String mf = "";
                		if(StringHelper.equals("Yes", m)){
                			mf = "<span style=\"color: #FF0000;\">* </span>";
                		}
                		
                	/*TODO
                		if(!StringHelper.isEmpty(doc.getDescriptionMultiLang())){
                			desc = mf + "("+doc.getDescriptionMultiLang()+")";
                		}else{
                			desc = mf;
                		}
                		String label = doc.getLabelMultiLang();
                		if(StringHelper.isEmpty(label)){
                			label = doc.getName();
                		} */
                		desc = mf;
                		String label = doc.getName();
                		
            %>
            <div style="padding-bottom:30px;" class="content-body fileUploadContainer">
                <div class="field" style="vertical-align: top"><label style="width: 100%"><div style="width: 90%;display: inline-block;float: left"><%= label %></div> <%= desc %>:</label></div>
                <span class="fileType" style="display:none"><%= doc.getName() %></span>
				<span class="fileFilter" style="display:none"><%= doc.getFileType() %></span>


                <div class="control" style="position: relative;padding-top: 10px">
                    <div class="fileList" style="display:inline;margin-left:0;">
                        <%
                            if(docUploads != null) {
                                for(DocUpload docUpload : docUploads) {
                                    if(docUpload.getDocType().equals(doc.getName())) {
                        %>
                        <span class="filename" style="display:inline;" id="<%= docUpload.getDocId()%>"><a title="Download" href="<%=request.getContextPath()%>/process/EGPCLOUD/DownloadSupportDocument_MC?docId=<%=docUpload.getDocId()%>&appNo=<%=appNo%>"><%= docUpload.getDocFilename()%></a> (<%= FileUtils.byteCountToDisplaySize(docUpload.getDocFileSize()) %>) </span>
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
            	
            }
            %>
        </div>
    </form>
</div>