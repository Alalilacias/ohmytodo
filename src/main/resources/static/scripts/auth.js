// auth.js
document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.getElementById('loginForm');
  if (loginForm) {
    loginForm.addEventListener('submit', async function (e) {
      e.preventDefault();

      const username = document.getElementById('login-username').value;
      const password = document.getElementById('login-password').value;

      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password })
      });

      const msg = document.createElement('div');
      msg.classList.add('alert', 'mt-2', 'fade', 'show');
      msg.setAttribute('role', 'alert');

      if (res.ok) {
        msg.classList.add('alert-success');
        msg.innerText = 'Login successful!';
        setTimeout(() => location.reload(), 1000);
      } else {
        msg.classList.add('alert-danger');
        msg.innerText = 'Login failed. Please check your credentials.';
      }

      this.appendChild(msg);
    });
  }
});