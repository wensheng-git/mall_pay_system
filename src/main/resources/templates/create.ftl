<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
</head>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.5.1/jquery.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
<style>
    #box{
        text-align: center;
    }
</style>
<body>
<div id="box">
<h1>支付界面</h1>
<div id="myqrcode"></div>
<div id="orderId"  hidden="hidden">${orderId}</div>
<div id="returnUrl" hidden="hidden">${returnUrl}</div>
</div>
</body>
<script>
    jQuery("#myqrcode").qrcode({
        text :  "${codeUrl}"
    });


    /*轮循器*/
    $(function (){
        setInterval(function (){
            console.log("开始查询支付状态")
            $.ajax({
                "url":"/pay/query",
                data:{
                    "orderId":$('#orderId').text()
                },
                success: function (result){
                    if(result.platformStatus!=null&&result.platformStatus==='SUCCESS'){
                        location.href=$('#returnUrl').text();
                    }
                }
            })
        },1000)
    });

</script>
</html>