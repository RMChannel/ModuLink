let currentDate = new Date();
let currentView = 'week';

document.addEventListener('DOMContentLoaded', function() {
    // Initial Render
    renderView();
    
    // View Switchers
    document.querySelectorAll('[data-view]').forEach(btn => {
        btn.addEventListener('click', (e) => {
            // UI Toggle
            document.querySelectorAll('[data-view]').forEach(b => b.classList.remove('active'));
            e.target.classList.add('active');
            
            // Logic Switch
            currentView = e.target.dataset.view;
            
            // Container Toggle
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
             label.textContent = `${start.getDate()} ${start.toLocaleDateString('it-IT', {month:'short'})} - ${end.getDate()} ${start.toLocaleDateString('it-IT', {month:'short', year:'numeric'})}`;
        }
    } else {
        label.textContent = currentDate.toLocaleDateString('it-IT', { year: 'numeric', month: 'long' });
    }
}

function getStartOfWeek(date) {
    const d = new Date(date);
    const day = d.getDay(); // 0 (Sun) to 6 (Sat)
    // We want Monday as start.
    // If Sun(0), diff = -6. If Mon(1), diff = 0.
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

    // Clear content
    headerContainer.innerHTML = '<div class="time-column-header" style="width: 60px;"></div>';
    timeGrid.innerHTML = '';
    eventsGrid.innerHTML = '';
    
    // Render Time Grid (0-23)
    for(let i=0; i<24; i++) {
        const timeStr = i < 10 ? `0${i}:00` : `${i}:00`;
        timeGrid.innerHTML += `<div class="time-grid-row"><div class="time-label">${timeStr}</div></div>`;
    }
    
    // Render Days Headers & Columns
    const startOfWeek = getStartOfWeek(currentDate);
    const days = [];
    
    // We need to associate columns in eventsGrid with days
    for(let i=0; i<7; i++) {
        const d = new Date(startOfWeek);
        d.setDate(d.getDate() + i);
        days.push(d);
        
        const isToday = isSameDay(d, new Date());
        
        // Header
        const headerCell = document.createElement('div');
        headerCell.className = `day-header ${isToday ? 'today' : ''}`;
        headerCell.innerHTML = `<div>${d.toLocaleDateString('it-IT', {weekday:'short'})}</div><div>${d.getDate()}</div>`;
        headerContainer.appendChild(headerCell);
        
        // Events Column
        const col = document.createElement('div');
        col.className = 'day-column';
        // Store date in data attribute for potential use
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
            // Default 1h duration if not specified
            const end = event.data_fine ? new Date(event.data_fine) : new Date(start.getTime() + 3600000);
            
            // Check overlap with this week
            if (end > weekStart && start < weekEnd) {
                // Determine which day column(s) it belongs to
                // For simplicity, we only render the part in this week, and primarily in the start day column
                // Multi-day events in this view are tricky. We'll render it in the start day column (if in week)
                
                // Find column index
                // Note: getDay() returns 0 for Sunday, we want 0 for Monday
                let dayIndex = start.getDay() === 0 ? 6 : start.getDay() - 1;
                
                // If event started before this week, it might appear on Monday
                if(start < weekStart) {
                    dayIndex = 0; // Show on Monday
                }
                
                // If start is in future of this week (shouldn't happen due to if check), ignore
                
                // Render
                const columns = document.querySelectorAll('.day-column');
                if(columns[dayIndex]) {
                    const col = columns[dayIndex];
                    
                    // Calculate Top and Height
                    // Relative to the day.
                    // If start < dayStart, top = 0.
                    // If end > dayEnd, height = to bottom.
                    
                    const dayDate = days[dayIndex];
                    const dayStart = new Date(dayDate); dayStart.setHours(0,0,0,0);
                    const dayEnd = new Date(dayDate); dayEnd.setHours(23,59,59,999);
                    
                    let effectiveStart = start < dayStart ? dayStart : start;
                    let effectiveEnd = end > dayEnd ? dayEnd : end;
                    
                    const startH = effectiveStart.getHours() + effectiveStart.getMinutes()/60;
                    const endH = effectiveEnd.getHours() + effectiveEnd.getMinutes()/60;
                    
                    // Cap at 24 for endH calculation if it wraps to 0
                    const durationH = Math.max(endH - startH, 0.5); // Min 30min visual
                    
                    const el = document.createElement('div');
                    el.className = 'week-event';
                    el.style.top = `${startH * 60}px`; // 60px per hour
                    el.style.height = `${durationH * 60}px`;
                    el.innerHTML = `<div class="event-title">${event.nome}</div><div class="event-time">${formatTime(effectiveStart)}</div>`;
                    el.onclick = (e) => { e.stopPropagation(); showEventDetails(event); };
                    
                    // Tooltip
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
    const dayOfWeek = startDay.getDay(); // 0 Sun .. 6 Sat
    const daysBack = (dayOfWeek === 0 ? 6 : dayOfWeek - 1); // Mon=0, Sun=6
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

// ==========================================
// HELPERS & MODALS
// ==========================================

function isSameDay(d1, d2) {
    return d1.getFullYear() === d2.getFullYear() &&
           d1.getMonth() === d2.getMonth() &&
           d1.getDate() === d2.getDate();
}

function formatTime(date) {
    return date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
}

// Create Event
function saveEvent() {
    const form = document.getElementById('createEventForm');
    if(!form.checkValidity()) {
        form.reportValidity();
        return;
    }
    
    const formData = new FormData(form);
    
    const newEvent = {
        id_evento: Date.now(),
        nome: formData.get('nome'),
        luogo: formData.get('luogo'),
        data_ora_inizio: formData.get('inizio'),
        data_fine: formData.get('fine'),
        azienda: null 
    };
    
    if(typeof eventsData !== 'undefined') {
        eventsData.push(newEvent);
    }
    
    const modalEl = document.getElementById('createEventModal');
    const modal = bootstrap.Modal.getInstance(modalEl);
    modal.hide();
    form.reset();
    
    renderView();
}

// Details & Delete
let selectedEvent = null;

function showEventDetails(event) {
    selectedEvent = event;
    document.getElementById('detailTitle').textContent = event.nome;
    const s = new Date(event.data_ora_inizio);
    const e = event.data_fine ? new Date(event.data_fine) : s;
    document.getElementById('detailTime').textContent = `${s.toLocaleString()} - ${e.toLocaleString()}`;
    document.getElementById('detailPlace').textContent = event.luogo || 'Nessun luogo specificato';
    document.getElementById('detailId').value = event.id_evento;
    
    const modal = new bootstrap.Modal(document.getElementById('eventDetailsModal'));
    modal.show();
}

function deleteEvent() {
    if(selectedEvent && typeof eventsData !== 'undefined') {
        eventsData = eventsData.filter(e => e.id_evento !== selectedEvent.id_evento);
        // We need to re-assign to the global variable.
        // Since we are in the same scope or global, direct assignment works if 'eventsData' is var.
        // It was defined as var in HTML.
        
        // Wait, filter returns a new array. `eventsData` inside `forEach` works?
        // JS passes reference to objects, but reassignment needs to hit the variable.
        // Since `eventsData` is global (window.eventsData effectively), we can just assign.
        // But `filter` creates a new array.
        
        // Let's modify the array in place or reassign window.eventsData
        // Or simpler: use splice if we find index.
        const idx = eventsData.findIndex(e => e.id_evento === selectedEvent.id_evento);
        if(idx !== -1) eventsData.splice(idx, 1);
        
        const modalEl = document.getElementById('eventDetailsModal');
        const modal = bootstrap.Modal.getInstance(modalEl);
        modal.hide();
        renderView();
    }
}
