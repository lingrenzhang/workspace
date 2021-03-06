<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String err = request.getParameter("err")==null?"": request.getParameter("err");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>注册</title>


<link href="/TicketSchedule/CSS/master.css" type="text/css"	rel="stylesheet">
<link href="/TicketSchedule/CSS/style.css" type="text/css"	rel="stylesheet">
<link href="/TicketSchedule/CSS/register.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/ui-lightness/jquery.ui.all.css"	type="text/css" rel="stylesheet">
<link rel="shortcut icon" href="/TicketSchedule/favicon.ico" type="image/x-icon" /> 
<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/jquery-ui-1.8.13.custom.min.js"></script>
<script src="/TicketSchedule/JS/ajaxFileUpload.js"></script>

<script>
function showpic(value){
	document.getElementById("pic").innerHTML="<img src="+value+ "alt='Image Hosting' border='0'>";
}

var __avatar_handlerUrl ="/TicketSchedule/servlet/AvaterService";
var avatarFileName = "sample";
var __avatar_size = 1;  
var __avatar_x = 0;
var __avatar_y = 0;
var __avatar_w = 0;
var __avatar_h = 0;
var ready_for_upload = false;

$(document).ready(function(){
	$("#divBG").resizable().children().not("#divCuter").remove();
	var gbOS = $("#divBG").offset();
	$("#divCuter").resizable({ containment: "#divBG", aspectRatio: 1, minHeight: 20, minWidth: 20,
	    stop: function() {
	        _viewImg();
	    }
	}).draggable({
	    containment: "#divBG",
	    scroll: false,
	    stop: function() {
	        _viewImg();
	    }
	}).offset({ 
		top: gbOS.top + 63, 
		left: gbOS.left + 63 });
	
	function emailAddressValidator(){
		// TODO: email format check
		var uniqueCheckUrl = "/TicketSchedule/servlet/UniqueUsernameCheck";
		var flag = true;
	    $.ajaxSetup({
			async: false
		});
		$.get(uniqueCheckUrl, {username : $('#emailAddress').val()},
			  function(responseText, status){
			  	  flag = responseText;
		      }, "html");
		if(flag == "0"){
			return true;
		}else{
			return "用户名已存在";
		}
	}
	
	function repasswordValidator(){
		var _pwd = $('#password').val();
		var _repwd = $('#repassword').val();
		if(_pwd != _repwd){
			return "两次输入密码不一致";
		}else{
			return true;
		}
	}
	
	function cellphoneValidator(){
		// TODO: cellphone number format check
		return true;
	}
	
	function authcodeValidator(){
		var checkUrl = "/TicketSchedule/servlet/AuthCheck";
		$.ajaxSetup({
			async : false
		});
		$.get(checkUrl,    
			{authCode : document.getElementById("authcode").value},     
			 function(responseText){document.getElementById("groupID").value=responseText;},    
				"html"   );

		var resultvali= /^[1-9]/i;
		
		var result = document.getElementById("groupID").value;
		if (resultvali.test(result))
		{
			return true;
		}
		else{
			return result;
		}
	}
	
	(function ($){
		$.formField = function (ele_id, is_required, err_msg, sp_validator) {
			this.eid = ele_id;
			this.err_msg = err_msg;
			this.generalValidator = function (){
				if(!is_required){
					return true;
				}
				var ele = $('#' + ele_id);
				if(ele.val() == ""){
					return false;
				}
				return true;
			};
			if(sp_validator){
				this.specialValidator = sp_validator;
			}else{
				this.specialValidator = function(){return true;};
			}
			
		};
	})(jQuery);
	
	var requiredFields = [new $.formField('emailAddress', true, '请输入用户名', emailAddressValidator),
	                      new $.formField('password', true, '请输入密码'),
	                      new $.formField('repassword', true, '请确认密码', repasswordValidator),
	                      new $.formField('givenname', true, '请输入您的姓'),
	                      new $.formField('surname', true, '请输入您的名'),
	                      new $.formField('cellphone', true, '请输入手机号码', cellphoneValidator),
						  new $.formField('authcode', false, '', authcodeValidator)];
	
	for(var i=0; i<requiredFields.length; i++){
		(function(){
			var j = i;
			var ele = requiredFields[j];
			$('#' + ele.eid).focus(function(){
				$('#' + ele.eid).removeClass('requiredErr');
				$('#' + ele.eid + 'Msg').hide();	
			});
			$('#' + ele.eid).blur(function(){
				$('#' + ele.eid + 'Msg').show();
				$('#' + ele.eid).addClass('requiredErr');
				$('#' + ele.eid + 'Succ').hide();
				if(!ele.generalValidator()){
					$('#' + ele.eid + 'Msg').html("<span>" + ele.err_msg + "</span>");
				}else if((err_msg = ele.specialValidator()) != true){
					$('#' + ele.eid + 'Msg').html("<span>" + err_msg + "</span>");
				}else{
					$('#' + ele.eid + 'Msg').hide();
					$('#' + ele.eid).removeClass('requiredErr');
					$('#' + ele.eid + 'Succ').show();
				}
			});
		})();
	}

	$('#add_ride').submit(function(event){
		var is_valid = true;
		for(var i=0; i<requiredFields.length; i++){
			var ele = $('#' + requiredFields[i].eid);
			ele.blur();
			if(ele.hasClass('requiredErr')){
				is_valid = false;
			}
		}
		if(is_valid){
			this.submit();
		}
	});
});

function _prepare_for_upload() {
  ready_for_upload = true;
}

function _uploadImg() {
    if(!ready_for_upload){
        return;
    }
    ready_for_upload = false; // workround for the Chrome v36 upload file issue, see www.redmine.org/issues/17151
    $.ajaxFileUpload({
        url: __avatar_handlerUrl,
        secureuri: false,
        fileElementId: 'avatarFile',
        data: { myaction: "upload" },
        success: function(data) {
            var obj = $.parseJSON(data);
            if (obj.result == 1) {
                var file = obj.msg;
                avatarFileName = file;
                document.getElementById("avatarID").value = obj.avatarID;
                __avatar_size = obj.size;
                if (obj.size != 1) {
                    file += ".view.jpg";
                }
                var pof = $("#divContenter").offset();
                $("#divBG").css({
                    "background-image": "url(" + file + ")",
                    width: obj.w,
                    height: obj.h
                }).offset({ top: pof.top + (300 - obj.h) / 2 + 1, left: pof.left + (300 - obj.w) / 2 + 1 });
 				//set cuter size
                var mh = Math.min(175, obj.h);
                var mw = Math.min(175, obj.w);
                $("#divCuter").height(mh).width(mw);
				//put cuter to the middle
                pof = $("#divBG").offset();
                $("#divCuter").offset({ top: pof.top + (obj.h - mh) / 2 + 1, left: pof.left + (obj.w - mw) / 2 + 1 });
                _viewImg();
            }
            else {
                alert(obj.msg);
            }
        },
        error: function(data) {
        	var obj = $.parseJSON(data);
        	if (obj!=null)
        	{
        		alert(obj.msg);
        	}
        	else{
        		alert("upload failure，please check file format.");
        	}
        }
    });
}

function _viewImg() {
    if (avatarFileName != "") {
        var c = $("#divCuter");
        var os1 = c.offset();
        var os2 = $("#divBG").offset();
        var width = c.width();
        var height = c.height();
        var x = os1.left - os2.left;
        var y = os1.top - os2.top;
        __avatar_x = x;
        __avatar_y = y;
        __avatar_h = height;
        __avatar_w = width;
        var img = __avatar_handlerUrl + "&myaction=view&file=" + avatarFileName + "&size=" + __avatar_size + "&x=" + x + "&y=" + y + "&w=" + width + "&h=" + height;
        $("#imgAvatarView").attr("src", avatarFileName).show();
    }
}
function _uploadAvatarOK() {
    //saveFile
    if (avatarFileName != "") {
        $.get(__avatar_handlerUrl,
        { myaction: "save",
            size: __avatar_size,
            file: avatarFileName,
            x: __avatar_x,
            y: __avatar_y,
            h: __avatar_h,
            w: __avatar_w
        },
        function(data, status) {
            if (data == "1") {
                if (window.OnAvatarUploaded) {//External function
                    OnAvatarUploaded(avatarFileName);
                }
                $("#divSaveInfo").html("Save Success!").css("color", "orange");
            }
            else {
                $("#divSaveInfo").html("Save Failure!").css("color", "red");
            }
            $("#divBG").css("background", "none");
            $("#imgAvatarView").hide();
            avatarFileName = "";
            __avatar_size = 1;
            setTimeout('$("#divSaveInfo").html("");', 1500);
        });
    }
}
function _uploadAvatarCancel() {
    if (avatarFileName != "") {
        $.get(__avatar_handlerUrl, { myaction: "delete", size: __avatar_size, file: avatarFileName }, function(data, status) {
            $("#divBG").css("background", "none");
            $("#imgAvatarView").hide();
            avatarFileName = "";
            __avatar_size = 1;
        });
        if (window.OnAvatarUploadCancel) {//External function
            OnAvatarUploadCancel();
        }
    }
}


</script>

</head>
<body>
	<div id="header_wrap">
		<div id="logo_wrap">
			<div id="logo"></div>
		</div>
	</div>
	<div id="content_wrapper">
		<div id="content_container" class="clearfix">
			<div id="wide_column_left" class="newAddRideStyle">
				<form action="/TicketSchedule/servlet/Register" method="Post"
					id="add_ride" class="standard requires_login_results"
					onkeypress="if(event.keyCode==13||event.which==13){return false;}"
					onsubmit="return false;" accept-charset="UTF-8">
					<fieldset id="reg_fld">
						<dl id="reg_dl">
							<dt>
								<label class="register-label"> <span class="req">*</span>
									用户名
								</label>
							</dt>
							<dd class="emailAddress" >
								<input type="text" class="required" placeholder="邮箱"
									name="emailAddress" id="emailAddress" maxlength="50"
									autocomplete="off"> <%= err.equals("existed_user")?"<p style=\"color:#FF0000;\">用户名已存在</p>":""%>
									<img id="emailAddressSucc" src="/TicketSchedule/Picture/success.png" class="imgPos"/>
									<div id="emailAddressMsg" class="error"></div>
							</dd>

							<dt>
								<label class="register-label"> <span class="req">*</span>
									密码
								</label>
							</dt>
							<dd>
								<input type="password" class="required" placeholder="密码"
									name="password" id="password" maxlength="50" autocomplete="off">
								<img id="passwordSucc" src="/TicketSchedule/Picture/success.png" class="imgPos"/>
								<div id="passwordMsg" class="error"></div>
							</dd>

							<dt>
								<label class="register-label"> <span class="req">*</span>
									确认密码
								</label>
							</dt>
							<dd>
								<input type="password" class="required" placeholder="再次输入密码"
									name="repassword" id="repassword" maxlength="50"
									autocomplete="off">
								<img id="repasswordSucc" src="/TicketSchedule/Picture/success.png" class="imgPos"/>
								<div id="repasswordMsg" class="error"></div>
							</dd>
							<dt>
								<label class="register-label"> <span class="req">*</span>
									姓
								</label>
							</dt>
							<dd>
								<input type="text" class="required" placeholder="你的姓"
									name="surname" id="surname" maxlength="100" autocomplete="off">
								<img id="surnameSucc" src="/TicketSchedule/Picture/success.png" class="imgPos"/>
								<div id="surnameMsg" class="error"></div>
							</dd>
							<dt>
								<label class="register-label"> <span class="req">*</span>
									名
								</label>
							</dt>
							<dd>
								<input type="text" class="required" placeholder="你的名字"
									name="givenname" id="givenname" maxlength="100"
									autocomplete="off">
								<img id="givennameSucc" src="/TicketSchedule/Picture/success.png" class="imgPos"/>
									<div id="givennameMsg" class="error"></div>
							</dd>
							<dt>
								<label class="register-label"> 电话 </label>
							</dt>
							<dd>
								<input type="text" class="required" placeholder="手机号码"
									name="cellphone" id="cellphone" maxlength="15"
									autocomplete="off">
								<img id="cellphoneSucc" src="/TicketSchedule/Picture/success.png" class="imgPos"/>
								<div id="cellphoneMsg" class="error"></div>
								<p>11位手机号码</p>
							</dd>
							<dt>
								<label class="register-label"> 授权码 </label>
							</dt>
							<dd>
								<input type="text" class="optional" placeholder="abcdefghi"
									name="authcode" id="authcode" maxlength="10" autocomplete="off">
								<img id="authcodeSucc" src="/TicketSchedule/Picture/success.png" class="imgPos"/>
								<div id="authcodeMsg" class="error"></div>
								<p>9位授权码</p>
							</dd>

							<dt>
								<label class="register-label"> <span class="req">*</span>
									上传头像
								</label>
							</dt>
							<dd>
								<input type="file" id="avatarFile" name="avatarFile"
									onclick="_prepare_for_upload();" onchange="_uploadImg();">
							</dd>

						</dl>

						<div id="divUploadAvatar">
							<div id="divContenter" style="display: none;">
								<div id="divBG">
									<div id="divCuter"></div>
								</div>
							</div>
							<div id="divAvatarInfo">
								<div id="divImgAvatar">
									<img id="imgAvatarView" alt="Avatar" src="/TicketSchedule/UserProfile/default.jpg" />
								</div>
								<!--  
				        <div id="divUploadTxt">
				        	You can use left block to resize picture;
				        </div>
				        <div id="divUploadBtn">
				            <input type="button" class="btnAvatar" onclick="_uploadAvatarOK()" value="Confirm" />
				            <input type="button" class="btnAvatar" onclick="_uploadAvatarCancel()" value="Cancel" />
				        </div>
				        <div id="divSaveInfo">
				        </div>
				        -->
							</div>
						</div>

						<input type="submit" value="注册" id="register"></input>

					</fieldset>
					<input type="text" class="hidden" id="avatarID" name="avatarID"
						value="" /> <input type="text" class="hidden" id="groupID"
						name="groupID" value="" />
				</form>
			</div>
		</div>
	</div>
	<div id="baidu_tongji" style="display: none">
		<script type="text/javascript">
			var _bdhmProtocol = (("https:" == document.location.protocol) ? " https://"
					: " http://");
			document
					.write(unescape("%3Cscript src='"
							+ _bdhmProtocol
							+ "hm.baidu.com/h.js%3F04d65d39238bfa4301b173d21ddcfeb7' type='text/javascript'%3E%3C/script%3E"));
		</script>
	</div>
</body>
</html>
