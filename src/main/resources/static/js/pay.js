$(function(){
    getpayList();
    initFormCheck();
    $("#payType").find("option[name='日卡']").attr("selected",true);
});
var ligerDialogDiv = null;
function openpayDialog(payId)
{
    $("#payForm").get(0).reset();
    if(ligerDialogDiv!=null){
        ligerDialogDiv.show();
    }else{
        ligerDialogDiv = $.ligerDialog.open({ target: $("#target1") ,height:400,width:500});
    }
}

function initFormCheck()
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
            $("form .l-text,.l-textarea").ligerHideTip();
            updatepay();

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

function deletepay(id){
    $.ligerDialog.confirm('确定要删除此条支付码信息吗？', function (yes) {
        if(yes){
            $.ajax({
                url: "pay/removepay",
                type: "POST",
                cache:false,
                data: {"payId":id},
                dataType: "json",
                success: function(resp){
                    if(resp.success){
                        grid.reload();
                        var tip = $.ligerDialog.tip({ title: '提示信息', content: '记录已经删除！',height:150 });
                        setTimeout(function () { tip.close(); }, 2000);
                    }
                },
                error: function(e,r){
                    $.ligerDialog.error("删除支付码出错："+r);
                }
            });
        }
    });
}
var grid;
function getpayList(){
    var coloModelList = [
        {display:"ID",name:'id',width:360,hide:true},
        {display:"支付码",name:'payCode',width:300},
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
        {display:"所属用户",name:'userName',width:150}
        // {display:"操作",name:"caozuo",width:350,align:'center',render:function(item,index){
        //         var btns = "<input type='button' value='修改' class='l-button' style='width:120px' onclick='openpayDialog("+item.id+");'>&nbsp;&nbsp;";
        //         btns += "<input type='button' value='删除' class='l-button' style='width:120px' onclick='deletepay("+item.id+");'>";
        //         return btns;
        //     }
        // }
    ];
    grid = $("#payTable").ligerGrid({
        url: "pay/query-list?userId=0",
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

function updatepay(){
    var id = $("#id").attr("value");
    var payType = $("#payType").val();
    var username = $("#username").val();
    var num = $("#num").val();

    if (payType == 0){
        $.ligerDialog.error('请选择卡种！');
        return;
    }

    var posPattern = /^\d+$/;
    if(!posPattern.test(num)){
        $.ligerDialog.error('数量必须为正整数');
        return;
    }

    var data = {"payType":payType, "userName":username, "num":num};
    var url = "";
    if(id!=null && id!=""){
        url = "pay/modifypay";
    }else{
        url = "pay/add";
    }
    $.ajax({
        url: url,
        type: "POST",
        data: JSON.stringify(data),
        cache:false,
        contentType: "application/json",
        success: function(resp){
            if(resp.success){
                ligerDialogDiv.hide();
                grid.reload();
                var tip = $.ligerDialog.tip({ title: '提示信息', content: '记录已经更新成功！',height:150 });
                setTimeout(function () { tip.close(); }, 5000);
            }else {
                $.ligerDialog.error(resp.retDesc);
            }
        },
        error: function(e,r){
            $.ligerDialog.error("添加或修改支付码出错："+r);
        }
    });
}


