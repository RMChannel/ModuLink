let currentDate = new Date();
let currentView = 'week';
let currentTimeInterval = null;
let cachedUsers = null;
let selectedUsers = [];
let editSelectedUsers = [];
let selectedEventElement = null;

document.addEventListener('DOMContentLoaded', function() {
    initializeCalendar();
});

function initializeCalendar() {
    renderView();
    setupEventListeners();

    // Update current time indicator every minute for week view
    if(currentView === 'week') {
        updateCurrentTimeLine();
        currentTimeInterval = setInterval(updateCurrentTimeLine, 60000);
    }
}

function setupEventListeners() {
    // Load users when create modal opens
    const createModal = document.getElementById('createEventModal');
    if(createModal) {
        createModal.addEventListener('show.bs.modal', function () {
            loadUsers();
            selectedUsers = [];
            updateSelectedUsersList('selectedUsersList', selectedUsers);

            // Set default date/time if not already set
            const now = new Date();
            const startInput = document.querySelector('input[name="inizio"]');
            const endInput = document.querySelector('input[name="fine"]');
            if(startInput && !startInput.value) {
                startInput.value = formatDateTimeLocal(now);
            }
            if(endInput && !endInput.value) {
                const endTime = new Date(now.getTime() + 3600000);
                endInput.value = formatDateTimeLocal(endTime);
            }
        });

        createModal.addEventListener('hidden.bs.modal', function () {
            document.getElementById('createEventForm').reset();
            selectedUsers = [];
            document.getElementById('userSearchInput').value = '';
            document.getElementById('userSearchResults').classList.remove('show');
        });
    }

    // Edit modal setup
    const editModal = document.getElementById('editEventModal');
    if(editModal) {
        editModal.addEventListener('show.bs.modal', function() {
            loadUsers();
        });

        editModal.addEventListener('hidden.bs.modal', function() {
            document.getElementById('editEventForm').reset();
            editSelectedUsers = [];
            document.getElementById('editUserSearchInput').value = '';
            document.getElementById('editUserSearchResults').classList.remove('show');
        });
    }

    // User search for create modal
    const userSearchInput = document.getElementById('userSearchInput');
    if(userSearchInput) {
        userSearchInput.addEventListener('input', function(e) {
            filterUsers(e.target.value, 'userSearchResults', selectedUsers);
        });

        userSearchInput.addEventListener('keydown', function(e) {
            if(e.key === 'Enter') {
                e.preventDefault();
                const results = document.getElementById('userSearchResults');
                const firstResult = results.querySelector('.user-search-item');
                if(firstResult) {
                    firstResult.click();
                }
            }
        });
    }

    // User search for edit modal
    const editUserSearchInput = document.getElementById('editUserSearchInput');
    if(editUserSearchInput) {
        editUserSearchInput.addEventListener('input', function(e) {
            filterUsers(e.target.value, 'editUserSearchResults', editSelectedUsers);
        });

        editUserSearchInput.addEventListener('keydown', function(e) {
            if(e.key === 'Enter') {
                e.preventDefault();
                const results = document.getElementById('editUserSearchResults');
                const firstResult = results.querySelector('.user-search-item');
                if(firstResult) {
                    firstResult.click();
                }
            }
        });
    }

    // View Switchers
    document.querySelectorAll('[data-view]').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            const targetView = this.getAttribute('data-view');
            if(currentView === targetView) return;
            switchView(targetView);
        });
    });

    // Navigation
    const prevBtn = document.getElementById('prevPeriod');
    const nextBtn = document.getElementById('nextPeriod');
    const todayBtn = document.getElementById('todayBtn');

    if(prevBtn) {
        prevBtn.addEventListener('click', (e) => {
            e.preventDefault();
            changePeriod(-1);
        });
    }

    if(nextBtn) {
        nextBtn.addEventListener('click', (e) => {
            e.preventDefault();
            changePeriod(1);
        });
    }

    if(todayBtn) {
        todayBtn.addEventListener('click', (e) => {
            e.preventDefault();
            currentDate = new Date();
            renderView();
        });
    }
}

function switchView(view) {
    console.log('Switching to view:', view);

    // Previeni cambio se già nella vista corretta
    if(currentView === view) return;

    const oldView = currentView;
    currentView = view;

    // Clear interval if switching away from week view
    if(currentTimeInterval) {
        clearInterval(currentTimeInterval);
        currentTimeInterval = null;
    }

    // Update active button - FIX: gestione più robusta
    document.querySelectorAll('[data-view]').forEach(btn => {
        if(!btn) return; // Safety check
        const btnView = btn.getAttribute('data-view');
        if(btnView === view) {
            btn.classList.add('active');
        } else {
            btn.classList.remove('active');
        }
    });

    // Hide all views first
    const weekView = document.getElementById('weekView');
    const monthView = document.getElementById('monthView');

    if(weekView) {
        weekView.style.display = 'none';
        if(weekView.classList) weekView.classList.remove('active');
    }

    if(monthView) {
        monthView.style.display = 'none';
        if(monthView.classList) monthView.classList.remove('active');
    }

    // Show target view
    const targetView = document.getElementById(`${view}View`);
    if(targetView) {
        targetView.style.display = 'flex';
        if(targetView.classList) targetView.classList.add('active');

        // Render the view con un piccolo delay per assicurarci che il DOM sia pronto
        setTimeout(() => {
            renderView();

            // Start time indicator for week view
            if(view === 'week') {
                setTimeout(() => {
                    updateCurrentTimeLine();
                    currentTimeInterval = setInterval(updateCurrentTimeLine, 60000);
                }, 100);
            }
        }, 10);
    } else {
        console.error('Target view not found:', `${view}View`);
        // Ripristina la vista precedente se fallisce
        currentView = oldView;
    }
}

function loadUsers() {
    // Se abbiamo già la cache, usala
    if(cachedUsers && cachedUsers.length > 0) {
        return;
    }

    fetch('/dashboard/gdu/api/users')
        .then(res => {
            if(!res.ok) throw new Error("API Error");
            return res.json();
        })
        .then(users => {
            cachedUsers = users;
        })
        .catch(err => {
            console.error(err);
            cachedUsers = [];
        });
}

function filterUsers(searchTerm, resultsId, excludeUsers) {
    const resultsContainer = document.getElementById(resultsId);
    if(!resultsContainer || !cachedUsers) return;

    if(!searchTerm || searchTerm.trim() === '') {
        resultsContainer.classList.remove('show');
        resultsContainer.innerHTML = '';
        return;
    }

    const term = searchTerm.toLowerCase();
    const excludeIds = excludeUsers.map(u => u.id);

    const filtered = cachedUsers.filter(user => {
        if(excludeIds.includes(user.id)) return false;
        const fullName = `${user.nome} ${user.cognome}`.toLowerCase();
        return fullName.includes(term);
    });

    if(filtered.length === 0) {
        resultsContainer.innerHTML = '<div class="user-search-empty">Nessun utente trovato</div>';
        resultsContainer.classList.add('show');
        return;
    }

    resultsContainer.innerHTML = '';
    filtered.forEach(user => {
        const item = document.createElement('div');
        item.className = 'user-search-item';
        item.textContent = `${user.nome} ${user.cognome}`;
        item.onclick = () => addUser(user, resultsId);
        resultsContainer.appendChild(item);
    });

    resultsContainer.classList.add('show');
}

function addUser(user, resultsId) {
    const isEdit = resultsId === 'editUserSearchResults';
    const targetArray = isEdit ? editSelectedUsers : selectedUsers;
    const listId = isEdit ? 'editSelectedUsersList' : 'selectedUsersList';
    const inputId = isEdit ? 'editUserSearchInput' : 'userSearchInput';

    // Controlla se già aggiunto
    if(targetArray.find(u => u.id === user.id)) {
        return;
    }

    targetArray.push(user);
    updateSelectedUsersList(listId, targetArray);

    // Clear search
    document.getElementById(inputId).value = '';
    document.getElementById(resultsId).classList.remove('show');
    document.getElementById(resultsId).innerHTML = '';
}

function removeUser(userId, listId, targetArray) {
    const index = targetArray.findIndex(u => u.id === userId);
    if(index > -1) {
        targetArray.splice(index, 1);
        updateSelectedUsersList(listId, targetArray);
    }
}

function updateSelectedUsersList(listId, users) {
    const container = document.getElementById(listId);
    if(!container) return;

    if(users.length === 0) {
        container.innerHTML = '<span class="text-muted small">Nessun partecipante selezionato</span>';
        return;
    }

    container.innerHTML = '';
    users.forEach(user => {
        const tag = document.createElement('div');
        tag.className = 'selected-user-tag';
        tag.innerHTML = `
            ${escapeHtml(user.nome)} ${escapeHtml(user.cognome)}
            <button type="button" class="remove-user-btn" onclick="removeUser(${user.id}, '${listId}', ${listId === 'selectedUsersList' ? 'selectedUsers' : 'editSelectedUsers'})" title="Rimuovi">
                <i class="bi bi-x-lg"></i>
            </button>
        `;
        container.appendChild(tag);
    });
}

function changePeriod(delta) {
    if (currentView === 'week') {
        currentDate.setDate(currentDate.getDate() + (delta * 7));
    } else if (currentView === 'month') {
        currentDate.setMonth(currentDate.getMonth() + delta);
    }
    renderView();
}

function renderView() {
    updateHeaderLabel();

    // Render solo la vista corrente
    if (currentView === 'week') {
        renderWeekView();
    } else if (currentView === 'month') {
        renderMonthView();
    }
}

function updateHeaderLabel() {
    const label = document.getElementById('currentPeriodLabel');
    if(!label) return;

    if (currentView === 'week') {
        const start = getStartOfWeek(currentDate);
        const end = new Date(start);
        end.setDate(end.getDate() + 6);

        if(start.getMonth() === end.getMonth()) {
            label.textContent = `${start.getDate()} - ${end.getDate()} ${start.toLocaleDateString('it-IT', {month:'long', year:'numeric'})}`;
        } else {
            label.textContent = `${start.getDate()} ${start.toLocaleDateString('it-IT', {month:'short'})} - ${end.getDate()} ${end.toLocaleDateString('it-IT', {month:'short', year:'numeric'})}`;
        }
    } else {
        label.textContent = currentDate.toLocaleDateString('it-IT', { year: 'numeric', month: 'long' });
    }
}

function getStartOfWeek(date) {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    return new Date(d.setDate(diff));
}

// ==========================================
// WEEK VIEW
// ==========================================
function renderWeekView() {
    const weekView = document.getElementById('weekView');
    if(!weekView) {
        console.error('Week view container not found');
        return;
    }

    const headerContainer = weekView.querySelector('.week-header');
    const weekBody = weekView.querySelector('.week-body');

    if(!headerContainer || !weekBody) {
        console.error('Week view elements not found');
        return;
    }

    // Clear previous content
    headerContainer.innerHTML = '<div class="time-column-header"></div>';
    weekBody.innerHTML = '';

    // Create week structure
    const weekContent = document.createElement('div');
    weekContent.className = 'week-content';

    const timeGrid = document.createElement('div');
    timeGrid.className = 'time-grid';

    const eventsGrid = document.createElement('div');
    eventsGrid.className = 'events-grid';

    // Height per hour in pixels
    const pxPerHour = 60;

    // Render Time Slots (24 hours)
    for(let i = 0; i < 24; i++) {
        const slot = document.createElement('div');
        slot.className = 'time-slot';
        const timeStr = i.toString().padStart(2, '0') + ':00';
        slot.innerHTML = `<div class="time-label">${timeStr}</div>`;
        timeGrid.appendChild(slot);
    }

    // Render Week Days
    const startOfWeek = getStartOfWeek(currentDate);
    const days = [];
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    for(let i = 0; i < 7; i++) {
        const d = new Date(startOfWeek);
        d.setDate(d.getDate() + i);
        days.push(d);

        const dayDate = new Date(d);
        dayDate.setHours(0, 0, 0, 0);
        const isToday = dayDate.getTime() === today.getTime();

        // Header
        const headerCell = document.createElement('div');
        headerCell.className = `day-header ${isToday ? 'today' : ''}`;
        headerCell.innerHTML = `
            <div class="day-header-day">${d.toLocaleDateString('it-IT', {weekday:'short'})}</div>
            <div class="day-header-date">${d.getDate()}</div>
        `;
        headerContainer.appendChild(headerCell);

        // Column
        const col = document.createElement('div');
        col.className = 'day-column';
        col.dataset.date = d.toISOString().split('T')[0];

        // Add click handler to create event
        col.addEventListener('click', function(e) {
            // Don't trigger if clicking on an event
            if(e.target.closest('.week-event')) return;

            const rect = col.getBoundingClientRect();
            const scrollTop = col.parentElement.parentElement.scrollTop;
            const clickY = e.clientY - rect.top + scrollTop;
            const clickedHour = Math.floor(clickY / pxPerHour);
            const clickedMinute = Math.floor(((clickY % pxPerHour) / pxPerHour) * 60);

            openCreateEventModal(d, clickedHour, clickedMinute);
        });

        eventsGrid.appendChild(col);
    }

    weekContent.appendChild(timeGrid);
    weekContent.appendChild(eventsGrid);
    weekBody.appendChild(weekContent);

    // Place Events
    const weekStart = new Date(days[0]);
    weekStart.setHours(0, 0, 0, 0);
    const weekEnd = new Date(days[6]);
    weekEnd.setHours(23, 59, 59, 999);

    if(typeof eventsData !== 'undefined' && eventsData.length > 0) {
        // Raggruppa eventi per colonna per gestire sovrapposizioni
        const eventsByColumn = {};

        eventsData.forEach((event) => {
            const start = new Date(event.data_ora_inizio);
            const end = event.data_fine ? new Date(event.data_fine) : new Date(start.getTime() + 3600000);

            if (end > weekStart && start < weekEnd) {
                let dayIndex = start.getDay() === 0 ? 6 : start.getDay() - 1;
                if(start < weekStart) dayIndex = 0;

                if(!eventsByColumn[dayIndex]) {
                    eventsByColumn[dayIndex] = [];
                }

                eventsByColumn[dayIndex].push({
                    event: event,
                    start: start,
                    end: end
                });
            }
        });

        // Renderizza eventi per ogni colonna
        Object.keys(eventsByColumn).forEach(dayIndex => {
            const columns = eventsGrid.querySelectorAll('.day-column');
            if(!columns[dayIndex]) return;

            const col = columns[dayIndex];
            const dayDate = new Date(days[dayIndex]);
            dayDate.setHours(0, 0, 0, 0);
            const dayEndTime = new Date(dayDate);
            dayEndTime.setHours(23, 59, 59, 999);

            // Ordina eventi per ora di inizio
            eventsByColumn[dayIndex].sort((a, b) => a.start - b.start);

            // Rileva sovrapposizioni
            const events = eventsByColumn[dayIndex];
            events.forEach((eventData, idx) => {
                const {event, start, end} = eventData;

                let effectiveStart = start < dayDate ? dayDate : start;
                let effectiveEnd = end > dayEndTime ? dayEndTime : end;

                const startH = effectiveStart.getHours() + effectiveStart.getMinutes() / 60;
                const endH = effectiveEnd.getHours() + effectiveEnd.getMinutes() / 60;
                const durationH = Math.max(endH - startH, 0.5);

                const el = document.createElement('div');
                el.className = 'week-event';
                el.dataset.eventId = event.id_evento;
                el.style.top = `${startH * pxPerHour}px`;
                el.style.height = `${durationH * pxPerHour}px`;

                // Controlla sovrapposizione con evento precedente
                if(idx > 0) {
                    const prevEvent = events[idx - 1];
                    if(prevEvent.end > start) {
                        el.classList.add('overlapping');
                    }
                }

                let content = `<div class="week-event-title">${escapeHtml(event.nome)}</div>`;
                content += `<div class="week-event-time">${formatTime(effectiveStart)} - ${formatTime(effectiveEnd)}</div>`;
                if(event.luogo) {
                    content += `<div class="week-event-location"><i class="bi bi-geo-alt"></i> ${escapeHtml(event.luogo)}</div>`;
                }

                el.innerHTML = content;
                el.addEventListener('click', function(e) {
                    e.stopPropagation();
                    selectEvent(el);
                    showEventDetails(event);
                });
                el.title = `${event.nome}\n${start.toLocaleString('it-IT')} - ${end.toLocaleString('it-IT')}`;

                col.appendChild(el);
            });
        });
    }

    // Add current time line if today is visible
    setTimeout(() => updateCurrentTimeLine(), 100);
}

function updateCurrentTimeLine() {
    // Remove existing line
    const existing = document.querySelector('.current-time-line');
    if(existing) existing.remove();

    if(currentView !== 'week') return;

    const now = new Date();
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const startOfWeek = getStartOfWeek(currentDate);
    const endOfWeek = new Date(startOfWeek);
    endOfWeek.setDate(endOfWeek.getDate() + 6);
    endOfWeek.setHours(23, 59, 59, 999);

    // Check if today is in current week
    if(now >= startOfWeek && now <= endOfWeek) {
        const dayIndex = now.getDay() === 0 ? 6 : now.getDay() - 1;
        const columns = document.querySelectorAll('.day-column');

        if(columns[dayIndex]) {
            const currentHour = now.getHours() + now.getMinutes() / 60;
            const pxPerHour = 60;
            const topPosition = currentHour * pxPerHour;

            const line = document.createElement('div');
            line.className = 'current-time-line';
            line.style.top = `${topPosition}px`;

            columns[dayIndex].appendChild(line);
        }
    }
}

// ==========================================
// MONTH VIEW
// ==========================================
function renderMonthView() {
    const monthView = document.getElementById('monthView');
    if(!monthView) {
        console.error('Month view container not found');
        return;
    }

    const container = monthView.querySelector('.month-grid');
    if(!container) {
        console.error('Month grid not found');
        return;
    }

    container.innerHTML = '';

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();

    const firstDayOfMonth = new Date(year, month, 1);
    let startDay = new Date(firstDayOfMonth);
    const dayOfWeek = startDay.getDay();
    const daysBack = (dayOfWeek === 0 ? 6 : dayOfWeek - 1);
    startDay.setDate(startDay.getDate() - daysBack);

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    for (let i = 0; i < 42; i++) {
        const d = new Date(startDay);
        d.setDate(d.getDate() + i);

        const cellDate = new Date(d);
        cellDate.setHours(0, 0, 0, 0);

        const isToday = cellDate.getTime() === today.getTime();
        const isOtherMonth = d.getMonth() !== month;

        const dayEl = document.createElement('div');
        dayEl.className = `month-day ${isOtherMonth ? 'other-month' : ''} ${isToday ? 'today' : ''}`;
        dayEl.innerHTML = `<span class="month-day-number">${d.getDate()}</span>`;

        // Add click handler to create event on this day
        dayEl.addEventListener('click', function(e) {
            // Don't trigger if clicking on an event
            if(e.target.closest('.month-event') || e.target.closest('.month-event-more')) return;

            // Default to 9:00 AM
            openCreateEventModal(d, 9, 0);
        });

        const eventsContainer = document.createElement('div');
        eventsContainer.className = 'month-events-container';

        if(typeof eventsData !== 'undefined') {
            const dayEvents = eventsData.filter(event => {
                const start = new Date(event.data_ora_inizio);
                return isSameDay(start, d);
            });

            const maxVisible = 3;
            dayEvents.slice(0, maxVisible).forEach((event) => {
                const evEl = document.createElement('div');
                evEl.className = 'month-event';
                evEl.dataset.eventId = event.id_evento;
                const startTime = new Date(event.data_ora_inizio);
                evEl.textContent = `${formatTime(startTime)} ${event.nome}`;
                evEl.addEventListener('click', function(e) {
                    e.stopPropagation();
                    selectEvent(evEl);
                    showEventDetails(event);
                });
                eventsContainer.appendChild(evEl);
            });

            if(dayEvents.length > maxVisible) {
                const more = document.createElement('div');
                more.className = 'month-event-more';
                more.textContent = `+${dayEvents.length - maxVisible} altri`;
                more.addEventListener('click', function(e) {
                    e.stopPropagation();
                });
                eventsContainer.appendChild(more);
            }
        }

        dayEl.appendChild(eventsContainer);
        container.appendChild(dayEl);
    }
}

// ==========================================
// EVENT SELECTION
// ==========================================
function selectEvent(element) {
    // Rimuovi selezione precedente
    if(selectedEventElement) {
        selectedEventElement.classList.remove('selected');
    }

    // Aggiungi nuova selezione
    if(element) {
        element.classList.add('selected');
        selectedEventElement = element;
    }
}

function clearEventSelection() {
    if(selectedEventElement) {
        selectedEventElement.classList.remove('selected');
        selectedEventElement = null;
    }
}

// ==========================================
// EVENT CREATION HELPER
// ==========================================
function openCreateEventModal(date, hour, minute) {
    const startDate = new Date(date);
    startDate.setHours(hour, minute, 0, 0);

    const endDate = new Date(startDate);
    endDate.setHours(hour + 1, minute, 0, 0);

    const startInput = document.querySelector('input[name="inizio"]');
    const endInput = document.querySelector('input[name="fine"]');

    if(startInput) startInput.value = formatDateTimeLocal(startDate);
    if(endInput) endInput.value = formatDateTimeLocal(endDate);

    const modal = new bootstrap.Modal(document.getElementById('createEventModal'));
    modal.show();
}

// ==========================================
// UTILITY FUNCTIONS
// ==========================================

function isSameDay(d1, d2) {
    return d1.getFullYear() === d2.getFullYear() &&
        d1.getMonth() === d2.getMonth() &&
        d1.getDate() === d2.getDate();
}

function formatTime(date) {
    return date.toLocaleTimeString('it-IT', {hour: '2-digit', minute:'2-digit'});
}

function formatDateTimeLocal(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}

function escapeHtml(text) {
    if(!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==========================================
// API ACTIONS
// ==========================================

function saveEvent() {
    const form = document.getElementById('createEventForm');
    if(!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const formData = new FormData(form);
    const partecipanti = selectedUsers.map(u => u.id);

    const payload = {
        nome: formData.get('nome'),
        luogo: formData.get('luogo') || '',
        inizio: formData.get('inizio'),
        fine: formData.get('fine'),
        partecipanti: partecipanti
    };

    // Disable button and show loading
    const saveBtn = document.querySelector('#createEventModal .btn-primary');
    const originalText = saveBtn.innerHTML;
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Salvataggio...';

    fetch('/dashboard/calendar/api/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) {
                throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            if(data.status === 'success') {
                const newEvent = {
                    id_evento: data.id,
                    nome: payload.nome,
                    luogo: payload.luogo,
                    data_ora_inizio: payload.inizio,
                    data_fine: payload.fine
                };

                if(typeof eventsData !== 'undefined') eventsData.push(newEvent);

                const modalEl = document.getElementById('createEventModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                if(modal) modal.hide();
                form.reset();
                selectedUsers = [];
                renderView();
            } else {
                alert('Errore durante la creazione dell\'evento');
            }
        })
        .catch(err => {
            console.error('Errore creazione evento:', err);
            alert('Errore di comunicazione con il server');
        })
        .finally(() => {
            saveBtn.disabled = false;
            saveBtn.innerHTML = originalText;
        });
}

let selectedEvent = null;

function showEventDetails(event) {
    selectedEvent = event;
    document.getElementById('detailTitle').textContent = event.nome;

    const s = new Date(event.data_ora_inizio);
    const e = event.data_fine ? new Date(event.data_fine) : s;

    document.getElementById('detailTime').textContent =
        `${s.toLocaleDateString('it-IT', {weekday: 'long', year: 'numeric', month: 'long', day: 'numeric'})} ${formatTime(s)} - ${formatTime(e)}`;

    document.getElementById('detailPlace').textContent = event.luogo || 'Nessun luogo specificato';

    const detailIdInput = document.getElementById('detailId');
    if(detailIdInput) {
        detailIdInput.value = event.id_evento;
    }

    const modal = new bootstrap.Modal(document.getElementById('eventDetailsModal'));
    modal.show();
}

function openEditModal() {
    if(!selectedEvent) return;

    // Chiudi modal dettagli
    const detailsModal = bootstrap.Modal.getInstance(document.getElementById('eventDetailsModal'));
    if(detailsModal) detailsModal.hide();

    // Popola form di modifica
    document.getElementById('editEventId').value = selectedEvent.id_evento;
    document.getElementById('editEventName').value = selectedEvent.nome;
    document.getElementById('editEventLocation').value = selectedEvent.luogo || '';

    const start = new Date(selectedEvent.data_ora_inizio);
    const end = selectedEvent.data_fine ? new Date(selectedEvent.data_fine) : start;

    document.getElementById('editEventStart').value = formatDateTimeLocal(start);
    document.getElementById('editEventEnd').value = formatDateTimeLocal(end);

    // Reset lista partecipanti
    editSelectedUsers = [];
    updateSelectedUsersList('editSelectedUsersList', editSelectedUsers);

    // Apri modal modifica
    const editModal = new bootstrap.Modal(document.getElementById('editEventModal'));
    editModal.show();
}

function updateEvent() {
    const form = document.getElementById('editEventForm');
    if(!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const formData = new FormData(form);
    const partecipanti = editSelectedUsers.map(u => u.id);

    const payload = {
        id: parseInt(formData.get('id')),
        nome: formData.get('nome'),
        luogo: formData.get('luogo') || '',
        inizio: formData.get('inizio'),
        fine: formData.get('fine'),
        partecipanti: partecipanti
    };

    const saveBtn = document.querySelector('#editEventModal .btn-primary');
    const originalText = saveBtn.innerHTML;
    saveBtn.disabled = true;
    saveBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Salvataggio...';

    fetch('/dashboard/calendar/api/update', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify(payload)
    })
        .then(res => {
            if (!res.ok) {
                throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            if(data.status === 'success') {
                // Aggiorna evento nella lista
                const idx = eventsData.findIndex(e => e.id_evento === payload.id);
                if(idx > -1) {
                    eventsData[idx] = {
                        id_evento: payload.id,
                        nome: payload.nome,
                        luogo: payload.luogo,
                        data_ora_inizio: payload.inizio,
                        data_fine: payload.fine
                    };
                }

                const modalEl = document.getElementById('editEventModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                if(modal) modal.hide();
                form.reset();
                editSelectedUsers = [];
                clearEventSelection();
                renderView();
            } else {
                alert('Errore durante l\'aggiornamento dell\'evento');
            }
        })
        .catch(err => {
            console.error('Errore aggiornamento evento:', err);
            alert('Errore di comunicazione con il server');
        })
        .finally(() => {
            saveBtn.disabled = false;
            saveBtn.innerHTML = originalText;
        });
}

function deleteEventFromEdit() {
    if(!selectedEvent) return;

    if(!confirm('Sei sicuro di voler eliminare questo evento?')) {
        return;
    }

    const deleteBtn = document.querySelector('#editEventModal .btn-danger');
    const originalText = deleteBtn.innerHTML;
    deleteBtn.disabled = true;
    deleteBtn.innerHTML = '<span class="spinner-border spinner-border-sm me-2"></span>Eliminazione...';

    fetch('/dashboard/calendar/api/delete', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({ id: selectedEvent.id_evento })
    })
        .then(res => {
            if(!res.ok) {
                throw new Error(`HTTP error! status: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            if(data.status === 'deleted') {
                if(typeof eventsData !== 'undefined') {
                    eventsData = eventsData.filter(e => e.id_evento !== selectedEvent.id_evento);
                }

                const modalEl = document.getElementById('editEventModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                if(modal) modal.hide();

                clearEventSelection();
                renderView();
            } else {
                alert('Errore durante l\'eliminazione dell\'evento');
            }
        })
        .catch(err => {
            console.error('Errore eliminazione evento:', err);
            alert('Errore di comunicazione con il server');
        })
        .finally(() => {
            deleteBtn.disabled = false;
            deleteBtn.innerHTML = originalText;
        });
}