let currentPage = 0;
let currentSort = 'id';
let currentDirection = 'asc';

function getFormState() {
  const textInput = document.getElementById('filter-title').value.trim();
  const usernameInput = document.getElementById('filter-username').value.trim();
  const sortField = document.getElementById('filter-sort').value;

  if (sortField !== currentSort) {
    currentSort = sortField;
    currentDirection = 'asc';
  }

  const text = textInput === '' ? null : textInput;
  const username = usernameInput === '' ? null : usernameInput;

  return { text, username, sort: currentSort, direction: currentDirection };
}


async function fetchTodos(page = 0) {
  const { text, username, sort, direction } = getFormState();

  // Map simple sort values to actual entity property paths
  const sortMapping = {
    'title': 'title',
    'username': 'user.username',    // Map to nested property
    'country': 'user.address.country', // Map to nested property
    'completed': 'completed'
  };

  const sortProperty = sortMapping[sort] || 'id'; // Default to 'id' if not found

  const params = new URLSearchParams();

  if (text !== null) params.append('text', text);
  if (username !== null) params.append('username', username);

  params.append('page', page);
  params.append('size', '10');
  params.append('sort', `${sortProperty},${direction}`); // Use mapped property

  const url = `api/todos/filter?${params.toString()}`;

  const res = await fetch(url);
  const data = await res.json();

  const tbody = document.getElementById('todo-table-body');
  tbody.innerHTML = '';

  if (data.content.length === 0) {
    tbody.innerHTML = `<tr><td colspan="4" data-i18n="todotable.noresults">No results found</td></tr>`;
  } else {
    data.content.forEach(todo => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${todo.title}</td>
        <td>${todo.userDto.username}</td>
        <td>${todo.userDto.country}</td>
        <td>
          ${todo.completed
            ? '<i class="bi bi-check-circle text-success"></i>'
            : '<i class="bi bi-x-circle text-danger"></i>'}
        </td>
      `;
      tbody.appendChild(row);
    });
  }

  renderPagination(data);
}

function renderPagination(data) {
  const pagination = document.getElementById('pagination');
  pagination.innerHTML = '';

  const prev = document.createElement('li');
  prev.className = `page-item ${data.first ? 'disabled' : ''}`;
  prev.innerHTML = `<a class="page-link" href="#">&#171; <span data-i18n="todotable.pagination.previous">Previous</span></a>`;
  prev.onclick = e => {
    e.preventDefault();
    if (!data.first) {
      currentPage--;
      fetchTodos(currentPage);
    }
  };
  pagination.appendChild(prev);

  for (let i = 0; i < data.totalPages; i++) {
    const li = document.createElement('li');
    li.className = `page-item ${i === data.number ? 'active' : ''}`;
    li.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
    li.onclick = e => {
      e.preventDefault();
      currentPage = i;
      fetchTodos(currentPage);
    };
    pagination.appendChild(li);
  }

  const next = document.createElement('li');
  next.className = `page-item ${data.last ? 'disabled' : ''}`;
  next.innerHTML = `<a class="page-link" href="#"><span data-i18n="todotable.pagination.next">Next</span> &#187;</a>`;
  next.onclick = e => {
    e.preventDefault();
    if (!data.last) {
      currentPage++;
      fetchTodos(currentPage);
    }
  };
  pagination.appendChild(next);
}

document.getElementById('filterForm').addEventListener('submit', function (e) {
  e.preventDefault();
  const sortField = document.getElementById('filter-sort').value;

  if (sortField === currentSort) {
    currentDirection = currentDirection === 'asc' ? 'desc' : 'asc';
  } else {
    currentSort = sortField;
    currentDirection = 'asc';
  }

  currentPage = 0;
  fetchTodos(0);
});

// Initial fetch
fetchTodos();