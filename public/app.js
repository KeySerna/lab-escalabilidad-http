const resultDiv = document.getElementById('result');
const errorDiv = document.getElementById('error');

function clearMessages() {
    resultDiv.textContent = '';
    errorDiv.textContent = '';
}

function showLoading(button) {
    button.disabled = true;
    button.dataset.originalText = button.textContent;
    button.textContent = 'Cargando...';
}

function hideLoading(button) {
    button.disabled = false;
    button.textContent = button.dataset.originalText;
}

async function callService(url, button) {
    clearMessages();
    showLoading(button);
    try {
        const res = await fetch(url);

        if (!res.ok) {
            let msg = 'Error del servidor (' + res.status + ')';
            try {
                const data = await res.json();
                if (data.error) msg = data.error;
            } catch (e) { /* respuesta sin JSON válido */ }
            errorDiv.textContent = msg;
            return;
        }

        const data = await res.json();
        resultDiv.textContent = JSON.stringify(data);

    } catch (networkError) {
        errorDiv.textContent = 'No se pudo conectar con el servidor: ' + networkError.message;
    } finally {
        hideLoading(button);
    }
}

document.getElementById('form-greeting').addEventListener('submit', function (e) {
    e.preventDefault();
    const name = document.getElementById('name').value;
    const button = e.target.querySelector('button');
    callService('/greeting?name=' + encodeURIComponent(name), button);
});

document.getElementById('form-square').addEventListener('submit', function (e) {
    e.preventDefault();
    const value = document.getElementById('value').value;
    const button = e.target.querySelector('button');
    callService('/square?value=' + encodeURIComponent(value), button);
});

document.getElementById('btn-time').addEventListener('click', function (e) {
    callService('/server-time', e.target);
});