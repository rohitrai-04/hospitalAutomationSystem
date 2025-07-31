

// Toggle 'active' class for filter type buttons
const filterButtons = document.querySelectorAll(".filter-type");
filterButtons.forEach((btn) => {
  btn.addEventListener("click", () => {
    filterButtons.forEach((b) => b.classList.remove("active"));
    btn.classList.add("active");
  });
});

// Toggle checkbox manually on label click
document.addEventListener("click", (e) => {
  if (e.target.classList.contains("circle-label")) {
    e.preventDefault(); // Prevent native toggle
    const forId = e.target.getAttribute("for");
    const checkbox = document.getElementById(forId);
    if (checkbox) checkbox.checked = !checkbox.checked;
  }
});


const signUpButton = document.getElementById("signUp");
const signInButton = document.getElementById("signIn");
const container = document.getElementById("container");

if (signUpButton && signInButton && container) {
  signUpButton.addEventListener("click", () => {
    container.classList.add("right-panel-active");
  });

  signInButton.addEventListener("click", () => {
    container.classList.remove("right-panel-active");
  });
}

