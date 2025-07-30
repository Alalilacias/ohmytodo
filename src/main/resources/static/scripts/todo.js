// Global state
const state = {
    currentPage: 0,
    currentSort: 'id',
    currentDirection: 'asc',
    allUsers: []
};

// DOM Elements
const elements = {
    filterForm: document.getElementById('filterForm'),
    createTodoForm: document.getElementById('createTodoForm'),
    editTodoForm: document.getElementById('editTodoForm'),
    todoTableBody: document.getElementById('todo-table-body'),
    pagination: document.getElementById('pagination'),
    userDropdown: document.getElementById('create-todo-username'),
    alertContainer: document.getElementById('floating-alert'),
    authButtons: document.querySelector('.auth-buttons')
};

// Initialize application
document.addEventListener('DOMContentLoaded', async () => {
    await initializeModals();
    await fetchUsers();
    fetchTodos();
});

// Core functions
async function initializeModals() {
    // Initialize modals but don't store instances
    new bootstrap.Modal(document.getElementById('createTodoModal'));
    new bootstrap.Modal(document.getElementById('editTodoModal'));
}

async function fetchUsers() {
    try {
        const response = await fetch('/api/users/all');
        const result = await parseResponse(response);

        if (result.success) {
            state.allUsers = result.data;
            populateUserDropdown();
        } else {
            handleApiError(result.error);
        }
    } catch (error) {
        handleUnexpectedError(error, 'Failed to load users');
    }
}

function populateUserDropdown() {
    if (!elements.userDropdown) return;
    elements.userDropdown.innerHTML = `
        <option value="" selected disabled data-i18n="todotable.select_user">Select a user</option>
        ${state.allUsers.map(user => `<option value="${user.username}">${user.username}</option>`).join('')}
    `;
}

async function fetchTodos(page = 0) {
    showLoadingState();

    try {
        const url = buildTodosUrl(page);
        const response = await fetch(url);
        const result = await parseResponse(response);

        if (result.success) {
            renderTodoTable(result.data.content);
            renderPagination(result.data);
        } else {
            handleApiError(result.error);
        }
    } catch (error) {
        handleUnexpectedError(error, 'Failed to load todos');
    }
}

function buildTodosUrl(page) {
    const { text, username, sort, direction } = getFormState();
    const sortMapping = {
        'title': 'title',
        'username': 'user.username',
        'country': 'user.address.country',
        'completed': 'completed'
    };

    const params = new URLSearchParams({
        page,
        size: 10,
        sort: `${sortMapping[sort] || 'id'},${direction}`
    });

    if (text) params.append('text', text);
    if (username) params.append('username', username);

    return `api/todos/filter?${params.toString()}`;
}

function getFormState() {
    const text = elements.filterForm.querySelector('#filter-title').value.trim() || null;
    const username = elements.filterForm.querySelector('#filter-username').value.trim() || null;
    const sortField = elements.filterForm.querySelector('#filter-sort').value;

    if (sortField !== state.currentSort) {
        state.currentSort = sortField;
        state.currentDirection = 'asc';
    }

    return { text, username, sort: state.currentSort, direction: state.currentDirection };
}

// Enhanced response and error handling
async function parseResponse(response) {
    try {
        const data = await response.json();

        if (!response.ok) {
            // Handle OhMyTodoError structure
            if (data.status && data.message) {
                return {
                    success: false,
                    error: {
                        status: data.status,
                        message: data.message,
                        data: data
                    }
                };
            }
            // Fallback for non-standard error responses
            return {
                success: false,
                error: {
                    status: response.status,
                    message: data.message || `HTTP error ${response.status}`,
                    data: data
                }
            };
        }

        return { success: true, data };
    } catch (error) {
        // Handle cases where response isn't JSON
        return {
            success: false,
            error: {
                status: response.status,
                message: `Request failed with status ${response.status}`,
                data: null
            }
        };
    }
}

function handleApiError(error) {
    console.error('API Error:', error);

    // Special handling for 401 (Unauthorized)
    if (error.status === 401) {
        showAlert('Please log in to perform this action', 'warning');
        const loginButton = elements.authButtons?.querySelector('[data-bs-target="#loginModal"]');
        if (loginButton) loginButton.click();
        return;
    }

    // Special handling for 403 (Forbidden)
    if (error.status === 403) {
        showAlert('You are not authorized to perform this action', 'danger');
        return;
    }

    // Use the error message from the backend if available
    const message = error.data?.message || error.message || 'An unexpected error occurred';
    showAlert(message, 'danger');
}

function handleUnexpectedError(error, context) {
    console.error(`${context}:`, error);
    showAlert('An unexpected error occurred. Please try again.', 'danger');
}

// Rendering functions
function showLoadingState() {
    elements.todoTableBody.innerHTML = `
        <tr><td colspan="5" class="text-center">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </td></tr>`;
}

function renderTodoTable(todos) {
    if (todos.length === 0) {
        elements.todoTableBody.innerHTML = `
            <tr><td colspan="5" data-i18n="todotable.noresults">No results found</td></tr>`;
        return;
    }

    elements.todoTableBody.innerHTML = todos.map(todo => `
        <tr>
            <td>${todo.title}</td>
            <td>${todo.userDto.username}</td>
            <td>${todo.userDto.country}</td>
            <td>
                ${todo.completed
                    ? '<i class="bi bi-check-circle text-success"></i>'
                    : '<i class="bi bi-x-circle text-danger"></i>'}
            </td>
            <td class="d-flex justify-content-center gap-2">
                <button class="btn btn-warning btn-sm edit-btn" data-id="${todo.id}">
                    <i class="bi bi-pencil"></i>
                </button>
                <button class="btn btn-danger btn-sm delete-btn" data-id="${todo.id}">
                    <i class="bi bi-trash"></i>
                </button>
            </td>
        </tr>
    `).join('');

    setupTableEventListeners();
}

function setupTableEventListeners() {
    document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', () => openEditModal(btn.dataset.id));
    });

    document.querySelectorAll('.delete-btn').forEach(btn => {
        btn.addEventListener('click', () => deleteTodo(btn.dataset.id));
    });
}

function renderPagination(data) {
    elements.pagination.innerHTML = `
        <li class="page-item ${data.first ? 'disabled' : ''}">
            <a class="page-link" href="#" data-action="prev">
                &#171; <span data-i18n="todotable.pagination.previous">Previous</span>
            </a>
        </li>
        ${Array.from({ length: data.totalPages }, (_, i) => `
            <li class="page-item ${i === data.number ? 'active' : ''}">
                <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
            </li>
        `).join('')}
        <li class="page-item ${data.last ? 'disabled' : ''}">
            <a class="page-link" href="#" data-action="next">
                <span data-i18n="todotable.pagination.next">Next</span> &#187;
            </a>
        </li>
    `;

    elements.pagination.querySelectorAll('[data-page]').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            state.currentPage = parseInt(link.dataset.page);
            fetchTodos(state.currentPage);
        });
    });

    elements.pagination.querySelector('[data-action="prev"]').addEventListener('click', (e) => {
        e.preventDefault();
        if (!data.first) {
            state.currentPage--;
            fetchTodos(state.currentPage);
        }
    });

    elements.pagination.querySelector('[data-action="next"]').addEventListener('click', (e) => {
        e.preventDefault();
        if (!data.last) {
            state.currentPage++;
            fetchTodos(state.currentPage);
        }
    });
}

// Form handlers
elements.filterForm?.addEventListener('submit', (e) => {
    e.preventDefault();
    const sortField = e.target.querySelector('#filter-sort').value;

    if (sortField === state.currentSort) {
        state.currentDirection = state.currentDirection === 'asc' ? 'desc' : 'asc';
    } else {
        state.currentSort = sortField;
        state.currentDirection = 'asc';
    }

    state.currentPage = 0;
    fetchTodos();
});

// FIXED CREATE TODO FORM HANDLER - IMPROVED MODAL CLOSING BEHAVIOR
elements.createTodoForm?.addEventListener('submit', async function(e) {
    e.preventDefault();
    const form = e.target;
    const submitButton = form.querySelector('button[type="submit"]');
    const originalButtonText = submitButton.innerHTML;
    const modal = bootstrap.Modal.getInstance(document.getElementById('createTodoModal'));
    const modalBody = form.querySelector('.modal-body');

    try {
        // Show loading state on button
        submitButton.disabled = true;
        submitButton.innerHTML = `
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            <span data-i18n="todotable.creating">Creating...</span>
        `;

        // Get selected user from dropdown
        const selectedUsername = document.getElementById('create-todo-username').value;
        const selectedUser = state.allUsers.find(user => user.username === selectedUsername);

        if (!selectedUser) {
            throw new Error('Selected user not found');
        }

        const title = document.getElementById('create-todo-title').value;
        const completed = document.querySelector('#createTodoForm input[name="completed"]').checked;

        const response = await fetch('/api/todos', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({
                userId: selectedUser.id,
                title,
                completed
            })
        });

        const result = await parseResponse(response);

        if (result.success) {
            // Show success message in modal
            const successMessage = document.createElement('div');
            successMessage.className = 'alert alert-success mt-3';
            successMessage.textContent = 'Todo created successfully!';
            modalBody.appendChild(successMessage);

            // Close modal after 2 seconds
            setTimeout(() => {
                modal.hide();
                form.reset();
                fetchTodos(state.currentPage);
                successMessage.remove();
            }, 2000);
        } else {
            // Show error message in modal
            const errorMessage = document.createElement('div');
            errorMessage.className = 'alert alert-danger mt-3';
            errorMessage.textContent = result.error.message || 'Failed to create todo';
            modalBody.appendChild(errorMessage);

            // Remove error message after 5 seconds
            setTimeout(() => {
                errorMessage.remove();
            }, 5000);

            throw result.error;
        }
    } catch (error) {
        if (!error?.status) {
            // Show unexpected error in modal
            const errorMessage = document.createElement('div');
            errorMessage.className = 'alert alert-danger mt-3';
            errorMessage.textContent = 'An unexpected error occurred';
            modalBody.appendChild(errorMessage);

            // Remove error message after 5 seconds
            setTimeout(() => {
                errorMessage.remove();
            }, 5000);
        }
    } finally {
        // Restore button state
        submitButton.disabled = false;
        submitButton.innerHTML = originalButtonText;
    }
});

// ENHANCED EDIT TODO FORM HANDLER WITH COMPLETE ERROR HANDLING
elements.editTodoForm?.addEventListener('submit', async function(e) {
    e.preventDefault();
    const form = e.target;
    const submitButton = form.querySelector('button[type="submit"]');
    const originalButtonText = submitButton.innerHTML;
    const modal = bootstrap.Modal.getInstance(document.getElementById('editTodoModal'));
    const modalBody = form.querySelector('.modal-body');

    // Clear any existing error messages
    const existingAlerts = modalBody.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());

    try {
        // Show loading state on button
        submitButton.disabled = true;
        submitButton.innerHTML = `
            <span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
            <span data-i18n="todotable.saving">Saving...</span>
        `;

        // Get values directly from inputs
        const id = document.getElementById('edit-todo-id').value;
        const title = document.getElementById('edit-todo-title').value;
        const completed = document.getElementById('edit-todo-completed').checked;

        const response = await fetch('/api/todos', {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({ todoId: id, title, completed })
        });

        const result = await parseResponse(response);

        if (result.success) {
            // Show success message in modal
            const successMessage = document.createElement('div');
            successMessage.className = 'alert alert-success mt-3';
            successMessage.textContent = 'Todo updated successfully!';
            modalBody.appendChild(successMessage);

            // Close modal after 2 seconds and refresh
            setTimeout(() => {
                modal.hide();
                fetchTodos(state.currentPage);
                successMessage.remove();
            }, 2000);
        } else {
            // Handle specific error cases
            if (result.error.status === 401 || result.error.status === 403) {
                // For auth errors, show message and optionally trigger login
                const errorMessage = document.createElement('div');
                errorMessage.className = 'alert alert-danger mt-3';
                errorMessage.textContent = result.error.message;
                modalBody.appendChild(errorMessage);

                if (result.error.status === 401) {
                    const loginButton = elements.authButtons?.querySelector('[data-bs-target="#loginModal"]');
                    if (loginButton) loginButton.click();
                }
            } else {
                // For other errors, show message in modal
                const errorMessage = document.createElement('div');
                errorMessage.className = 'alert alert-danger mt-3';
                errorMessage.textContent = result.error.message || 'Failed to update todo';
                modalBody.appendChild(errorMessage);

                // Remove error after 5 seconds
                setTimeout(() => {
                    errorMessage.remove();
                }, 5000);
            }
        }
    } catch (error) {
        // Handle network errors or unexpected errors
        const errorMessage = document.createElement('div');
        errorMessage.className = 'alert alert-danger mt-3';

        if (error?.status) {
            errorMessage.textContent = error.message || 'API request failed';
        } else {
            errorMessage.textContent = 'An unexpected error occurred';
            console.error('Update todo failed:', error);
        }

        modalBody.appendChild(errorMessage);

        // Remove error after 5 seconds
        setTimeout(() => {
            errorMessage.remove();
        }, 5000);
    } finally {
        // Restore button state
        submitButton.disabled = false;
        submitButton.innerHTML = originalButtonText;
    }
});

// FINAL FIXED DELETE FUNCTION - Updated for plain text responses
async function deleteTodo(todoId) {
    if (!todoId || !confirm('Are you sure you want to delete this todo?')) {
        return;
    }

    try {
        // Show loading state
        try {
            showAlert('Deleting todo...', 'info');
        } catch (alertError) {
            console.error('Could not show alert:', alertError);
        }

        const response = await fetch(`/api/todos/${todoId}`, {
            method: 'DELETE',
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        // First check if response is ok (2xx status)
        if (response.ok) {
            // Handle plain text success response
            const successMessage = await response.text();
            showAlert(successMessage || 'Todo deleted successfully!', 'success');
            fetchTodos(state.currentPage);
            return;
        }

        // Try to parse error response as JSON (for error cases)
        let errorData;
        try {
            errorData = await response.json();
        } catch (e) {
            // If response isn't JSON, use status text
            throw {
                status: response.status,
                message: response.statusText || 'Delete failed'
            };
        }

        // Handle structured OhMyTodoError
        if (errorData.status && errorData.message) {
            const error = {
                status: errorData.status,
                message: errorData.message,
                data: errorData
            };

            // Special handling for auth errors
            if (error.status === 401) {
                showAlert(error.message || 'Please login to delete todos', 'danger');
                const loginButton = document.querySelector('[data-bs-target="#loginModal"]');
                if (loginButton) loginButton.click();
            }
            else if (error.status === 403) {
                showAlert(error.message || 'You are not authorized to delete this todo', 'danger');
            }
            else {
                showAlert(error.message || 'Failed to delete todo', 'danger');
            }
            return;
        }

        // Fallback for non-standard error responses
        throw {
            status: response.status,
            message: errorData.message || `Delete failed with status ${response.status}`
        };

    } catch (error) {
        console.error('Delete error:', error);

        // Network errors or other exceptions
        try {
            if (error?.status) {
                showAlert(error.message || 'Delete request failed', 'danger');
            } else {
                showAlert('An unexpected error occurred while deleting', 'danger');
            }
        } catch (alertError) {
            console.error('Could not show error alert:', alertError);
        }
    }
}

// Todo actions
async function openEditModal(todoId) {
    try {
        const response = await fetch(`/api/todos/${todoId}`);
        const result = await parseResponse(response);

        if (result.success) {
            const todo = result.data;
            document.getElementById('edit-todo-id').value = todo.id;
            document.getElementById('edit-todo-title').value = todo.title;
            document.getElementById('edit-todo-completed').checked = todo.completed;
            new bootstrap.Modal(document.getElementById('editTodoModal')).show();
        } else {
            handleApiError(result.error);
        }
    } catch (error) {
        handleUnexpectedError(error, 'Failed to open edit modal');
    }
}

// Utility functions
function showAlert(message, type) {
    const alertContainer = elements.alertContainer;
    if (!alertContainer) {
        console.error('Alert container not found');
        return;
    }

    alertContainer.innerHTML = `
        <div class="alert alert-${type} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;

    const alertElement = alertContainer.querySelector('.alert');
    const bsAlert = bootstrap.Alert.getOrCreateInstance(alertElement); // ✅ explicitly initialize

    setTimeout(() => {
        bsAlert.close(); // ✅ works now
    }, 5000);
}