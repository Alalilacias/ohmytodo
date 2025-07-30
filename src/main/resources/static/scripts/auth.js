document.addEventListener("DOMContentLoaded", () => {
  // Login
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

  // Register
  const registerForm = document.getElementById('registerForm');
  if (registerForm) {
    registerForm.addEventListener('submit', async function (e) {
      e.preventDefault();
      const form = e.target;
      const formData = new FormData(form);

      const jsonData = {
        name: formData.get("name"),
        username: formData.get("username"),
        password: formData.get("password"),
        address: {
          street: formData.get("address.street"),
          city: formData.get("address.city"),
          zipcode: formData.get("address.zipcode"),
          country: formData.get("address.country")
        }
      };

      const res = await fetch('/api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(jsonData)
      });

      const msg = document.createElement('div');
      msg.classList.add('alert', 'mt-2', 'fade', 'show');
      msg.setAttribute('role', 'alert');

      if (res.ok) {
        msg.classList.add('alert-success');
        msg.innerText = 'Registration successful!';
        setTimeout(() => location.reload(), 1000);
      } else {
        const text = await res.text();
        msg.classList.add('alert-danger');
        msg.innerText = 'Registration failed: ' + text;
      }

      this.appendChild(msg);
    });
  }

  // Logout
  const logoutForm = document.getElementById("logoutForm");
  if (logoutForm) {
    logoutForm.addEventListener("submit", async function (e) {
      e.preventDefault();

      const res = await fetch('/api/auth/logout', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });

      if (res.ok) {
        showFloatingAlert('Logout successful!', 'success');
        setTimeout(() => location.reload(), 1000);
      } else {
        showFloatingAlert('Logout failed.', 'danger');
      }
    });
  }
});

// Floating alert
function showFloatingAlert(message, type = 'success', timeout = 10000) {
  let container = document.getElementById('floating-alert');
  if (!container) {
    container = document.createElement('div');
    container.id = 'floating-alert';
    container.className = 'position-fixed bottom-0 end-0 p-3';
    container.style.zIndex = '1055';
    document.body.appendChild(container);
  }

  const alert = document.createElement('div');
  alert.className = `alert alert-${type} alert-dismissible fade show`;
  alert.setAttribute('role', 'alert');
  alert.innerHTML = `${message} <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>`;
  container.appendChild(alert);

  setTimeout(() => {
    alert.classList.remove('show');
    alert.classList.add('hide');
    alert.addEventListener('transitionend', () => alert.remove());
  }, timeout);
}