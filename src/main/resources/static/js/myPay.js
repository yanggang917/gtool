$(function(){
    getpayList();
    initFormCheck();
});

function openmachineDialog(id, payCode, payType) {
    $("#payForm").get(0).reset();
    ligerDialogDiv = $.ligerDialog.open({ target: $("#target1") ,height:400,width:500});

    $("#id").val(id);
    $("#payCode").val(payCode);
    var pt;
    if (payType == 1){
        pt = "日卡";
    }
    if (payType == 2){
        pt = "周卡";
    }
    if (payType == 3){
        pt = "月卡";
    }
    $("#payType").val(pt);
    $("#payType").attr("readonly","readonly");
    $("#payCode").attr("readonly","readonly");

}


var grid;
function getpayList(){
    var coloModelList = [
        {display:"ID",name:'id',width:360,hide:true},
        {display:"支付码",name:'payCode',width:200},
        {display:"卡类",width:100,render:function(item,index){
                if (item.payType == 1){
                    return "日卡";
                }
                if (item.payType == 2){
                    return "周卡";
                }
                if (item.payType == 3){
                    return "月卡";
                }
            }},
        {display:"时效（天）",name:'dayLength',width:100},
        {display:"生成时间",name:'createTime',width:150},
        {display:"是否使用",name:'isUsed',width:120, render:function(item,index){
                if (item.isUsed == 1){
                    return "<span style='color: red'>已使用</span>";
                }else {
                    return "<span style='color: green'>未使用</span>";
                }
            }},
        {display:"使用时间",name:'useTime',width:150, render:function(item,index){
                if (item.useTime == null){
                    return "--";
                }else {
                    return item.useTime;
                }
            }},
        {display:"机器名称",name:'machineName',width:100, render:function(item,index){
                if (item.machineName == ''){
                    return "--";
                }else {
                    return item.machineName;
                }
            }},
        {display:"操作",name:"caozuo",width:150,align:'center',render:function(item,index){
                if (item.isUsed == 1){
                    return "--";
                }else {
                    var btns = "<input type='button' value='转让' class='l-button' style='width:120px' onclick='openmachineDialog("+item.id+",&quot;"+item.payCode+"&quot;,&quot;"+item.payType+"&quot;);'>";
                    return btns;
                }

            }
        }


    ];
    var uid = getCookie("UID");
    isNoTLogin(uid);
    grid = $("#payTable").ligerGrid({
        url: "pay/query-list?userId="+uid,
        columns: coloModelList,
        pageSize:10,
        newPage:0,
        pageParmName: "page",
        pagesizeParmName: "size",
        dataAction:'server',
        root:"retData",//返回的数据源名称
        record:'total',
        method:"GET",
        //where : f_getWhere(),
        isScroll:false,
        frozen:false,
        allowAdjustColWidth:true,
        width: '100%',
        height:'90%',
        rownumbers:true
    });
}

function initFormCheck()
{
    $.metadata.setType("attr", "validate");
    var v = $("form").validate({
        debug: false,
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
            $("form .l-text,.l-textarea").ligerHideTip();
            update();
        }
    });
    $("form").ligerForm();
    $("#l-button-close").click(function ()
    {
        $("input[type='text']").each(function(i,obj){
            $(obj).ligerHideTip();
        });
        ligerDialogDiv.hide();
    });

}


///新增机器码或续期。。
function update(){
    var uid = getCookie("UID");
    isNoTLogin(uid);

    var payCode = $("#payCode").ligerGetTextBoxManager().getValue();
    var userName = $("#userName").ligerGetTextBoxManager().getValue();

    var data = {"userName":userName,"payCode":payCode};


    $.ajax({
        url: "pay/zr",
        type: "POST",
        data: JSON.stringify(data),
        cache:false,
        contentType: "application/json",
        success: function(resp){
            if(resp.success){
                grid.reload();
                var tip = $.ligerDialog.tip({ title: '提示信息', content: '已成功转让给'+userName ,height:150 });
                setTimeout(function () { tip.close(); }, 5000);
                ligerDialogDiv.hide();
            }else {
                $.ligerDialog.warn(resp.retDesc);
            }
        },
        error: function(e,r){
            $.ligerDialog.error("添加或修改机器码出错："+r);
        }
    });
}



