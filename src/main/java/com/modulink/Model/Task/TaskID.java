package com.modulink.Model.Task;

import java.util.Objects;

public class TaskID {
    private int id_task;
    private int azienda;

    public TaskID(){}

    public TaskID(int id_task, int azienda) {
        this.id_task = id_task;
        this.azienda = azienda;
    }

    public int getId_task() {
        return id_task;
    }

    public void setId_task(int id_task) {
        this.id_task = id_task;
    }

    public int getAzienda() {
        return azienda;
    }

    public void setAzienda(int azienda) {
        this.azienda = azienda;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TaskID taskID = (TaskID) o;
        return id_task == taskID.id_task && azienda == taskID.azienda;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_task, azienda);
    }
}
