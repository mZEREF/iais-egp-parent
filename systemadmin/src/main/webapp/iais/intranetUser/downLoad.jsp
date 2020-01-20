<%@page import = "java.io.OutputStream" %>
<%@page import = "java.io.BufferedOutputStream" %>
<%
    byte[] content = (byte[])request.getAttribute("content");
    String fileName = (String)request.getAttribute("fileName") ;
    response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
    response.addHeader("Content-Length", "" + content.length);
    OutputStream ops = new BufferedOutputStream(response.getOutputStream());
    response.setContentType("application/x-octet-stream");
    ops.write(content);
    ops.flush();
    response.getOutputStream().close();
    ops.close();
%>