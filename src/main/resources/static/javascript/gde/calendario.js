let currentDate = new Date();
let currentView = 'week';
let currentTimeInterval = null;
let cachedUsers = null;
let selectedUsers = [];
let editSelectedUsers = [];
let selectedEvent = null;

// Gestione doppia sorgente dati (Cache)
let storedEvents = [];
let storedTasks = [];
let eventsData = []; // Array finale renderizzato

document.addEventListener('DOMContentLoaded', function() {
    initializeCalendar();

    // Chiudi menu contestuale se clicco altrove
    document.addEventListener('click', function(e) {
        if (!e.target.closest('#contextMenu')) {
            closeContextMenu();
        }
    });
});

function initializeCalendar() {
    fetchEvents();
    setupEventListeners();

    if(currentView === 'week') {
        updateCurrentTimeLine();
        currentTimeInterval = setInterval(updateCurrentTimeLine, 60000);
    }
}

function setupEventListeners() {
    const refreshBtn = document.getElementById('refreshEventsBtn');
    if(refreshBtn) refreshBtn.addEventListener('click', (e) => { e.preventDefault(); fetchEvents(); });

    // Modali User Load
    const createModal = document.getElementById('createEventModal');
    if(createModal) {
        createModal.addEventListener('show.bs.modal', function () {
            loadUsers();
            selectedUsers = [];
            updateSelectedUsersList('selectedUsersList', selectedUsers);
            document.getElementById('dateErrorAlert').classList.add('d-none');

            const now = new Date();
            const startInput = document.querySelector('input[name="inizio"]');
            const endInput = document.querySelector('input[name="fine"]');

            if(startInput) startInput.min = formatDateTimeLocal(now);

            if(startInput && !startInput.value) {
                now.setMinutes(0,0,0);
                now.setHours(now.getHours() + 1);
                startInput.value = formatDateTimeLocal(now);
            }
            if(endInput && !endInput.value) {
                const endTime = new Date(now);
                endTime.setHours(endTime.getHours() + 1);
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

    const editModal = document.getElementById('editEventModal');
    if(editModal) {
        editModal.addEventListener('show.bs.modal', () => {
            loadUsers();
            const alertBox = document.getElementById('editDateErrorAlert');
            if(alertBox) alertBox.classList.add('d-none');
        });
        editModal.addEventListener('hidden.bs.modal', () => {
            document.getElementById('editEventForm').reset();
            editSelectedUsers = [];
            document.getElementById('editUserSearchInput').value = '';
            document.getElementById('editUserSearchResults').classList.remove('show');
        });
    }

    // Filtri Eventi/Task
    const filterEventsBtn = document.getElementById('filterEvents');
    const filterTasksBtn = document.getElementById('filterTasks');
    if(filterEventsBtn) filterEventsBtn.addEventListener('change', () => { mergeEventsAndTasks(); renderView(); });
    if(filterTasksBtn) filterTasksBtn.addEventListener('change', () => { mergeEventsAndTasks(); renderView(); });

    // Ricerca Utenti
    document.getElementById('userSearchInput')?.addEventListener('input', (e) => filterUsers(e.target.value, 'userSearchResults', selectedUsers));
    document.getElementById('editUserSearchInput')?.addEventListener('input', (e) => filterUsers(e.target.value, 'editUserSearchResults', editSelectedUsers));

    // View Switcher
    document.querySelectorAll('[data-view]').forEach(btn => {
        btn.addEventListener('click', function(e) {
            e.preventDefault();
            switchView(this.getAttribute('data-view'));
        });
    });

    // Navigazione
    document.getElementById('prevPeriod')?.addEventListener('click', () => changePeriod(-1));
    document.getElementById('nextPeriod')?.addEventListener('click', () => changePeriod(1));
    document.getElementById('todayBtn')?.addEventListener('click', () => { currentDate = new Date(); renderView(); });
}

function switchView(view) {
    if(currentView === view) return;
    const oldView = currentView;
    currentView = view;
    if(currentTimeInterval) { clearInterval(currentTimeInterval); currentTimeInterval = null; }

    document.querySelectorAll('[data-view]').forEach(btn => {
        btn.classList.toggle('active', btn.getAttribute('data-view') === view);
    });

    document.getElementById('weekView').style.display = 'none';
    document.getElementById('monthView').style.display = 'none';

    const target = document.getElementById(`${view}View`);
    target.style.display = 'flex';
    setTimeout(() => {
        renderView();
        if(view === 'week') {
            updateCurrentTimeLine();
            currentTimeInterval = setInterval(updateCurrentTimeLine, 60000);
        }
    }, 10);
}

// DATA FETCHING & MERGING
function fetchEvents() {
    const icon = document.querySelector('#refreshEventsBtn i');
    if(icon) { icon.style.transition = 'transform 0.5s'; icon.style.transform = 'rotate(180deg)'; }

    const p1 = fetch('/dashboard/calendar/api/get').then(r => r.ok ? r.json() : []).then(d => storedEvents = d.map(e => ({...e, type: 'event'})));
    const p2 = fetch('/dashboard/calendar/api/getet').then(r => r.ok ? r.json() : []).then(d => storedTasks = d.map(t => ({...t, type: 'task'})));

    Promise.all([p1, p2])
        .then(() => { mergeEventsAndTasks(); renderView(); })
        .catch(e => console.error("Err fetch", e))
        .finally(() => { if(icon) setTimeout(() => icon.style.transform = 'none', 500); });
}

function mergeEventsAndTasks() {
    eventsData = [];
    const showEvents = document.getElementById('filterEvents')?.checked ?? true;
    const showTasks = document.getElementById('filterTasks')?.checked ?? true;

    if(showEvents) eventsData = eventsData.concat(storedEvents);
    if(showTasks) eventsData = eventsData.concat(storedTasks);
}

function loadUsers() {
    if(cachedUsers) return;
    fetch('/dashboard/calendar/api/users').then(r => r.json()).then(u => cachedUsers = u).catch(() => cachedUsers = []);
}

// RENDERING
function renderView() {
    updateHeaderLabel();
    if(currentView === 'week') renderWeekView();
    else renderMonthView();
}

// WEEK VIEW (Con algoritmo overlapping colonne)
function renderWeekView() {
    const weekBody = document.querySelector('.week-body');
    const headerContainer = document.querySelector('.week-header');
    if(!weekBody) return;

    headerContainer.innerHTML = '<div class="time-column-header"></div>';
    weekBody.innerHTML = '';

    const content = document.createElement('div');
    content.className = 'week-content';

    const timeGrid = document.createElement('div');
    timeGrid.className = 'time-grid';
    for(let i=0; i<24; i++) {
        timeGrid.innerHTML += `<div class="time-slot"><div class="time-label">${i.toString().padStart(2,'0')}:00</div></div>`;
    }

    const eventsGrid = document.createElement('div');
    eventsGrid.className = 'events-grid';

    const startOfWeek = getStartOfWeek(currentDate);
    const today = new Date(); today.setHours(0,0,0,0);

    for(let i=0; i<7; i++) {
        const d = new Date(startOfWeek);
        d.setDate(d.getDate() + i);
        const isToday = d.getTime() === today.getTime();

        // Header
        headerContainer.innerHTML += `
            <div class="day-header ${isToday?'today':''}">
                <div class="day-header-day">${d.toLocaleDateString('it-IT',{weekday:'short'})}</div>
                <div class="day-header-date">${d.getDate()}</div>
            </div>`;

        // Column
        const col = document.createElement('div');
        col.className = 'day-column';
        col.setAttribute('data-date', d.toISOString());

        // Click sinistro: crea evento
        col.addEventListener('click', (e) => {
            if(e.target.closest('.week-event')) return;
            const rect = col.getBoundingClientRect();
            const clickY = e.clientY - rect.top + col.parentElement.parentElement.scrollTop;
            const mins = Math.floor((clickY/60)*60);
            const rounded = Math.round(mins/30)*30;
            openCreateEventModal(d, Math.floor(rounded/60), rounded%60);
        });

        // Click destro: Context Menu
        col.addEventListener('contextmenu', (e) => handleContextMenu(e, d));

        // RENDER EVENTI DELLA GIORNATA
        const dayEvents = eventsData.filter(e => {
            const start = new Date(e.data_ora_inizio);
            const end = e.data_fine ? new Date(e.data_fine) : start;

            // Per task multi-giorno: mostra se il giorno corrente è nel range
            if(e.type === 'task' && e.data_fine) {
                const dayStart = new Date(d);
                dayStart.setHours(0,0,0,0);
                const dayEnd = new Date(d);
                dayEnd.setHours(23,59,59,999);

                return start <= dayEnd && end >= dayStart;
            }

            // Per eventi normali: solo se inizia quel giorno
            return start.getDate() === d.getDate() &&
                start.getMonth() === d.getMonth() &&
                start.getFullYear() === d.getFullYear();
        });

        if(dayEvents.length > 0) {
            layoutDayEvents(dayEvents, col, d);
        }

        eventsGrid.appendChild(col);
    }

    content.append(timeGrid, eventsGrid);
    weekBody.appendChild(content);
}

// Algoritmo per gestire eventi sovrapposti visivamente
function layoutDayEvents(events, container, currentDay) {
    events.sort((a,b) => new Date(a.data_ora_inizio) - new Date(b.data_ora_inizio));

    const items = events.map(e => {
        const start = new Date(e.data_ora_inizio);
        const end = e.data_fine ? new Date(e.data_fine) : new Date(start.getTime()+3600000);

        // Per task multi-giorno: calcola l'ora di inizio/fine per il giorno corrente
        let startH, endH;

        if(e.type === 'task' && e.data_fine) {
            const dayStart = new Date(currentDay);
            dayStart.setHours(0,0,0,0);
            const dayEnd = new Date(currentDay);
            dayEnd.setHours(23,59,59,999);

            // Se il task inizia prima di oggi, parte da mezzanotte
            startH = start <= dayStart ? 0 : (start.getHours() + start.getMinutes()/60);

            // Se il task finisce dopo oggi, arriva a mezzanotte
            endH = end >= dayEnd ? 24 : (end.getHours() + end.getMinutes()/60);
        } else {
            startH = start.getHours() + start.getMinutes()/60;
            endH = end.getHours() + end.getMinutes()/60;
            if(endH <= startH) endH = startH + 1;
        }

        return { event: e, start: startH, end: endH, col: 0, maxCols: 1 };
    });

    // Calcola colonne
    const columns = [];
    items.forEach(item => {
        let placed = false;
        for(let i=0; i<columns.length; i++) {
            const lastInCol = columns[i][columns[i].length-1];
            if(item.start >= lastInCol.end) {
                columns[i].push(item);
                item.col = i;
                placed = true;
                break;
            }
        }
        if(!placed) {
            columns.push([item]);
            item.col = columns.length - 1;
        }
    });

    const maxCols = columns.length;

    items.forEach(item => {
        const el = document.createElement('div');
        const isMultiDay = item.event.type === 'task' && item.event.data_fine &&
            (new Date(item.event.data_fine) - new Date(item.event.data_ora_inizio)) > 86400000;

        el.className = `week-event ${item.event.type === 'task' ? 'task-event' : ''} ${isMultiDay ? 'multi-day-task' : ''}`;

        if(item.event.type === 'task') {
            el.style.backgroundColor = '#fff3cd';
            el.style.borderLeft = '4px solid #ffc107';
            el.style.color = '#856404';
        }

        // Mostra indicatore per task multi-giorno
        const taskLabel = isMultiDay ?
            `📅 ${item.event.nome}` :
            item.event.nome;

        el.textContent = taskLabel;
        el.style.top = `${item.start * 60}px`;
        el.style.height = `${(item.end - item.start) * 60}px`;

        const width = 100 / maxCols;
        el.style.width = `calc(${width}% - 4px)`;
        el.style.left = `${item.col * width}%`;

        el.addEventListener('click', (ev) => { ev.stopPropagation(); showEventDetails(item.event); });
        el.addEventListener('contextmenu', (ev) => { ev.stopPropagation(); });

        container.appendChild(el);
    });
}

// MONTH VIEW
function renderMonthView() {
    const container = document.querySelector('.month-grid');
    if(!container) return;
    container.innerHTML = '';

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    let startDay = new Date(firstDay);
    const dayOfWeek = startDay.getDay();
    const daysBack = dayOfWeek === 0 ? 6 : dayOfWeek - 1;
    startDay.setDate(startDay.getDate() - daysBack);

    for(let i=0; i<42; i++) {
        const d = new Date(startDay);
        d.setDate(d.getDate() + i);

        const cell = document.createElement('div');
        const isOther = d.getMonth() !== month;
        const isToday = isSameDay(d, new Date());

        cell.className = `month-day ${isOther?'other-month':''} ${isToday?'today':''}`;
        cell.setAttribute('data-date', d.toISOString());
        cell.innerHTML = `<span class="month-day-number">${d.getDate()}</span>`;

        // Filtra eventi per il giorno, includendo task multi-giorno
        const dayEvents = eventsData.filter(e => {
            const start = new Date(e.data_ora_inizio);
            const end = e.data_fine ? new Date(e.data_fine) : start;

            if(e.type === 'task' && e.data_fine) {
                const dayStart = new Date(d);
                dayStart.setHours(0,0,0,0);
                const dayEnd = new Date(d);
                dayEnd.setHours(23,59,59,999);
                return start <= dayEnd && end >= dayStart;
            }

            return isSameDay(start, d);
        });

        const evCont = document.createElement('div');
        evCont.className = 'month-events-container';

        dayEvents.slice(0, 3).forEach(ev => {
            const el = document.createElement('div');
            const isMultiDay = ev.type === 'task' && ev.data_fine &&
                (new Date(ev.data_fine) - new Date(ev.data_ora_inizio)) > 86400000;

            el.className = `month-event ${isMultiDay ? 'multi-day-task' : ''}`;
            if(ev.type === 'task') {
                el.style.backgroundColor = '#ffc107';
                el.style.color = '#000';
            }
            el.textContent = isMultiDay ? `📅 ${ev.nome}` : ev.nome;
            el.onclick = (e) => { e.stopPropagation(); showEventDetails(ev); };
            evCont.appendChild(el);
        });

        if(dayEvents.length > 3) {
            evCont.innerHTML += `<div class="month-event-more">+${dayEvents.length-3} altri</div>`;
        }

        cell.appendChild(evCont);
        cell.addEventListener('click', (e) => {
            if(e.target===cell || e.target.classList.contains('month-day-number'))
                openCreateEventModal(d, 9, 0);
        });

        cell.addEventListener('contextmenu', (e) => handleContextMenu(e, d));
        container.appendChild(cell);
    }
}

function handleContextMenu(e, date) {
    e.preventDefault();
    const menu = document.getElementById('contextMenu');
    const content = document.getElementById('ctxContent');
    const dateLabel = document.getElementById('ctxDateLabel');

    // Filtra eventi per quel giorno, includendo task multi-giorno
    const items = eventsData.filter(ev => {
        const start = new Date(ev.data_ora_inizio);
        const end = ev.data_fine ? new Date(ev.data_fine) : start;

        if(ev.type === 'task' && ev.data_fine) {
            const dayStart = new Date(date);
            dayStart.setHours(0,0,0,0);
            const dayEnd = new Date(date);
            dayEnd.setHours(23,59,59,999);
            return start <= dayEnd && end >= dayStart;
        }

        return isSameDay(start, date);
    });

    items.sort((a,b) => new Date(a.data_ora_inizio) - new Date(b.data_ora_inizio));

    dateLabel.textContent = date.toLocaleDateString('it-IT', {weekday:'long', day:'numeric', month:'long'});
    content.innerHTML = '';

    if(items.length === 0) {
        content.innerHTML = '<div class="p-2 text-muted text-center">Nessun evento o task</div>';
    } else {
        items.forEach(ev => {
            const row = document.createElement('div');
            row.className = 'ctx-item';
            const time = formatTime(new Date(ev.data_ora_inizio));
            const badgeClass = ev.type === 'task' ? 'badge-task' : 'badge-event';
            const typeLabel = ev.type === 'task' ? 'TASK' : 'EVENTO';

            const isMultiDay = ev.type === 'task' && ev.data_fine &&
                (new Date(ev.data_fine) - new Date(ev.data_ora_inizio)) > 86400000;
            const multiDayIcon = isMultiDay ? '📅 ' : '';

            row.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <span class="badge ${badgeClass} me-2" style="font-size:0.7rem">${typeLabel}</span>
                        <strong>${time}</strong> ${multiDayIcon}${escapeHtml(ev.nome)}
                    </div>
                </div>
            `;
            row.onclick = () => {
                showEventDetails(ev);
                closeContextMenu();
            };
            content.appendChild(row);
        });
    }

    const createBtn = document.createElement('div');
    createBtn.className = 'ctx-item text-primary fw-bold border-top mt-1';
    createBtn.innerHTML = '<i class="bi bi-plus-lg me-2"></i>Crea nuovo qui';
    createBtn.onclick = () => {
        closeContextMenu();
        const now = new Date();
        now.setHours(0,0,0,0);
        const checkDate = new Date(date);
        checkDate.setHours(0,0,0,0);

        if(checkDate < now) {
            alert("Non puoi creare eventi nel passato.");
        } else {
            openCreateEventModal(date, 9, 0);
        }
    };
    content.appendChild(createBtn);

    menu.style.display = 'block';
    let x = e.pageX;
    let y = e.pageY;

    if(x + 300 > window.innerWidth) x -= 300;
    if(y + menu.offsetHeight > window.innerHeight) y -= menu.offsetHeight;

    menu.style.left = x + 'px';
    menu.style.top = y + 'px';
}

function closeContextMenu() {
    document.getElementById('contextMenu').style.display = 'none';
}

// CRUD & VALIDATION
function saveEvent() {
    const form = document.getElementById('createEventForm');
    const alertBox = document.getElementById('dateErrorAlert');
    alertBox.classList.add('d-none');

    if(!form.checkValidity()) { form.reportValidity(); return; }

    const formData = new FormData(form);
    const startStr = formData.get('inizio');
    const startDate = new Date(startStr);
    const now = new Date();

    if(startDate < new Date(now.getTime() - 60000)) {
        alertBox.innerHTML = '<i class="bi bi-exclamation-triangle-fill me-2"></i>Impossibile creare eventi nel passato!';
        alertBox.classList.remove('d-none');
        return;
    }

    const payload = {
        nome: formData.get('nome'),
        luogo: formData.get('luogo'),
        inizio: startStr,
        fine: formData.get('fine'),
        partecipanti: selectedUsers.map(u => u.id_utente)
    };

    // Update ottimistico: aggiungi temporaneamente l'evento
    const tempEvent = {
        id_evento: 'temp_' + Date.now(),
        nome: payload.nome,
        luogo: payload.luogo,
        data_ora_inizio: payload.inizio,
        data_fine: payload.fine,
        type: 'event',
        temp: true
    };
    storedEvents.push(tempEvent);
    mergeEventsAndTasks();
    renderView();

    fetch('/dashboard/calendar/api/create', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) {
                // Rimuovi evento temporaneo in caso di errore
                storedEvents = storedEvents.filter(e => !e.temp);
                mergeEventsAndTasks();
                renderView();

                alertBox.innerHTML = `<i class="bi bi-exclamation-triangle-fill me-2"></i>${data.error || 'Errore durante la creazione'}`;
                alertBox.classList.remove('d-none');
                return;
            }
            if(data.status === 'success') {
                bootstrap.Modal.getInstance(document.getElementById('createEventModal')).hide();
                // Ricarica per ottenere i dati reali dal server
                setTimeout(() => fetchEvents(), 300);
            }
        })
        .catch(() => {
            // Rimuovi evento temporaneo in caso di errore
            storedEvents = storedEvents.filter(e => !e.temp);
            mergeEventsAndTasks();
            renderView();

            alertBox.innerHTML = '<i class="bi bi-exclamation-triangle-fill me-2"></i>Errore di connessione.';
            alertBox.classList.remove('d-none');
        });
}

function updateEvent() {
    const form = document.getElementById('editEventForm');
    const alertBox = document.getElementById('editDateErrorAlert');
    if(alertBox) alertBox.classList.add('d-none');

    const formData = new FormData(form);
    const payload = {
        id: formData.get('id'),
        nome: formData.get('nome'),
        luogo: formData.get('luogo'),
        inizio: formData.get('inizio'),
        fine: formData.get('fine'),
        partecipanti: editSelectedUsers.map(u => u.id_utente)
    };

    // Update ottimistico
    const eventIndex = storedEvents.findIndex(e => e.id_evento == payload.id);
    const oldEvent = eventIndex >= 0 ? {...storedEvents[eventIndex]} : null;

    if(eventIndex >= 0) {
        storedEvents[eventIndex] = {
            ...storedEvents[eventIndex],
            nome: payload.nome,
            luogo: payload.luogo,
            data_ora_inizio: payload.inizio,
            data_fine: payload.fine,
            temp: true
        };
        mergeEventsAndTasks();
        renderView();
    }

    fetch('/dashboard/calendar/api/update', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(payload)
    }).then(async response => {
        const data = await response.json();
        if(!response.ok) {
            // Ripristina vecchio evento in caso di errore
            if(oldEvent && eventIndex >= 0) {
                storedEvents[eventIndex] = oldEvent;
                mergeEventsAndTasks();
                renderView();
            }

            if(alertBox) {
                alertBox.innerHTML = `<i class="bi bi-exclamation-triangle-fill me-2"></i>${data.error || "Errore durante l'aggiornamento"}`;
                alertBox.classList.remove('d-none');
            } else {
                alert(data.error || "Errore durante l'aggiornamento");
            }
            return;
        }
        if(data.status==='updated') {
            bootstrap.Modal.getInstance(document.getElementById('editEventModal')).hide();
            setTimeout(() => fetchEvents(), 300);
        }
    }).catch(e => {
        // Ripristina vecchio evento in caso di errore
        if(oldEvent && eventIndex >= 0) {
            storedEvents[eventIndex] = oldEvent;
            mergeEventsAndTasks();
            renderView();
        }
        console.error(e);
    });
}

function deleteEventFromEdit() {
    if(!confirm('Eliminare?')) return;

    // Update ottimistico
    const eventId = selectedEvent.id_evento;
    const eventIndex = storedEvents.findIndex(e => e.id_evento == eventId);
    const oldEvent = eventIndex >= 0 ? {...storedEvents[eventIndex]} : null;

    if(eventIndex >= 0) {
        storedEvents.splice(eventIndex, 1);
        mergeEventsAndTasks();
        renderView();
    }

    fetch('/dashboard/calendar/api/delete', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({id: eventId})
    }).then(response => {
        if(!response.ok && oldEvent && eventIndex >= 0) {
            // Ripristina in caso di errore
            storedEvents.splice(eventIndex, 0, oldEvent);
            mergeEventsAndTasks();
            renderView();
            alert('Errore durante l\'eliminazione');
            return;
        }
        bootstrap.Modal.getInstance(document.getElementById('editEventModal')).hide();
        setTimeout(() => fetchEvents(), 300);
    }).catch(e => {
        // Ripristina in caso di errore
        if(oldEvent && eventIndex >= 0) {
            storedEvents.splice(eventIndex, 0, oldEvent);
            mergeEventsAndTasks();
            renderView();
        }
        console.error(e);
    });
}

function showEventDetails(event) {
    selectedEvent = event;
    const isTask = event.type === 'task';
    document.getElementById('detailTitle').innerHTML = isTask ? `<span class="badge badge-task me-2">TASK</span>${event.nome}` : event.nome;

    const start = new Date(event.data_ora_inizio);
    const end = event.data_fine ? new Date(event.data_fine) : start;
    document.getElementById('detailTime').textContent = `${start.toLocaleDateString()} ${formatTime(start)} - ${formatTime(end)}`;
    document.getElementById('detailPlace').textContent = event.luogo || '-';

    const editBtn = document.getElementById('btnEditDetail');
    if(editBtn) editBtn.style.display = isTask ? 'none' : 'block';

    const partSection = document.getElementById('detailParticipantsSection');
    const partList = document.getElementById('detailParticipants');

    if(!isTask) {
        partSection.style.display = 'flex';
        partList.innerHTML = '...';
        fetch(`/dashboard/calendar/api/users?id=${event.id_evento}`).then(r=>r.json()).then(users=>{
            partList.innerHTML = '';
            if(!users.length) partList.textContent = 'Nessuno';
            users.forEach(u => {
                partList.innerHTML += `<span class="badge bg-light text-dark border me-1">${u.nome} ${u.cognome}</span>`;
            });
        });
    } else {
        partSection.style.display = 'none';
    }

    new bootstrap.Modal(document.getElementById('eventDetailsModal')).show();
}

function openEditModal() {
    if(!selectedEvent || selectedEvent.type==='task') return;
    bootstrap.Modal.getInstance(document.getElementById('eventDetailsModal')).hide();
    const m = new bootstrap.Modal(document.getElementById('editEventModal'));

    document.getElementById('editEventId').value = selectedEvent.id_evento;
    document.getElementById('editEventName').value = selectedEvent.nome;
    document.getElementById('editEventLocation').value = selectedEvent.luogo;
    document.getElementById('editEventStart').value = formatDateTimeLocal(new Date(selectedEvent.data_ora_inizio));
    document.getElementById('editEventEnd').value = formatDateTimeLocal(new Date(selectedEvent.data_fine));

    fetch(`/dashboard/calendar/api/users?id=${selectedEvent.id_evento}`).then(r=>r.json()).then(u=>{
        editSelectedUsers = u;
        updateSelectedUsersList('editSelectedUsersList', editSelectedUsers);
    });
    m.show();
}

// UTILS
function openCreateEventModal(date, h, m) {
    const now = new Date();
    const d = new Date(date);
    d.setHours(h, m, 0, 0);

    if(d < now) {
        const nextHour = new Date(now);
        nextHour.setMinutes(0,0,0);
        nextHour.setHours(nextHour.getHours() + 1);

        document.querySelector('input[name="inizio"]').value = formatDateTimeLocal(nextHour);
        const end = new Date(nextHour); end.setHours(end.getHours()+1);
        document.querySelector('input[name="fine"]').value = formatDateTimeLocal(end);
    } else {
        const end = new Date(d); end.setHours(h+1);
        document.querySelector('input[name="inizio"]').value = formatDateTimeLocal(d);
        document.querySelector('input[name="fine"]').value = formatDateTimeLocal(end);
    }

    new bootstrap.Modal(document.getElementById('createEventModal')).show();
}

function filterUsers(term, resId, selArr) {
    const box = document.getElementById(resId);
    if(!term) { box.classList.remove('show'); return; }
    const ex = selArr.map(u=>u.id_utente);
    const res = cachedUsers.filter(u => !ex.includes(u.id_utente) && (u.nome+' '+u.cognome).toLowerCase().includes(term.toLowerCase()));

    box.innerHTML = '';
    if(!res.length) box.innerHTML = '<div class="user-search-empty">Nessuno trovato</div>';
    else res.forEach(u => {
        const d = document.createElement('div');
        d.className = 'user-search-item';
        d.textContent = u.nome+' '+u.cognome;
        d.onclick = () => {
            selArr.push(u);
            updateSelectedUsersList(resId==='userSearchResults'?'selectedUsersList':'editSelectedUsersList', selArr);
            box.classList.remove('show');
            document.getElementById(resId==='userSearchResults'?'userSearchInput':'editUserSearchInput').value='';
        };
        box.appendChild(d);
    });
    box.classList.add('show');
}

function updateSelectedUsersList(id, arr) {
    const c = document.getElementById(id);
    c.innerHTML = '';
    arr.forEach(u => {
        const listName = id === 'selectedUsersList' ? 'selectedUsers' : 'editSelectedUsers';
        const d = document.createElement('div');
        d.className = 'selected-user-tag';
        d.innerHTML = `${u.nome} <button type="button" class="remove-user-btn"><i class="bi bi-x-lg"></i></button>`;
        d.querySelector('button').onclick = () => {
            const idx = (listName==='selectedUsers'?selectedUsers:editSelectedUsers).findIndex(x=>x.id_utente===u.id_utente);
            if(idx>-1) (listName==='selectedUsers'?selectedUsers:editSelectedUsers).splice(idx,1);
            updateSelectedUsersList(id, (listName==='selectedUsers'?selectedUsers:editSelectedUsers));
        };
        c.appendChild(d);
    });
}

function changePeriod(d) {
    if(currentView==='week') currentDate.setDate(currentDate.getDate()+(d*7));
    else currentDate.setMonth(currentDate.getMonth()+d);
    renderView();
}
function updateHeaderLabel() {
    const l = document.getElementById('currentPeriodLabel');
    if(currentView==='week') {
        const s = getStartOfWeek(currentDate);
        const e = new Date(s); e.setDate(e.getDate()+6);
        l.textContent = `${s.getDate()} - ${e.getDate()} ${s.toLocaleDateString('it-IT',{month:'long'})}`;
    } else l.textContent = currentDate.toLocaleDateString('it-IT',{year:'numeric',month:'long'});
}
function getStartOfWeek(d) {
    const t = new Date(d); const day = t.getDay();
    const diff = t.getDate() - day + (day===0?-6:1);
    const r = new Date(t.setDate(diff)); r.setHours(0,0,0,0); return r;
}
function updateCurrentTimeLine() {
    document.querySelector('.current-time-line')?.remove();
    if(currentView!=='week') return;
    const now=new Date(); const start=getStartOfWeek(currentDate);
    const diff=now-start;
    if(diff>=0 && diff<604800000) {
        const col=document.querySelectorAll('.day-column')[Math.floor(diff/86400000)];
        if(col) {
            const l=document.createElement('div'); l.className='current-time-line';
            l.style.top=`${(now.getHours()+now.getMinutes()/60)*60}px`; col.appendChild(l);
        }
    }
}
function formatTime(d) { return d.toLocaleTimeString('it-IT',{hour:'2-digit',minute:'2-digit'}); }
function isSameDay(d1, d2) { return d1.toDateString()===d2.toDateString(); }
function formatDateTimeLocal(d) { return new Date(d.getTime() - d.getTimezoneOffset()*60000).toISOString().slice(0,16); }
function escapeHtml(t) { const d=document.createElement('div'); d.innerText=t||''; return d.innerHTML; }