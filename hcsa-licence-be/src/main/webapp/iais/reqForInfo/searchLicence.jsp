<%@ page import="com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts" %>
<%@ taglib uri="http://www.ecquaria.com/webui" prefix="webui" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%
    //handle to the Engine APIs
    sop.webflow.rt.api.BaseProcessClass process =
            (sop.webflow.rt.api.BaseProcessClass)request.getAttribute("process");
%>
<webui:setLayout name="iais-intranet"/>
<form method="post" id="mainForm" action=<%=process.runtime.continueURL()%>>
    <%@ include file="/include/formHidden.jsp" %>
    <input type="hidden" name="crud_action_type" value="">
    <input type="hidden" name="crud_action_value" value="">
    <input type="hidden" name="crud_action_additional" value="">
    <div class="main-content">
        <div id="base" class="">

            <!-- Unnamed (Rectangle) -->
            <div id="u1698" class="ax_default label">
                <div id="u1698_div" class=""></div>
                <div id="u1698_text" class="text ">
                    <p><span>&nbsp; Advanced Search Criteria For Licence</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1699" class="ax_default label">
                <div id="u1699_div" class=""></div>
                <div id="u1699_text" class="text ">
                    <p><span>Service Licence Type:</span></p>
                </div>
            </div>

            <!-- Unnamed (Droplist) -->
            <div id="u1700" class="ax_default droplist">
                <select id="u1700_input">
                </select>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1701" class="ax_default label">
                <div id="u1701_div" class=""></div>
                <div id="u1701_text" class="text ">
                    <p><span>Licence No:</span></p>
                </div>
            </div>

            <!-- Unnamed (Text Field) -->
            <div id="u1702" class="ax_default text_field">
                <input id="u1702_input" type="text" value=""/>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1703" class="ax_default label">
                <div id="u1703_div" class=""></div>
                <div id="u1703_text" class="text ">
                    <p><span>Licence Period:</span></p>
                </div>
            </div>

            <!-- Unnamed (Group) -->
            <div id="u1704" class="ax_default" data-left="593" data-top="123" data-width="122" data-height="30">

                <!-- date field (Text Field) -->
                <div id="u1705" class="ax_default text_field" data-label="date field">
                    <input id="u1705_input" type="text" value=""/>
                </div>

                <!-- calendar grid (Group) -->
                <div id="u1706" class="ax_default ax_default_hidden" data-label="calendar grid" style="display:none; visibility: hidden" data-left="0" data-top="0" data-width="0" data-height="0">

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1707" class="ax_default box_1">
                        <div id="u1707_div" class=""></div>
                    </div>

                    <!-- header (Rectangle) -->
                    <div id="u1708" class="ax_default box_1" data-label="header">
                        <div id="u1708_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1709" class="ax_default box_1">
                        <div id="u1709_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1710" class="ax_default box_1">
                        <div id="u1710_div" class=""></div>
                    </div>

                    <!-- grid (Repeater) -->
                    <div id="u1711" class="ax_default" data-label="grid">
                        <script id="u1711_script" type="axure-repeater-template" data-label="grid">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712" class="ax_default box_1 u1712" data-label="grid square">
                                <div id="u1712_div" class="u1712_div"></div>
                            </div>
                        </script>
                        <div id="u1711-1" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-1" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-1_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-2" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-2" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-2_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-3" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-3" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-3_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-4" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-4" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-4_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-5" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-5" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-5_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-6" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-6" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-6_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-7" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-7" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-7_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-8" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-8" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-8_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-9" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-9" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-9_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-10" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-10" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-10_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-11" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-11" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-11_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-12" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-12" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-12_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-13" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-13" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-13_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-14" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-14" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-14_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-15" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-15" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-15_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-16" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-16" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-16_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-17" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-17" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-17_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-18" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-18" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-18_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-19" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-19" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-19_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-20" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-20" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-20_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-21" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-21" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-21_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-22" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-22" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-22_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-23" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-23" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-23_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-24" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-24" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-24_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-25" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-25" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-25_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-26" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-26" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-26_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-27" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-27" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-27_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-28" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-28" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-28_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-29" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-29" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-29_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-30" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-30" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-30_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-31" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-31" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-31_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-32" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-32" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-32_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-33" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-33" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-33_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-34" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-34" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-34_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-35" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-35" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-35_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-36" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-36" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-36_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1711-37" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1712-37" class="ax_default box_1 u1712" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1712-37_div" class="u1712_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1713" class="ax_default box_3">
                        <img id="u1713_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1714" class="ax_default box_3">
                        <img id="u1714_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>
                </div>

                <!-- Unnamed (Group) -->
                <div id="u1715" class="ax_default" data-left="689" data-top="123" data-width="26" data-height="28">

                    <!-- calendar icon (Shape) -->
                    <div id="u1716" class="ax_default icon" data-label="calendar icon">
                        <img id="u1716_img" class="img " src="images/cease_1st_page/calendar_icon_u170.png"/>
                    </div>

                    <!-- current date (Text Field) -->
                    <div id="u1717" class="ax_default text_field ax_default_hidden" data-label="current date" style="display:none; visibility: hidden">
                        <input id="u1717_input" type="text" value=""/>
                    </div>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1718" class="ax_default label">
                <div id="u1718_div" class=""></div>
                <div id="u1718_text" class="text ">
                    <p><span>To</span></p>
                </div>
            </div>

            <!-- Unnamed (Group) -->
            <div id="u1719" class="ax_default" data-left="751" data-top="121" data-width="122" data-height="30">

                <!-- date field (Text Field) -->
                <div id="u1720" class="ax_default text_field" data-label="date field">
                    <input id="u1720_input" type="text" value=""/>
                </div>

                <!-- calendar grid (Group) -->
                <div id="u1721" class="ax_default ax_default_hidden" data-label="calendar grid" style="display:none; visibility: hidden" data-left="0" data-top="0" data-width="0" data-height="0">

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1722" class="ax_default box_1">
                        <div id="u1722_div" class=""></div>
                    </div>

                    <!-- header (Rectangle) -->
                    <div id="u1723" class="ax_default box_1" data-label="header">
                        <div id="u1723_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1724" class="ax_default box_1">
                        <div id="u1724_div" class=""></div>
                    </div>

                    <!-- Unnamed (Rectangle) -->
                    <div id="u1725" class="ax_default box_1">
                        <div id="u1725_div" class=""></div>
                    </div>

                    <!-- grid (Repeater) -->
                    <div id="u1726" class="ax_default" data-label="grid">
                        <script id="u1726_script" type="axure-repeater-template" data-label="grid">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727" class="ax_default box_1 u1727" data-label="grid square">
                                <div id="u1727_div" class="u1727_div"></div>
                            </div>
                        </script>
                        <div id="u1726-1" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-1" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-1_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-2" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-2" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-2_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-3" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-3" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-3_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-4" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-4" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-4_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-5" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-5" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-5_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-6" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-6" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-6_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-7" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-7" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-7_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-8" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-8" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-8_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-9" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-9" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-9_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-10" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-10" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-10_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-11" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-11" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-11_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-12" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-12" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-12_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-13" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-13" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-13_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-14" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-14" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-14_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-15" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-15" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-15_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-16" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-16" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-16_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-17" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-17" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-17_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-18" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-18" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-18_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-19" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-19" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-19_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-20" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-20" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-20_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-21" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-21" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-21_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-22" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-22" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-22_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-23" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-23" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-23_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-24" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-24" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-24_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-25" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-25" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-25_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-26" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-26" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-26_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-27" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-27" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-27_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-28" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-28" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-28_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-29" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-29" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-29_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-30" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-30" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-30_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-31" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-31" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-31_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-32" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-32" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-32_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-33" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-33" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-33_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-34" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-34" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-34_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-35" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-35" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-35_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-36" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-36" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-36_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                        <div id="u1726-37" class="" style="width: 22px; height: 18px;">

                            <!-- grid square (Rectangle) -->
                            <div id="u1727-37" class="ax_default box_1 u1727" data-label="grid square" style="width: 22px; height: 18px; left: 0px; top: 0px;visibility: inherit">
                                <div id="u1727-37_div" class="u1727_div" style="width: 22px; height: 18px;visibility: inherit"></div>
                            </div>
                        </div>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1728" class="ax_default box_3">
                        <img id="u1728_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>

                    <!-- Unnamed (Triangle) -->
                    <div id="u1729" class="ax_default box_3">
                        <img id="u1729_img" class="img " src="images/cease_1st_page/u167.png"/>
                    </div>
                </div>

                <!-- Unnamed (Group) -->
                <div id="u1730" class="ax_default" data-left="847" data-top="121" data-width="26" data-height="28">

                    <!-- calendar icon (Shape) -->
                    <div id="u1731" class="ax_default icon" data-label="calendar icon">
                        <img id="u1731_img" class="img " src="images/cease_1st_page/calendar_icon_u170.png"/>
                    </div>

                    <!-- current date (Text Field) -->
                    <div id="u1732" class="ax_default text_field ax_default_hidden" data-label="current date" style="display:none; visibility: hidden">
                        <input id="u1732_input" type="text" value=""/>
                    </div>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1733" class="ax_default label">
                <div id="u1733_div" class=""></div>
                <div id="u1733_text" class="text ">
                    <p><span>Licence Status:</span></p>
                </div>
            </div>

            <!-- Unnamed (Droplist) -->
            <div id="u1734" class="ax_default droplist">
                <select id="u1734_input">
                </select>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1735" class="ax_default primary_button">
                <div id="u1735_div" class=""></div>
                <div id="u1735_text" class="text ">
                    <p><span>Search</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1736" class="ax_default primary_button">
                <div id="u1736_div" class=""></div>
                <div id="u1736_text" class="text ">
                    <p><span>Clear</span></p>
                </div>
            </div>

            <!-- Unnamed (Rectangle) -->
            <div id="u1737" class="ax_default primary_button">
                <div id="u1737_div" class=""></div>
                <div id="u1737_text" class="text ">
                    <p><span>Back</span></p>
                </div>
            </div>
        </div>
    </div>
</form>