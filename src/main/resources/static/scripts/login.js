document.addEventListener("DOMContentLoaded", () => {
    const toggle = document.getElementById("passwordToggle");
    const input  = document.getElementById("password");
    const icon   = document.getElementById("passwordToggleIcon");

    toggle.addEventListener("click", () => {
        const isPass = input.type === "password";

        input.type = isPass ? "text" : "password";

        icon.classList.toggle("bi-eye", !isPass);
        icon.classList.toggle("bi-eye-slash", isPass);
    });
});
