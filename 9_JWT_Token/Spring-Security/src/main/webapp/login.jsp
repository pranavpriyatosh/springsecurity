<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Page</title>
</head>
<body>
	<h1>Login</h1>
	
	<!-- for bad credentials -->
	${SPRING_SECURITY_LAST_EXCEPTION.message} 
	
	<form action="login" method="POST">
		<table>
			<tr>
				<td>User: </td>
				<td><input type='text' name='username' value=''></td>
			</tr>
			<tr>
				<td>Password: </td>
				<td><input type='text' name='password' value=''></td>
			</tr>
			<tr>
				<td><input type='submit' name='submit' value='submit'></td>
			</tr>
		</table>
	</form>
		
</body>
</html>