<#import "template.ftl" as layout>
<@layout.registrationLayout>

  <style>
    body {
      margin: 0;
      padding: 0;
      font-family: 'Segoe UI', sans-serif;
      background: url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?auto=format&fit=crop&w=1950&q=80') no-repeat center center fixed;
      background-size: cover;
    }

    .login-box {
      max-width: 400px;
      background-color: rgba(255, 255, 255, 0.96);
      margin: 8% auto;
      padding: 40px;
      border-radius: 16px;
      box-shadow: 0 6px 18px rgba(0, 0, 0, 0.2);
      text-align: center;
    }

    h1 {
      color: #ff4081;
      margin-bottom: 30px;
    }

    input[type="text"],
    input[type="password"] {
      width: 100%;
      padding: 12px;
      margin: 10px 0;
      border-radius: 8px;
      border: 1px solid #ccc;
      font-size: 16px;
    }

    button[type="submit"] {
      width: 100%;
      background-color: #ff4081;
      border: none;
      color: white;
      padding: 12px;
      border-radius: 8px;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    button[type="submit"]:hover {
      background-color: #f50057;
    }

    .register-link {
      margin-top: 20px;
      font-size: 14px;
    }

    .register-link a {
      color: #ff4081;
      font-weight: bold;
      text-decoration: none;
    }

    .logo {
      font-size: 22px;
      font-weight: bold;
      margin-bottom: 15px;
      color: #ff4081;
    }
  </style>

  <div class="login-box">
    <div class="logo">🏋️ FitnessFreak</div>

    <h1>Welcome to FitnessFreak</h1>

    <form id="kc-form-login" action="${url.loginAction}" method="post">
      <input type="text" id="username" name="username" placeholder="Username" required><br>
      <input type="password" id="password" name="password" placeholder="Password" required><br>
      <button type="submit">Login</button>
    </form>

    <#if realm.registrationAllowed && url.registrationUrl??>
      <div class="register-link">
        Don't have an account? <a href="${url.registrationUrl}">Register here</a>
      </div>
    </#if>
  </div>

</@layout.registrationLayout>
