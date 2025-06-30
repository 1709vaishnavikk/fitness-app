<#import "template.ftl" as layout>
<@layout.registrationLayout>

<style>
    body {
        background: url('https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b') no-repeat center center fixed;
        background-size: cover;
        font-family: 'Segoe UI', sans-serif;
    }

    .register-container {
        max-width: 450px;
        margin: 60px auto;
        padding: 30px;
        background-color: rgba(255, 255, 255, 0.95);
        border-radius: 15px;
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.25);
        text-align: center;
    }

    .register-container h1 {
        color: #ff4081;
        margin-bottom: 25px;
    }

    .register-container input {
        width: 100%;
        padding: 12px;
        margin-bottom: 15px;
        border: 1px solid #ccc;
        border-radius: 8px;
        font-size: 14px;
    }

    .register-container button {
        background-color: #ff4081;
        color: white;
        border: none;
        padding: 12px;
        width: 100%;
        border-radius: 8px;
        font-size: 15px;
        cursor: pointer;
    }

    .register-container a {
        color: #ff4081;
        font-weight: bold;
        text-decoration: none;
    }

    .logo {
        font-size: 20px;
        font-weight: bold;
        margin-bottom: 10px;
        color: #ff4081;
    }
</style>

<div class="register-container">
    <div class="logo">🏋️‍♀️ FitnessFreak</div>

    <h1>Create Your FitnessFreak Account</h1>

    <form id="kc-register-form" action="${url.registrationAction}" method="post">
        <input type="text" id="firstName" name="firstName" placeholder="First Name" value="${(register.formData.firstName!'')}" required>
        <input type="text" id="lastName" name="lastName" placeholder="Last Name" value="${(register.formData.lastName!'')}" required>
        <input type="email" id="email" name="email" placeholder="Email" value="${(register.formData.email!'')}" required>
        <input type="text" id="username" name="username" placeholder="Username" value="${(register.formData.username!'')}" required>
        <input type="password" id="password" name="password" placeholder="Password" required>
        <input type="password" id="password-confirm" name="password-confirm" placeholder="Confirm Password" required>

        <button type="submit">Register</button>
    </form>

    <p style="margin-top: 15px;">Already have an account? <a href="${url.loginUrl}">Login</a></p>
</div>

</@layout.registrationLayout>
