<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Login - Ocean View HMS</title>
    <style>
        body{
            font-family: Arial;
            background:#0b1a2f;
            color:white;
            display:flex;
            justify-content:center;
            align-items:center;
            height:100vh;
        }
        .box{
            background:#112b4a;
            padding:40px;
            border-radius:12px;
            width:350px;
        }
        input{
            width:100%;
            padding:10px;
            margin:10px 0;
        }
        button{
            width:100%;
            padding:12px;
            background:#00c2ff;
            border:none;
            font-weight:bold;
        }
    </style>
</head>

<body>
<div class="box">
    <h2>Ocean View Login</h2>

    <form action="<%=request.getContextPath()%>/login" method="post">
        Username:
        <input type="text" name="username" required>

        Password:
        <input type="password" name="password" required>

        <button type="submit">LOGIN</button>
    </form>

</div>
</body>
</html>
