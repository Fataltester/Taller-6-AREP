document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("auth-form");
  const feedback = document.getElementById("feedback");
  const repeatGroup = document.getElementById("repeat-pass-group");
  const actionBtn = document.getElementById("action-btn");
  const switchBtn = document.getElementById("switch-btn");
  const switchLabel = document.getElementById("switch-label");
  const formTitle = document.getElementById("form-title");

  let registerMode = false;

  switchBtn.addEventListener("click", () => {
    registerMode = !registerMode;
    repeatGroup.classList.toggle("hidden", !registerMode);
    actionBtn.textContent = registerMode ? "Crear cuenta" : "Entrar";
    formTitle.textContent = registerMode ? "Registro" : "Iniciar sesión";
    switchLabel.textContent = registerMode ? "¿Ya tienes cuenta?" : "¿No tienes cuenta?";
    switchBtn.textContent = registerMode ? "Iniciar sesión" : "Registrarse";
    feedback.textContent = "";
  });

  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("user").value.trim();
    const password = document.getElementById("pass").value.trim();
    const repeat = document.getElementById("repeat-pass").value.trim();

    if (!email || !password || (registerMode && password !== repeat)) {
      return showFeedback("Verifica los campos ingresados", "error");
    }

    const endpoint = registerMode ? "/api/auth/register" : "/api/auth/login";
    try {
      const res = await fetch(`https://springserverjuan.duckdns.org:8443${endpoint}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (res.ok) {
        showFeedback(registerMode ? "Registro exitoso" : "Acceso concedido", "success");
        if (!registerMode) setTimeout(() => (window.location.href = "dashboard.html"), 1000);
      } else {
        const err = await res.text();
        showFeedback(err, "error");
      }
    } catch (err) {
      showFeedback("Error de conexión", "error");
    }
  });

  function showFeedback(message, type) {
    feedback.textContent = message;
    feedback.className = `feedback ${type}`;
  }
});
