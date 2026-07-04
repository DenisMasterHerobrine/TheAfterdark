package dev.denismasterherobrine.afterdark.events;

public class AfterdarkPlayerEventState {
    private AfterdarkCaveEventType activeEvent;
    private int activeTicks;
    private int cooldownTicks;
    private int checkTicks;
    private int marks;

    public AfterdarkPlayerEventState(int initialCooldownTicks) {
        cooldownTicks = initialCooldownTicks;
        checkTicks = 20 * 30;
    }

    public boolean hasActiveEvent() {
        return activeEvent != null;
    }

    public AfterdarkCaveEventType getActiveEvent() {
        return activeEvent;
    }

    public int getActiveAge() {
        return activeEvent == null ? 0 : activeEvent.getDurationTicks() - activeTicks;
    }

    public int getActiveTicks() {
        return activeTicks;
    }

    public void start(AfterdarkCaveEventType event) {
        activeEvent = event;
        activeTicks = event.getDurationTicks();
        marks = 0;
    }

    public void clearActiveEvent() {
        activeEvent = null;
        activeTicks = 0;
        marks = 0;
    }

    public void decrementActiveTicks() {
        if (activeTicks > 0) {
            activeTicks--;
        }
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public void setCooldownTicks(int cooldownTicks) {
        this.cooldownTicks = cooldownTicks;
    }

    public void decrementCooldownTicks() {
        if (cooldownTicks > 0) {
            cooldownTicks--;
        }
    }

    public int getCheckTicks() {
        return checkTicks;
    }

    public void resetCheckTicks(int checkTicks) {
        this.checkTicks = checkTicks;
    }

    public void decrementCheckTicks() {
        if (checkTicks > 0) {
            checkTicks--;
        }
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public void addMarks(int amount) {
        marks += amount;
    }
}
