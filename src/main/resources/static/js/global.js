
var global_IP;

$(function(){
    
    global_IP = "http://10.2.60.179:8088/";
});

   /**
 * 获取url参数,para为参数名
 */
function getParameter(para) {
    var url = window.location.href;
    var ret= url.getQuery(para);
    if (ret != null && ret.endWith("#")) {
        ret = ret.substring(0,ret.length-1);
    }
    if (ret == "undefined") {
        return null;
    }
    return ret;
}
String.prototype.getQuery = function(name)
{
var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
var r = this.substr(this.indexOf("\?")+1).match(reg);
if (r!=null) return r[2]; return null;
}
String.prototype.endWith=function(str){     
    var reg=new RegExp(str+"$");     
    return reg.test(this);        
}

/**
 * ifame层页面跳转
 * @param resp
 */
function isNoTLogin(resp){
	if(resp==null){
		alert("您还没有登录，请重新登录哦。");
		top.location.href = "login.html";
		return;
	};
}	


function buildGridParam(type,dataCount,callback){
	var obj = $("select[name='rp']").get(0);
	var index = obj.selectedIndex;
	size = obj.options[index].value;
	var maxSize = Math.ceil(dataCount/size);
	var _page = parseInt($("#page").attr("value"));
	if(type=="first"){
		_page = 0;
	}else if(type=="prev"){
		_page = (_page -1)<=0?0:(_page-1);
	}else if(type=="next"){
		_page = (_page +1)>=maxSize?maxSize:(_page+1);
	}else if(type=="last"){
		_page = maxSize;
	}
	$("#page").attr("value",_page);
	$("#size").attr("value",size);
	callback();
}
