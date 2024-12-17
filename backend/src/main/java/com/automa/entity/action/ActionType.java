package com.automa.entity.action;

public enum ActionType {

    SENDMAIL(ActionGroup.GMAIL),
    YOUTUBEINFO(ActionGroup.YOUTUBE),
    RUNONCE(ActionGroup.SCHEDULER),
    RUNDAILY(ActionGroup.SCHEDULER);

    private final ActionGroup group;

    ActionType(ActionGroup group) {
        this.group = group;
    }

    public ActionGroup getActionGroup() {
        return group;
    }
}
