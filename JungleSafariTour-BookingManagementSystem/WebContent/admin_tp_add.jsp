<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <!DOCTYPE html>
<html lang="en">
<head>
  <title>form</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body >
 <h2 style="text-align: center;"> | Please fill this Details |</h2>
 <br>
 <br>
<div class="container" style="margin-left: 29%">
  <form action="tp_add" method="post">
    <div class="form-group" style="width: 50%">
      <label for="pname">Package Name :</label>
      <input type="text" class="form-control"  placeholder="Enter Package Name" name="pname">
    </div>
    <div class="form-group" style="width: 25%">
      <label for="no_of_days">No. Of Days:</label>
      <input type="text" class="form-control"  placeholder="Enter No.of Days" name="no_of_days">
    </div>
    <div class="form-group" style="width: 25%">
      <label for="no_of_seats_available">Number Of Seats Available:</label>
      <input type="text" class="form-control"  placeholder="Enter Number Of Seats Available" name="no_of_seats_available">
    </div>
    <div class="form-group" style="width: 25%; ">
      <label for="c_rate">Child Rate :</label>
      <input type="text" class="form-control" placeholder="Enter Rate for Child"  name="c_rate">
    </div>
    <div class="form-group" style="width: 25%">
        <label for="a_rate">Adult Rate :</label>
        <input type="text" class="form-control" placeholder="Enter Rate for Adult"  name="a_rate">
    </div>
      <div class="form-group" style="width: 50%">
          <label for="details">Details:</label>
          <textarea name="details" class="form-control" rows="5"></textarea>
      </div>
      </div>
    <button type="submit" class="btn btn-default" style="margin-left: 35%">Submit</button>
  </form>


</body>
</html>