let currentDate = new Date();
let currentView = 'week';

document.addEventListener('DOMContentLoaded', function() {
    // Initial Render
    renderView();

    // Load users when create modal opens
    const createModal = document.getElementById('createEventModal');
    if(createModal) {
        createModal.addEventListener('show.bs.modal', function () {
            loadUsers();
        });
    }

    // View Switchers
    document.querySelectorAll('[data-view]').forEach(btn => {
        btn.addEventListener('click', (e) => {
            document.querySelectorAll('[data-view]').forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');

            currentView = e.target.dataset.view;

            document.querySelectorAll('.calendar-view').forEach(v => {
                v.style.display = 'none';
                v.classList.remove('active');
            });

            const targetId = `${currentView}View`;
            const viewEl = document.getElementById(targetId);
            if(viewEl) {
                viewEl.style.display = 'flex';
                viewEl.classList.add('active');
                renderView();
            }
        });
    });

    // Navigation
    document.getElementById('prevPeriod').addEventListener('click', () => changePeriod(-1));
    document.getElementById('nextPeriod').addEventListener('click', () => changePeriod(1));
    document.getElementById('todayBtn').addEventListener('click', () => {
        currentDate = new Date();
        renderView();
    });
});

function loadUsers() {
    const list = document.getElementById('userSelectionList');
    if(!list) return;

    list.innerHTML = '<div class="text-center text-muted p-2">Caricamento utenti...</div>';

    fetch('/dashboard/gdu/api/users')
        .then(res => {
            if(!res.ok) throw new Error("API Error");
            return res.json();
        })
        .then(users => {
            if(users.length === 0) {
                list.innerHTML = '<div class="text-muted p-2">Nessun collega trovato</div>';
                return;
            }
            list.innerHTML = '';
            users.forEach(u => {
                const div = document.createElement('div');
                div.className = 'user-select-item';
                div.innerHTML = `
                    <input type="checkbox" id="user_${u.id}" name="partecipanti" value="${u.id}" class="form-check-input">
                    <label for="user_${u.id}">${u.nome} ${u.cognome}</label>
                `;
                list.appendChild(div);
            });
        })
        .catch(err => {
            console.error(err);
            list.innerHTML = '<div class="text-danger p-2">Errore caricamento utenti</div>';
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
    if (currentView === 'week') renderWeekView();
    else if (currentView === 'month') renderMonthView();
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
    const headerContainer = document.querySelector('.week-header');
    const timeGrid = document.querySelector('.time-grid');
    const eventsGrid = document.querySelector('.events-grid');

    if(!headerContainer || !timeGrid || !eventsGrid) return;

    headerContainer.innerHTML = '<div class="time-column-header" style="width: 60px;"></div>';
    timeGrid.innerHTML = '';
    eventsGrid.innerHTML = '';

    // Pixel height per hour (must match CSS)
    const pxPerHour = 45;

    // Render Time Grid
    for(let i=0; i<24; i++) {
        const timeStr = i < 10 ? `0${i}:00` : `${i}:00`;
        timeGrid.innerHTML += `<div class="time-grid-row"><div class="time-label">${timeStr}</div></div>`;
    }

    const startOfWeek = getStartOfWeek(currentDate);
    const days = [];

    for(let i=0; i<7; i++) {
        const d = new Date(startOfWeek);
        d.setDate(d.getDate() + i);
        days.push(d);

        const isToday = isSameDay(d, new Date());

        const headerCell = document.createElement('div');
        headerCell.className = `day-header ${isToday ? 'today' : ''}`;
        headerCell.innerHTML = `<div>${d.toLocaleDateString('it-IT', {weekday:'short'})}</div><div>${d.getDate()}</div>`;
        headerContainer.appendChild(headerCell);

        const col = document.createElement('div');
        col.className = 'day-column';
        col.dataset.date = d.toISOString().split('T')[0];
        eventsGrid.appendChild(col);
    }

    // Place Events
    const weekStart = new Date(days[0]);
    weekStart.setHours(0,0,0,0);
    const weekEnd = new Date(days[6]);
    weekEnd.setHours(23,59,59,999);

    if(typeof eventsData !== 'undefined') {
        eventsData.forEach(event => {
            const start = new Date(event.data_ora_inizio);
            const end = event.data_fine ? new Date(event.data_fine) : new Date(start.getTime() + 3600000);

            if (end > weekStart && start < weekEnd) {
                let dayIndex = start.getDay() === 0 ? 6 : start.getDay() - 1;
                if(start < weekStart) dayIndex = 0;

                const columns = document.querySelectorAll('.day-column');
                if(columns[dayIndex]) {
                    const col = columns[dayIndex];

                    const dayDate = days[dayIndex];
                    const dayStart = new Date(dayDate); dayStart.setHours(0,0,0,0);
                    const dayEnd = new Date(dayDate); dayEnd.setHours(23,59,59,999);

                    let effectiveStart = start < dayStart ? dayStart : start;
                    let effectiveEnd = end > dayEnd ? dayEnd : end;

                    const startH = effectiveStart.getHours() + effectiveStart.getMinutes()/60;
                    const endH = effectiveEnd.getHours() + effectiveEnd.getMinutes()/60;

                    const durationH = Math.max(endH - startH, 0.5);

                    const el = document.createElement('div');
                    el.className = 'week-event';
                    el.style.top = `${startH * pxPerHour}px`;
                    el.style.height = `${durationH * pxPerHour}px`;
                    el.innerHTML = `<div class="fw-bold">${escapeHtml(event.nome)}</div><div>${formatTime(effectiveStart)}</div>`;
                    el.onclick = (e) => { e.stopPropagation(); showEventDetails(event); };
                    el.title = `${event.nome}\n${start.toLocaleString()} - ${end.toLocaleString()}`;

                    col.appendChild(el);
                }
            }
        });
    }
}

// ==========================================
// MONTH VIEW
// ==========================================
function renderMonthView() {
    const container = document.querySelector('.month-grid');
    if(!container) return;
    container.innerHTML = '';

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();

    const firstDayOfMonth = new Date(year, month, 1);

    let startDay = new Date(firstDayOfMonth);
    const dayOfWeek = startDay.getDay();
    const daysBack = (dayOfWeek === 0 ? 6 : dayOfWeek - 1);
    startDay.setDate(startDay.getDate() - daysBack);

    for (let i = 0; i < 42; i++) {
        const d = new Date(startDay);
        d.setDate(d.getDate() + i);

        const isToday = isSameDay(d, new Date());
        const isOtherMonth = d.getMonth() !== month;

        const dayEl = document.createElement('div');
        dayEl.className = `month-day ${isOtherMonth ? 'other-month' : ''} ${isToday ? 'today' : ''}`;
        dayEl.innerHTML = `<span class="month-day-number">${d.getDate()}</span>`;

        if(typeof eventsData !== 'undefined') {
            eventsData.forEach(event => {
                const start = new Date(event.data_ora_inizio);
                if(isSameDay(start, d)) {
                    const evEl = document.createElement('div');
                    evEl.className = 'month-event';
                    evEl.textContent = event.nome;
                    evEl.onclick = (e) => { e.stopPropagation(); showEventDetails(event); };
                    dayEl.appendChild(evEl);
                }
            });
        }

        container.appendChild(dayEl);
    }
}

function isSameDay(d1, d2) {
    return d1.getFullYear() === d2.getFullYear() &&
        d1.getMonth() === d2.getMonth() &&
        d1.getDate() === d2.getDate();
}

function formatTime(date) {
    return date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
}

function escapeHtml(text) {
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
    const partecipanti = [];
    form.querySelectorAll('input[name="partecipanti"]:checked').forEach(cb => {
        partecipanti.push(parseInt(cb.value));
    });

    // Format dates to match Java LocalDateTime format (ISO-8601)
    const inizioInput = formData.get('inizio');
    const fineInput = formData.get('fine');

    const payload = {
        nome: formData.get('nome'),
        luogo: formData.get('luogo') || '',
        inizio: inizioInput,  // Should be in format: yyyy-MM-ddTHH:mm
        fine: fineInput,      // Should be in format: yyyy-MM-ddTHH:mm
        partecipanti: partecipanti
    };

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
                renderView();
            } else {
                alert('Errore creazione evento');
            }
        })
        .catch(err => {
            console.error('Errore creazione evento:', err);
            alert('Errore di comunicazione con il server');
        });
}

let selectedEvent = null;

function showEventDetails(event) {
    selectedEvent = event;
    document.getElementById('detailTitle').textContent = event.nome;
    const s = new Date(event.data_ora_inizio);
    const e = event.data_fine ? new Date(event.data_fine) : s;
    document.getElementById('detailTime').textContent = `${s.toLocaleString('it-IT')} - ${e.toLocaleString('it-IT')}`;
    document.getElementById('detailPlace').textContent = event.luogo || 'Nessun luogo specificato';

    const detailIdInput = document.getElementById('detailId');
    if(detailIdInput) {
        detailIdInput.value = event.id_evento;
    }

    const modal = new bootstrap.Modal(document.getElementById('eventDetailsModal'));
    modal.show();
}

function deleteEvent() {
    if(!selectedEvent) return;

    if(!confirm('Sei sicuro di voler eliminare questo evento?')) {
        return;
    }

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

                const modalEl = document.getElementById('eventDetailsModal');
                const modal = bootstrap.Modal.getInstance(modalEl);
                if(modal) modal.hide();

                renderView();
            } else {
                alert('Errore eliminazione evento');
            }
        })
        .catch(err => {
            console.error('Errore eliminazione evento:', err);
            alert('Errore di comunicazione con il server');
        });
}