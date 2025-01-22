package com.automa.entity.action;

public enum ActionType {

    SENDMAIL(ActionGroup.GMAIL),
    YOUTUBEINFO(ActionGroup.YOUTUBE),
    RUNONCE(ActionGroup.SCHEDULER),
    RUNDAILY(ActionGroup.SCHEDULER),
    READSHEET(ActionGroup.SPREADSHEET);

    private final ActionGroup group;

    ActionType(ActionGroup group) {
        this.group = group;
    }

    public ActionGroup getActionGroup() {
        return group;
    }
}
