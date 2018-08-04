<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="keys" content="">
    <meta name="author" content="">
	<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="/css/font-awesome.min.css">
	<link rel="stylesheet" href="/css/login.css">
	<style>

	</style>
  </head>
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <div><a class="navbar-brand" href="index.html" style="font-size:32px;">尚筹网-创意产品众筹平台</a></div>
        </div>
      </div>
    </nav>

    <div class="container">

      <form class="form-signin" role="form">
        <h2 class="form-signin-heading"><i class="glyphicon glyphicon-picture"></i> 用户登录</h2>
		  <div class="form-group has-success has-feedback">
			<input type="text" class="form-control" id="loginacct" name="loginacct" placeholder="请输入登录账号" autofocus>
			<span class="glyphicon glyphicon-user form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<input type="password" class="form-control" id="userpswd" name="userpswd" placeholder="请输入登录密码" style="margin-top:10px;">
			<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		  </div>
		  <div class="form-group has-success has-feedback">
			<select class="form-control" >
                <option value="member">会员</option>
                <option value="user">管理</option>
            </select>
		  </div>
        <div class="checkbox">
          <label>
            <input type="checkbox" value="remember-me"> 记住我
          </label>
          <br>
          <label>
            忘记密码
          </label>
          <label style="float:right">
            <a href="reg.html">我要注册</a>
          </label>
        </div>
        <a class="btn btn-lg btn-success btn-block" onclick="dologin()" > 登录</a>
      </form>
    </div>
    <script src="/jquery/jquery-2.1.1.min.js"></script>
    <script src="/bootstrap/js/bootstrap.min.js"></script>
    <script src="/layer/layer.js"></script>
    <script>
    function dologin() {
        //验证表单元素
        var loginacct = $("#loginacct");
        if( $.trim(loginacct.val()) == "" ){
        	//alert("登录账号不能为空，请输入");
        	layer.msg("登录账号不能为空，请输入", {time:2000, icon:5, shift:6}, function(){
	        	loginacct.focus();
        	});
        	return;
        }

        var userpswd = $("#userpswd");
        if( $.trim(userpswd.val()) == "" ){
        	layer.msg("登录密码不能为空，请输入", {time:2000, icon:5, shift:6}, function(){
	        	userpswd.focus();
        	});
        	return;
        }
        
        var loadingIndex = 0;
        var jsonObj = {
        		type : "POST",
        		url : "/checkLogin",
        		dataType : "json",
        		data : {
        			"loginacct" : loginacct.val(),
        			"memberpswd" : userpswd.val()
        			},
        		beforeSend : function(){
        			loadingIndex = layer.load(2,{time:10*1000});
        		},
        		success : function(result){
        			layer.close(loadingIndex);
        			if(result.success){
        				window.location.href = "/main";
        			}else {
        				layer.msg("账号或密码不正确，请重新输入", {time:2000, icon:5, shift:6}, function(){
        	        	});
        			}
        		}
        };
        $.ajax(jsonObj);
    }
    </script>
  </body>
</html>