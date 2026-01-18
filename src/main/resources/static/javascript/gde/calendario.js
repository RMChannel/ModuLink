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

// Scroll sync per mobile
let isScrollingProgrammatically = false;

document.addEventListener('DOMContentLoaded', function () {
    initializeCalendar();

    // Chiudi menu contestuale se clicco altrove
    document.addEventListener('click', function (e) {
        if (!e.target.closest('#contextMenu')) {
            closeContextMenu();
        }
    });
});

function initializeCalendar() {
    fetchEvents();
    setupEventListeners();

    if (currentView === 'week') {
        updateCurrentTimeLine();
        currentTimeInterval = setInterval(updateCurrentTimeLine, 60000);
        setupWeekViewScrollSync();
    }
}

function setupEventListeners() {
    const refreshBtn = document.getElementById('refreshEventsBtn');
    if (refreshBtn) refreshBtn.addEventListener('click', (e) => {
        e.preventDefault();
        fetchEvents();
    });

    // Modali User Load
    const createModal = document.getElementById('createEventModal');
    if (createModal) {
        createModal.addEventListener('show.bs.modal', function () {
            loadUsers();
            selectedUsers = [];
            updateSelectedUsersList('selectedUsersList', selectedUsers);
            document.getElementById('dateErrorAlert').classList.add('d-none');

            const now = new Date();
            const startInput = document.querySelector('input[name="inizio"]');
            const endInput = document.querySelector('input[name="fine"]');

            if (startInput) startInput.min = formatDateTimeLocal(now);

            if (startInput && !startInput.value) {
                now.setMinutes(0, 0, 0);
                now.setHours(now.getHours() + 1);
                startInput.value = formatDateTimeLocal(now);
            }
            if (endInput && !endInput.value) {
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
    if (editModal) {
        editModal.addEventListener('show.bs.modal', () => {
            loadUsers();
            const alertBox = document.getElementById('editDateErrorAlert');
            if (alertBox) alertBox.classList.add('d-none');
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
    if (filterEventsBtn) filterEventsBtn.addEventListener('change', () => {
        mergeEventsAndTasks();
        renderView();
    });
    if (filterTasksBtn) filterTasksBtn.addEventListener('change', () => {
        mergeEventsAndTasks();
        renderView();
    });

    // Ricerca Utenti
    document.getElementById('userSearchInput')?.addEventListener('input', (e) => filterUsers(e.target.value, 'userSearchResults', selectedUsers));
    document.getElementById('editUserSearchInput')?.addEventListener('input', (e) => filterUsers(e.target.value, 'editUserSearchResults', editSelectedUsers));

    // View Switcher
    document.querySelectorAll('[data-view]').forEach(btn => {
        btn.addEventListener('click', function (e) {
            e.preventDefault();
            switchView(this.getAttribute('data-view'));
        });
    });

    // Navigazione
    document.getElementById('prevPeriod')?.addEventListener('click', () => changePeriod(-1));
    document.getElementById('nextPeriod')?.addEventListener('click', () => changePeriod(1));
    document.getElementById('todayBtn')?.addEventListener('click', () => {
        currentDate = new Date();
        renderView();
    });
}

function switchView(view) {
    if (currentView === view) return;

    currentView = view;
    if (currentTimeInterval) {
        clearInterval(currentTimeInterval);
        currentTimeInterval = null;
    }

    document.querySelectorAll('[data-view]').forEach(btn => {
        btn.classList.toggle('active', btn.getAttribute('data-view') === view);
    });

    document.getElementById('weekView').style.display = 'none';
    document.getElementById('monthView').style.display = 'none';

    const target = document.getElementById(`${view}View`);
    target.style.display = 'flex';
    setTimeout(() => {
        renderView();
        if (view === 'week') {
            updateCurrentTimeLine();
            currentTimeInterval = setInterval(updateCurrentTimeLine, 60000);
            setupWeekViewScrollSync();
        }
    }, 10);
}

// DATA FETCHING & MERGING
function fetchEvents() {
    const icon = document.querySelector('#refreshEventsBtn i');
    if (icon) {
        icon.style.transition = 'transform 0.5s';
        icon.style.transform = 'rotate(180deg)';
    }

    const p1 = fetch('/dashboard/calendar/api/get')
        .then(r => r.ok ? r.json() : [])
        .then(d => storedEvents = d.map(e => ({ ...e, type: 'event' })));
    const p2 = fetch('/dashboard/calendar/api/getet')
        .then(r => r.ok ? r.json() : [])
        .then(d => storedTasks = d.map(t => ({ ...t, type: 'task' })));

    Promise.all([p1, p2])
        .then(() => {
            mergeEventsAndTasks();
            renderView();
        })
        .catch(e => console.error("Errore nel caricamento dati:", e))
        .finally(() => {
            if (icon) setTimeout(() => icon.style.transform = 'none', 500);
        });
}

function mergeEventsAndTasks() {
    eventsData = [];
    const showEvents = document.getElementById('filterEvents')?.checked ?? true;
    const showTasks = document.getElementById('filterTasks')?.checked ?? true;

    if (showEvents) eventsData = eventsData.concat(storedEvents);
    if (showTasks) eventsData = eventsData.concat(storedTasks);
}

function loadUsers() {
    if (cachedUsers) return;
    fetch('/dashboard/calendar/api/users')
        .then(r => r.json())
        .then(u => cachedUsers = u)
        .catch(() => cachedUsers = []);
}

// HELPER LOGICA VISUALIZZAZIONE
function isEventActiveInDay(ev, dateObj) {
    const dayStart = new Date(dateObj);
    dayStart.setHours(0, 0, 0, 0);

    const dayEnd = new Date(dateObj);
    dayEnd.setHours(23, 59, 59, 999);

    if (ev.type === 'task') {
        const targetDate = ev.data_fine ? new Date(ev.data_fine) : new Date(ev.data_ora_inizio);
        return targetDate >= dayStart && targetDate <= dayEnd;
    } else {
        const evStart = new Date(ev.data_ora_inizio);
        const evEnd = ev.data_fine ? new Date(ev.data_fine) : evStart;
        return evStart <= dayEnd && evEnd >= dayStart;
    }
}

// RENDERING
function renderView() {
    updateHeaderLabel();
    if (currentView === 'week') renderWeekView();
    else renderMonthView();
}

// WEEK VIEW DISPATCHER
function renderWeekView() {
    if (window.innerWidth <= 768) {
        renderMobileWeekView();
    } else {
        renderDesktopWeekView();
    }
}

// DESKTOP WEEK VIEW (Original Logic)
function renderDesktopWeekView() {
    const weekBody = document.querySelector('.week-body');
    const headerContainer = document.querySelector('.week-header');
    if (!weekBody || !headerContainer) return;

    headerContainer.style.display = 'flex'; // Ensure visible on desktop
    headerContainer.innerHTML = '<div class="time-column-header"></div>';
    weekBody.innerHTML = '';

    const content = document.createElement('div');
    content.className = 'week-content';

    const timeGrid = document.createElement('div');
    timeGrid.className = 'time-grid';
    for (let i = 0; i < 24; i++) {
        timeGrid.innerHTML += `<div class="time-slot"><div class="time-label">${i.toString().padStart(2, '0')}:00</div></div>`;
    }

    const eventsGrid = document.createElement('div');
    eventsGrid.className = 'events-grid';

    const startOfWeek = getStartOfWeek(currentDate);
    const today = new Date(); today.setHours(0, 0, 0, 0);

    for (let i = 0; i < 7; i++) {
        const d = new Date(startOfWeek);
        d.setDate(d.getDate() + i);
        const isToday = d.getTime() === today.getTime();

        // Header
        headerContainer.innerHTML += `
            <div class="day-header ${isToday ? 'today' : ''}">
                <div class="day-header-day">${d.toLocaleDateString('it-IT', { weekday: 'short' })}</div>
                <div class="day-header-date">${d.getDate()}</div>
            </div>`;

        // Column
        const col = document.createElement('div');
        col.className = 'day-column';
        col.setAttribute('data-date', d.toISOString());

        // Click sinistro: crea evento
        col.addEventListener('click', (e) => {
            if (e.target.closest('.week-event')) return;
            const rect = col.getBoundingClientRect();
            const clickY = e.clientY - rect.top;
            const mins = Math.floor((clickY / 60) * 60);
            const rounded = Math.round(mins / 30) * 30;
            openCreateEventModal(d, Math.floor(rounded / 60), rounded % 60);
        });

        // Click destro: Context Menu
        col.addEventListener('contextmenu', (e) => handleContextMenu(e, d));
        addLongPressListener(col, (e) => handleContextMenu(e, d));

        const dayEvents = eventsData.filter(e => isEventActiveInDay(e, d));

        if (dayEvents.length > 0) {
            layoutDayEvents(dayEvents, col, d);
        }

        eventsGrid.appendChild(col);
    }

    content.append(timeGrid, eventsGrid);
    weekBody.appendChild(content);

    // Setup scroll sync (unused on desktop typically, but kept for logic consistency)
    setupWeekViewScrollSync();
}

// MOBILE WEEK VIEW (New Logic)
function renderMobileWeekView() {
    const weekBody = document.querySelector('.week-body');
    const headerContainer = document.querySelector('.week-header');
    if (!weekBody) return;

    if (headerContainer) headerContainer.style.display = 'none'; // Hide desktop header
    weekBody.innerHTML = '';

    const content = document.createElement('div');
    content.className = 'week-mobile-content';

    // 1. Day Selector Strip
    const daySelector = document.createElement('div');
    daySelector.className = 'mobile-day-selector';
    
    const startOfWeek = getStartOfWeek(currentDate);
    
    for (let i = 0; i < 7; i++) {
        const d = new Date(startOfWeek);
        d.setDate(d.getDate() + i);
        
        const isSelected = isSameDay(d, currentDate);
        const isToday = isSameDay(d, new Date());
        
        const dayEl = document.createElement('div');
        dayEl.className = `mobile-day ${isSelected ? 'active' : ''} ${isToday ? 'today-indicator' : ''}`;
        dayEl.innerHTML = `
            <span class="m-day-num">${d.getDate()}</span>
            <span class="m-day-name">${d.toLocaleDateString('it-IT', { weekday: 'short' }).toUpperCase()}</span>
        `;
        
        dayEl.onclick = () => {
            currentDate = d;
            renderView(); // Re-render to update selection and timeline
        };
        
        daySelector.appendChild(dayEl);
    }
    
    // 2. Timeline Area
    const timelineContainer = document.createElement('div');
    timelineContainer.className = 'mobile-timeline-container';
    
    // Time labels column
    const timeCol = document.createElement('div');
    timeCol.className = 'mobile-time-col';
    for (let i = 0; i < 24; i++) {
        timeCol.innerHTML += `<div class="mobile-time-slot">${i.toString().padStart(2, '0')}:00</div>`;
    }
    
    // Events column
    const eventsCol = document.createElement('div');
    eventsCol.className = 'mobile-events-col';
    
    // Background lines
    for (let i = 0; i < 24; i++) {
        eventsCol.innerHTML += `<div class="mobile-grid-line"></div>`;
    }
    
    // Click listener to create event
    eventsCol.addEventListener('click', (e) => {
        if (e.target.closest('.week-event')) return;
        const rect = eventsCol.getBoundingClientRect();
        // Since the container scrolls, we use e.clientY relative to the top of the element, which includes scroll offset effectively
        // BUT getBoundingClientRect().top changes as we scroll.
        // The click Y relative to the element top is (e.clientY - rect.top).
        const clickY = e.clientY - rect.top;
        const mins = Math.floor((clickY / 60) * 60);
        const rounded = Math.round(mins / 30) * 30;
        openCreateEventModal(currentDate, Math.floor(rounded / 60), rounded % 60);
    });

    // Render events
    const dayEvents = eventsData.filter(e => isEventActiveInDay(e, currentDate));
    layoutDayEvents(dayEvents, eventsCol, currentDate);
    
    // Current Time Line (Managed in updateCurrentTimeLine generally, but added here for immediate render)
    // We let updateCurrentTimeLine handle it to avoid duplication logic, but call it.

    timelineContainer.append(timeCol, eventsCol);
    
    content.append(daySelector, timelineContainer);
    weekBody.appendChild(content);

    // Ensure scroll to 9AM or current time
    setTimeout(() => {
        const now = new Date();
        const hour = now.getHours();
        const scrollY = (hour > 2 ? hour - 1 : 0) * 60;
        timelineContainer.scrollTop = scrollY;
    }, 0);
}

function layoutDayEvents(events, container, currentDay) {
    events.sort((a, b) => new Date(a.data_ora_inizio) - new Date(b.data_ora_inizio));

    const items = events.map(e => {
        const start = new Date(e.data_ora_inizio);
        const end = e.data_fine ? new Date(e.data_fine) : new Date(start.getTime() + 3600000);

        let startH, endH;

        const dayStart = new Date(currentDay); dayStart.setHours(0, 0, 0, 0);
        const dayEnd = new Date(currentDay); dayEnd.setHours(23, 59, 59, 999);

        if (e.type === 'event') {
            startH = start < dayStart ? 0 : (start.getHours() + start.getMinutes() / 60);
            endH = end > dayEnd ? 24 : (end.getHours() + end.getMinutes() / 60);
            if (endH <= startH) endH = startH + 0.5;
        } else {
            startH = start.getHours() + start.getMinutes() / 60;
            endH = end.getHours() + end.getMinutes() / 60;
            if (endH <= startH) endH = startH + 1;
        }

        return { event: e, start: startH, end: endH, col: 0, maxCols: 1 };
    });

    // Calcolo colonne per sovrapposizioni
    const columns = [];
    items.forEach(item => {
        let placed = false;
        for (let i = 0; i < columns.length; i++) {
            const lastInCol = columns[i][columns[i].length - 1];
            if (item.start >= lastInCol.end) {
                columns[i].push(item);
                item.col = i;
                placed = true;
                break;
            }
        }
        if (!placed) {
            columns.push([item]);
            item.col = columns.length - 1;
        }
    });

    const maxCols = columns.length;

    items.forEach(item => {
        const el = document.createElement('div');
        const isMultiDay = item.event.type === 'event' && item.event.data_fine &&
            (new Date(item.event.data_fine) - new Date(item.event.data_ora_inizio)) > 86400000;

        el.className = `week-event ${item.event.type === 'task' ? 'task-event' : ''} ${isMultiDay ? 'multi-day-event' : ''}`;

        if (item.event.type === 'task') {
            el.style.backgroundColor = '#fff3cd';
            el.style.borderLeft = '4px solid #ffc107';
            el.style.color = '#856404';
        } else if (isMultiDay) {
            el.style.borderLeft = '4px solid #0d6efd';
        }

        el.textContent = (isMultiDay || item.event.type === 'task') ?
            `${item.event.nome}` : item.event.nome;
        el.style.top = `${item.start * 60}px`;
        el.style.height = `${(item.end - item.start) * 60}px`;

        const width = 100 / maxCols;
        el.style.width = `calc(${width}% - 4px)`;
        el.style.left = `${item.col * width}%`;

        el.addEventListener('click', (ev) => {
            ev.stopPropagation();
            showEventDetails(item.event);
        });
        el.addEventListener('contextmenu', (ev) => {
            ev.stopPropagation();
        });

        container.appendChild(el);
    });
}

// MONTH VIEW
function renderMonthView() {
    const container = document.querySelector('.month-grid');
    if (!container) return;
    container.innerHTML = '';

    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    let startDay = new Date(firstDay);
    const dayOfWeek = startDay.getDay();
    const daysBack = dayOfWeek === 0 ? 6 : dayOfWeek - 1;
    startDay.setDate(startDay.getDate() - daysBack);

    for (let i = 0; i < 42; i++) {
        const d = new Date(startDay);
        d.setDate(d.getDate() + i);

        const cell = document.createElement('div');
        const isOther = d.getMonth() !== month;
        const isToday = isSameDay(d, new Date());

        cell.className = `month-day ${isOther ? 'other-month' : ''} ${isToday ? 'today' : ''}`;
        cell.setAttribute('data-date', d.toISOString());
        cell.innerHTML = `<span class="month-day-number">${d.getDate()}</span>`;

        const dayEvents = eventsData.filter(e => isEventActiveInDay(e, d));

        const evCont = document.createElement('div');
        evCont.className = 'month-events-container';

        const MAX_VISIBLE = 2;
        dayEvents.slice(0, MAX_VISIBLE).forEach(ev => {
            const el = document.createElement('div');
            const isMultiDay = ev.type === 'event' && ev.data_fine &&
                (new Date(ev.data_fine) - new Date(ev.data_ora_inizio)) > 86400000;

            el.className = `month-event ${isMultiDay ? 'multi-day-task' : ''}`;
            if (ev.type === 'task') {
                el.style.backgroundColor = '#ffc107';
                el.style.color = '#000';
            }
            let label = ev.nome;
            if (isMultiDay) label = '↕ ' + label;

            el.textContent = label;
            el.onclick = (e) => {
                e.stopPropagation();
                showEventDetails(ev);
            };
            evCont.appendChild(el);
        });

        if (dayEvents.length > MAX_VISIBLE) {
            evCont.innerHTML += `<div class="month-event-more">+${dayEvents.length - MAX_VISIBLE} altri</div>`;
        }

        // MOBILE INDICATORS
        const mobileInd = document.createElement('div');
        mobileInd.className = 'mobile-indicators';

        const dotsToShow = dayEvents.slice(0, 4);
        dotsToShow.forEach(ev => {
            const dot = document.createElement('span');
            dot.className = ev.type === 'task' ? 'mobile-dot dot-task' : 'mobile-dot dot-event';
            mobileInd.appendChild(dot);
        });

        if (dayEvents.length > 4) {
            const plus = document.createElement('span');
            plus.className = 'mobile-dot-plus';
            plus.textContent = '+';
            mobileInd.appendChild(plus);
        }

        cell.appendChild(evCont);
        cell.appendChild(mobileInd);

        cell.addEventListener('click', (e) => {
            if (window.innerWidth <= 768) {
                showMobileDayModal(d, dayEvents);
            } else {
                openCreateEventModal(d, 9, 0);
            }
        });

        cell.addEventListener('contextmenu', (e) => handleContextMenu(e, d));
        addLongPressListener(cell, (e) => handleContextMenu(e, d));
        container.appendChild(cell);
    }
}

function showMobileDayModal(date, events) {
    const modalEl = document.getElementById('mobileDayModal');
    const modal = new bootstrap.Modal(modalEl);

    const title = document.getElementById('mobileDayTitle');
    const subtitle = document.getElementById('mobileDaySubtitle');
    const content = document.getElementById('mobileDayContent');
    const addBtn = document.getElementById('mobileAddEventBtn');

    const dayName = date.toLocaleDateString('it-IT', { weekday: 'long' });
    const dayDate = date.toLocaleDateString('it-IT', { day: 'numeric', month: 'long', year: 'numeric' });

    title.textContent = dayName.charAt(0).toUpperCase() + dayName.slice(1);
    subtitle.textContent = dayDate;

    content.innerHTML = '';

    if (events.length === 0) {
        content.innerHTML = `
            <div class="text-center py-5 opacity-50">
                <i class="bi bi-calendar-x display-1"></i>
                <p class="mt-3 fw-medium">Nessun impegno per oggi</p>
            </div>
        `;
    } else {
        events.sort((a, b) => new Date(a.data_ora_inizio) - new Date(b.data_ora_inizio));

        events.forEach(ev => {
            const isTask = ev.type === 'task';
            const card = document.createElement('div');
            card.className = `mobile-day-card ${isTask ? 'task' : 'event'}`;
            card.style.cursor = 'pointer';

            const timeStart = formatTime(new Date(ev.data_ora_inizio));
            const timeEnd = ev.data_fine ? formatTime(new Date(ev.data_fine)) : '';

            card.innerHTML = `
                <div class="mobile-day-time-box">
                    <span class="mobile-day-time-start">${timeStart}</span>
                    ${timeEnd ? `<small class="mobile-day-time-end">${timeEnd}</small>` : ''}
                </div>
                <div class="flex-grow-1 overflow-hidden">
                    <h6 class="mobile-day-title">${escapeHtml(ev.nome)}</h6>
                    <div class="d-flex align-items-center gap-2 small opacity-75">
                         ${ev.luogo ? `<i class="bi bi-geo-alt-fill"></i> ${escapeHtml(ev.luogo)}` : ''}
                         <span class="badge ${isTask ? 'bg-warning text-dark' : 'bg-primary'}">${isTask ? 'TASK' : 'EVENTO'}</span>
                    </div>
                </div>
                <i class="bi bi-chevron-right opacity-50"></i>
            `;

            card.onclick = () => {
                modal.hide();
                setTimeout(() => showEventDetails(ev), 200);
            };

            content.appendChild(card);
        });
    }

    addBtn.onclick = () => {
        modal.hide();
        setTimeout(() => openCreateEventModal(date, 9, 0), 200);
    };

    modal.show();
}

function handleContextMenu(e, date) {
    if (e.type === 'contextmenu') e.preventDefault();

    if (window.innerWidth <= 768) {
        openCreateEventModal(date, 9, 0);
        return;
    }

    const menu = document.getElementById('contextMenu');
    const content = document.getElementById('ctxContent');
    const dateLabel = document.getElementById('ctxDateLabel');

    const items = eventsData.filter(ev => isEventActiveInDay(ev, date));

    items.sort((a, b) => new Date(a.data_ora_inizio) - new Date(b.data_ora_inizio));

    dateLabel.textContent = date.toLocaleDateString('it-IT', { weekday: 'long', day: 'numeric', month: 'long' });
    content.innerHTML = '';

    if (items.length === 0) {
        content.innerHTML = '<div class="p-2 text-muted text-center">Nessun evento o task</div>';
    } else {
        items.forEach(ev => {
            const row = document.createElement('div');
            row.className = 'ctx-item';
            const time = formatTime(new Date(ev.data_ora_inizio));
            const badgeClass = ev.type === 'task' ? 'badge-task' : 'badge-event';
            const typeLabel = ev.type === 'task' ? 'TASK' : 'EVENTO';

            row.innerHTML = `
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <span class="badge ${badgeClass} me-2" style="font-size:0.7rem">${typeLabel}</span>
                        <strong>${time}</strong> ${escapeHtml(ev.nome)}
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
        now.setHours(0, 0, 0, 0);
        const checkDate = new Date(date);
        checkDate.setHours(0, 0, 0, 0);

        if (checkDate < now) {
            showToast("Non puoi creare eventi nel passato.", "danger");
        } else {
            openCreateEventModal(date, 9, 0);
        }
    };
    content.appendChild(createBtn);

    menu.style.display = 'block';

    let x = e.pageX;
    let y = e.pageY;

    if (e.touches && e.touches.length > 0) {
        x = e.touches[0].pageX;
        y = e.touches[0].pageY;
    } else if (e.changedTouches && e.changedTouches.length > 0) {
        x = e.changedTouches[0].pageX;
        y = e.changedTouches[0].pageY;
    }

    if (x + 300 > window.innerWidth) x -= 300;
    if (y + menu.offsetHeight > window.innerHeight) y -= menu.offsetHeight;

    menu.style.left = x + 'px';
    menu.style.top = y + 'px';
}

function closeContextMenu() {
    document.getElementById('contextMenu').style.display = 'none';
}

function addLongPressListener(el, callback) {
    let timer;
    const duration = 800;

    el.addEventListener('touchstart', (e) => {
        timer = setTimeout(() => {
            callback(e);
        }, duration);
    }, { passive: true });

    el.addEventListener('touchend', () => clearTimeout(timer));
    el.addEventListener('touchmove', () => clearTimeout(timer));
}

// SCROLL SYNC FOR DESKTOP WEEK VIEW
function setupWeekViewScrollSync() {
    // Only needed for desktop or when header and body are separate
    if (window.innerWidth <= 768) return; // Mobile uses single view, no sync needed

    const weekHeader = document.querySelector('.week-header');
    const weekBody = document.querySelector('.week-body');

    if (!weekHeader || !weekBody) return;

    // Rimuovi listener precedente
    weekBody.removeEventListener('scroll', syncHeaderScroll);

    // Aggiungi listener
    weekBody.addEventListener('scroll', syncHeaderScroll);
}

function syncHeaderScroll(e) {
    if (isScrollingProgrammatically) return;

    const weekBody = e.target;
    const weekHeader = document.querySelector('.week-header');

    if (!weekHeader) return;

    isScrollingProgrammatically = true;
    weekHeader.scrollLeft = weekBody.scrollLeft;

    requestAnimationFrame(() => {
        isScrollingProgrammatically = false;
    });
}

// Responsive listener
window.addEventListener('resize', () => {
    if (currentView === 'week') {
        renderView(); // Full re-render to switch between mobile and desktop layouts
    }
});

// CRUD & VALIDATION
function saveEvent() {
    const form = document.getElementById('createEventForm');
    const alertBox = document.getElementById('dateErrorAlert');
    alertBox.classList.add('d-none');

    if (!form.checkValidity()) {
        form.reportValidity();
        return;
    }

    const formData = new FormData(form);
    const startStr = formData.get('inizio');
    const startDate = new Date(startStr);
    const now = new Date();

    if (startDate < new Date(now.getTime() - 60000)) {
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
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
        .then(async response => {
            const data = await response.json();
            if (!response.ok) {
                storedEvents = storedEvents.filter(e => !e.temp);
                mergeEventsAndTasks();
                renderView();

                alertBox.innerHTML = `<i class="bi bi-exclamation-triangle-fill me-2"></i>${data.error || 'Errore durante la creazione'}`;
                alertBox.classList.remove('d-none');
                return;
            }
                    if (data.status === 'success') {
                        bootstrap.Modal.getInstance(document.getElementById('createEventModal')).hide();
                        showToast('Evento creato con successo', 'success');
                        setTimeout(() => fetchEvents(), 300);
                    }
            
        })
        .catch(() => {
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
    if (alertBox) alertBox.classList.add('d-none');

    const formData = new FormData(form);
    const payload = {
        id: formData.get('id'),
        nome: formData.get('nome'),
        luogo: formData.get('luogo'),
        inizio: formData.get('inizio'),
        fine: formData.get('fine'),
        partecipanti: editSelectedUsers.map(u => u.id_utente)
    };

    const eventIndex = storedEvents.findIndex(e => e.id_evento === payload.id);
    const oldEvent = eventIndex >= 0 ? { ...storedEvents[eventIndex] } : null;

    if (eventIndex >= 0) {
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
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    }).then(async response => {
        const data = await response.json();
        if (!response.ok) {
            if (oldEvent && eventIndex >= 0) {
                storedEvents[eventIndex] = oldEvent;
                mergeEventsAndTasks();
                renderView();
            }

            if (alertBox) {
                alertBox.innerHTML = `<i class="bi bi-exclamation-triangle-fill me-2"></i>${data.error || "Errore durante l'aggiornamento"}`;
                alertBox.classList.remove('d-none');
            } else {
                showToast(data.error || "Errore durante l'aggiornamento", "danger");
            }
            return;
        }
        if (data.status === 'updated') {
            bootstrap.Modal.getInstance(document.getElementById('editEventModal')).hide();
            showToast('Evento aggiornato con successo', 'success');
            setTimeout(() => fetchEvents(), 300);
        }
    }).catch(e => {
        if (oldEvent && eventIndex >= 0) {
            storedEvents[eventIndex] = oldEvent;
            mergeEventsAndTasks();
            renderView();
        }
        console.error(e);
    });
}

function deleteEventFromEdit() {
    const eventId = selectedEvent.id_evento;
    const eventIndex = storedEvents.findIndex(e => e.id_evento === eventId);
    const oldEvent = eventIndex >= 0 ? { ...storedEvents[eventIndex] } : null;

    if (eventIndex >= 0) {
        storedEvents.splice(eventIndex, 1);
        mergeEventsAndTasks();
        renderView();
    }

    fetch('/dashboard/calendar/api/delete', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: eventId })
    }).then(response => {
        if (!response.ok) {
            if (oldEvent && eventIndex >= 0) {
                storedEvents.splice(eventIndex, 0, oldEvent);
                mergeEventsAndTasks();
                renderView();
            }
            showToast('Errore durante l\'eliminazione', 'danger');
            return;
        }
        bootstrap.Modal.getInstance(document.getElementById('editEventModal')).hide();
        showToast('Evento cancellato con successo', 'success');
        setTimeout(() => fetchEvents(), 300);
    }).catch(e => {
        if (oldEvent && eventIndex >= 0) {
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
    document.getElementById('detailTitle').innerHTML = isTask ?
        `<span class="badge badge-task me-2">TASK</span>${event.nome}` :
        event.nome;

    const start = new Date(event.data_ora_inizio);
    const end = event.data_fine ? new Date(event.data_fine) : start;
    document.getElementById('detailTime').textContent = `${start.toLocaleDateString()} ${formatTime(start)} - ${formatTime(end)}`;
    document.getElementById('detailPlace').textContent = event.luogo || '-';

    const editBtn = document.getElementById('btnEditDetail');
    if (editBtn) editBtn.style.display = isTask ? 'none' : 'block';

    const partSection = document.getElementById('detailParticipantsSection');
    const partList = document.getElementById('detailParticipants');

    if (!isTask) {
        partSection.style.display = 'flex';
        partList.innerHTML = '...';
        fetch(`/dashboard/calendar/api/users?id=${event.id_evento}`)
            .then(r => r.json())
            .then(users => {
                partList.innerHTML = '';
                if (!users.length) partList.textContent = 'Nessuno';
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
    if (!selectedEvent || selectedEvent.type === 'task') return;
    bootstrap.Modal.getInstance(document.getElementById('eventDetailsModal')).hide();
    const m = new bootstrap.Modal(document.getElementById('editEventModal'));

    document.getElementById('editEventId').value = selectedEvent.id_evento;
    document.getElementById('editEventName').value = selectedEvent.nome;
    document.getElementById('editEventLocation').value = selectedEvent.luogo;
    document.getElementById('editEventStart').value = formatDateTimeLocal(new Date(selectedEvent.data_ora_inizio));
    document.getElementById('editEventEnd').value = formatDateTimeLocal(new Date(selectedEvent.data_fine));

    fetch(`/dashboard/calendar/api/users?id=${selectedEvent.id_evento}`)
        .then(r => r.json())
        .then(u => {
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

    if (d < now) {
        const nextHour = new Date(now);
        nextHour.setMinutes(0, 0, 0);
        nextHour.setHours(nextHour.getHours() + 1);

        document.querySelector('input[name="inizio"]').value = formatDateTimeLocal(nextHour);
        const end = new Date(nextHour); end.setHours(end.getHours() + 1);
        document.querySelector('input[name="fine"]').value = formatDateTimeLocal(end);
    } else {
        const end = new Date(d); end.setHours(h + 1);
        document.querySelector('input[name="inizio"]').value = formatDateTimeLocal(d);
        document.querySelector('input[name="fine"]').value = formatDateTimeLocal(end);
    }

    new bootstrap.Modal(document.getElementById('createEventModal')).show();
}

function filterUsers(term, resId, selArr) {
    const box = document.getElementById(resId);
    if (!term) {
        box.classList.remove('show');
        return;
    }
    const ex = selArr.map(u => u.id_utente);
    const termLower = term.toLowerCase();

    const res = cachedUsers.filter(u =>
            !ex.includes(u.id_utente) && (
                (u.nome + ' ' + u.cognome).toLowerCase().includes(termLower) ||
                (u.email && u.email.toLowerCase().includes(termLower))
            )
    );

    box.innerHTML = '';
    if (!res.length) box.innerHTML = '<div class="user-search-empty">Nessuno trovato</div>';
    else res.forEach(u => {
        const d = document.createElement('div');
        d.className = 'user-search-item d-flex flex-column gap-0';
        d.innerHTML = `
        <span class="fw-bold">${escapeHtml(u.nome + ' ' + u.cognome)}</span>
        <small class="text-muted" style="font-size: 0.75rem;">${escapeHtml(u.email)}</small>
    `;
        d.onclick = () => {
            selArr.push(u);
            updateSelectedUsersList(resId === 'userSearchResults' ? 'selectedUsersList' : 'editSelectedUsersList', selArr);
            box.classList.remove('show');
            document.getElementById(resId === 'userSearchResults' ? 'userSearchInput' : 'editUserSearchInput').value = '';
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
            const idx = (listName === 'selectedUsers' ? selectedUsers : editSelectedUsers).findIndex(x => x.id_utente === u.id_utente);
            if (idx > -1) (listName === 'selectedUsers' ? selectedUsers : editSelectedUsers).splice(idx, 1);
            updateSelectedUsersList(id, listName === 'selectedUsers' ? selectedUsers : editSelectedUsers);
        };
        c.appendChild(d);
    });
}

function changePeriod(d) {
    if (currentView === 'week') currentDate.setDate(currentDate.getDate() + (d * 7));
    else currentDate.setMonth(currentDate.getMonth() + d);
    renderView();
}

function updateHeaderLabel() {
    const l = document.getElementById('currentPeriodLabel');
    if (currentView === 'week') {
        const s = getStartOfWeek(currentDate);
        const e = new Date(s);
        e.setDate(e.getDate() + 6);
        l.textContent = `${s.getDate()} - ${e.getDate()} ${s.toLocaleDateString('it-IT', { month: 'long' })}`;
    } else {
        l.textContent = currentDate.toLocaleDateString('it-IT', { year: 'numeric', month: 'long' });
    }
}

function getStartOfWeek(d) {
    const t = new Date(d);
    const day = t.getDay();
    const diff = t.getDate() - day + (day === 0 ? -6 : 1);
    const r = new Date(t.setDate(diff));
    r.setHours(0, 0, 0, 0);
    return r;
}

function updateCurrentTimeLine() {
    document.querySelector('.current-time-line')?.remove();
    if (currentView !== 'week') return;
    const now = new Date();
    
    // Logic for mobile view (only show if selected day is today)
    if (window.innerWidth <= 768) {
        if (isSameDay(currentDate, now)) {
            const container = document.querySelector('.mobile-events-col');
            if (container) {
                const l = document.createElement('div');
                l.className = 'current-time-line mobile'; 
                l.style.top = `${(now.getHours() + now.getMinutes() / 60) * 60}px`;
                container.appendChild(l);
            }
        }
    } else {
        // Desktop Logic
        const start = getStartOfWeek(currentDate);
        const diff = now - start;
        if (diff >= 0 && diff < 604800000) {
            const col = document.querySelectorAll('.day-column')[Math.floor(diff / 86400000)];
            if (col) {
                const l = document.createElement('div');
                l.className = 'current-time-line';
                l.style.top = `${(now.getHours() + now.getMinutes() / 60) * 60}px`;
                col.appendChild(l);
            }
        }
    }
}

function formatTime(d) {
    return d.toLocaleTimeString('it-IT', { hour: '2-digit', minute: '2-digit' });
}

function isSameDay(d1, d2) {
    return d1.toDateString() === d2.toDateString();
}

function formatDateTimeLocal(d) {
    return new Date(d.getTime() - d.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
}

function escapeHtml(t) {

    const d = document.createElement('div');

    d.innerText = t || '';

    return d.innerHTML;

}



function showToast(message, type = 'success') {

    const container = document.querySelector('.floating-alert-container');

    if (!container) return;



    const alertDiv = document.createElement('div');

    alertDiv.className = `floating-alert alert-${type}`;

    alertDiv.setAttribute('role', 'alert');



    const iconClass = type === 'success' ? 'bi-check-circle-fill' : 'bi-exclamation-circle-fill';



    alertDiv.innerHTML = `

        <div class="floating-alert-icon">

            <i class="bi ${iconClass}"></i>

        </div>

        <div class="floating-alert-content">

            <span class="text-break">${message}</span>

        </div>

        <button type="button" class="floating-alert-close" aria-label="Close">

            <i class="bi bi-x"></i>

        </button>

    `;



    container.appendChild(alertDiv);



    // Add close functionality

    alertDiv.querySelector('.floating-alert-close').onclick = () => {

        alertDiv.classList.add('fading-out');

        setTimeout(() => alertDiv.remove(), 500);

    };



    // Auto-remove after 3 seconds

    setTimeout(() => {

        if (alertDiv.parentElement) {

            alertDiv.classList.add('fading-out');

            setTimeout(() => alertDiv.remove(), 500);

        }

    }, 3000);

}
