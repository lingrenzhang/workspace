<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<link href="./CSS/master.css" type="text/css" rel="stylesheet">
<link href="./CSS/style.css" type="text/css" rel="stylesheet">

<script src="../JS/jquery-1.10.1.js"></script>

<title>Search for rides - HitRide</title>
</head>


<body id="search_index">
<div id="header_wrap">
</div>
<div id="content_wrap">
	<div id="content_container">
	<link href="./CSS/custom_jqueryui.css" 
		type="text/css" rel="stylesheet">
		<div id="content">
			<div id="head">
				<form class="search">
					<div class="text_input">
						<label class="pin start" for="search_s"></label>
						<input id="search_s" class="clickaway input_text" type="text" 
							placeholder="Starting from..." name="s" alt="search_start" 
							autocomplete="off">
						</input>
					</div>
					<div class="text_input">
						<label class="pin end" for="search_e"></label>
						<input id="search_e" class="clickaway input_text" type="text" placeholder="Going to..." name="e" alt="search_end" autocomplete="off">
					</div>
					<div class="text_input datetime">
						<label class="datetime_icon" for="search_date"></label>
						<input id="search_date" class="slim datepicker hasDatepicker" type="text" value="exp" name="date">
					</div>
					<button class="button confirm clickaway_confirm" type="submit">Search</button>
				</form>
			</div>
			<div id="results">
				<div class="ride_list">
					<h3 class="headline first">Departing<em>Today</em>
						<span> - Friday, July 5th</span>
					</h3>
    				<a href="http://www.baidu.com">
						<div class="entry">
							<div class="passenger_box">
								<p>
									<span class="icon"></span>
									<strong>passenger</strong>
								</p>
							</div>
							<div class="userpic">
								<div class="username"></div>
								<img src="" alt="Profile Picture"></img>
								<span class="passenger"></span>
							</div>
							<div class="inner_content">
								<h3>
									<span class="inner">
										<span class="trip_type round_trip"></span>
									</span>
								</h3>
								<h4>
								</h4>
							</div>
						</div>
					</a>
					<a href="http://www.baidu.com">
						<div class="entry">
							<div class="passenger_box">
								<p>
									<span class="icon"></span> Xiyao is a 
									<strong>passenger</strong>
								</p>
							</div>
							<div class="userpic">
								<div class="username">Xiyao J</div>
								<img src="" alt="Profile Picture"></img>
								<span class="passenger"></span>
							</div>
							<div class="inner_content">
								<h3>
									<span class="inner"> Stanford
										<span class="trip_type round_trip"></span>
										 Berkeley
									</span>
								</h3>
								<h4>
								</h4>
							</div>
						</div>
					</a>
					
					<div id="action">
						<div class="item postride">
							<h2>
								<a href="">Didn't find what you were looking for?						
								</a>
							</h2>
							<p>Post a ride as a driver or passenger and get notified when new matches are found!							
							</p>
							<a class="button post" href="">Post a ride</a>
						</div>
					</div>
				</div>
				<div class="page_list">
					<span class="showing">Showing 1 - 2 of
						<strong> 20 results</strong>
					</span>
					<div class="pagination">
						<span class="first_page">
							<span> Prev </span>
						</span>
						<span class="current_page">
							<span>1</span>
						</span>
						<a href="id2">
							<span>2</span>
						</a>
						<a href="id2" class="next_page"><span> Next</span></a>
					</div>
					
				</div>
			</div>
			<div id="info">
			</div>
		</div>
	</div>
</div>
<div id="footer">
</div>




</body>
</html>