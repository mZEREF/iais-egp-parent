<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/6/5
  Time: 15:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@page import = "java.io.OutputStream" %>
<%@page import = "java.io.BufferedOutputStream" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%
    byte[] content = (byte[])request.getAttribute("processDownloadFileByteData");
    String fileName = (String) request.getAttribute("processDownloadFileName");
    if (content != null && !StringUtils.isEmpty(fileName)){
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Content-Length", "" + content.length);
        out.clear();
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/x-octet-stream");
        ops.write(content);
        ops.flush();
        ops.close();
    }



%>
