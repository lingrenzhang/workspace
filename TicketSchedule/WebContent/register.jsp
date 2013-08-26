<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Register</title>
<link href="/TicketSchedule/CSS/master.css" type="text/css" rel="stylesheet">
<link href="/TicketSchedule/CSS/style.css" type="text/css" rel="stylesheet">


<script>
function showpic(value){
	
	document.getElementById("pic").innerHTML="<img src="+value+ "alt='Image Hosting' border='0'>";
}

function validForm(){
	if ((document.getElementById("password").value != document.getElementById("repassword").value)
		|| (document.getElementById("password").value =="")
		|| (document.getElementById("emailAddress").value == "")
		|| (document.getElementById("givenname").value == "")
		|| (document.getelementById("surename").value == ""))
	return false;
}

$(function() {
    $("#divUp").load("uploadAvatar.htm?n=" + Math.random());
})
function OnAvatarUploaded(file) {
    $("#img").attr("src", file + "?n=" + Math.random()); //avoid cache
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
				<fieldset id="step_1">
					<dl>
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
                       		<input type="text" class="required" placeholder="XXXX" name="surename" id="surename" maxlength="100" autocomplete="off">
                        	<p>Enter your family name</p>
                	    </dd>
						
						
                	    <dt>
                        	<label class="register-label">
								<span class="req">*</span>
								Upload Picture
							</label>
						</dt>
                        <dd>
                        	    <div style="height: 500px; clear: both; margin: 80px auto; width: 800px;">
							        <div style="width: 175px; height: 175px; float: left; border: 1px solid #ccc;">
							            <img src="" id="img" style="width: 175px; height: 175px" />
							        </div>
							        <div id="divUp" style="width: 520px; float: left; margin-left: 20px">
							        </div>
							    </div>
                	    </dd>
					</dl>
				</fieldset>
				<input type="submit" value = "Register" ></input>
				
			</form>
		</div>
	</div>
</div>

</body>
</html>