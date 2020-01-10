<%@page import = "java.io.OutputStream" %>
<%@page import = "java.io.BufferedOutputStream" %>
<%
    out.print("weqweqweqwe111111111111111111111");
    byte[] content = (byte[])request.getSession().getAttribute("content");
   // String fileName = (String)request.getAttribute("fileName") ;
    String fileName= "test.xml";
    response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes()));
    //response.addHeader("Content-Disposition", "attachment;filename=111.xml");
    response.addHeader("Content-Length", "" + content.length);
    //out.clear();
    OutputStream ops = new BufferedOutputStream(response.getOutputStream());
    response.setContentType("application/x-octet-stream");
    ops.write(content);
    //ops.flush();
    ops.close();
    out.print("weqweqweqwe");
%>