const BACKEND_URL = "http://34.227.221.17:8080/properties";

document.addEventListener("DOMContentLoaded", loadProperties);

// Manejo del formulario
document.getElementById("property-form").addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("property-id").value;
  const property = {
    address: document.getElementById("address").value,
    price: parseFloat(document.getElementById("price").value),
    size: parseFloat(document.getElementById("size").value),
    description: document.getElementById("description").value
  };

  try {
    if (id) {
      // Update
      await fetch(`${BACKEND_URL}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(property)
      });
    } else {
      // Create
      const res = await fetch(BACKEND_URL, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(property)
      });
      const newProperty = await res.json();
      console.log("Guardado:", newProperty);
      loadProperties(); // <--- recarga la lista después de crear
    }
    resetForm();
  } catch (err) {
    alert("Error al guardar la propiedad: " + err);
  }
});

// Cargar todas las propiedades
async function loadProperties() {
  try {
    const res = await fetch(BACKEND_URL);
    let properties = await res.json();

    if (!Array.isArray(properties)) {
      console.error("Expected array, got:", properties);
      properties = [];
    }

    const tbody = document.getElementById("property-list");
    tbody.innerHTML = "";

    properties.forEach(p => {
      const row = `
        <tr>
          <td>${p.id}</td>
          <td>${p.address}</td>
          <td>${p.price}</td>
          <td>${p.size}</td>
          <td>${p.description}</td>
          <td>
            <button onclick="editProperty(${p.id}, '${p.address}', ${p.price}, ${p.size}, '${p.description}')">Editar</button>
            <button onclick="deleteProperty(${p.id})">Eliminar</button>
          </td>
        </tr>
      `;
      tbody.innerHTML += row;
    });
  } catch (err) {
    alert("Error al cargar propiedades: " + err);
  }
}

// Editar propiedad (llenar el formulario)
function editProperty(id, address, price, size, description) {
  document.getElementById("property-id").value = id;
  document.getElementById("address").value = address;
  document.getElementById("price").value = price;
  document.getElementById("size").value = size;
  document.getElementById("description").value = description;
  document.getElementById("form-title").innerText = "Editar Propiedad";
}

// Eliminar propiedad
async function deleteProperty(id) {
  if (!confirm("¿Seguro que deseas eliminar esta propiedad?")) return;
  try {
    await fetch(`${BACKEND_URL}/${id}`, { method: "DELETE" });
    loadProperties();
  } catch (err) {
    alert("Error al eliminar: " + err);
  }
}

// Resetear formulario
function resetForm() {
  document.getElementById("property-id").value = "";
  document.getElementById("property-form").reset();
  document.getElementById("form-title").innerText = "Nueva Propiedad";
}
