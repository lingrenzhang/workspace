<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Register</title>
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/style.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/register.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/ui-lightness/jquery.ui.all.css" type="text/css" rel="stylesheet">
<script src="/TicketSchedule/JS/jquery-1.10.1.js"></script>
<script src="/TicketSchedule/JS/jquery-ui-1.8.13.custom.min.js"></script>
<script src="/TicketSchedule/JS/ajaxFileUpload.js"></script>

<script>
function showpic(value){
	
	document.getElementById("pic").innerHTML="<img src="+value+ "alt='Image Hosting' border='0'>";
}

function validForm(){
	if ((document.getElementById("password").value != document.getElementById("repassword").value)
		|| (document.getElementById("password").value =="")
		|| (document.getElementById("emailAddress").value == "")
		|| (document.getElementById("givenname").value == "")
		|| (document.getElementById("surname").value == ""))
	return false;
}


var __avatar_handlerUrl ="/TicketSchedule/servlet/AvaterService";
var avatarFileName = "sample";
var __avatar_size = 1;  
var __avatar_x = 0;
var __avatar_y = 0;
var __avatar_w = 0;
var __avatar_h = 0;


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

function _uploadImg() {
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
        error: function() {
            alert("upload failure，please check file format.");
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
        }
    )
    }
}
function _uploadAvatarCancel() {
    if (avatarFileName != "") {
        $.get(__avatar_handlerUrl, { myaction: "delete", size: __avatar_size, file: avatarFileName }, function(data, status) {
            $("#divBG").css("background", "none");
            $("#imgAvatarView").hide();
            avatarFileName = "";
            __avatar_size = 1;
        })
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
		<div id="logo">
		</div>
	</div>
</div>
<div id="content_wrapper">
	<div id="content_container" class="clearfix">
		<div id="wide_column_left" class="newAddRideStyle">
			<form action="/TicketSchedule/servlet/Register" method="Post" id="add_ride" class="standard requires_login_results" onkeypress="if(event.keyCode==13||event.which==13){return false;}" onsubmit="return validForm()">
				<fieldset id="reg_fld">
					<dl id="reg_dl">
						<dt>
							<label class="register-label">
								<span class="req">*</span>
								Username
							</label>
						</dt>
						<dd class="emailAddress" id="emailAddress">
							<input type="text" class="required" placeholder="emailAddress" name="emailAddress" id="emailAddress" maxlength="50"  autocomplete="off">
                        </dd>
                        
                        <dt>
                        	<label class="register-label">
								<span class="req">*</span>
								Password
							</label>
						</dt>
						<dd>
                        	<input type="password" class="required" placeholder="password" name="password" id="password" maxlength="50"  autocomplete="off">
                        </dd>
                        
                        <dt>
                        	<label class="register-label">
								<span class="req">*</span>
								Confirm Password
							</label>
						</dt>
                        <dd>
                        	<input type="password" class="required" placeholder="repassword" name="repassword" id="repassword" maxlength="50"  autocomplete="off">
                        </dd>
                        
                        
                        <dt>
                        	<label class="register-label">
								<span class="req">*</span>
								Given Name
							</label>
						</dt>
                        <dd>
                       		<input type="text" class="required" placeholder="XXXX" name="givenname" id="givenname" maxlength="100" autocomplete="off">
                        	<p>Enter your given name</p>
                	    </dd>
                	    
						<dt>
                        	<label class="register-label">
								<span class="req">*</span>
								Family name/Surname
							</label>
						</dt>
                        <dd>
                       		<input type="text" class="required" placeholder="XXXX" name="surname" id="surname" maxlength="100" autocomplete="off">
                        	<p>Enter your family name</p>
                	    </dd>
						
						
                	    <dt>
                        	<label class="register-label">
								<span class="req">*</span>
								Upload Picture
							</label>
						</dt>
                        <dd>
                        	 <input type="file" id="avatarFile" name="avatarFile" onchange="_uploadImg();">
                	    </dd>
					</dl>
					
					<div id="divUploadAvatar">
				    <div id="divContenter" style="display:none;">
				        <div id="divBG">
				            <div id="divCuter">
				            </div>
				        </div>
				    </div>
				    <div id="divAvatarInfo">
				        <div id="divImgAvatar">
				            <img id="imgAvatarView" alt="Avatar" style="display: none;" />
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

				<input type="submit" value = "Register" id="register"></input>
				
				</fieldset>
				<input type="text" class="hidden" id="avatarID" name="avatarID" value=""></input>

			</form>
		</div>
	</div>
</div>

</body>
</html>