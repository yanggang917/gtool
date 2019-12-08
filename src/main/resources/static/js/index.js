var tab = null;
var accordion = null;
var tree = null;
$(function ()
{

    var username = getCookie("USERNAME");
    if (username == null){
        loginOut();
    }

    //布局
    $("#layout1").ligerLayout({
        leftWidth: 190,
        height: '100%',
        heightDiff: -34,
        space: 4,
        onHeightChanged: f_heightChanged
    });

    var height = $(".l-layout-center").height();

    //Tab
    $("#framecenter").ligerTab({ height: height });

    //面板
    $("#accordion1").ligerAccordion({ height: height - 24, speed: null });

    $(".l-link").hover(function ()
    {
        $(this).addClass("l-link-over");
    }, function ()
    {
        $(this).removeClass("l-link-over");
    });

    //树
    var indexdata;
    if (decrypt(username) === 'admin'){
        indexdata = adminIndexdata;
    }else {
        indexdata = userIndexdata;
    }
    $("#tree1").ligerTree({
        data : indexdata,
        checkbox: false,
        slide: false,
        nodeWidth: 120,
        attribute: ['nodename', 'url'],
        onSelect: function (node)
        {
            if (!node.data.url) return;
            var tabid = $(node.target).attr("tabid");
            if (!tabid)
            {
                tabid = new Date().getTime();
                $(node.target).attr("tabid", tabid)
            }
            f_addTab(tabid, node.data.text, node.data.url);
        }
    });

    tab = $("#framecenter").ligerGetTabManager();
    accordion = $("#accordion1").ligerGetAccordionManager();
    tree = $("#tree1").ligerGetTreeManager();
    $("#pageloading").hide();

    initUserpasswordFormCheck();

    $("#username").html("欢迎您! "+ decrypt(username));
});
function f_heightChanged(options)
{
    if (tab)
        tab.addHeight(options.diff);
    if (accordion && options.middleHeight - 24 > 0)
        accordion.setHeight(options.middleHeight - 24);
}
function f_addTab(tabid,text, url)
{
    tab.addTabItem({ tabid : tabid,text: text, url: url });
}

var updatePasswordDiv=null;
function openPasswordDialog(){
    $("#userpasswordForm").get(0).reset();
    if(updatePasswordDiv!=null){
        updatePasswordDiv.show();
    }else{
        updatePasswordDiv = $.ligerDialog.open({ target: $("#updatePasswordDiv") ,height:400,width:600});
    }
}
function updatePassword(oldPassword, newPassword){
    var uid = getCookie("UID");
    var data = {"userId":uid, "oldPass":oldPassword, "newPass":newPassword};
    $.ajax({
        url: "user/updatePassword",
        type: "POST",
        cache:false,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function(resp){
            var content = "密码修改成功！";
            if(!resp.success){
                content = "密码修改失败！";
            }
            updatePasswordDiv.hide();
            var tip = $.ligerDialog.tip({ title: '提示信息', content: content,height:150 });
            setTimeout(function () { tip.close(); }, 2000);
        },
        error: function(e,r){
            $.ligerDialog.error('修改密码出错：'+r);
        }
    });
}

function initUserpasswordFormCheck()
{
    $.metadata.setType("attr", "validate");
    var v = $("form").validate({
        debug: true,
        errorPlacement: function (lable, element)
        {
            if (element.hasClass("l-textarea"))
            {
                element.ligerTip({ content: lable.html(), target: element[0] });
            }
            else if (element.hasClass("l-text-field"))
            {
                element.parent().ligerTip({ content: lable.html(), target: element[0] });
            }
            else
            {
                lable.appendTo(element.parents("td:first").next("td"));
            }
        },
        success: function (lable)
        {
            lable.ligerHideTip();
            lable.remove();
        },
        submitHandler: function ()
        {
            var oldPassword = $("#oldPassword").ligerGetTextBoxManager().getValue();
            var username = decrypt(getCookie("USERNAME"));
            var newPassword = $("#newPassword").ligerGetTextBoxManager().getValue();
            var repeatPassword = $("#repeatPassword").ligerGetTextBoxManager().getValue();
            if(newPassword != repeatPassword){
                $.ligerDialog.error('密码与确认密码不相符合！请重新输入。');
                $("#repeatPassword").get(0).focus();
                return false;
            }
            var data = {"username":username , "password":oldPassword};
            $.ajax({
                url: "user/login",
                type: "POST",
                cache:false,
                contentType: "application/json",
                data:JSON.stringify(data),
                success: function(resp){
                    if(!resp.success){
                        $.ligerDialog.error('原密码输入错误！请重新输入！');
                        $("#oldPassword").get(0).focus();
                        return false;
                    }
                    $("form .l-text,.l-textarea").ligerHideTip();
                    updatePassword(oldPassword, newPassword);
                    updatePasswordDiv.hide();
                },
                error: function(e,r){
                    $.ligerDialog.error('检查用户名和密码出错：'+r);
                }
            });

        }
    });
    $("form").ligerForm();
    $("#l-button-close").click(function ()
    {
        $("input.inputName").each(function(i,obj){
            $(obj).ligerHideTip();
        });
        updatePasswordDiv.hide();
    });
}

function loginOut(){
    delCookie("UID");
    delCookie("USERNAME");
    location.href = "login.html";
}