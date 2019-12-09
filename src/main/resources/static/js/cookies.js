//--------------------------cookie的相关操作---------------------
//获取cookie 
function getCookie(sName) 
{ 
    var sRE = "(?:; )?" + sName + "=([^;]*);?";  
    var oRE = new RegExp(sRE);                      
    if (oRE.test(document.cookie)) {  
        return unescape(RegExp["$1"]);  
    } else {  
        return null;
    } 
}
/**
 * 设置cookie
 * @param name
 * @param value
 */
function setCookie(name, value){
    var argv = arguments;
    var argc = arguments.length;
    // var expires = (argc > 2) ? argv[2] : null;
    var expires = new Date();
    expires.setTime(expires.getTime() + 60 * 1000 * 30);//有效期30分钟

    var path = (argc > 3) ? argv[3] : '/';
    var domain = (argc > 4) ? argv[4] : null;
    var secure = (argc > 5) ? argv[5] : false;
     document.cookie = name + "=" + escape (value) +
       ((expires == null) ? "" : ("; expires=" + expires.toGMTString())) +
       ((path == null) ? "" : ("; path=" + path)) +
       ((domain == null) ? "" : ("; domain=" + domain)) +
       ((secure == true) ? "; secure" : "");
}
var seed = 0;

function generateSeed()
{      
     seed = Math.floor(Math.random() * 0x7f) + 1;
     $("key").value = seed;
}
//cookie加密
function encrypt(s)
{      
     var fnl = "", code = 0;
     for(var i = 0; i < s.length; i++){
           code = s.charCodeAt(i) & 0x7f ^ (seed << 7 - i % 8 | seed >> i % 8 | 0x80) & 0xff;
           fnl += code.toString(16);
     }
     return fnl;
}
//cookie解密
function decrypt(s)
{
	var fnl = "", code = 0;
	if(s!=null){		
		for(var i = 0; i < s.length >> 1; i++){
			code = new Number("0x" + s.substr(i * 2, 2));
			fnl += String.fromCharCode((code ^ (seed << 7 - i % 8 | seed >> i % 8 | 0x80)) & 0x7f);
		}
	}
     return fnl;
}

function delCookie(name)
//删除Cookie
{
var expdate = new Date(); 
expdate.setTime(expdate.getTime()- (86400 * 1000 * 1)); 
setCookie(name, "", expdate); 
}
