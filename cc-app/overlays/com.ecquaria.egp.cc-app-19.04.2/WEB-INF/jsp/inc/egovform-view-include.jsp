<%@page import="ecq.commons.util.EgpcloudPortFactory"%>
<%@ page import="com.ecquaria.cloud.helper.EngineHelper" %>
<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\">
<style type="text/css">

/* jquery-ui-custom.css */
.ui-helper-reset { margin: 0; padding: 0; border: 0; outline: 0; line-height: 1.3; text-decoration: none; font-size: 100%; list-style: none; }
.ui-helper-clearfix { display: inline-block; }
.ui-helper-clearfix { display:block; }
.ui-widget { font-family: Verdana,Arial,sans-serif; font-size: 1.1em; }
.ui-widget-content a { color: #222222; }
.ui-widget-header a { color: #222222; }
.ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default { border: 1px solid #d3d3d3; background: #e6e6e6 url(images/ui-bg_glass_75_e6e6e6_1x400.png) 50% 50% repeat-x; font-weight: normal; color: #555555; }
.ui-state-active, .ui-widget-content .ui-state-active, .ui-widget-header .ui-state-active { border: 1px solid #aaaaaa; background: #ffffff url(images/ui-bg_glass_65_ffffff_1x400.png) 50% 50% repeat-x; font-weight: normal; color: #212121; }
.ui-tabs { position: relative; padding: .2em; zoom: 1; } 
.ui-tabs .ui-tabs-nav { margin: 0; padding: .2em .2em 0; }
.ui-tabs .ui-tabs-nav li { list-style: none; float: left; position: relative; top: 1px; margin: 0 .2em 1px 0; border-bottom: 0 !important; padding: 0; white-space: nowrap; }
.ui-tabs .ui-tabs-nav li a { float: left; padding: .5em 1em; text-decoration: none; }
.ui-tabs .ui-tabs-nav li.ui-tabs-selected { margin-bottom: 0; padding-bottom: 1px; }
.ui-tabs .ui-tabs-panel { display: block; border-width: 0; padding: 1em 1.4em; background: none; }
.ui-tabs .ui-tabs-hide { display: none !important; }


/* jquery-ui-all.css */
.ui-helper-reset { margin: 0; padding: 0; border: 0; outline: 0; line-height: 1.3; text-decoration: none; font-size: 100%; list-style: none; }
.ui-tabs { position: relative; padding: .2em; zoom: 1; } 
.ui-tabs .ui-tabs-nav { margin: 0; padding: .2em .2em 0; }
.ui-tabs .ui-tabs-nav li { list-style: none; float: left; position: relative; top: 0; margin: 1px .2em 0 0; border-bottom: 0; padding: 0; white-space: nowrap; }
.ui-tabs .ui-tabs-nav li a { float: left; padding: .5em 1em; text-decoration: none; }
.ui-tabs .ui-tabs-panel { display: block; border-width: 0; padding: 1em 1.4em; background: none; }

/* template.css */
body { font-family: Arial,Helvetica,sans-serif; font-size: 0.75em; color: rgb(54, 54, 54); background: none repeat scroll 0% 0% rgb(255, 255, 255); }
a { color: rgb(51, 153, 204); text-decoration: none; text-align: left; }
input[type="button"]{ font-weight: bold; font-size: 1em; }
label { font-size: 100%; }
input { font-size: 100%; color: rgb(0, 0, 0); font-family: tahoma; }
a{ color: rgb(31, 146, 255); text-decoration: none; text-shadow: 0px 0px 0px transparent; }
label { font-weight: normal; }
.arrow-prev { background: url("data:image/gif;base64,R0lGODdhAQAYAPMKAN3d3eLi4uHh4d/f3+zs7N7e3uvr6+Dg4O3t7f///+7u7gAAAAAAAAAAAAAAAAAAACwAAAAAAQAYAAAEDjApRAkxOAhx+hhFCIwRADs=") repeat-x scroll 0% 0% rgb(221, 221, 221); }
.arrow-nxt { background: url("data:image/gif;base64,R0lGODdhAQAYAPMKAN3d3eLi4uHh4d/f3+zs7N7e3uvr6+Dg4O3t7f///+7u7gAAAAAAAAAAAAAAAAAAACwAAAAAAQAYAAAEDjApRAkxOAhx+hhFCIwRADs=") repeat-x scroll 0% 0% rgb(221, 221, 221); width: 10px; }
.arrow-prev .img{ background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAYAAABWdVznAAAAcklEQVR42qXPMQrAIAyF4R7TQ7jr5OAlXFxF0HsIpbbqiV6hQzsISUuHf0o+SBYAnyKHow8IIfAKtDaglIIxhgfH3qG1Rs4ZzjkarOt2L5dS4L2ngZQSMcZrudZKgacQAgfmUkrESQRinp6z1k6A7Rc4ASRKrJA/gD8fAAAAAElFTkSuQmCC"); height: 12px; width: 12px; margin: 5px auto; }
.arrow-nxt .img{ background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAwAAAAMCAYAAABWdVznAAAAbUlEQVR42q3ROwrAIBCE4RzTQ9hrZeElbGxF0HsIIebhif5AirTZkBTbzQcz7AS8uu9AKcU4BmLgnMMYw74PRCCEQK0Vay3bevAIYoy01m40zwuPoPd+oZwzWms5SCnJK5VSxKPvsAh47/n10ydeoqyQypS4gQAAAABJRU5ErkJggg=="); height: 12px; width: 12px; margin: 5px auto; }


/* custom.css */
.ui-tabs .ui-tabs-hide { display: none ! important; }
.ui-state-default  { background: none repeat scroll 0% 0% rgb(174, 174, 174); display: inline-block; vertical-align: top; font-size: 12px; color: rgb(144, 140, 140); padding: 5px; text-shadow: none; position: relative; top: 0px; }
li.ui-state-default { background: none repeat scroll 0% 0% rgb(174, 174, 174); }
.ui-state-default a { color: rgb(144, 140, 140); }
.ui-tabs-selected a { color: rgb(31, 146, 255); }
body { line-height: 1.5; }
.ui-tabs { padding: 0px; }
.ui-widget-content { border: 0px none; }
.ui-tabs .ui-tabs-nav { padding: 0px; }
.ui-tabs .ui-widget-header { background: none repeat scroll 0% 0% transparent; border: 0px none; }
.ui-tabs .ui-widget-header .ui-state-active { border: 0px none; }
.ui-tabs .ui-tabs-panel { padding: 0px; }
.ui-tabs .ui-state-active a,.ui-tabs .ui-state-active a:link{ color: rgb(255, 198, 0); }
.ui-tabs .ui-tabs-nav li a { padding: 0px; }
.ui-tabs-nav .ui-state-default,.ui-widget-content .ui-tabs-nav .ui-state-default,.ui-widget-header .ui-state-default { background: none repeat scroll 0px 0px rgb(164, 164, 164); font-weight: bold; z-index: 1; border-radius: 0px; }
.ui-helper-reset { line-height: 18px; }
.ui-tabs .ui-tabs-nav li { padding: 3px 15px; }
.ui-state-default a,.ui-state-default a:link{ color: rgb(239, 239, 239); }
.ui-widget-header .ui-state-default { border: 0px none; color: rgb(31, 146, 255); }
.ui-tabs .ui-tabs-nav li { margin: 0px 5px 1px 0px; }
div.control label.control-font-normal { color: rgb(115, 115, 115); font-family: Arial,Helvetica,sans-serif; font-size: 13px; font-style: normal; font-weight: normal; line-height: 18px; }
div.control label.control-font-label { color: rgb(115, 115, 115); font-family: Arial,Helvetica,sans-serif; font-size: 14px; font-style: normal; font-weight: normal; line-height: 20px; }
.sopform.ui-tabs .ui-tabs-panel { padding: 0px; }
.ui-tabs-nav  .ui-state-hover a:hover {	color: #ffc600; text-decoration: none; }

/* default.css */
.error_placements { font-family: Tahoma,Arial,sans-serif; font-size: 11px; color: red; }
html,body { margin: 0px; padding: 0px; }

/* sopform.form.css */
.control { margin: 3px 0px; overflow: auto; padding: 0px; }
.control.control-caption-horizontal { overflow-y: hidden; }
.section { border: 1px solid rgb(224, 224, 224); margin: 5px 0px; overflow: hidden; }
.control-grid { width: 100%; empty-cells: show; border-collapse: collapse; }
.control-grid td { vertical-align: top; }
.repeatable-row .control-grid > tbody > tr > td{ border: 0px solid rgb(96, 96, 96); height: 20px; }
.repeatable-row .control-grid td.rownum { padding: 2px 5px; }
.repeatable-row .control-grid tr.first td { padding: 2px 5px; background-color: rgb(224, 224, 224); }
.control-input { padding-left: 5px; }
label.control-input { display: block; padding-top: 2px; }
.section,.repeatable-row { min-height: 30px; min-width: 30px; }
.page { padding: 0px; }
.control-caption-horizontal .control-label-span { float: left; max-width: 350px; width: 90%; }
div.form-tab-panel { overflow-x: auto; overflow-y: hidden; background-color: transparent; }
.ui-tabs-nav * { font-family: Tahoma,sans-serif; font-size: 11px; }
.control-caption-horizontal .control-label-span { display: inline-block; }
.repeatable-row .control-label-span { display: none; }
.sopform { border: 1px solid rgb(47, 103, 177); }
.repeatable-section-header { background-color: rgb(204, 204, 255); height: 20px; line-height: 20px; position: relative; }
.repeatable-section-item { position: relative; }
.ui-tabs .ui-tabs-nav li.ui-state-default { border-right: 1px solid rgb(221, 223, 255); color: rgb(65, 64, 64); font-weight: bold; height: 26px; padding: 5px 35px 0px; text-shadow: none; }
.ui-tabs .ui-tabs-nav li.ui-tabs-selected { background-color: rgb(221, 221, 221); background-image: url("data:image/gif;base64,R0lGODdhAQAYAPMKAN3d3eLi4uHh4d/f3+zs7N7e3uvr6+Dg4O3t7f///+7u7gAAAAAAAAAAAAAAAAAAACwAAAAAAQAYAAAEDjApRAkxOAhx+hhFCIwRADs="); background-repeat: repeat-x; border-top-left-radius: 0px; border-top-right-radius: 0px; color: rgb(65, 64, 64); font-weight: bold; height: 26px; padding: 5px 35px 0px; margin-left: 1px ! important; }
.design-panel .control-grid > tr > td:hover,.design-panel .control-grid > tbody > tr > td:hover { background: none repeat scroll 0% 0% rgb(237, 237, 237); }

/* custom.css */
.form-inner-content { clear: both; }
#formPanel{ display: none; }
#formPanel .page-tab { background: none repeat scroll 0px 0px rgb(144, 137, 137); border-bottom: 1px solid rgb(187, 187, 187); border-top: 1px solid rgb(221, 221, 221); height: 30px; margin-top: 1px; }
#formPanel .page-tab ul li { display: inline-block; margin: 0px 1px 0px 0px; }
.ui-corner-all { border-radius: 0px; }
.sopform { border: 0px none; }
#formPanel { display: none; }
input[type="button"] { background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAAbCAIAAAAyOnIjAAAAOElEQVQI11WMsQ0AIBACDaM6qa1bOIILcFj4FhaEgxBaH2iuLeBTQNjFLrbyui9bKc7b10dyvcEBecdOVVSS3qUAAAAASUVORK5CYII=") repeat-x scroll 0px 0px rgb(7, 105, 176); border: 1px solid rgb(0, 137, 168); border-radius: 5px; color: rgb(255, 255, 255); font-size: 1em; font-weight: bold; padding: 5px; text-align: center; }
.ui-tabs-nav .ui-corner-top { border-top-left-radius: 0px; border-top-right-radius: 0px; }
.page-tab { height: 36px; overflow: hidden; position: relative; }
.page-tab ul { clear: both; position: absolute; width: 20000em; }
.arrow-nxt { z-index: 9999; padding: 6px 0px 0px; display: block; position: absolute; right: 0px; top: 0px; height: 27px; width: 2%; text-align: center; }
.arrow-prev { height: 27px; top: 0px; padding: 6px 0px 0px; display: block; position: absolute; width: 2%; text-align: center; }
.arrow-prev img { left: 0px; }
.arrow-nxt img { right: 0px; }
.control-caption-horizontal .control-label-span { margin: 5px 0px; text-align: right; }
.control-caption-horizontal .control-label-span label { color: rgb(45, 61, 133); font-weight: bold; font-size: 12px; font-family: Arial,Helvetica,sans-serif; font-style: normal; line-height: 18px; }
.control-caption-horizontal .control-input-span { margin: 4px 0px; }
.control-caption-horizontal .control-input-span { display: inline-block; }
.control-caption-horizontal .control-input-span .normal-indicator { padding: 0px 0px 0px 16px; width: 90%; }
.section-header { background: none repeat scroll 0px 0px rgb(170, 170, 170); padding: 5px 10px; }
.repeatable-section-header { background: none repeat scroll 0px 0px rgb(170, 170, 170); padding: 5px 10px; }
.section-header > label { color: rgb(255, 255, 255); font-size: 14px; font-weight: bold; text-align: left; vertical-align: middle; }
.repeatable-section-header > label { font-size: 14px; font-weight: bold; text-align: left; vertical-align: middle; color: rgb(255, 255, 255); }
.tab-panel { position: relative; height: 34px; display: block; }
#formPanel .page-tab { display: block; position: absolute; width: 96%; left: 2%; }
.arrow-nxt { display: block; position: absolute; right: 0px; top: 0px; height: 27px; width: 2%; }
.section table.control-grid > tbody > tr:nth-child(2n) { background-color: rgb(245, 245, 245); }
.section table.control-grid > tbody > tr:nth-child(2n+1) { background-color: rgb(236, 236, 236); }
.section table.control-grid > tbody > tr { }
.ui-state-active,.ui-widget-content .ui-state-active,.ui-widget-header .ui-state-active { background: none repeat scroll 0px 0px rgb(224, 237, 246); }
.ui-state-active a,.ui-state-active a:link { color: rgb(31, 146, 255); font-weight: bold; }
.page { margin: 5px; padding: 5px; background: none repeat scroll 0px 0px rgb(245, 245, 245); border: 1px solid rgb(227, 227, 227); }
.repeatable-row table.control-grid > tbody > tr:nth-child(2n) { background-color: rgb(242, 242, 242); border: 1px solid rgb(204, 204, 204); }
.repeatable-row table.control-grid > tbody > tr:nth-child(2n+1) { background-color: rgb(230, 230, 230); border: 1px solid rgb(204, 204, 204); }
.repeatable-row table.control-grid > tbody > tr { border: 1px solid rgb(204, 204, 204); }
.table_header { background: none repeat scroll 0px 0px transparent; color: rgb(61, 48, 57); font-size: 12px; font-weight: bold; padding: 5px 5px 5px 0px; text-align: left; }
.table_header td { padding: 5px 10px; }
.repeatable-row table.control-grid .control .control-input-span div.normal-indicator { padding: 0px; }
.control-caption-horizontal .label-control { float: none; }
.section > table { margin: 5px; }
.section .control-grid { width: 99%; }
.error_placements { margin-left: 10px; }
.control-input-span { float: left; }
.repeatable-section-item:nth-child(2n) { background-color: rgb(242, 242, 242); border: 1px solid rgb(230, 230, 230); }
.repeatable-section,.repeatable-row,.section { margin-bottom: 15px; }
.control-set-alignment.control-label-span { width: 90%; }
.control-set-alignment.control-input-span { width: 90%; }
.control-caption-horizontal .control-set-alignment.control-label-span { width: 48%; }
.control-caption-horizontal .control-set-alignment.control-input-span { width: 48%; }
.repeatable-row .control-label-span { width: 90%; }
.repeatable-row .control-input-span { width: 90%; }
.control-grid { margin-bottom: 3px; }

.rs-pagination { position: absolute; right: 0;}
.pagination { float: right; margin: 0; padding: 0 10px; text-align: right; position: relative; width: 300px; display: inline-block; font-size: 0.917em; font-weight: bold; color: #666; }

.pagination a, .pagination span {
    background: url("data:img/jpg;base64,Qk06CgAAAAAAAIoAAAB8AAAAFAAAAB8AAAABACAAAAAAALAJAAAAAAAAAAAAAAAAAAAAAAAA/wAAAAD/AAAAAP8AAAAA/0JHUnMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAz8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/83Nzf/Nzc3/zc3N/87Ozv/Ozs7/zs7O/87Ozv/Ozs7/zs7O/87Ozv/Ozs7/zs7O/87Ozv/Ozs7/zs7O/87Ozv/Ozs7/zs7O/87Ozv/Ozs7/zs7O/87Ozv/Ozs7/z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//R0dH/0dHR/9HR0f/R0dH/0dHR/9HR0f/R0dH/0dHR/9HR0f/R0dH/0dHR/9HR0f/R0dH/0dHR/9HR0f/R0dH/0dHR/9HR0f/R0dH/0dHR/9PT0//T09P/09PT/9PT0//T09P/09PT/9PT0//T09P/09PT/9PT0//T09P/09PT/9PT0//T09P/09PT/9PT0//T09P/09PT/9PT0//T09P/1dXV/9XV1f/V1dX/1dXV/9XV1f/V1dX/1dXV/9XV1f/V1dX/1dXV/9XV1f/V1dX/1dXV/9XV1f/V1dX/1dXV/9XV1f/V1dX/1dXV/9XV1f/X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9nZ2f/Z2dn/2dnZ/9nZ2f/Z2dn/2dnZ/9nZ2f/Z2dn/2dnZ/9nZ2f/Z2dn/2dnZ/9nZ2f/Z2dn/2dnZ/9nZ2f/Z2dn/2dnZ/9nZ2f/Z2dn/29vb/9vb2//b29v/29vb/9vb2//b29v/29vb/9vb2//b29v/29vb/9vb2//b29v/29vb/9vb2//b29v/29vb/9vb2//b29v/29vb/9vb2//d3d3/3d3d/93d3f/d3d3/3d3d/93d3f/d3d3/3d3d/93d3f/d3d3/3d3d/93d3f/d3d3/3d3d/93d3f/d3d3/3d3d/93d3f/d3d3/3d3d/+Dg4P/g4OD/4ODg/+Dg4P/g4OD/4ODg/+Dg4P/g4OD/4ODg/+Dg4P/g4OD/4ODg/+Dg4P/g4OD/4ODg/+Dg4P/g4OD/4ODg/+Dg4P/g4OD/4uLi/+Li4v/i4uL/4uLi/+Li4v/i4uL/4uLi/+Li4v/i4uL/4uLi/+Li4v/i4uL/4uLi/+Li4v/i4uL/4uLi/+Li4v/i4uL/4uLi/+Li4v/l5eX/5eXl/+Xl5f/l5eX/5eXl/+Xl5f/l5eX/5eXl/+Xl5f/l5eX/5eXl/+Xl5f/l5eX/5eXl/+Xl5f/l5eX/5eXl/+Xl5f/l5eX/5eXl/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/6urq/+rq6v/q6ur/6urq/+rq6v/q6ur/6urq/+rq6v/q6ur/6urq/+rq6v/q6ur/6urq/+rq6v/q6ur/6urq/+rq6v/q6ur/6urq/+rq6v/s7Oz/7Ozs/+zs7P/s7Oz/7Ozs/+zs7P/s7Oz/7Ozs/+zs7P/s7Oz/7Ozs/+zs7P/s7Oz/7Ozs/+zs7P/s7Oz/7Ozs/+zs7P/s7Oz/7Ozs/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//8fHx//Hx8f/x8fH/8fHx//Hx8f/x8fH/8fHx//Hx8f/x8fH/8fHx//Hx8f/x8fH/8fHx//Hx8f/x8fH/8fHx//Hx8f/x8fH/8fHx//Hx8f/z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//Pz8//z8/P/8/Pz//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9fX1//X19f/19fX/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/9/f3//f39//39/f/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//7+/v/+/v7//v7+//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/+fn5//n5+f/5+fn/0tLS/9LS0v/S0tL/0tLS/9LS0v/S0tL/0tLS/9LS0v/S0tL/0tLS/9LS0v/S0tL/0tLS/9LS0v/S0tL/0tLS/9LS0v/S0tL/0tLS/9LS0v8=") repeat-x scroll 0 0 #ccc;
    border: 1px solid #aaa9a9; color: #333; margin: 0 0 0 3px; padding: 0 7px; font-family: inherit; font-size: 14px; font-style: inherit; font-weight: inherit;
}

.pagination .current {
    background: url("data:img/jpg;base64,Qk1SAQAAAAAAAIoAAAB8AAAAAQAAADIAAAABACAAAAAAAMgAAAAAAAAAAAAAAAAAAAAAAAAA/wAAAAD/AAAAAP8AAAAA/0JHUnMAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAtbW2/7W1tf+1tbX/tbW1/7S0tP+0tLT/s7O0/7Oys/+ysrL/srKy/7Kxsf+wsLD/sLCw/6+wr/+urq//rq6u/62trf+sra3/rKys/6urq/+rq6v/qqqq/6mpqf+oqKj/qKio/6enp/+mpqb/pqam/6WlpP+kpKT/o6Oj/6Ojo/+ioqP/oaGh/6Ghof+goaD/n5+g/56fn/+enp7/np6e/52dnf+dnJ3/nJyc/5ycnP+cm5v/mpua/5qbmv+ZmZr/mZmZ/5mZmf8=") repeat-x scroll 0 0 #999999;
}

.repeatable-row table.control-grid > tbody > tr:nth-child(2n+1) {
    background-color: #e6e6e6;
    border: 1px solid #cccccc;
}

.repeatable-row table.control-grid > tbody > tr:nth-child(2n) {
    background-color: #f2f2f2;
    border: 1px solid #cccccc;
}

.page{
    font-size: 13px;
}

html, body, div, span, object, iframe, h1, h2, h3, h4, h5, h6, p, blockquote, pre, a, abbr, acronym, address, code, del, dfn, em, img, q, dl, dt, dd, ol, ul, li, fieldset, form, label, legend, table, caption, tbody, tfoot, thead, tr, th, td {
    font-family: inherit;
    font-size: 100%;
    font-style: inherit;
    font-weight: inherit;
}

</style>

<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.1.4.1.js"></script>
<script type="text/javascript" src="<%=EngineHelper.getResourcePath()%>/javascripts/egov/jquery-migrate.3.0.0.js"></script>
<script type="text/javascript" src="<%=EgpcloudPortFactory.servletRoot %>/javascripts/sop/jquery-ui-custom.min.js"></script>
<script type="text/javascript" src="<%=EgpcloudPortFactory.servletRoot %>/javascripts/jquery.pagination.js"></script>



<script type="text/javascript">

/**
 * @license 
 * jQuery Tools @VERSION Scrollable - New wave UI design
 * 
 * NO COPYRIGHTS OR LICENSES. DO WHAT YOU LIKE.
 * 
 * http://flowplayer.org/tools/scrollable.html
 *
 * Since: March 2008
 * Date: @DATE 
 */
(function($) { 

	// static constructs
	$.tools = $.tools || {version: '@VERSION'};
	
	$.tools.scrollable = {
		
		conf: {	
			activeClass: 'active',
			circular: false,
			clonedClass: 'cloned',
			disabledClass: 'disabled',
			easing: 'swing',
			initialIndex: 0,
			item: null,
			items: '.items',
			keyboard: true,
			mousewheel: false,
			next: '.next',   
			prev: '.prev', 
			speed: 400,
			vertical: false,
			touch: true,
			wheelSpeed: 0
		} 
	};
					
	// get hidden element's width or height even though it's hidden
	function dim(el, key) {
		var v = parseInt(el.css(key), 10);
		if (v) { return v; }
		var s = el[0].currentStyle; 
		return s && s.width && parseInt(s.width, 10);	
	}

	function find(root, query) { 
		var el = $(query);
		return el.length < 2 ? el : root.parent().find(query);
	}
	
	var current;		
	
	// constructor
	function Scrollable(root, conf) {   
		
		// current instance
		var self = this, 
			 fire = root.add(self),
			 itemWrap = root.children(),
			 index = 0,
			 vertical = conf.vertical;
				
		if (!current) { current = self; } 
		if (itemWrap.length > 1) { itemWrap = $(conf.items, root); }
		
		// methods
		$.extend(self, {
				
			getConf: function() {
				return conf;	
			},			
			
			getIndex: function() {
				return index;	
			}, 

			getSize: function() {
				return self.getItems().size();	
			},

			getNaviButtons: function() {
				return prev.add(next);	
			},
			
			getRoot: function() {
				return root;	
			},
			
			getItemWrap: function() {
				return itemWrap;	
			},
			
			getItems: function() {
				return itemWrap.children(conf.item).not("." + conf.clonedClass).not(':hidden');	
			},
							
			move: function(offset, time) {
				return self.seekTo(index + offset, time);
			},
			
			next: function(time) {
				return self.move(1, time);	
			},
			
			prev: function(time) {
				return self.move(-1, time);	
			},
			
			begin: function(time) {
				return self.seekTo(0, time);	
			},
			
			end: function(time) {
				return self.seekTo(self.getSize() -1, time);	
			},	
			
			focus: function() {
				current = self;
				return self;
			},
			
			addItem: function(item) {
				item = $(item);
				
				if (!conf.circular)  {
					itemWrap.append(item);
				} else {
					itemWrap.children("." + conf.clonedClass + ":last").before(item);
					itemWrap.children("." + conf.clonedClass + ":first").replaceWith(item.clone().addClass(conf.clonedClass)); 						
				}
				
				fire.trigger("onAddItem", [item]);
				return self;
			},
			
			
			/* all seeking functions depend on this */		
			seekTo: function(i, time, fn) {	
				
				// ensure numeric index
				if (!i.jquery) { i *= 1; }
				
				// avoid seeking from end clone to the beginning
				if (conf.circular && i === 0 && index == -1 && time !== 0) { return self; }
				
				// check that index is sane				
				if (!conf.circular && i < 0 || i > self.getSize() || i < -1) { return self; }
				
				var item = i;
			
				if (i.jquery) {
					i = self.getItems().index(i);	
					
				} else {
					item = self.getItems().eq(i);
				}  
				
				// onBeforeSeek
				var e = $.Event("onBeforeSeek"); 
				if (!fn) {
					fire.trigger(e, [i, time]);				
					if (e.isDefaultPrevented() || !item.length) { return self; }			
				}  
	
				var props = vertical ? {top: -item.position().top} : {left: -item.position().left};  
				
				index = i;
				current = self;  
				if (time === undefined) { time = conf.speed; }   
				
				itemWrap.animate(props, time, conf.easing, fn || function() { 
					fire.trigger("onSeek", [i]);		
				});	 
				
				return self; 
			}					
			
		});
				
		// callbacks	
		$.each(['onBeforeSeek', 'onSeek', 'onAddItem'], function(i, name) {
				
			// configuration
			if ($.isFunction(conf[name])) { 
				$(self).bind(name, conf[name]); 
			}
			
			self[name] = function(fn) {
				if (fn) { $(self).bind(name, fn); }
				return self;
			};
		});  
		
		// circular loop
		if (conf.circular) {
			
			var cloned1 = self.getItems().slice(-1).clone().prependTo(itemWrap),
				 cloned2 = self.getItems().eq(1).clone().appendTo(itemWrap);
				
			cloned1.add(cloned2).addClass(conf.clonedClass);
			
			self.onBeforeSeek(function(e, i, time) {

				
				if (e.isDefaultPrevented()) { return; }
				
				/*
					1. animate to the clone without event triggering
					2. seek to correct position with 0 speed
				*/
				if (i == -1) {
					self.seekTo(cloned1, time, function()  {
						self.end(0);		
					});          
					return e.preventDefault();
					
				} else if (i == self.getSize()) {
					self.seekTo(cloned2, time, function()  {
						self.begin(0);		
					});	
				}
				
			});
			
			// seek over the cloned item
			self.seekTo(0, 0, function() {});
		}
		
		// next/prev buttons
		var prev = find(root, conf.prev).click(function() { self.prev(); }),
			 next = find(root, conf.next).click(function() { self.next(); });	
		
		if (!conf.circular && self.getSize() > 1) {
			
			self.onBeforeSeek(function(e, i) {
				setTimeout(function() {
					if (!e.isDefaultPrevented()) {
						prev.toggleClass(conf.disabledClass, i <= 0);
						next.toggleClass(conf.disabledClass, i >= self.getSize() -1);
					}
				}, 1);
			}); 
			
			if (!conf.initialIndex) {
				prev.addClass(conf.disabledClass);	
			}
		}
			
		// mousewheel support
		if (conf.mousewheel && $.fn.mousewheel) {
			root.mousewheel(function(e, delta)  {
				if (conf.mousewheel) {
					self.move(delta < 0 ? 1 : -1, conf.wheelSpeed || 50);
					return false;
				}
			});			
		}
		
		// touch event
		if (conf.touch) {
			var touch = {};
			
			itemWrap[0].ontouchstart = function(e) {
				var t = e.touches[0];
				touch.x = t.clientX;
				touch.y = t.clientY;
			};
			
			itemWrap[0].ontouchmove = function(e) {
				
				// only deal with one finger
				if (e.touches.length == 1 && !itemWrap.is(":animated")) {			
					var t = e.touches[0],
						 deltaX = touch.x - t.clientX,
						 deltaY = touch.y - t.clientY;
	
					self[vertical && deltaY > 0 || !vertical && deltaX > 0 ? 'next' : 'prev']();				
					e.preventDefault();
				}
			};
		}
		
		if (conf.keyboard)  {
			
			$(document).bind("keydown.scrollable", function(evt) {

				// skip certain conditions
				if (!conf.keyboard || evt.altKey || evt.ctrlKey || $(evt.target).is(":input")) { return; }
				
				// does this instance have focus?
				if (conf.keyboard != 'static' && current != self) { return; }
					
				var key = evt.keyCode;
			
				if (vertical && (key == 38 || key == 40)) {
					self.move(key == 38 ? -1 : 1);
					return evt.preventDefault();
				}
				
				if (!vertical && (key == 37 || key == 39)) {					
					self.move(key == 37 ? -1 : 1);
					return evt.preventDefault();
				}	  
				
			});  
		}
		
		// initial index
		if (conf.initialIndex) {
			self.seekTo(conf.initialIndex, 0, function() {});
		}
	} 

		
	// jQuery plugin implementation
	$.fn.scrollable = function(conf) { 
			
		// already constructed --> return API
		var el = this.data("scrollable");
		if (el) { return el; }		 

		conf = $.extend({}, $.tools.scrollable.conf, conf); 
		
		this.each(function() {			
			el = new Scrollable($(this), conf);
			$(this).data("scrollable", el);	
		});
		
		return conf.api ? el: this; 
		
	};
			
	
})(jQuery);

</script>

<script type="text/javascript">

function inIframe() {
	try {
		return window.self !== window.top;
	} catch (e) {
		return true;
	}
}

if (inIframe()) {
	$(window).resize(adjustPage);
}
var buttonsCount = 0;
var wizardMode = false;
var pagesCount = 0;
var lessHeight = 27;
var timerid;

var __respCSSItems = {};
var __respCSSNodes = {};
function __addRespCSSItem(width, selector, extra) {
	if (extra) {
		var stylesheet = '<style type="text/css">\n';
		stylesheet += selector + ' table, ';
		stylesheet += selector + ' thead, ';
		stylesheet += selector + ' tbody, ';
		stylesheet += selector + ' th, ';
		stylesheet += selector + ' tr, ';
		stylesheet += selector + ' td {display: block}\n';
		stylesheet += selector + ' tr.table_header {position: absolute; top: -9999px; left: -9999px;}\n';
		// stylesheet += selector + ' tr {border: 1px solid #ccc;}\n';
		stylesheet += selector + ' td {border: none; border-bottom: 1px solid #eee; position: relative; padding-left: 50%; height: auto;}\n';
		stylesheet += selector + ' td:before {position: absolute; top: 6px; left: 6px; width: 45%; padding-right: 10px; white-space: nowrap;}\n';
		stylesheet += selector + ' td:nth-of-type(1):before { content: "No."; }\n';
		for (var i = 1; i <= extra.length; i++) {
			stylesheet += selector + ' td:nth-of-type(' + (i + 1) + '):before { content: "' + extra[i - 1].replace(/"/g, '\\"') + '"; }\n';
		}
		stylesheet += selector + ' td:nth-of-type(' + (extra.length + 2) + '):before { content: ""; }\n';
		stylesheet += '</style>';

		var val = __respCSSNodes[width];
		if (val)
			val.push(stylesheet);
		else
			__respCSSNodes[width] = [stylesheet];
	} else {
		var val = __respCSSItems[width];
		if (val)
			val.push(selector);
		else
			__respCSSItems[width] = [selector];
	}
}

function adjustPage() {
	var curWndWidth = $(window).width();
	$.each(__respCSSItems, function(key, val) {
		var $val = $(val);
		var len = $val.length;
		if (curWndWidth >= parseInt(key)) {
			for (var i = 0; i < len; i++) {
				var parent = $($val[i]).parent();
				if (parent.is('.control-caption-horz-disabled')) {
					parent.removeClass('control-caption-horz-disabled');
					parent.addClass('control-caption-horizontal')
				}
			}
		} else {
			for (var i = 0; i < len; i++) {
				var parent = $($val[i]).parent();
				if (parent.is('.control-caption-horizontal')) {
					parent.removeClass('control-caption-horizontal');
					parent.addClass('control-caption-horz-disabled');
				}
			}
		}
	});

	var $head = $('head');
	$.each(__respCSSNodes, function(key, val) {
		if (curWndWidth >= parseInt(key)) {
			for (var i = 0; i < val.length; i++) {
				$('#rrrcss-' + key + '-' + i).remove();
			}
		} else {
			for (var i = 0; i < val.length; i++) {
				if ($('#rrrcss-' + key + '-' + i).length <= 0) {
					var valItem = val[i];
					var $valItem = $(valItem);
					$head.append($valItem);
					$valItem.attr('id', 'rrrcss-' + key + '-' + i);
				}
			}
		}
	});

	try {
		if (window.parent && window.parent.EGP && window.parent.EGP.Common && window.parent.EGP.Common.getAutoHeightMode() == 1)
			return;
	} catch (e) {
	}

	var tmpHeight = lessHeight;
	if (wizardMode) {
		tmpHeight += 71;
	} else {
		if (buttonsCount > 0)
			tmpHeight += 47;
		if (pagesCount > 1)
			tmpHeight += 41;
	}
	var globalError = $('#global-error');
	if(globalError)
		tmpHeight += globalError.height();
	
    (timerid && clearTimeout(timerid));
    timerid = setTimeout(function () {
    	if($.browser.msie){
			$(".control-area").height($("html").height() - tmpHeight);
		}else{
			$(".control-area").height($(window).height() - tmpHeight);
		}
    }, 10);
}
$(function() {
	if(inIframe())
		pagesCount = $(".control-area").length;
		adjustPage();
	try {
	    if (window !== window.parent && window.parent && window.parent.EGP && window.parent.EGP.Common)
	    	window.parent.EGP.Common.onEgovFormReady(document.documentElement.scrollHeight);
	} catch (e) {}
})

$.fn.sopformTabs = function(){
	var obj ;
	if($(this).parent().is('.page-tab')){
		if(typeof arguments[0] == 'string' && arguments[0] == 'hide'){
			$(this).parents('.tab-panel').hide();
			var args = [];
			for ( var int = 1; int < arguments.length; int++) {
				args.push(arguments[i]);
			}
			obj = $.fn.tabs.apply($(this).parents(".sopform"), args);
		}else if(typeof arguments[0] == 'string' && arguments[0] == 'show'){
			$(this).parents('.tab-panel').show();
			var args = [];
			for ( var int = 1; int < arguments.length; int++) {
				args.push(arguments[i]);
			}
			obj = $.fn.tabs.apply($(this).parents(".sopform"), args);
		}else if(typeof arguments[0] == 'string' && arguments[0] == 'selected'){
			var currentPageId = Form.currentPage.properties.id;
			var idx = Form.getPageIndexByPageId(currentPageId);
			return idx;
		}else{
			obj = $.fn.tabs.apply($(this).parents(".sopform"), arguments);
		}
	}else
		obj = $.fn.tabs.apply($(this).parent(), arguments);
	if(obj.jquery) 
		return $(this);
	else
		return obj;
};

var SOP = {};
SOP.SOPForms = {};
SOP.SOPForms.setupScrollable = function($el, options){
	var __pxToNum = function(px) {
		if (typeof px != 'string') return 0;

		var match = px.match(/^([0-9]+(\.[0-9]+)?)px$/);
		if (!match) return 0;

		return parseFloat(match[1]);
	};

	var __width = function(element) {
		var $el = $(element);
		var w = __pxToNum($el.css('padding-left'));
		w += $el.width();
		w += __pxToNum($el.css('padding-right'));
		return w;
	};
	
	var __find = function(root, query) { 
		var el = $(query);
		return el.length < 2 ? el : root.parent().find(query);
	};
	
	var elWidth = __width($el);
	var childrenWidth = 0;
	$el.children('ul').children('li').each(function(){
		childrenWidth += __width($(this)); 
	});
	if(!options) options = {};
	if(childrenWidth > elWidth){
		$el.scrollable(options);
	}else{
		var $scroller = $el.data("scrollable");
		if($scroller && $scroller.length > 0){
			var root = $scroller.getRoot();
			var nextButton = __find(root, $scroller.getConf().next);
			if(nextButton.length>0) 
				nextButton.unbind();
			var prevButton = __find(root, $scroller.getConf().prev).unbind();
			if(prevButton.length>0) 
				prevButton.unbind();
			$scroller.seekTo(0, 0);
			$el.data("scrollable","");
		}
	}
};

$(document).ready(function(){
	$(".rs-pagination").each(function(){
		var $this = $(this);
		if($this.data("ispagination")) {
			var rs = $this.parents(".repeatable-section");
			
			var num_entries = jQuery('.repeatable-section-item', rs).length;
			var itemSelect = ".repeatable-section-item";
			
			
			$this.pagination(num_entries, {
	            callback: window._pageSelectCallBack,
	            items_per_page: 1,
	            current_page: 0,
	            num_display_entries: 5,
	            next_text: ">>",
	            prev_text: "<<",
	            element: rs,
	            itemSelect: itemSelect
	        });
		}
		
	});
});
	
function _pageSelectCallBack(page_index, jq){
	$(this.itemSelect, this.element).hide();
	$(this.itemSelect + ':eq('+page_index+')', this.element).show();
       
       return false;
}

</script>