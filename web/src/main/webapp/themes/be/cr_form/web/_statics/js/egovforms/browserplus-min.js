/*
 * browserplus.js
 *
 * Provides a gateway between user JavaScript and the BrowserPlus platform
 * 
 * Copyright 2007-2009 Yahoo! Inc. All rights reserved.
 */
BrowserPlus=(typeof BrowserPlus!="undefined"&&BrowserPlus)?BrowserPlus:(function(){var P=false;var F="__browserPlusPluginID";var E="uninitialized";var G=[];var D="application/x-yahoo-browserplus_2";var J,K,L,H,A;return{initWhenAvailable:function(R,S){setTimeout(function(){try{navigator.plugins.refresh(false)}catch(T){}BrowserPlus.init(R,function(U){if(U.success){S(U)}else{BrowserPlus.initWhenAvailable(R,S)}})},1000)},clientSystemInfo:function(){return I()},listActiveServices:function(R){if(R==null||R.constructor!=Function){throw new Error("BrowserPlus.services() invoked without  required callback parameter.")}return N().EnumerateServices(R)},getPlatformInfo:function(){if(N()===null){throw new Error("BrowserPlus.getPlatformInfo() invoked, but init() has not completed successfully.")}return N().Info()},isServiceLoaded:function(S,R){return((S!=undefined&&BrowserPlus.hasOwnProperty(S))&&(R==undefined||BrowserPlus[S].hasOwnProperty(R)))},describeService:function(S,R){if(R==null||R.constructor!=Function){throw new Error("BrowserPlus.services() invoked without  required callback parameter")}if(N()===null){throw new Error("BrowserPlus.describeService() invoked, but init() has not completed successfully.")}return N().DescribeService(S,R)},isServiceActivated:function(S,R){return N().DescribeService(S,(function(){var T=R;return function(U){T(U.success)}})())},isInitialized:function(){return(E==="succeeded")},require:function(S,T){if(T==null||T.constructor!=Function){throw new Error("BrowserPlus.require() invoked without required callback parameter")}var R=function(V){if(V.success){var W=[];for(var U=0;U<V.value.length;U++){if(V.value[U].fullDesc){M({value:V.value[U].fullDesc});W.push({service:V.value[U].service,version:V.value[U].version})}else{BrowserPlus.describeService({service:V.value[U].service,version:V.value[U].version},M);W=V.value}}V.value=W}T(V)};N().RequireService(S,R);return true},init:function(T,S){if(I().browser=="Safari"){navigator.plugins.refresh(false)}var X=null;var W=null;if(S==null){W=T}else{X=T;W=S}if(W==null||W.constructor!=Function){throw new Error("BrowserPlus.init() invoked without  required callback parameter")}if(X===null){X=new Object}if(typeof(X.locale)=="undefined"){X.locale=I().locale}var R=true;if(!X.supportLevel||X.supportLevel==="experimental"){R=(I().supportLevel!=="unsupported")}else{if(X.supportLevel==="supported"){R=(I().supportLevel==="supported")}}if(!R){W({success:false,error:"bp.unsupportedClient"});return}if(E==="succeeded"){W({success:true});return}if(E=="inprogress"){G.push(W);return}E="inprogress";if(typeof(window.onunload)=="undefined"){window.onunload=function(){}}var V=false;if(Q()&&N()!==null){V=true}if(!V){E="uninitialized";W({success:false,error:"bp.notInstalled"});return}else{G.push(W);var U=(function(){var Y=X;return function(){var d=function(g){if(!g.success&&g.error==="bp.switchVersion"&&I().browser!=="Explorer"){var h="application/x-yahoo-browserplus_";h+=g.verboseError;if(h!==D){D=h;setTimeout(function(){try{var j=document.getElementById(F);if(j){document.documentElement.removeChild(j.parentNode)}navigator.plugins.refresh(false)}catch(i){}if(Q()&&N()!==null){setTimeout(function(){N().Initialize(Y,d)},10)}else{d(g)}},10);return}}if(!g.success&&g.error==="bp.switchVersion"){g={success:false,error:"bp.notInstalled",verboseError:"BrowserPlus isn't installed, or couldn't be loaded"}}E=g.success?"succeeded":"uninitialized";var e=G;G=[];for(var f=0;f<e.length;f++){e[f](g)}};try{N().Initialize(Y,d)}catch(b){var Z=I();var c={success:false,error:"bp.notInstalled",verboseError:String(b)};if(Z.browser=="Explorer"){c.error="bp.unsupportedClient"}else{if(Z.browser=="Firefox"){try{navigator.plugins.refresh(true)}catch(a){}}}d(c)}}})();setTimeout(U,0)}},_detectBrowser:function(){return BrowserPlus.clientSystemInfo()}};function Q(){if(B()){return C()}else{return O()}}function O(){var R=navigator.mimeTypes[D];if(typeof(R)!=="object"||typeof(R.enabledPlugin)!=="object"){return false}var S=document.createElement("div");S.style.visibility="hidden";S.style.borderStyle="hidden";S.style.width=0;S.style.height=0;S.style.border=0;S.style.position="absolute";S.style.top=0;S.style.left=0;S.innerHTML='<object type="'+D+'" id="'+F+'" name="'+F+'"></object>';document.documentElement.appendChild(S);P=true;return true}function C(){if(N()!=null){return true}try{var R=document.createElement("object");R.id=F;R.type=D;R.style.display="none";document.body.appendChild(R);document.getElementById(F).Ping();P=true;return true}catch(T){try{document.body.removeChild(R)}catch(S){}}return false}function N(){if(P){return document.getElementById(F)}return null}function B(){return(I().browser=="Explorer")}function M(T){T=T.value;var V=T.name;var R=T.versionString;if(!BrowserPlus[V]){BrowserPlus[V]={};BrowserPlus[V].corelet=V;BrowserPlus[V].version=R}if(!BrowserPlus[V][R]){BrowserPlus[V][R]={};BrowserPlus[V][R].corelet=V;BrowserPlus[V][R].version=R}if(V=="core"){BrowserPlus[V].version=R}if(T.functions){for(var S=0;S<T.functions.length;S++){var W=T.functions[S].name;var U=(function(){var X=W;return function(Z,Y){if(Y==null||Y.constructor!=Function){throw new Error("BrowserPlus."+V+"."+X+"() invoked without  required callback parameter")}return N().ExecuteMethod(V,R,X,Z,Y)}})();BrowserPlus[V][R][W]=U;if(R==BrowserPlus[V].version){BrowserPlus[V][W]=U}}}}function I(){var X=[{string:navigator.vendor,subString:"Apple",identity:"Safari"},{string:navigator.vendor,subString:"Google",identity:"Chrome"},{prop:window.opera,identity:"Opera"},{string:navigator.userAgent,subString:"Firefox",identity:"Firefox"},{string:navigator.userAgent,subString:"MSIE",identity:"Explorer",versionSearch:"MSIE"}];var W=[{string:navigator.platform,subString:"Win",identity:"Windows"},{string:navigator.platform,subString:"Mac",identity:"Mac"},{string:navigator.platform,subString:"Linux",identity:"Linux"}];var U={Mac:{Firefox:"supported",Safari:"supported"},Windows:{Explorer:{"8":"supported","7":"supported","6":"experimental"},Safari:"supported",Firefox:"supported",Chrome:"supported"}};var S;function T(b){for(var Y=0;Y<b.length;Y++){var Z=b[Y].string;var a=b[Y].prop;S=b[Y].versionSearch||b[Y].identity;if(Z){if(Z.indexOf(b[Y].subString)!=-1){return b[Y].identity}}else{if(a){return b[Y].identity}}}return null}function R(Z){var Y=Z.indexOf(S);if(Y==-1){return null}else{return parseFloat(Z.substring(Y+S.length+1))}}function V(){if(U[L]&&U[L][J]){var Y=U[L][J];if(typeof Y==="string"){return Y}else{if(Y[K]){return Y[K]}else{return"unsupported"}}}else{return"unsupported"}}if(!J){J=T(X)||"An unknown browser";K=R(navigator.userAgent)||R(navigator.appVersion)||"an unknown version";L=T(W)||"an unknown OS";if(L==="Mac"&&navigator.userAgent.indexOf("Intel")==-1){A="unsupported"}else{if(navigator.userAgent.indexOf("Firefox/2.0.0")!=-1){A="unsupported"}else{A=V()}}H=navigator.language||navigator.browserLanguage||navigator.userLanguage||navigator.systemLanguage}return{browser:J,version:K,os:L,locale:H,supportLevel:A}}})();if(typeof YAHOO=="undefined"||!YAHOO){var YAHOO={}}if(typeof YAHOO.bp=="undefined"||!YAHOO.bp){YAHOO.bp=BrowserPlus};