<%@page import="java.io.File"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="sop.webflow.process5.util.FileUtil"%>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui"%>
<webui:setLayout name="none"/>
<%!
	private synchronized String getTempFileName() {
		String floder = new SimpleDateFormat("yyyy-MM-dd/").format(new Date());
		File file = new File(FileUtil.TEMP_DIR + floder);
		file.mkdir();
		return floder + "form_embed_" + System.currentTimeMillis() + ".pdf";
	}
%>
<%
	String url = request.getParameter("url");
	String content = request.getParameter("content");
	String tempFilename = getTempFileName();
	java.io.OutputStream outx = new java.io.FileOutputStream(FileUtil.TEMP_DIR + tempFilename);
	new sop.util.DefaultPDFExecutor().exportHTMLToPDF(url, content, outx);
	out.print("<result>");
	out.print(request.getContextPath() + "/sopTemp/" +tempFilename);
	out.print("</result>");
%>